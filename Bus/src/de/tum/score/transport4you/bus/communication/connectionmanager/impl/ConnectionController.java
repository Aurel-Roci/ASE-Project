package de.tum.score.transport4you.bus.communication.connectionmanager.impl;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import de.tum.score.transport4you.bus.communication.connectionmanager.CommunicationType;
import de.tum.score.transport4you.bus.communication.connectionmanager.IConnection;
import de.tum.score.transport4you.bus.communication.connectionmanager.IConnectionContext;
import de.tum.score.transport4you.bus.communication.connectionmanager.IConnectionListener;
import de.tum.score.transport4you.bus.communication.connectionmanager.error.SendDataException;
import de.tum.score.transport4you.bus.communication.connectionmanager.impl.protocol.IProtocol;
import de.tum.score.transport4you.bus.communication.connectionmanager.impl.protocol.IncomingType;

/**
 * The controller capable of creating connections and passing connections to the requestors
 * @author hoerning
 *
 */
public class ConnectionController implements IConnection{
	
	/* Singleton instance */
	private static ConnectionController instance;
	
	/* Logger */
	private Logger logger = Logger.getLogger("Communication");
	
	/* List of referenced protocols */
	private ArrayList<IProtocol> protocolList = new ArrayList<IProtocol>();
	
	/* List of connection listeners */
	private ArrayList<TypedConnectionListener> listenerList = new ArrayList<TypedConnectionListener>();
	
	/**
	 * Returns the singleton instance
	 * @return
	 */
	public static ConnectionController getInstance(){
		if(instance==null){
			instance = new ConnectionController();
		}
		return instance;
	}
	
	/**
	 * Adds a protocol to be used
	 */
	protected void addProtocol(IProtocol protocol) {
		protocolList.add(protocol);
	}
	
	/**
	 * Starts all protocols
	 */
	protected void startProtocols(){
		
		logger.debug("Starting all protocols");
		
		for(IProtocol protocol:protocolList) {
			protocol.startProtocol();
		}
		
		logger.debug("Protocols started");
	}

	@Override
	public void registerForConnection(CommunicationType communicationType, IConnectionListener listener) {
		
		logger.debug("Registering new listener with communication type: "+communicationType);
		
		// Store new Listener
		TypedConnectionListener newListener = new TypedConnectionListener();
		newListener.setCommunicationType(communicationType);
		newListener.setConnectionListener(listener);
		
		synchronized(this) {
			if(!this.listenerList.contains(newListener)){
				this.listenerList.add(newListener);
			}
		}
		
	}

	@Override
	public void deregisterForConnection(CommunicationType communicationType, IConnectionListener listener) {
		
		logger.debug("Removing listener from communication type: "+communicationType);
		
		//Remove Listener
		TypedConnectionListener removeListener = new TypedConnectionListener();
		removeListener.setCommunicationType(communicationType);
		removeListener.setConnectionListener(listener);
		
		synchronized(this){
			this.listenerList.remove(removeListener);
		}
	}
	
	/**
	 * Handles incoming application data from protocols and passes them by to listeners.
	 * @param context
	 * @param data
	 */
	public void handleData(IConnectionContext context, IncomingType incomingType, Object data){
		
		//Notify listener
		synchronized(this){
			for(TypedConnectionListener listener : this.listenerList){
				if(listener.getCommunicationType().equals(context.getCommunicationType())){
					if(incomingType == IncomingType.IncomingConnection) {
						listener.getConnectionListener().incomingConnection(context);
					} else if(incomingType == IncomingType.IncomingData) {
						listener.getConnectionListener().incomingData(context, data);
					} else if(incomingType == IncomingType.ClosingConnection) {
						listener.getConnectionListener().closingConnection(context);
					}
				}
			}
		}
		
		
	}

	@Override
	public synchronized void sendData(IConnectionContext context, Object data) throws SendDataException {
		
		logger.debug("Passing by Data to responsible Protocols");
		
		//Pass by to responsible Protocols
		for(IProtocol protocol:protocolList) {
			if(protocol.getCommunicationType().equals(context.getCommunicationType())){
				protocol.sendData(context,data);
			}
		}
	}

}
