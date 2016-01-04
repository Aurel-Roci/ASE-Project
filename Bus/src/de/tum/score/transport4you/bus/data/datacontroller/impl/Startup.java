package de.tum.score.transport4you.bus.data.datacontroller.impl;

import java.io.File;

import org.apache.log4j.Logger;

import de.tum.score.transport4you.bus.data.datacontroller.IStartup;
import de.tum.score.transport4you.bus.data.datacontroller.error.DataControllerInitializingException;

/**
 * This class is capable of handling the startup of the component
 * @author hoerning
 *
 */
public class Startup implements IStartup{

	/* Used for storing the singleton reference */
	private static Startup instance;
	
	private Logger logger = Logger.getLogger("Data");
	
	/**
	 * Returns the singelton instance of this class
	 * @return
	 */
	public static IStartup getInstance() {
		if(instance==null){
			instance = new Startup();
		}
		
		return instance;
	}

	@Override
	public void init(File configurationFile) throws DataControllerInitializingException {

		logger.debug("Starting the Data Controller Component");
		DataController.getInstance().init(configurationFile);
		PersistentDataController.getInstance();
		logger.debug("Data Controller Component started");
	}

}
