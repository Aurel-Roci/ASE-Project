package de.tum.score.transport4you.bus.application.applicationcontroller.impl;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import de.tum.score.transport4you.shared.mobilebus.data.impl.message.AvailableETicketTypesMessage;
import de.tum.score.transport4you.shared.mobilebus.data.impl.message.ETicketPurchaseFailedMessage;
import de.tum.score.transport4you.shared.mobilebus.data.impl.message.PurchaseETicketFinishMessage;
import de.tum.score.transport4you.shared.mobilebus.data.impl.message.PurchaseETicketTypeMessage;
import de.tum.score.transport4you.shared.mobilebus.data.impl.message.ValidETicketMessage;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.AbstractPersistenceObject;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.BlobEntry;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.BlobEnvelope;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.ETicket;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.ETicketType;
import de.tum.score.transport4you.bus.communication.connectionmanager.ConnectionManagerInterfaceCoordinator;
import de.tum.score.transport4you.bus.communication.connectionmanager.IConnectionContext;
import de.tum.score.transport4you.bus.communication.connectionmanager.error.SendDataException;
import de.tum.score.transport4you.bus.data.datacontroller.DataControllerInterfaceCoordinator;
import de.tum.score.transport4you.bus.data.datacontroller.data.PaymentTransaction;
import de.tum.score.transport4you.bus.data.datacontroller.data.PostPayTransaction;
import de.tum.score.transport4you.bus.data.datacontroller.data.PrePayTransaction;
import de.tum.score.transport4you.bus.data.datacontroller.data.Trip;
import de.tum.score.transport4you.bus.data.datacontroller.error.ConfigurationLoadingException;
import de.tum.score.transport4you.bus.data.datacontroller.error.PersistenceException;

/**
 * This class represents a connection with the customer
 * @author hoerning
 *
 */
public class CustomerConnection {
	
	/* Logger*/
	private Logger logger = Logger.getLogger("Application");
	
	/* The Context of the connection */
	private IConnectionContext context;
	
	/* The state of the connection */
	private boolean active;
	
	/* The Blob, which is associated with the customer */
	private BlobEntry customerBlob;
	
	/* The Transaction if the user bought tickets */
	private PaymentTransaction transaction;
	
	/* The current acitve Trip */
	private Trip activeTrip;
	
	/**
	 * The constructor
	 * @param context
	 */
	public CustomerConnection(IConnectionContext context) {
		this.context = context;
		this.active = false;
	}

	public IConnectionContext getContext() {
		return context;
	}

	/** 
	 * Notifies the connection about the begin of communication
	 */
	public void incomingConnection() {
		
		this.active = true;
		logger.debug("Incoming Connection");
		
	}

