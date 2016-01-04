package de.tum.score.transport4you.mobile.communication.bluetoothcontroller.impl;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import de.tum.score.transport4you.mobile.shared.MobileIntents;

public class BluetoothBroadcastReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		IBluetoothBroadcast bluetoothBroadcast = BluetoothControllerSingleton.getBluetoothBroadcast(context);
		if (bluetoothBroadcast == null) {
			return;
		}
		BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		String action = intent.getAction();
		
		if(action.equals(MobileIntents.TEAR_DOWN)) {
			//TODO unregister receiver if tear down occurs
		}
		
        // When discovery finds a device
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
        	
            // Get the BluetoothDevice object from the Intent
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            
            if (bluetoothBroadcast.getBluetoothName().equals(device.getName())) {
            	Log.i("T4Y", "Found Bluetooth device");
            	mBtAdapter.cancelDiscovery();
            	String busAddress;
                busAddress = device.getAddress().toString();
                bluetoothBroadcast.connect(busAddress);
                bluetoothBroadcast.setBtDeviceFound(true);
                return;
            }
            
        // When discovery is finished, change the Activity title
        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action) && !bluetoothBroadcast.isBtDeviceFound()) {
        	if (bluetoothBroadcast.getRetries() > 0) {
        		Log.i("T4Y", "BluetoothController tries to connect to bus ("+bluetoothBroadcast.getRetries()+")");
        		bluetoothBroadcast.decrementRetries();
        		mBtAdapter.startDiscovery();
        	} else {
        		Log.i("T4Y", "BluetoothController stopped trying to connect to bus");
        		
            	//send bus bt timeout intent
            	Intent busBTTimeout = new Intent(MobileIntents.BUS_BT_TIMEOUT);
            	context.sendBroadcast(busBTTimeout);
        		
        		mBtAdapter.cancelDiscovery();
        		return;
        	}
        }
	}

}
