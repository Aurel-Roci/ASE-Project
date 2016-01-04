package de.tum.score.transport4you.bus.data.datacontroller.error;

/**
 * The core exception class of this component
 * @author hoerning
 *
 */
public abstract class AbstractDataException extends Exception{

		private static final long serialVersionUID = -542657804094472650L;
		
		public AbstractDataException(String message){
			super(message);
		}

}
