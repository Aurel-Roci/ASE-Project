package de.tum.score.transport4you.bus.communication.bluetoothcontroller.error;

public abstract class AbstractBluetoothException extends Exception{

	private static final long serialVersionUID = 5203691116041158359L;

	public AbstractBluetoothException(String message){
		super(message);
	}
	
}
