package de.tum.score.transport4you.mobile.communication.wificontroller.impl;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;
import de.tum.score.transport4you.mobile.communication.wificontroller.IWiFi;
import de.tum.score.transport4you.mobile.shared.MobileIntents;

public class WiFiController extends Service implements IWiFi, IWiFiBroadcast {

	private WifiManager wifiManager;
	private String busSSID = "";
	private Integer scanRetries = 30; //time until scanning process is canceled
	private Context context;
	
	//indicates if a bus wifi beacon was detected
	private boolean busWIFIFound = false;
	
	private WiFiController() {
		
	}
	
	WiFiController(Context cntxt) {
		context = cntxt;
		Log.i("T4Y", "Initializing WiFiController");
        // Get the local Wifi adapter
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
        	wifiManager.setWifiEnabled(true);
        }
	}
	
	@Override
	public void waitForSSID(String ssid, Integer retries) {
		// Register for broadcasts when wifi scan result is there
		
		this.busSSID = ssid;
		this.scanRetries = retries;
		
		setBusWIFIFound(false);
		
		new Thread (){
	        public void run() {	      	
		        for(int i=0; i < scanRetries; i++) {
		        	if(isBusWIFIFound()) break;
		        	try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Log.i("T4Y", "WiFiController starts new beacon scan (" + busSSID + ")");
		        	wifiManager.startScan();
		        }
		        
		        if(!busWIFIFound) {
			        Log.i("T4Y", "WiFiController stopped scanning for beacons");
			        
			        //send bus wifi timeout intent
	            	Intent busWIFITimeout = new Intent(MobileIntents.BUS_WIFI_TIMEOUT);
	            	context.sendBroadcast(busWIFITimeout);
		        }
	        }
        }.start();
	}
    
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.tukl.score.transport4you.mobile.communication.wificontroller.impl.IWifiBroadcast#isBusWIFIFound()
	 */
	@Override
	public boolean isBusWIFIFound() {
		return busWIFIFound;
	}

	/* (non-Javadoc)
	 * @see de.tukl.score.transport4you.mobile.communication.wificontroller.impl.IWifiBroadcast#setBusWIFIFound(boolean)
	 */
	@Override
	public void setBusWIFIFound(boolean busWIFIFound) {
		this.busWIFIFound = busWIFIFound;
	}

	/* (non-Javadoc)
	 * @see de.tukl.score.transport4you.mobile.communication.wificontroller.impl.IWifiBroadcast#getBusSSID()
	 */
	@Override
	public String getBusSSID() {
		return busSSID;
	}
	
}