	/**
	 * Parse and process incoming data
	 * @param data
	 * @throws ConfigurationLoadingException 
	 */
	public synchronized void incomingData(Object data) {
		
		logger.debug("Receiving data from a Mobile System");
		
		// Check if the data type is ok
		if(data instanceof BlobEnvelope){
			BlobEnvelope envelope = (BlobEnvelope) data;
			
			try {
				if(envelope.checkConsistency(DataControllerInterfaceCoordinator.getSettingsDataController().getKeyConfiguration().getBlobPublicKey())) {
					//Consistent Envelope
					this.customerBlob = envelope.getPublicBlobEntry();
					//Check if there is an ETicket in the Blob
					logger.debug("Checking for tickets");
					ArrayList<ETicket> eTicketList = envelope.getPublicBlobEntry().geteTicketList();
					if(eTicketList.isEmpty()) {
						logger.debug("No ETicket in list, need to send ETicket Type list");
						sendETicketTypeList();
					} else {
						logger.debug("ETickets found in list, checking validity");
						ETicket validTicket = checkForValidTicket(eTicketList);
						
						if(validTicket ==null) {
							logger.debug("No valid ticket in list found, need to send ETicket Type list");
							sendETicketTypeList();
						} else {
							
							logger.debug("Valid ticket found in ETicket list");
							
							BlobEnvelope newBlob = null;
							if(!validTicket.isInvalidated()){
								//Invalidate ticket and modify
								this.invalidateTicket(validTicket);
								
								
								//Calculate and send new Blob
								newBlob = new BlobEnvelope(envelope.getPublicBlobEntry(), DataControllerInterfaceCoordinator.getSettingsDataController().getKeyConfiguration().getBlobPrivateKey());
								
							} else {
								
								//No new blob needs to be generated
								logger.debug("No need to invalidate a ticket");
							}
							
							this.checkInWithTicket(validTicket);
							logger.debug("Sending Valid ETicket Message");
							ValidETicketMessage message = new ValidETicketMessage();
							message.setBlob(newBlob); // may be null, but that is ok
							ConnectionManagerInterfaceCoordinator.getConnection().sendData(this.getContext(), message);
							
							//disconnect
							this.disconnect();
						}
					}
					
					
				} else {
					//Error
					logger.error("Received invalid/faked Blob from Mobile System. Going to kill the connection");
					disconnect();
					return;
				}
			} catch (InvalidKeyException e) {
				logger.error("Error while loading the crypto libraries: "+e.getMessage());
				this.disconnect();
				return;
			} catch (IOException e) {
				logger.error("Error while accessing IO: "+e.getMessage());
				this.disconnect();
				return;
			} catch (ClassNotFoundException e) {
				logger.error(e.getMessage());
				this.disconnect();
				return;
			} catch (ConfigurationLoadingException e) {
				logger.error("Error while loading the local configuration: "+e.getMessage());
				this.disconnect();
				return;
			} catch (SendDataException e) {
				logger.error("Error while sending data to the communication controller, going to abort communication");
				this.disconnect();
				return;
			} catch (PersistenceException e) {
				logger.error("Error while doing the check in: "+e.getMessage());
				this.disconnect();
				return;
			}
			
		} else if(data instanceof PurchaseETicketTypeMessage){
			
			PurchaseETicketTypeMessage purchaseMessage = (PurchaseETicketTypeMessage) data;
			logger.debug("Retrieved a Purchase ETicket Type Message");
			//Retrieve the ETicketType => note that Customer may fake, therefore compare the types itself
			List<ETicketType> eTicketTypeList = DataControllerInterfaceCoordinator.getETicketTypeDataController().getAllETicketTypes();
			ETicketType selectedType = null;
			for(ETicketType eticketType : eTicketTypeList){
				if(eticketType.getName().equals(purchaseMessage.getSelectedETicketType().getName())) {
					selectedType = eticketType;
				}
			}
			if(selectedType==null){
				//Error in selection
				logger.error("Error in protocol, system has not received a valid E Ticket Type selection");
				disconnect();
				return;
			}
			
			try {
				if(this.customerBlob==null) {
					//Error in protocol
					logger.error("Error in protocol, system has not yet gained a Customer Blob");
					ConnectionManagerInterfaceCoordinator.getConnection().sendData(this.getContext(), null);
				} else {
					
					try {
						//Check Account Type and perform payment process
						if(this.customerBlob.getAccountType().equals(DataControllerInterfaceCoordinator.getSettingsDataController().getApplicationConfiguration().getPrepayAccountRepresentation())){
							//Prepay account => check balance
							
							if(this.customerBlob.getAccountBalance() >= selectedType.getPrice()){
								//Ticket can be sold
								logger.debug("Enough money on account to buy the ticket(s)");
								this.createETicketsAndSendNewBlob(selectedType,true);
								
							} else {
								//Ticket can not be sold
								logger.debug("Ticket can not be sold due to not enough money on account");
								ETicketPurchaseFailedMessage failedMessage = new ETicketPurchaseFailedMessage("Not enough money on account");
								ConnectionManagerInterfaceCoordinator.getConnection().sendData(this.getContext(), failedMessage);
								
								//disconnect() //wait some time because sending may be multi threaded
								this.disconnect();
							}
							
						} else {
							//PostPay Account => sell Tickets
							
							this.createETicketsAndSendNewBlob(selectedType,false);
						}
					} catch (SendDataException e){
						logger.error("Error while sending data to the communication controller, going to abort communication");
						disconnect();
						return;
					} catch (InvalidKeyException e) {
						logger.error("Error while loading crypt module");
						disconnect();
						return;
					} catch (ConfigurationLoadingException ex) {
						logger.error("Error while loading local configuration");
						disconnect();
						return;
					}
				}
			} catch (SendDataException e) {
				logger.error("Error while sending data to the communication controller, going to abort communication");
				disconnect();
				return;
			}
			
			
		} else if(data instanceof PurchaseETicketFinishMessage){
			
			//Check if there is an transaction
			if(transaction == null) {
				//No transaction found => kill
				logger.debug("No transaction was found, going to kill connection");
				disconnect();
				return;
			} else {
				logger.debug("Received Finish Message, going to set the Transaction to success");
				//Set transaction
				
				this.transaction.setSuccess(true);
				ArrayList<AbstractPersistenceObject> apoList = new ArrayList<AbstractPersistenceObject>();
				apoList.add(transaction);
				try {
					DataControllerInterfaceCoordinator.getPersistentDataController().save(apoList);
				} catch (PersistenceException e) {
					logger.error("Could not save the transaction: "+e.getMessage());
					disconnect();
					return;
				}
				
				//Disconnect here: Normally the mobile system would have to send keepalive to notify the bus about its presence, this is not implemented in the frist increment
				// Disconnect => save Trip
				this.disconnect();
				
				
			}
			
		} else {
			//Fire Error Message
			logger.error("Received Message with no valid data! Abort connection");
			disconnect();
		}
		
	}

	
	private void createETicketsAndSendNewBlob(ETicketType selectedType,boolean prepay) throws InvalidKeyException, ConfigurationLoadingException, SendDataException {
		
		logger.debug("Create ETickets for ETicketType: "+selectedType.getName());
		
		//Objects to log
		List<AbstractPersistenceObject> loggingList = new ArrayList<AbstractPersistenceObject>();
		
		//Create Tickets (local and shared)
		List<ETicket> newTickets = new ArrayList<ETicket>();
		for(int i=0; i< selectedType.getAmountTickets(); i++){
			ETicket newTicket = new ETicket();
			newTicket.setInvalidated(false);
			newTicket.setValidTime(selectedType.getValidMinutes());
			newTickets.add(newTicket);
		}
		
		//Invalidate one Ticket
		ETicket ticketToInvalidate = newTickets.get(0);
		this.invalidateTicket(ticketToInvalidate);
		
		logger.debug("Creating local logging information - ETickets");
		//Add the tickets to logging list
		List<ETicket> loggingETicketList = convertETicketsSharedToLocal(newTickets);
		//Set selling date and add to logging list
		for(ETicket localETicket : loggingETicketList) {
			localETicket.setSellingDate(new Date());
			loggingList.add(localETicket);
			if(localETicket.getInvalidatedAt()!=null){
				//That is the local representation of the ETicket currently used for boarding
				try {
					checkInWithTicket(localETicket);
				} catch (PersistenceException e) {
					logger.error("Error while creating the Trip entry");
					disconnect();
					return;
				}
			}
		}
		//Add a Transaction and modify Blob if needed
		logger.debug("Creating local logging information - Transactions");
		if(prepay){
			PrePayTransaction prePayTrans = new PrePayTransaction();
			prePayTrans.setAmount(selectedType.getPrice());
			prePayTrans.setAmount(this.customerBlob.getAccountBalance());
			this.customerBlob.setAccountBalance(this.customerBlob.getAccountBalance()-selectedType.getPrice());
			prePayTrans.setAmount(this.customerBlob.getAccountBalance());
			prePayTrans.setSoldETickets(loggingETicketList);
			prePayTrans.setTime(new Date());
			prePayTrans.setUserID(this.customerBlob.getUserId());
			loggingList.add(prePayTrans);
			this.transaction = prePayTrans;
		} else { //postpay
			PostPayTransaction postPayTrans = new PostPayTransaction();
			postPayTrans.setAmount(selectedType.getPrice());
			postPayTrans.setSoldETickets(loggingETicketList);
			postPayTrans.setTime(new Date());
			postPayTrans.setUserID(this.customerBlob.getUserId());
			loggingList.add(postPayTrans);
			this.transaction = postPayTrans;
		}
		
		logger.debug("Tickets generated. Creating new Blob");
		this.customerBlob.geteTicketList().addAll(newTickets);
		
		//Calculate and send new Blob
		BlobEnvelope newBlob = new BlobEnvelope(this.customerBlob, DataControllerInterfaceCoordinator.getSettingsDataController().getKeyConfiguration().getBlobPrivateKey());
		ValidETicketMessage validMessage = new ValidETicketMessage();
		validMessage.setBlob(newBlob);
		
		logger.debug("Storing Logging Information - awaiting success message");
		try {
			DataControllerInterfaceCoordinator.getPersistentDataController().save(loggingList);
		} catch (PersistenceException e) {
			logger.error("Error while accessing persitence layer, cannot save logging information");
			//TODO: here exception messages need to be passed to the gui controller to inform user about problem with the system
			disconnect();
			return;
		}
		
		logger.debug("Sending new Blob in Valid E Ticket Message");
		ConnectionManagerInterfaceCoordinator.getConnection().sendData(this.getContext(), validMessage);
		
		
	}

