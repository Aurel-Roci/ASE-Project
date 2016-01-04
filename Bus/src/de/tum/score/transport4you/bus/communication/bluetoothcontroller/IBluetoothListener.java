package de.tum.score.transport4you.bus.communication.bluetoothcontroller;

import de.tum.score.transport4you.bus.communication.bluetoothcontroller.data.BluetoothConnection;

/**
 * Callback interface for passing by incoming connections after started listening
 * @author hoerning
 *
 */
public interface IBluetoothListener {

	/**
	 * Pass by an incoming connection. Note that the current Thread has to terminate in order to enable listening again.
	 * @param connection
	 */
	public void incomingBluetoothConnection(BluetoothConnection connection);
	
	
}
