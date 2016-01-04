package de.tum.score.transport4you.bus.data.datacontroller.error;

/**
 * Represents the exception if the DataController was already initialized
 * @author hoerning
 *
 */
public class ConfigurationLoadingException extends AbstractDataException{

	private static final long serialVersionUID = -3819803982782293921L;
	
	public ConfigurationLoadingException(String message){
		super(message);
	}

}
