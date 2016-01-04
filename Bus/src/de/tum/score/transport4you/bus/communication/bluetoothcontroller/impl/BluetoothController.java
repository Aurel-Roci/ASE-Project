package de.tum.score.transport4you.bus.communication.bluetoothcontroller.impl;

import java.io.IOException;
import java.util.ArrayList;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnectionNotifier;

import org.apache.log4j.Logger;

import de.tum.score.transport4you.bus.application.applicationcontroller.ApplicationControllerInterfaceCoordinator;
import de.tum.score.transport4you.bus.communication.bluetoothcontroller.IBluetooth;
import de.tum.score.transport4you.bus.communication.bluetoothcontroller.IBluetoothListener;
import de.tum.score.transport4you.bus.communication.bluetoothcontroller.data.BluetoothConnection;
import de.tum.score.transport4you.bus.communication.bluetoothcontroller.error.AbstractBluetoothException;
import de.tum.score.transport4you.bus.communication.bluetoothcontroller.error.BluetoothStackException;
import de.tum.score.transport4you.bus.communication.bluetoothcontroller.error.NoBluetoothDeviceException;
import de.tum.score.transport4you.bus.data.datacontroller.data.BluetoothConfiguration;

/**
 * The platform specific control class for the Bluetooth Device
 * @author hoerning
 *
 */
public class BluetoothController implements IBluetooth{

	/* Used for storing the singleton instance */
	private static BluetoothController instance = null;
	
	/* Logger */
	private Logger logger = Logger.getLogger("Communication");
	
	/* Used for handling Bluetooth Connections */
	private LocalDevice localDevice;
	private StreamConnectionNotifier streamConnectionNotifier;

	/* Callback interface for incomming connections */
	private IBluetoothListener bluetoothListener;
	
	/* List of all active Bluetooth Connection Listener Threads */
	private ArrayList<BluetoothConnectionListener> bluetoothConnectionListenerList = new ArrayList<BluetoothConnectionListener>();
	
	/**
	 * Returns the singleton instance of this class
	 * @return
	 */
	public static BluetoothController getInstance() {
		if(instance==null){
			instance = new BluetoothController();
		}
		
		return instance;
	}

	@Override
	public void stopListenOnConnection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void listenOnConnection(BluetoothConfiguration bluetoothConfiguration,IBluetoothListener callback) throws AbstractBluetoothException {

		//Store Callback
		this.bluetoothListener = callback;
		
		//Initialize Bluetooth
		logger.debug("Initializing Bluetooth");
		try {
			this.initBluetooth();
		} catch (BluetoothStateException e) {
			logger.error("Error using the Bluetooth Stack");
			throw new BluetoothStackException(e.getMessage());
		} 
		
		//Generate UUID out of BluetoohConfiguration
		final UUID MYSERVICEUUID_UUID = new UUID(bluetoothConfiguration.getServiceUUID(), false);
		// Define the server connection URL
		String connURL = "btspp://"+bluetoothConfiguration.getServerName()+":"+MYSERVICEUUID_UUID.toString()+";"+"name="+bluetoothConfiguration.getServiceName();
		
		logger.debug("Bluetooth Connection URL is: "+connURL);
		
		// Create a server connection (a notifier)
		try {
			streamConnectionNotifier = (StreamConnectionNotifier) Connector.open(connURL);
		} catch (IOException e) {
			logger.error("Error while opening connection URL");
			throw new BluetoothStackException(e.getMessage());
		}
		
		//Start a new listener Thread
		logger.debug("Starting new listener thread.");
		BluetoothConnectionListener connectionListener = new BluetoothConnectionListener(streamConnectionNotifier);
		this.bluetoothConnectionListenerList.add(connectionListener);
		ApplicationControllerInterfaceCoordinator.getSystem().getExecutor("BluetoothConnections").execute(connectionListener);
		
		logger.debug("Listening on connection procedure completed");
	}

	private void initBluetooth() throws BluetoothStateException, NoBluetoothDeviceException {

		//Reset
		localDevice = null;
	    
	    // Retrieve the local device to get to the Bluetooth Manager
	    localDevice = LocalDevice.getLocalDevice();
	    
	    if(localDevice == null) {
	    	logger.error("No Bluetooth Device found");
	    	throw new NoBluetoothDeviceException("No Bluetooth Device found");
	    }
	    logger.debug("Bluetooth Device Information - Address: "+localDevice.getBluetoothAddress()+" - Name: "+localDevice.getFriendlyName());
	
	    // Servers set the discoverable mode to GIAC
	    localDevice.setDiscoverable(DiscoveryAgent.GIAC);
	    // Clients retrieve the discovery agent
	    localDevice.getDiscoveryAgent();
		    
	}

	/**
	 * Called when there is a new established connection
	 * Passing a <i>null</i> connection means that there was an Error.
	 * @param bluetoothConnection
	 */
	protected synchronized void connectionEstablished(BluetoothConnection bluetoothConnection) {
	
		//Pass connection to listener
		this.bluetoothListener.incomingBluetoothConnection(bluetoothConnection);
		
		logger.debug("Ending listener thread ("+Thread.currentThread().getId()+")");
		
		//Start a new listener thread
		logger.debug("Starting new listener thread.");
		BluetoothConnectionListener connectionListener = new BluetoothConnectionListener(streamConnectionNotifier);
		this.bluetoothConnectionListenerList.add(connectionListener);
		ApplicationControllerInterfaceCoordinator.getSystem().getExecutor("BluetoothConnections").execute(connectionListener);
		
	}
	
}
