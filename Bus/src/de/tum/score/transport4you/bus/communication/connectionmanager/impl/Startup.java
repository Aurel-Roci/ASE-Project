package de.tum.score.transport4you.bus.communication.connectionmanager.impl;

import org.apache.log4j.Logger;

import de.tum.score.transport4you.bus.communication.bluetoothcontroller.BluetoothControllerInterfaceCoordinator;
import de.tum.score.transport4you.bus.communication.connectionmanager.IStartup;
import de.tum.score.transport4you.bus.communication.connectionmanager.impl.protocol.mobilesystem.MobileSystemListeningProtocol;

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
		
		logger.debug("Initializing subcomponents: Bluetooth Controller");
		BluetoothControllerInterfaceCoordinator.getStartup().init();
		logger.debug("Initializung subcomponents: Data Connection Controller");
		
		logger.debug("Initializing Connection Manager Component");
		logger.debug("Adding MobileSystemListenerProtocol");
		ConnectionController.getInstance().addProtocol(new MobileSystemListeningProtocol());
		ConnectionController.getInstance().startProtocols();
		
	}
	

}
