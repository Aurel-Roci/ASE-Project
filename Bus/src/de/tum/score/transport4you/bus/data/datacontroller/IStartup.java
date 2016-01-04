package de.tum.score.transport4you.bus.data.datacontroller;

import java.io.File;

import de.tum.score.transport4you.bus.data.datacontroller.error.DataControllerInitializingException;

/**
 * Describes interfaces to let the system start this component
 * @author hoerning
 *
 */
public interface IStartup {

	/**
	 * Loads the component.
	 * @param configurationFile - The file which is used as configuartion
	 * @throws DataControllerInitializingException 
	 */
	public void init(File configurationFile) throws DataControllerInitializingException;
}
