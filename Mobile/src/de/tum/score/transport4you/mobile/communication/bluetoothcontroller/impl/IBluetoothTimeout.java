package de.tum.score.transport4you.mobile.communication.bluetoothcontroller.impl;

public interface IBluetoothTimeout {

	public abstract boolean isTimeout();

	public abstract void setTimeout(boolean timeout);

}