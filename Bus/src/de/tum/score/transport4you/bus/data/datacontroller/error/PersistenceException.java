package de.tum.score.transport4you.bus.data.datacontroller.error;

/**
 * Represents the exception if the Persistence Controller has an error
 * @author hoerning
 *
 */
public class PersistenceException extends AbstractDataException{

	private static final long serialVersionUID = -6422573346591155291L;

	public PersistenceException(String message){
		super(message);
	}

}
