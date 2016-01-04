package de.tum.score.transport4you.bus.communication.connectionmanager.impl.protocol;

import de.tum.score.transport4you.bus.communication.connectionmanager.CommunicationType;
import de.tum.score.transport4you.bus.communication.connectionmanager.IConnectionContext;
import de.tum.score.transport4you.bus.communication.connectionmanager.error.SendDataException;

/**
 * The generic interface for protocols. It offers methods to control the lifecycle of a protocl implementation
 * @author hoerning
 *
 */
public interface IProtocol {

	/**
	 * Starts the protocol, restarts it if it is already started
	 */
	public void startProtocol();
	
	/**
	 * Stops the protocol
	 */
	public void stopProtocol();
	
	/**
	 * Returns the repsonsible communication type
	 * @return
	 */
	public CommunicationType getCommunicationType();

	/**
	 * Notifies protocol about data to send
	 * @param context
	 * @param data
	 * @throws SendDataException 
	 */
	public void sendData(IConnectionContext context, Object data) throws SendDataException;
	
	
}
