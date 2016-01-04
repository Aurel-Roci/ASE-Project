package de.tum.score.transport4you.bus.application.synchronizationmanager;

import de.tum.score.transport4you.bus.application.synchronizationmanager.impl.Startup;

/**
 * This class allows other components to retrieve interfaces to this component
 * @author hoerning
 *
 */
public class SychronizationManagerInterfaceCoordinator {
	
	/**
	 * Returns the startup class
	 * @return
	 */
	public static IStartup getStartup(){
		return Startup.getInstance();
	}
}
