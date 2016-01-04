package de.tum.score.transport4you.bus.communication.bluetoothcontroller;

import de.tum.score.transport4you.bus.communication.bluetoothcontroller.error.AbstractBluetoothException;
import de.tum.score.transport4you.bus.data.datacontroller.data.BluetoothConfiguration;

public interface IBluetooth {
	
	/**
	 * Listen on a given url and pass by connection if established
	 * @param bluetoothConfiguration
	 * @param callback
	 * @throws AbstractBluetoothException 
	 */
	public void listenOnConnection(BluetoothConfiguration bluetoothConfiguration,IBluetoothListener callback) throws AbstractBluetoothException;

	/**
	 * Stop Listening
	 */
	public void stopListenOnConnection();
	
}
