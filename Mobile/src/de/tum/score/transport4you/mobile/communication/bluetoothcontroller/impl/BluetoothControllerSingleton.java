package de.tum.score.transport4you.mobile.communication.bluetoothcontroller.impl;

import android.content.Context;
import de.tum.score.transport4you.mobile.communication.bluetoothcontroller.IBluetooth;

public class BluetoothControllerSingleton {
private static BluetoothController bluetoothController;

	public static IBluetooth getBluetooth(Context cntxt) {
		if (bluetoothController == null) {
			bluetoothController = new BluetoothController(cntxt);
		}
		return bluetoothController;
	}
	
	public static IBluetoothBroadcast getBluetoothBroadcast (Context cntxt) {
		return bluetoothController;
	}
	
	
}
