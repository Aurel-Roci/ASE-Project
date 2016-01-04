package de.tum.score.transport4you.bus.communication.connectionmanager.error;

/**
 * Represents an error that may happen while sending data
 * @author hoerning
 *
 */
public class SendDataException extends AbstractConnectionManagerException {

	private static final long serialVersionUID = 4199773804460695023L;

	public SendDataException(String message) {
		super(message);
	}

}
