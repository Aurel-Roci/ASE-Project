package de.tum.score.transport4you.mobile.communication.wificontroller;

public interface IWiFi {

	/**
	 * Wait for a bus by listening until a beacon with a specific ssid is received.
	 * 
	 * @param timeout The maximum timespan the system should wait for an ssid.
	 * @param ssid The ssid of the bus that should be waited for.
	 */
	public void waitForSSID(String ssid, Integer timeout);
}
