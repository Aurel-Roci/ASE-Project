package de.tukl.score.transport4you.bus.communication.bluetoothcontroller;

import de.tukl.score.transport4you.bus.communication.bluetoothcontroller.error.AbstractBluetoothException;
import de.tukl.score.transport4you.bus.data.datacontroller.data.BluetoothConfiguration;

public class BluetoothStub implements IBluetooth{
	
	private BluetoothConfiguration bluetoothConfiguration;
	private IBluetoothListener callback;
	
	public BluetoothStub(){
		//register
		BluetoothControllerInterfaceCoordinator.setBluetooth(this);
	}

	@Override
	public void listenOnConnection(BluetoothConfiguration bluetoothConfiguration,IBluetoothListener callback) throws AbstractBluetoothException {

		this.bluetoothConfiguration = bluetoothConfiguration;
		this.callback = callback;
		
	}
	
	@Override
	public void stopListenOnConnection() {
		// TODO Auto-generated method stub
		
	}

	public BluetoothConfiguration getBluetoothConfiguration() {
		return bluetoothConfiguration;
	}

	public void setBluetoothConfiguration(
			BluetoothConfiguration bluetoothConfiguration) {
		this.bluetoothConfiguration = bluetoothConfiguration;
	}

	public IBluetoothListener getCallback() {
		return callback;
	}

	public void setCallback(IBluetoothListener callback) {
		this.callback = callback;
	}
	
	

}
