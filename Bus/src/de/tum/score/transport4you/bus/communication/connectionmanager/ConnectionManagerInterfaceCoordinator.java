package de.tum.score.transport4you.bus.communication.connectionmanager;

import de.tum.score.transport4you.bus.communication.connectionmanager.impl.ConnectionController;
import de.tum.score.transport4you.bus.communication.connectionmanager.impl.Startup;

/**
 * This class allows other components to retrieve interfaces to this component
 * @author hoerning
 *
 */
public class ConnectionManagerInterfaceCoordinator {
	
	/* Store non standard interface */
	private static IConnection connection;
	
	/**
	 * Returns the startup class
	 * @return
	 */
	public static IStartup getStartup(){
		return Startup.getInstance();
	}
	
	/**
	 * Returns the Connection interface of the Connection Manager Component
	 * @return
	 */
	public static IConnection getConnection(){
		if(connection == null){
			return ConnectionController.getInstance();
		} else {
			return connection;
		}
	}
	
	/**
	 * Sets a non standard interface of Connection
	 * @param conn
	 */
	protected void setConnection(IConnection conn){
		connection = conn;
	}

}
