package de.tum.score.transport4you.bus.communication.connectionmanager;

import de.tum.score.transport4you.bus.communication.connectionmanager.error.SendDataException;

/**
 * The interface to the connection Manager component
 * @author hoerning
 *
 */
public interface IConnection {
	
	/**
	 * Register for a specific type of connection with the component. Callbacks are made through the listener.
	 * @param communicationType
	 * @param listener
	 */
	public void registerForConnection(CommunicationType communicationType, IConnectionListener listener);

	/**
	 * Deregister for communication with a specific type of connection.
	 * @param communicationType
	 * @param listener
	 */
	public void deregisterForConnection(CommunicationType communicationType, IConnectionListener listener);
	
	/**
	 * Sends data within a specified context, data = <i>null</i> is a signal that no other messages will be sent, the connection manager may therefore terminate the session
	 * @param context
	 * @param data
	 * @throws SendDataException 
	 */
	public void sendData(IConnectionContext context,Object data) throws SendDataException;
	
	
	
}
