package de.tum.score.transport4you.bus.communication.connectionmanager.impl.protocol.mobilesystem;


import org.apache.log4j.Logger;

import de.tum.score.transport4you.bus.communication.bluetoothcontroller.BluetoothControllerInterfaceCoordinator;
import de.tum.score.transport4you.bus.communication.bluetoothcontroller.IBluetooth;
import de.tum.score.transport4you.bus.communication.bluetoothcontroller.IBluetoothListener;
import de.tum.score.transport4you.bus.communication.bluetoothcontroller.data.BluetoothConnection;
import de.tum.score.transport4you.bus.communication.bluetoothcontroller.error.AbstractBluetoothException;
import de.tum.score.transport4you.bus.communication.connectionmanager.CommunicationType;
import de.tum.score.transport4you.bus.communication.connectionmanager.IConnectionContext;
import de.tum.score.transport4you.bus.communication.connectionmanager.error.SendDataException;
import de.tum.score.transport4you.bus.communication.connectionmanager.impl.protocol.IProtocol;
import de.tum.score.transport4you.bus.data.datacontroller.DataControllerInterfaceCoordinator;
import de.tum.score.transport4you.bus.data.datacontroller.data.BluetoothConfiguration;

/**
 * This class represents the protocol that is used to act on incoming requests of the Bluetooth Controller
 * @author hoerning
 *
 */
public class MobileSystemListeningProtocol implements IProtocol{
	
	/* Logger*/
	private Logger logger = Logger.getLogger("Communication");
	
	/* The Manager Thread */
	private MobileSystemConnectionManager manager = new MobileSystemConnectionManager();
	
	/* Remembers if protocol is started */
	private boolean isStarted = false;
	
	private class ProtocolBluetoothListener implements IBluetoothListener{

		@Override
		public void incomingBluetoothConnection(BluetoothConnection connection) {

			logger.info("New incoming bluetooth connection. Initializing communication protocol.");
			manager.openConnection(connection);
		}
		
	}
	
	/* Callback Listener */
	private ProtocolBluetoothListener protocolBluetoothListener = new ProtocolBluetoothListener();
	
	
	@Override
	public void startProtocol() {
		
		if(isStarted) {
			//Already end first and restart again
			//TODO
		}
		
		logger.debug("Starting Mobile System Listening Protocol");
		
		//Retrieve the configuration to be used
		BluetoothConfiguration bluetoothConfiguration = DataControllerInterfaceCoordinator.getSettingsDataController().getBluetoothConfiguration();
		
		// Tell the device to start listening on new connections
		IBluetooth bluetooth = BluetoothControllerInterfaceCoordinator.getBluetooth();
		try {
			bluetooth.listenOnConnection(bluetoothConfiguration, protocolBluetoothListener);
		} catch (AbstractBluetoothException e) {
			logger.warn("Error while initializing Bluetooth, Mobile System Listening Protocol could not been loaded");
			return;
		}
		
		isStarted = true;
		logger.debug("Mobile System Listening Protocol started");
	}

	
	@Override
	public synchronized void stopProtocol() {
		// TODO Auto-generated method stub
		throw new RuntimeException("not implemented yet");
	}


	@Override
	public CommunicationType getCommunicationType() {
		return CommunicationType.MOBILE_SYSTEM;
	}


	@Override
	public void sendData(IConnectionContext context, Object data) throws SendDataException {
		
		MobileSystemConnection mobileSystemConnection = (MobileSystemConnection) context;
		if(!mobileSystemConnection.isActive()) {
			logger.error("Connection with id "+mobileSystemConnection.getConnectionId()+" is not active any more or was never active");
			throw new SendDataException("Connection is not active, it may already be closed");
		}
		
		logger.debug("Passing data by to Mobile System Connection "+mobileSystemConnection.getConnectionId());
		mobileSystemConnection.sendMessage(data);
		
	}

}