	private void checkInWithTicket(ETicket localETicket) throws PersistenceException {
		
		logger.info("Allow check in for: "+this.customerBlob.getUserName());
		logger.debug("Creating new trip entry for User ID: "+this.customerBlob.getUserId());
		//Create Trip entry and check in with associated ticket
		Trip newTrip = new Trip();
		newTrip.setCustomerId(this.customerBlob.getUserId());
		newTrip.setStart(new Date());
		newTrip.setCustomerTicket(localETicket);
		
		ArrayList<AbstractPersistenceObject> saveList = new ArrayList<AbstractPersistenceObject>();
		saveList.add(newTrip);
		this.activeTrip = newTrip;
		DataControllerInterfaceCoordinator.getPersistentDataController().save(saveList);
		logger.debug("Saved trip successfully");
	}

/*	private void checkInWithTicket(ETicket ticket) throws PersistenceException {
		
		//Create local representation of ticket and check in with that
		//Note that here only a copy of the committed ETicket is made and stored. I.e. A ticket bought on this bus and used again on this bus is also stored as a copy
		//transform e ticket
		List<ETicket> sharedETicket = new ArrayList<ETicket>();
		sharedETicket.add(ticket);
	
		this.checkInWithTicket(convertETicketsSharedToLocal(sharedETicket).get(0));
	}*/

