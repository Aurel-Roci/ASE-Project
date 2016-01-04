package de.tum.score.transport4you.mobile.communication.connectionmanager;

import de.tum.score.transport4you.mobile.application.ICommunicationListener;
import de.tum.score.transport4you.mobile.communication.connectionmanager.error.BluetoothConnectionLostException;
import de.tum.score.transport4you.shared.mobilebus.data.impl.message.BluetoothEnvelope;

public interface ICommunication{
	
	/**
	 * Initiates the connection with a bus system
	 */
	public void initiateBusConnection();
	
	/**
	 * Resets all bus connections along with their corresponding bluetooth connections.
	 */
	public void resetConnection();
	
	/**
	 * Synchronizes with the web system
	 */
	public void synchronize();
	
	/**
	 * Registeres a new component.
	 * @param component
	 */
	public void registerComponent(ICommunicationListener component);
	
	/**
	 * Receives a new bluetooth message coming from a bus system
	 * @param message The received message
	 */
	public void receive(BluetoothEnvelope message);
	
	/**
	 * Send a new bluetooth message
	 * @param message
	 * @throws BluetoothConnectionLostException 
	 */
	public void send(BluetoothEnvelope message) throws BluetoothConnectionLostException;
}
