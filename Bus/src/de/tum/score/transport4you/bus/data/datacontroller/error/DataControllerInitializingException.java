package de.tum.score.transport4you.bus.data.datacontroller.error;

/**
 * Represents the exception that there was a problem while loading the configuration
 * @author hoerning
 *
 */
public class DataControllerInitializingException extends AbstractDataException{
	
	private static final long serialVersionUID = 8341589408493873626L;

	public DataControllerInitializingException(String message){
		super(message);
	}

}
