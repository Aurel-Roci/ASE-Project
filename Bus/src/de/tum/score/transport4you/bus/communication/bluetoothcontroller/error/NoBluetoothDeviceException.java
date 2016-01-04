package de.tum.score.transport4you.bus.communication.bluetoothcontroller.error;

public class NoBluetoothDeviceException extends AbstractBluetoothException{

	private static final long serialVersionUID = -1368712866262360812L;

	public NoBluetoothDeviceException(String message) {
		super(message);
	}
}
