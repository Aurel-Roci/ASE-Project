package de.tum.score.transport4you.bus.application.applicationcontroller;

import java.util.concurrent.Executor;

/**
 * This interface provides access to system methods, like calling exit routines or accessing Thread Pools
 * @author hoerning
 *
 */
public interface ISystem {
	
	/**
	 * Returns the Executor used for Thread Controlling for the specified category
	 * @return
	 */
	public Executor getExecutor(String category);

}
