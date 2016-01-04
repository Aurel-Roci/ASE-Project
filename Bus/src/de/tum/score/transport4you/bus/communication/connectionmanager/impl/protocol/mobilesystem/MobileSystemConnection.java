package de.tum.score.transport4you.bus.communication.connectionmanager.impl.protocol.mobilesystem;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.PrivateKey;

import javax.crypto.SecretKey;

import org.apache.log4j.Logger;

import de.tum.score.transport4you.shared.mobilebus.data.IKeyExchange;
import de.tum.score.transport4you.shared.mobilebus.data.error.KeyAgreementException;
import de.tum.score.transport4you.shared.mobilebus.data.impl.keyexchange.DHParameter;
import de.tum.score.transport4you.shared.mobilebus.data.impl.keyexchange.KeyExchange;
import de.tum.score.transport4you.shared.mobilebus.data.impl.keyexchange.SignedPublicKey;
import de.tum.score.transport4you.shared.mobilebus.data.impl.message.BluetoothData;
import de.tum.score.transport4you.shared.mobilebus.data.impl.message.BluetoothEnvelope;
import de.tum.score.transport4you.shared.mobilebus.data.impl.message.DataMessage;
import de.tum.score.transport4you.shared.mobilebus.data.impl.message.KeyExchangeFinishMessage;
import de.tum.score.transport4you.shared.mobilebus.data.impl.message.KeyExchangePublicKeyMessage;
import de.tum.score.transport4you.shared.mobilebus.data.impl.message.ProtocolExceptionMessage;
import de.tum.score.transport4you.bus.communication.bluetoothcontroller.data.BluetoothConnection;
import de.tum.score.transport4you.bus.communication.connectionmanager.CommunicationType;
import de.tum.score.transport4you.bus.communication.connectionmanager.IConnectionContext;
import de.tum.score.transport4you.bus.communication.connectionmanager.error.SendDataException;
import de.tum.score.transport4you.bus.communication.connectionmanager.impl.ConnectionController;
import de.tum.score.transport4you.bus.communication.connectionmanager.impl.protocol.IncomingType;
import de.tum.score.transport4you.bus.data.datacontroller.DataControllerInterfaceCoordinator;
import de.tum.score.transport4you.bus.data.datacontroller.error.ConfigurationLoadingException;

/**
 * This class represents a connection with input and output stream as well as a thread listening on the input 
 * @author hoerning
 *
 */
public class MobileSystemConnection extends Thread implements IConnectionContext{
	
	/* The id of the connection */
	private int id;
	
	/* Id counter */
	private static int idCounter = 0;
	
	/* Request to stop Thread */
	private boolean stopThread = false;
	
	/* The status of the Thread */
	private boolean active = false;
	
	/* The DH parameters and session key */
	private DHParameter dhParameter;
	private SignedPublicKey signedPublicKey;
	private IKeyExchange keyExchange;
	private SecretKey sessionKey;
	
	/* The BluetoothConnection */
	private BluetoothConnection bluetoothConnection;
	
	/* Loggger*/
	private Logger logger = Logger.getLogger("Mobile System Connection");
	
	/* The input and output streams */
	private ObjectInputStream objectInputStream;
	private ObjectOutputStream objectOutputStream;
	
	/* The state of the connection */
	private BluetoothState state = BluetoothState.Initializing;
	
	/** 
	 * Constructor
	 * @param bluetoothConnection
	 */
	public MobileSystemConnection(BluetoothConnection bluetoothConnection){
		this.bluetoothConnection = bluetoothConnection;
		this.id = idCounter++;
	}
	
