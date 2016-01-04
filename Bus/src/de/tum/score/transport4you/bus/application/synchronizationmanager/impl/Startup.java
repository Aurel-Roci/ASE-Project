package de.tum.score.transport4you.bus.application.synchronizationmanager.impl;

import de.tum.score.transport4you.bus.application.synchronizationmanager.IStartup;


/**
 * The class is responsible for initializing the component
 * @author hoerning
 *
 */
public class Startup implements IStartup{
	
	/* Used for storing the singleton instance */
	private static Startup instance = null;
	
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
		
		
	}

}
