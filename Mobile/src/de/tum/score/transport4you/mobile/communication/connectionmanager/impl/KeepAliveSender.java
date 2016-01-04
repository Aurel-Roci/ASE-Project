package de.tum.score.transport4you.mobile.communication.connectionmanager.impl;

import de.tum.score.transport4you.mobile.communication.connectionmanager.error.BluetoothConnectionLostException;
import de.tum.score.transport4you.shared.mobilebus.data.impl.message.BluetoothEnvelope;
import de.tum.score.transport4you.shared.mobilebus.data.impl.message.KeepAliveMessage;

public class KeepAliveSender extends Thread {

	private boolean sendKeepAlive = true;
	private ConnectionManager connectionManager;
	private long KEEP_ALIVE_INTERVAL = 3000;
	
	public KeepAliveSender(ConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
	}
	
	public void run() {
		KeepAliveMessage keepAlive = new KeepAliveMessage();
		BluetoothEnvelope envelope = new BluetoothEnvelope(keepAlive);
		
		while(sendKeepAlive) {
			try {
				connectionManager.send(envelope);
				Thread.sleep(KEEP_ALIVE_INTERVAL);
			} catch (InterruptedException e) {
				sendKeepAlive = false;
				e.printStackTrace();
			} catch (BluetoothConnectionLostException e) {
				sendKeepAlive = false;
			}
		}
	}

}