	private List<ETicket> convertETicketsSharedToLocal(List<ETicket> sharedETicketList) {
		
		//Transform ETickets
		List<ETicket> localETicketList = new ArrayList<ETicket>();
		for(ETicket sharedETicket : sharedETicketList){
			ETicket newLocalETicket = new ETicket();
			newLocalETicket.setCustomerId(this.customerBlob.getUserId());
			newLocalETicket.setInvalidated(sharedETicket.isInvalidated());
			newLocalETicket.setInvalidatedAt(sharedETicket.getInvalidatedAt());
			newLocalETicket.setValidTime(sharedETicket.getValidTime());
			newLocalETicket.setValidUntil(sharedETicket.getValidUntil());
			localETicketList.add(newLocalETicket);
		}
		
		return localETicketList;
	}

	private void invalidateTicket(ETicket validTicket) {
		
		logger.debug("Invalidating Ticket");
		Date date = new Date();
		validTicket.setInvalidatedAt(date);
		validTicket.setInvalidated(true);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, (int) validTicket.getValidTime());
		Date newDate = calendar.getTime();
		validTicket.setValidUntil(newDate);
		
	}

	private void sendETicketTypeList() throws SendDataException {
		
		AvailableETicketTypesMessage message = new AvailableETicketTypesMessage();
		List<ETicketType> eTicketTypeList = DataControllerInterfaceCoordinator.getETicketTypeDataController().getAllETicketTypes();
		
		logger.debug("Data Conversion");
		//Need to transform the ETicketType instances to the Exchange Format
		List<ETicketType> newList = new ArrayList<ETicketType>();
		for(ETicketType eTicketType : eTicketTypeList){
			ETicketType transformedType = new ETicketType();
			transformedType.setName(eTicketType.getName());
			transformedType.setPrice(eTicketType.getPrice());
			transformedType.setValidMinutes(eTicketType.getValidMinutes());
			transformedType.setAmountTickets(eTicketType.getAmountTickets());
			newList.add(transformedType);
		}
		
		message.setAvailableETicketTypes(newList);
		logger.debug("Sending");
		ConnectionManagerInterfaceCoordinator.getConnection().sendData(this.getContext(), message);
		
	}

	private ETicket checkForValidTicket(ArrayList<ETicket> eTicketList) {
		
		logger.debug("Checking ETicket List with size: "+eTicketList.size());
		//Check if a Ticket is still valid and return the ticket
		for(ETicket eTicket: eTicketList) {
			if(eTicket.isInvalidated()){
				//Check if ticket can be used
				Date timestampInv = eTicket.getValidUntil();
				Date now = new Date();
				if(!now.after(timestampInv)){
					//Found a valid ticket
					logger.debug("Found a valid already invalidated ticket");
					return eTicket;
				}
			} else {
				//Ticket is there
				logger.debug("Found a ticket which was not yet invalidated");
				return eTicket;
			}
			
		}
		
		return null;
	}

	private void disconnect() {
		// Disconnects the connection
		logger.debug("Disconnect Prepare");
		try {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
			}
			logger.debug("Disconnect");
			this.active=false;
			CustomerServiceController.getInstance().disconnect(this.getContext());
			ConnectionManagerInterfaceCoordinator.getConnection().sendData(this.getContext(), null);
		} catch (SendDataException e) {
			logger.error("Error while sending data to the communication controller, going to abort communication");
			//Aborting happens automatically since error while sending implies connection problems => connection automatically terminates
			return;
		}
	}

	public void closingConnection() {
		
		this.active = false;
		logger.debug("Closing Connection");
		
		if(this.activeTrip != null) {
			//Writing the EndDate
			logger.debug("Found an active Trip for connected user, ending the Trip");
			this.activeTrip.setEnd(new Date());
			ArrayList<AbstractPersistenceObject> apoList = new ArrayList<AbstractPersistenceObject>();
			apoList.add(activeTrip);
			try {
				DataControllerInterfaceCoordinator.getPersistentDataController().save(apoList);
			} catch (PersistenceException e) {
				logger.error("Could not save the trip: "+e.getMessage());
			}
		}
		
		CustomerServiceController.getInstance().disconnect(this.getContext());
		
	}

	public boolean isActive() {
		return active;
	}

}
