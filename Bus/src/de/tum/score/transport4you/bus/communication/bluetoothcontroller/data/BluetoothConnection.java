package de.tum.score.transport4you.bus.communication.bluetoothcontroller.data;


import javax.microedition.io.StreamConnection;

/**
 * The class which represents a Bluetooth Connection
 * @author hoerning
 *
 */
public class BluetoothConnection {
	
	/* The native connection class */
	private StreamConnection streamConnection;
	
	/**
	 * Constructor: Needs a native Connection
	 * @param streamConnection
	 */
	public BluetoothConnection(StreamConnection streamConnection){
		this.streamConnection = streamConnection;
	}

	public StreamConnection getStreamConnection() {
		return streamConnection;
	}
	
}
