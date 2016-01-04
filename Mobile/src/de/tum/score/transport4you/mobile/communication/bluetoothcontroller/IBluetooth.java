package de.tum.score.transport4you.mobile.communication.bluetoothcontroller;

import de.tum.score.transport4you.mobile.communication.bluetoothcontroller.error.BluetoothTransmissionException;
import de.tum.score.transport4you.mobile.communication.connectionmanager.ICommunication;
import de.tum.score.transport4you.shared.mobilebus.data.impl.message.BluetoothEnvelope;

public interface IBluetooth {
	
	/**
	 * Scans for the device with the provided bluetoothName.
	 * 
	 * @param bluetoothName
	 * @param maxRetries Maximum number of attempts to scan for the specified bluetoothname.
	 */
	public void scan(String bluetoothName, Integer maxRetries);
	
	/**
	 * Send a T4Y protocol bluetooth message to the communication partner.
	 * 
	 * @param message
	 */
	public void send(BluetoothEnvelope message) throws BluetoothTransmissionException ;
	
	/**
	 * Register to receive incoming messages.
	 * 
	 * @param receiver
	 */
	public void registerReceiver(ICommunication receiver);
	
	/**
	 * Tells the BluetoothController to reset all its connections and stop scanning.
	 */
	public void reset();
}
