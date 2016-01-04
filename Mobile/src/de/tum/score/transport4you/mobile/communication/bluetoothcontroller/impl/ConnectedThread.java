package de.tum.score.transport4you.mobile.communication.bluetoothcontroller.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.bluetooth.BluetoothSocket;
import android.util.Log;
import de.tum.score.transport4you.mobile.communication.bluetoothcontroller.error.BluetoothTransmissionException;
import de.tum.score.transport4you.mobile.communication.connectionmanager.ICommunication;
import de.tum.score.transport4you.shared.mobilebus.data.impl.message.BluetoothEnvelope;

public class ConnectedThread extends Thread {
    private final BluetoothSocket mmSocket;
    private InputStream mmInStream;
    private OutputStream mmOutStream;
    private ObjectInputStream objectInStream;
    private ObjectOutputStream objectOutStream;
    private ArrayList<ICommunication> receivers;
    private boolean stop = false;
    private IBluetoothTimeout bluetoothTimeout;
    
    public ConnectedThread(BluetoothSocket socket, ArrayList<ICommunication> receivers, IBluetoothTimeout bluetoothTimeout) {
        mmSocket = socket;
        this.receivers = receivers;
        this.bluetoothTimeout = bluetoothTimeout;
        
        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
        	mmInStream = socket.getInputStream();
        	mmOutStream = socket.getOutputStream();
            
            objectInStream = new ObjectInputStream(mmInStream);
            objectOutStream = new ObjectOutputStream(mmOutStream);
            
        } catch (IOException e) { }
     
    }

    public void run() {
        Log.i("T4Y", "New connection thread created");
        
        //Keep listening to the InputStream until an exception occurs
        new Thread() {
        	public void run() {
	        	while (!stop) {
		            try {
		            	if(objectInStream == null) {
		            		cancel();
		            		return;
		            	}
		            	
		                // Read from the InputStream
		            	BluetoothEnvelope bufferObj = null;
		            	Object incomingObject = objectInStream.readObject();
		            	bluetoothTimeout.setTimeout(false);
		            	Log.i("T4Y", "Object received - [" + incomingObject.getClass().getName() + "]");
		            	
		            	if(incomingObject == null) break;
		            	
		            	if (incomingObject instanceof BluetoothEnvelope) {
		            		bufferObj = (BluetoothEnvelope) incomingObject;
		            	} else {
		            		Log.i("T4Y", "Unknown incoming BTmessage - type is: "+incomingObject.getClass());
		            		break;
		            	}
		               
		            	for(ICommunication com : receivers) {
		            		if(com != null) com.receive(bufferObj);
		            	}		        
		            } catch (NullPointerException e1) {
		            	Log.e("T4Y", "Bluetooth Connection closed (NullPointerException)");
		            	stop = true;
		            	for(ICommunication com : receivers) {
		            		com.receive(null);
		            	}
		            } catch (IOException e) {
		            	Log.e("T4Y", "Bluetooth Connection closed (IOException)");
		            	stop = true;
		            	for(ICommunication com : receivers) {
		            		com.receive(null);
		            	}	
		            } catch (ClassNotFoundException e) {
		            	Log.e("T4Y", "Bluetooth Connection closed (ClassNotFoundException)");
		            	stop = true;
		            	for(ICommunication com : receivers) {
		            		com.receive(null);
		            	}	
					}
		        }
        	}
        }.start();
    }

    /* Call this from the main Activity to send data to the remote device */
    public void write(BluetoothEnvelope obj) throws BluetoothTransmissionException {
    	Log.i("T4Y", "Sending message");
    	if(obj == null) {
    		cancel();
    		return;
    	}
        try {
        	Log.i("T4Y", "Object sent - [" + obj.getClass().getName() + "]");
        	objectOutStream.writeObject(obj);
        } catch (IOException e) { 
        	throw new BluetoothTransmissionException("Bluetooth sending failed - maybe the connection is crashed?");
        }
    }
    
    /* Call this from the main Activity to shutdown the connection */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
}