	@Override
	public void run(){
	
		this.active = true;
		
		logger.debug("(ID:"+String.valueOf(id)+") Started Mobile System Connection");
		
		this.state = BluetoothState.Initializing;
		
		//Notify application about new connection
		ConnectionController.getInstance().handleData(this, IncomingType.IncomingConnection, null);
		
		//Create output stream
		try {
			//Create Output stream
		    OutputStream outputStream= bluetoothConnection.getStreamConnection().openOutputStream();
		    objectOutputStream = new ObjectOutputStream(outputStream);
		} catch (IOException e) {
			// Problem while creating connection => abort
			e.printStackTrace();
		}
		
	    //Send first message
	    KeyExchangePublicKeyMessage message = new KeyExchangePublicKeyMessage();
	    try {
	    	
	    	logger.debug("(ID:"+String.valueOf(id)+") Calculating and sending Key Exchange Message");
	    	
			try {
				this.keyExchange = new KeyExchange();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			PrivateKey privateKey = DataControllerInterfaceCoordinator.getSettingsDataController().getKeyConfiguration().getKeyAgreementPrivateKey();
			this.signedPublicKey = keyExchange.getOwnPublicKey(privateKey);
			this.dhParameter = keyExchange.getDHParameters();
			message.setDhSpec(this.dhParameter);
			message.setSignedPublicKey(this.signedPublicKey);
			
			BluetoothEnvelope envelope = new BluetoothEnvelope(message);
		    this.sendMessage(envelope);
		    
		    this.state = BluetoothState.PublicKeySent;
			
		} catch (KeyAgreementException e1) {
			this.stopThread = true;
			this.handleError("Problem while creating the Session Key");
		} catch (ConfigurationLoadingException e) {
			this.stopThread = true;
			this.handleError("Problem while creating the Session Key");
		} catch (SendDataException e) {
			this.stopThread = true;
		}
	    
	    //Start listening
	    while (!stopThread) {
	    	
	    	try {
	    		if(objectInputStream==null){
	    			
	    			logger.debug("(ID:"+String.valueOf(id)+") Listening on channel");
	    			//No input stream detected => create one
	    			InputStream inputStream = bluetoothConnection.getStreamConnection().openInputStream();
					objectInputStream = new ObjectInputStream(inputStream);
	    		}
				Object sendObject = objectInputStream.readObject();
				BluetoothEnvelope receivedEnvelope = (BluetoothEnvelope) sendObject;
				logger.debug("(ID:"+String.valueOf(id)+") Received envelope");
				this.handleIncomingData(receivedEnvelope);
			} catch (IOException e) {
				//IO Exception => close
				this.stopThread = true;
				logger.error("(ID:"+String.valueOf(id)+") Error while receiving object: "+e.getMessage());
			} catch (ClassNotFoundException e) {
				//Class Exception => Not recognizable input
				this.stopThread = true;
				logger.error("(ID:"+String.valueOf(id)+") Error while casting object: "+e.getMessage());
				//Send Error message to indiciate that connection is no longer available
				BluetoothEnvelope exceptionEnvelope = new BluetoothEnvelope(new ProtocolExceptionMessage());
				try {
					this.sendMessage(exceptionEnvelope);
				} catch (SendDataException e1) {
					logger.error("(ID:"+String.valueOf(id)+") Error while sending error message");
				}
			}
	    
	    }
	    
	    //Thread is stopping
	    logger.debug("(ID:"+String.valueOf(id)+") Closing Connection");
	    
	    //Notify application about closing connection
	    ConnectionController.getInstance().handleData(this, IncomingType.ClosingConnection, null);
	    
	    try {
	    	if(objectInputStream!=null)
	    		this.objectInputStream.close();
	    	if(objectOutputStream!=null)
	    		this.objectOutputStream.close();
			this.bluetoothConnection.getStreamConnection().close();
		} catch (IOException e) {
			logger.error("(ID:"+String.valueOf(id)+") Error while closing connection: "+ e.getMessage());
		}
	   
		this.active = false;
	}
	
	/**
	 * Sends the specified envelope to the connections destination
	 * @param envelope
	 * @throws SendDataException 
	 */
	public synchronized void sendMessage(BluetoothEnvelope envelope) throws SendDataException{
		
		logger.debug("(ID:"+String.valueOf(id)+") Sending Bluetooth Envelope");
		
		try {
			objectOutputStream.writeObject(envelope);
			objectOutputStream.flush();
		} catch (IOException e) {
			// IO Exception => close
			logger.error("(ID:"+String.valueOf(id)+") Error while sending object: "+e.getMessage());
			this.stopThread = true;
			throw new SendDataException("IO Exception");
		}
		
	}
	
	/** Sends the specified data as Data Message to the connections destination
	 * 
	 * @param data
	 * @throws SendDataException 
	 */
	public void sendMessage(Object data) throws SendDataException {
		
		if(data==null) {
			//Disconnect
			logger.warn("Null data to send => Kill connection");
			this.stopThread=true;
			this.active=false;
			try {
				bluetoothConnection.getStreamConnection().close();
			} catch (IOException e) {
				// IOException => does not matter, connection should already be closed
			}
			
		}
		
		//Encrypt data
		DataMessage message = new DataMessage(data);
		try {
			logger.debug("Encrypting Message with key: "+this.sessionKey.getEncoded()[0]);
			message.encryptData(this.sessionKey);
			
		} catch (InvalidKeyException e) {
			logger.error("(ID:"+String.valueOf(id)+") Encryption Error: "+e.getMessage());
			throw new SendDataException("Error while accessing Encryption");
		}

		BluetoothEnvelope env = new BluetoothEnvelope(message);
		this.sendMessage(env);
		
	}
	
	private synchronized void handleIncomingData(BluetoothEnvelope envelope){
		
		BluetoothData bluetoothData = envelope.getData();
		
		if(this.state == BluetoothState.PublicKeySent) {
			//Waiting for Message reply
			if(bluetoothData instanceof KeyExchangePublicKeyMessage){
				
				logger.debug("(ID:"+String.valueOf(id)+",State:"+this.state+") Received public key message");
				
				//Retrieve the signed public key and calculate session key
				KeyExchangePublicKeyMessage message = (KeyExchangePublicKeyMessage) bluetoothData;
				SignedPublicKey foreignSignedPublicKey = message.getSignedPublicKey();
				try {
					this.sessionKey = this.keyExchange.getSessionKey(foreignSignedPublicKey, null);
					logger.debug("(ID:"+String.valueOf(id)+",State:"+this.state+") Session Key generated");
					
					//Send Acceptance Message
					BluetoothEnvelope toSend = new BluetoothEnvelope(new KeyExchangeFinishMessage());
					this.sendMessage(toSend);
					
					this.state = BluetoothState.ConnectionEstablished;
					
					return;
				
				} catch (KeyAgreementException e) {
					logger.error("(ID:"+String.valueOf(id)+",State:"+this.state+") Error while calculating session key");
					this.stopThread = true;
					handleError("Error processing your Public Key Message");
				} catch (SendDataException e) { 
					logger.error("(ID:"+String.valueOf(id)+",State:"+this.state+") Error while sending message");
					this.stopThread = true;
				}
				
			} else if(bluetoothData instanceof ProtocolExceptionMessage){
				//Error in Protocol
				logger.error("(ID:"+String.valueOf(id)+",State:"+this.state+") Error while calculating session key");
				this.stopThread = true;
				
			} else {
			
				//Handle error
				handleError("Public Key Message expected");
			}
			
		} if(this.state == BluetoothState.ConnectionEstablished) {
			
			//Awaiting either data or keep alive tokens
			if(bluetoothData instanceof DataMessage){
				
				logger.debug("(ID:"+String.valueOf(id)+",State:"+this.state+") Received data message");
				
				//Pass by to application
				DataMessage message = (DataMessage) bluetoothData;
				
				//Data needs to be encrypted
				if(!message.isEncrypted()) {
					logger.error("(ID:"+String.valueOf(id)+",State:"+this.state+") Data Message is not encrypted, going to close connection.");
					this.handleError("Only accepting encrypted Data Messages");
					return;
				} else {
					//Decrypt data for application
					logger.debug("Decrypting data");
					try {
						message.decryptData(this.sessionKey);
					} catch (InvalidKeyException e) {
						logger.error("(ID:"+String.valueOf(id)+",State:"+this.state+") Decryption Error. Closing Connection: "+e.getMessage());
						this.handleError("Error while decryption. Are you using the right session key?");
						return;
					} 
				}
				
				logger.debug("(ID:"+String.valueOf(id)+",State:"+this.state+") Data decrypted. Passing by data");
				ConnectionController.getInstance().handleData(this, IncomingType.IncomingData, message.getData());
				
			} else if(bluetoothData instanceof ProtocolExceptionMessage){
				//Error in Protocol
				logger.error("(ID:"+String.valueOf(id)+",State:"+this.state+") Error while waiting on data");
				this.stopThread = true;
				
			} else {
			
				//Handle error
				handleError("Data Message expected");
			}

			
			
			
		} else {
			handleError("State execption");
		}
		
		
	}

	private void handleError(String message) {
		// Send error message
		BluetoothEnvelope envelope = new BluetoothEnvelope(new ProtocolExceptionMessage());
		try {
			this.sendMessage(envelope);
		} catch (SendDataException e) {
			logger.error("Error while sending message");
		}
		logger.error("(ID:"+String.valueOf(id)+") Error while handling incoming object: "+ message);
		this.stopThread = true;
		
	}

	/**
	 * Returns the id of the connection
	 */
	public int getConnectionId() {
		return id;
	}

	/**
	 * Returns the current state of the connection
	 * @return
	 */
	public synchronized boolean isActive() {
		return active&&(!stopThread);
	}

	@Override
	public CommunicationType getCommunicationType() {
		return CommunicationType.MOBILE_SYSTEM;
	}

	
	
}
