package de.tum.score.transport4you.bus.communication.bluetoothcontroller;

import de.tum.score.transport4you.bus.communication.bluetoothcontroller.impl.BluetoothController;
import de.tum.score.transport4you.bus.communication.bluetoothcontroller.impl.Startup;


/**
 * This class allows other components to retrieve interfaces to this component
 * @author hoerning
 *
 */
public class BluetoothControllerInterfaceCoordinator {
	
	/* Initialization possibilities */
	private static IBluetooth bluetooth = null;

	/**
	 * Returns the startup class
	 * @return
	 */
	public static IStartup getStartup(){
		return Startup.getInstance();
	}
	
	/**
	 * Returns the class capable of Bluetooth device handling
	 * @return
	 */
	public static IBluetooth getBluetooth(){
		
		if(BluetoothControllerInterfaceCoordinator.bluetooth == null) {
			return BluetoothController.getInstance();
		} else {
			return BluetoothControllerInterfaceCoordinator.bluetooth;
		}
	}

	/**
	 * This method allows the modification of the standard passed IBluetooth interface
	 * @param bluetooth
	 */
	protected static void setBluetooth(IBluetooth bluetooth){
		BluetoothControllerInterfaceCoordinator.bluetooth = bluetooth;
	}
	
}
