package de.tum.score.transport4you.mobile.communication.bluetoothcontroller.impl;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.UUID;

import de.tum.score.transport4you.mobile.communication.connectionmanager.ICommunication;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.os.ParcelUuid;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
public class BluetoothConn extends Thread implements IBluetoothTimeout {
    private BluetoothSocket mmSocket = null;
    private ArrayList<ICommunication> receivers;
    private BluetoothDevice mmDevice;
    private ConnectedThread connThread;
    private boolean timeout = true;
    
    public BluetoothConn(BluetoothDevice device, BluetoothAdapter mBtAdapter, ArrayList<ICommunication> receivers) {
    	
        BluetoothSocket tmp = null;
        mmDevice = device;
        this.receivers = receivers;
        
        Method insecureSocket = null;
      
        try {       	
            Log.i("T4Y", "Creating new connection socket");
            
            //Needed to get function via reflection in old Android version as it was not exported back then
            //insecureSocket = BluetoothDevice.class.getMethod("createInsecureRfcommSocket", new Class[] {Integer.TYPE});
        	//tmp = (BluetoothSocket) insecureSocket.invoke(mmDevice, new Integer(1));
        	
            ParcelUuid[] puuids = device.getUuids();
            
            UUID uuid = puuids[0].getUuid(); 
            
        	tmp = mmDevice.createInsecureRfcommSocketToServiceRecord(uuid);
        	
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
        if (tmp != null) {
        	mmSocket = tmp;
        } else {
        	Log.i("T4Y", "socket is null");
        }
    }

    public void run() {
        
        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mmSocket.connect();
            Log.i("T4Y", "Connecting to connection socket");
        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
        	Log.i("T4Y", "Connecting to connection socket failed!");
            try {
                mmSocket.close();
                BluetoothControllerSingleton.getBluetoothBroadcast(null).setBtDeviceFound(false);
            } catch (IOException closeException) { }
            return;
        }

        // Do work to manage the connection (in a separate thread)
        Log.i("T4Y", "Creating new connection thread");
        connThread = new ConnectedThread(mmSocket, receivers, this);
        connThread.start();
        
        Log.i("T4Y", "Creating new timeout thread");
        Thread timer = new Thread() {
        	public void run() {
        		boolean stop = false;
        		while (!stop) {
	        		try {
						sleep(20000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Log.e("T4Y", "Timeout check");
					if (isTimeout()) {
						Log.e("T4Y", "Timeout passed - interrupting thread");
						try {
							mmSocket.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						stop = true;
					} else {
						Log.e("T4Y", "Timeout not passed - resetting timeout");
						setTimeout(true);
					}
        		}
        	}
        };
        timer.start();
    }
    
    public ConnectedThread getConnectedThread () {
    	return connThread;
    }

    /** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
            mmSocket.close();
            BluetoothControllerSingleton.getBluetoothBroadcast(null).setBtDeviceFound(false);
            
        } catch (IOException e) { }
    }

	/* (non-Javadoc)
	 * @see de.tukl.score.transport4you.mobile.communication.bluetoothcontroller.impl.IBluetoothTimeout#isTimeout()
	 */
	@Override
	public synchronized boolean isTimeout() {
		return timeout;
	}

	/* (non-Javadoc)
	 * @see de.tukl.score.transport4you.mobile.communication.bluetoothcontroller.impl.IBluetoothTimeout#setTimeout(boolean)
	 */
	@Override
	public synchronized void setTimeout(boolean timeout) {
		this.timeout = timeout;
	}
    
    
    
}