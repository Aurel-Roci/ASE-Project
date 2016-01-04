package de.tum.score.transport4you.mobile.communication.wificontroller.impl;

public interface IWiFiBroadcast {

	public abstract boolean isBusWIFIFound();

	public abstract void setBusWIFIFound(boolean busWIFIFound);

	public abstract String getBusSSID();

}