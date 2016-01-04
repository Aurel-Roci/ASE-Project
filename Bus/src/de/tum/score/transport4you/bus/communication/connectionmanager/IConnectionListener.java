package de.tum.score.transport4you.bus.communication.connectionmanager;

/**
 * The Listener for connections of the connection manager
 * @author hoerning
 *
 */
public interface IConnectionListener {
	
	/**
	 * Is called when a new connection was established.
	 * @param context
	 */
	public void incomingConnection(IConnectionContext context);
	
	
	/**
	 * Is called when application related data needs to be exchanged
	 * @param context
	 * @param data
	 */
	public void incomingData(IConnectionContext context, Object data);
	
	/**
	 * Is called when connection closes
	 * @param context
	 */
	public void closingConnection(IConnectionContext context);

}
