package de.tum.score.transport4you.bus.communication.connectionmanager.error;

/**
 * An abstract exception
 * @author hoerning
 *
 */
public class AbstractConnectionManagerException extends Exception{

	private static final long serialVersionUID = 2750576378801794420L;
	
	public AbstractConnectionManagerException(String message) {
		super(message);
	}

}
