package de.tum.score.transport4you.mobile.communication.bluetoothcontroller.impl;

import java.util.ArrayList;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import de.tum.score.transport4you.mobile.communication.bluetoothcontroller.IBluetooth;
import de.tum.score.transport4you.mobile.communication.bluetoothcontroller.error.BluetoothTransmissionException;
import de.tum.score.transport4you.mobile.communication.connectionmanager.ICommunication;
import de.tum.score.transport4you.mobile.shared.MobileIntents;
import de.tum.score.transport4you.shared.mobilebus.data.impl.message.BluetoothEnvelope;

public class BluetoothController extends Service implements IBluetooth, IBluetoothBroadcast {

	private Integer maxRetries;
    private BluetoothAdapter mBtAdapter;
    private String bluetoothName;
    private ConnectedThread busCommunicationThread;
    private Context context;
    
    private ArrayList<ICommunication> receivers = new ArrayList<ICommunication>();
	private Integer retries = 3;
	private boolean btDeviceFound = false;
	
	private BluetoothController() {
		
	}
	
	BluetoothController(Context cntxt) {
		Log.i("T4Y", "Initializing BluetoothController");
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		context = cntxt;
	}
	
	@Override
	public void scan(String bluetoothName, Integer maxRetries) {
		this.bluetoothName = bluetoothName;
		this.maxRetries = maxRetries;
		
		this.retries = this.maxRetries;
		
		Log.i("T4Y", "Start BT scan");
		
		
    	// If Bluetooth service is not enabled then enable it
        if (!mBtAdapter.isEnabled()) {
        	mBtAdapter.enable();
            // If we're already discovering, stop it
            if (mBtAdapter.isDiscovering()) {
                mBtAdapter.cancelDiscovery();
            }
            
            while (!mBtAdapter.isEnabled()) {
            	
            }
        	
        }
        
        // Request discover from BluetoothAdapter
        mBtAdapter.startDiscovery();

	}

	public void connect(String address) {
		BluetoothDevice device;
		
		try {
			device = mBtAdapter.getRemoteDevice(address);
			
	        // Attempt to connect to the device
	        Log.i("T4Y", "Connecting to bus...");
	        BluetoothConn btConnection = new BluetoothConn(device, mBtAdapter, receivers);
	        btConnection.start();
	        
	        while(btConnection.getConnectedThread() == null) {
	        	Thread.sleep(10);
	        }
	        
	        busCommunicationThread = btConnection.getConnectedThread();
	        
        	//send bus bt connection established intent
        	Intent busFound = new Intent(MobileIntents.BUS_BT_FOUND);
        	context.sendBroadcast(busFound);
	        
		} catch (Exception e) {
			Toast.makeText(this, "Can't connect to " + address, Toast.LENGTH_SHORT).show();
		}
       
	}

	@Override
	public synchronized void send(BluetoothEnvelope message) throws BluetoothTransmissionException {
		busCommunicationThread.write(message);
		if(message == null) busCommunicationThread = null;
	}

	@Override
	public void registerReceiver(ICommunication receiver) {
		receivers.add(receiver);
		
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
    @Override
	public void onDestroy() {
        super.onDestroy();

        // Make sure we're not doing discovery anymore
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }

        // Unregister broadcast listeners
        //this.unregisterReceiver(mReceiver);
    }

	@Override
	public void reset() {
		this.retries = this.maxRetries;
		busCommunicationThread = null;
		mBtAdapter.cancelDiscovery();
	}



	public Integer getRetries() {
		return retries;
	}

	public void decrementRetries() {
		retries--;
	}
	
	public boolean isBtDeviceFound() {
		return btDeviceFound;
	}

	public void setBtDeviceFound(boolean btDeviceFound) {
		this.btDeviceFound = btDeviceFound;
	}

	/* (non-Javadoc)
	 * @see de.tukl.score.transport4you.mobile.communication.bluetoothcontroller.impl.IBluetoothblas#getBluetoothName()
	 */
	@Override
	public String getBluetoothName() {
		return bluetoothName;
	}
	
	


}
