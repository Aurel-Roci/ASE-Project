package de.tum.score.transport4you.mobile.communication.wificontroller.impl;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;
import de.tum.score.transport4you.mobile.shared.MobileIntents;

public class WiFiBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		IWiFiBroadcast wiFiBroadcast = WiFiControllerSingleton.getWiFiBroadcast(context);
		if (wiFiBroadcast == null) {
			return;
		}

        String action = intent.getAction();
		
		if(action.equals(MobileIntents.TEAR_DOWN)) {
			//TODO unregister receiver if tear down occurs
		}
		
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action) && !wiFiBroadcast.isBusWIFIFound()) {
        	Log.i("T4Y-WiFi", "Scanning for bus");
            // Get a list of wifi devices
            List<ScanResult> wifiDevices = wifiManager.getScanResults();
            
            if (wifiDevices.size() > 0) {
                for (ScanResult device : wifiDevices) {
                	
                	Log.i("T4Y", "WiFiController found wifi device ("+device.SSID+")");
                	
                    if (wiFiBroadcast.getBusSSID().equalsIgnoreCase(device.SSID)) {
                    	Log.i("T4Y", "Found bus wifi beacon");
                    	
                    	wiFiBroadcast.setBusWIFIFound(true);
                    	
                    	//send busfound broadcast intent
                    	Intent busFound = new Intent(MobileIntents.BUS_WIFI_FOUND);
                    	Log.i("T4Y", "WiFiController found wifi beacon");
                    	Log.e("T4Y", busFound.getAction());
                    	context.sendBroadcast(busFound);
                    }
                }
            }
        }
	}
}
