package de.tum.score.transport4you.bus.application.applicationcontroller.impl;

import org.apache.log4j.Logger;

import de.tum.score.transport4you.bus.application.applicationcontroller.IStartup;

/**
 * The class is responsible for initializing the component
 * @author hoerning
 *
 */
public class Startup implements IStartup{
	
	/* Used for storing the singleton instance */
	private static Startup instance = null;
	
	/* Logger */
	private Logger logger = Logger.getLogger("Application");
	
	/**
	 * Returns the singleton instance of this class
	 * @return
	 */
	public static IStartup getInstance() {
		if(instance==null){
			instance = new Startup();
		}
		
		return instance;
	}
	
	@Override
	public void init() {
		
		logger.debug("Initialzing additional controllers");
		CustomerServiceController.getInstance().initialize();
		logger.debug("New Controllers initialized");
	}

}
