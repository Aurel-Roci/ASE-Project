package de.tum.score.transport4you.mobile.communication.bluetoothcontroller.impl;

public interface IBluetoothBroadcast {

	public abstract String getBluetoothName();
	
	public void connect(String address);
	
	public Integer getRetries();

	public void decrementRetries();
	
	public boolean isBtDeviceFound();

	public void setBtDeviceFound(boolean btDeviceFound);
}