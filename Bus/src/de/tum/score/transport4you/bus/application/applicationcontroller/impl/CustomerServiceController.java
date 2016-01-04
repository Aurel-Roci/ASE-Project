package de.tum.score.transport4you.bus.application.applicationcontroller.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import de.tum.score.transport4you.bus.communication.connectionmanager.CommunicationType;
import de.tum.score.transport4you.bus.communication.connectionmanager.ConnectionManagerInterfaceCoordinator;
import de.tum.score.transport4you.bus.communication.connectionmanager.IConnectionContext;
import de.tum.score.transport4you.bus.communication.connectionmanager.IConnectionListener;

/**
 * This class is capable of handling the logic of a connection between the Bus and Customer
 * @author hoerning
 *
 */
public class CustomerServiceController {
	
	private Logger logger = Logger.getLogger("Application");
	
	/* The singleton instance */
	private static CustomerServiceController instance;
	
	/* The listener for mobile connections */
	private IConnectionListener mobileConnectionListener;
	
	/* The current Customer Connections associated with the context*/
	private Map<IConnectionContext,CustomerConnection> customerConnections = new HashMap<IConnectionContext,CustomerConnection>();
		
	/** Returns the singleton of the Customer Service Controller
	 */
	public static CustomerServiceController getInstance(){
		if(instance==null){
			instance = new CustomerServiceController();
		}
		return instance;
	}
	
	private class MobileSystemConnectionListener implements IConnectionListener{
		

		@Override
		public void incomingData(IConnectionContext context, Object data) {
			
			//parse data
			getCustomerConnection(context).incomingData(data);
			
		}

		@Override
		public void incomingConnection(IConnectionContext context) {
			
			//notify about incoming connection
			getCustomerConnection(context).incomingConnection();
		}

		@Override
		public void closingConnection(IConnectionContext context) {

			//notify about closing connection
			getCustomerConnection(context).closingConnection();
			
		}
		
	}
	
	/**
	 * Initializes this class
	 */
	protected void initialize(){
		
		logger.debug("Initializing Customer Service Controller");
		
		mobileConnectionListener = new MobileSystemConnectionListener();
		
		//Register at connection manager
		logger.debug("Registering for connection type: "+CommunicationType.MOBILE_SYSTEM.toString());
		ConnectionManagerInterfaceCoordinator.getConnection().registerForConnection(CommunicationType.MOBILE_SYSTEM,mobileConnectionListener);
		
		
		logger.debug("Customer Service Controller initialized");
		
	}

	/**
	 * This method will retrieve the associated connection or create a new one if it does not exist
	 * @param context
	 * @return 
	 */
	public synchronized CustomerConnection getCustomerConnection(IConnectionContext context) {
		
		CustomerConnection customerConnection = this.customerConnections.get(context);
		if(customerConnection == null) {
			customerConnection = new CustomerConnection(context);
			this.customerConnections.put(context, customerConnection);
		}
		
		return customerConnection;
	}
	
	/**
	 * Removes the connection from memory, if connection associated with contaxt is inactive
	 * @param context
	 */
	public synchronized void disconnect(IConnectionContext context){
		
		if(!this.customerConnections.get(context).isActive()) {
			logger.debug("Removing inactive Customer Connection");
			this.customerConnections.remove(context);
		}
		
	}

}
