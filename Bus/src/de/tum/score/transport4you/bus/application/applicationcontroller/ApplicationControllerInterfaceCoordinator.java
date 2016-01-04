package de.tum.score.transport4you.bus.application.applicationcontroller;

import de.tum.score.transport4you.bus.application.applicationcontroller.impl.Startup;
import de.tum.score.transport4you.bus.application.applicationcontroller.impl.System;

/**
 * This class allows other components to retrieve interfaces to this component
 * @author hoerning
 *
 */
public class ApplicationControllerInterfaceCoordinator {
	
	public static ISystem getSystem(){
		return System.getInstance();
	}

	/**
	 * Returns the startup class
	 * @return
	 */
	public static IStartup getStartup(){
		return Startup.getInstance();
	}
}
