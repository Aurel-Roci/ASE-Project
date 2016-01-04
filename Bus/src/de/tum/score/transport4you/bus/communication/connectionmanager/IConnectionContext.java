package de.tum.score.transport4you.bus.communication.connectionmanager;

/**
 * This context contains information about the connection propagated to a listener. It can be used to uniquely identify the sender of data.
 * @author hoerning
 *
 */
public interface IConnectionContext {
	
	/**
	 * Returns the communication type associated with this context
	 * @return
	 */
	public CommunicationType getCommunicationType();

}
