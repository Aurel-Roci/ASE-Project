package de.tum.score.transport4you.shared.mobilebus.data.impl.message;

import java.io.Serializable;

/**
 * Represents the envelope of the messages transfered between the Bluetooth Connections between Mobile and Web System
 * @author hoerning
 */
public class BluetoothEnvelope implements Serializable{

	private static final long serialVersionUID = 814369336831736037L;
	
	private BluetoothData data;
	
	/**
	 * Creates an envelope containing the specified data
	 * @param bluetoothData
	 */
	public BluetoothEnvelope(BluetoothData bluetoothData){
		this.data = bluetoothData;
	}

	/* Getter */
	public BluetoothData getData() {
		return data;
	}
		
}
