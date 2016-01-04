package de.tum.score.transport4you.mobile.communication.connectionmanager.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import de.tum.score.transport4you.mobile.application.applicationcontroller.IMainApplication;
import de.tum.score.transport4you.mobile.communication.bluetoothcontroller.IBluetooth;
import de.tum.score.transport4you.mobile.communication.bluetoothcontroller.impl.BluetoothControllerSingleton;
import de.tum.score.transport4you.mobile.shared.MobileIntents;

public class ConnectionBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		IConnectionBroadcast connectionBroadcast = ConnectionManagerSingleton.getConnectionBroadcast(context);
		if (connectionBroadcast == null) {
			return;
		}

		String action = intent.getAction();
		IBluetooth btController = BluetoothControllerSingleton.getBluetooth(context);
		IMainApplication mainApplication = connectionBroadcast.getMainApplication();
		
		if(action.equals(MobileIntents.TEAR_DOWN)) {
			//TODO unregister receiver if tear down occurs
		}
		
		if(action.equals(MobileIntents.BUS_WIFI_FOUND)) {
			//if bus wifi beacon is found start bt scanning procedure
			btController.scan(connectionBroadcast.getBUS_BT_NAME(), ConnectionManager.getBusBtScanRetries());
			Log.i("T4Y", "Bus wifi beacon found => start scanning for bt device...");
			mainApplication.sendStatusBarNotification("T4Y: Bus found","A bus WiFi beacon was found");
		}
		
		if(action.equals(MobileIntents.BUS_BT_FOUND)) {
			//if bus bt connection is established allow sending
			Log.i("T4Y", "Bus bt connection established");
			mainApplication.sendStatusBarNotification("T4Y: Bus found","A bus bluetooth device was found");
		}
		
		if(action.equals(MobileIntents.BUS_WIFI_TIMEOUT)) {
			//enable scan button if wifi timeout occurs
			Log.i("T4Y", "Bus wifi timeout");
			mainApplication.sendStatusBarNotification("T4Y: Connection failed","A timeout occured");
			mainApplication.resetConnection("A WiFi timeout occured");
		}
		
		if(action.equals(MobileIntents.BUS_BT_TIMEOUT)) {
			//enable scan button if bt timeout occurs
			Log.i("T4Y", "Bus bt timeout");
			mainApplication.sendStatusBarNotification("T4Y: Connection failed","A timeout occured");
			mainApplication.resetConnection("A BT timeout occured");
		}
	}
}
