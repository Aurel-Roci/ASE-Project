package de.tum.score.transport4you.mobile.communication.wificontroller.impl;

import de.tum.score.transport4you.mobile.communication.wificontroller.IWiFi;
import android.content.Context;

public class WiFiControllerSingleton {
	private static WiFiController wiFiController;

	public static IWiFi getWiFi(Context cntxt) {
		if (wiFiController == null) {
			wiFiController = new WiFiController(cntxt);
		}
		return wiFiController;
	}
	
	public static IWiFiBroadcast getWiFiBroadcast (Context cntxt) {
		return wiFiController;
	}
}
