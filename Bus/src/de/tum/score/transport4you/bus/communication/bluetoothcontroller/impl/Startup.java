package de.tum.score.transport4you.bus.communication.bluetoothcontroller.impl;

import org.apache.log4j.Logger;

import de.tum.score.transport4you.bus.communication.bluetoothcontroller.IStartup;

/**
 * Implements functionality to start up the component
 * @author hoerning
 *
 */
public class Startup implements IStartup{

	/* Used for storing the singleton instance */
	private static Startup instance = null;
	
	/* Logger */
	private Logger logger = Logger.getLogger("Communication");
	
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
		logger.debug("Bluetooth Controller Component initialized");
		
	}

}
