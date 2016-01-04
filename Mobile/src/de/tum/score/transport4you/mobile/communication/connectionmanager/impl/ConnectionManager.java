package de.tum.score.transport4you.mobile.communication.connectionmanager.impl;

import java.security.InvalidKeyException;
import java.security.PublicKey;
import java.util.ArrayList;

import javax.crypto.SecretKey;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import de.tum.score.transport4you.mobile.application.ICommunicationListener;
import de.tum.score.transport4you.mobile.application.applicationcontroller.IMainApplication;
import de.tum.score.transport4you.mobile.communication.bluetoothcontroller.IBluetooth;
import de.tum.score.transport4you.mobile.communication.bluetoothcontroller.error.BluetoothTransmissionException;
import de.tum.score.transport4you.mobile.communication.bluetoothcontroller.impl.BluetoothControllerSingleton;
import de.tum.score.transport4you.mobile.communication.connectionmanager.ICommunication;
import de.tum.score.transport4you.mobile.communication.connectionmanager.error.BluetoothConnectionLostException;
import de.tum.score.transport4you.mobile.communication.wificontroller.IWiFi;
import de.tum.score.transport4you.mobile.communication.wificontroller.impl.WiFiControllerSingleton;
import de.tum.score.transport4you.shared.mobilebus.data.IKeyExchange;
import de.tum.score.transport4you.shared.mobilebus.data.error.KeyAgreementException;
import de.tum.score.transport4you.shared.mobilebus.data.impl.keyexchange.DHParameter;
import de.tum.score.transport4you.shared.mobilebus.data.impl.keyexchange.KeyExchange;
import de.tum.score.transport4you.shared.mobilebus.data.impl.keyexchange.SignedPublicKey;
import de.tum.score.transport4you.shared.mobilebus.data.impl.message.BluetoothData;
import de.tum.score.transport4you.shared.mobilebus.data.impl.message.BluetoothEnvelope;
import de.tum.score.transport4you.shared.mobilebus.data.impl.message.DataMessage;
import de.tum.score.transport4you.shared.mobilebus.data.impl.message.KeyExchangeFinishMessage;
import de.tum.score.transport4you.shared.mobilebus.data.impl.message.KeyExchangePublicKeyMessage;
import de.tum.score.transport4you.shared.mobilebus.data.impl.message.ProtocolExceptionMessage;
import de.tum.score.transport4you.shared.mobileweb.impl.message.MobileSettings;

public class ConnectionManager extends Service implements ICommunication, IConnectionBroadcast {
	
	private String BUS_SSID = "";
	private String BUS_BT_NAME = "";
	private static final Integer BUS_WIFI_SCAN_RETRIES = 20;
	private static final Integer BUS_BT_SCAN_RETRIES = 1;
	
	private IWiFi wiFiController;
	private IBluetooth btController;
	private Context context;
	private IMainApplication mainApplication;
	private IKeyExchange dhGenerator;
	
	private ArrayList<ICommunicationListener> subscribers = new ArrayList<ICommunicationListener>();

	private boolean busConnected = false;
	private SecretKey sessionKey;
	private PublicKey publicRSAKey;
	
	private ConnectionManager() {
		
	}
	
	ConnectionManager(Context cntxt, IMainApplication mainApp) {
		this.context = cntxt;
		this.mainApplication = mainApp;
		
		MobileSettings mobileSettings = mainApp.getMobileSettings();
		
		this.publicRSAKey = mobileSettings.getPublicRSAkey();
		this.BUS_SSID = mobileSettings.getBusSSID();
		this.BUS_BT_NAME = mobileSettings.getBusBTName();
		
		Log.i("T4Y", "Initializing ConnectionManager");
		
		wiFiController = WiFiControllerSingleton.getWiFi(context);
		btController = BluetoothControllerSingleton.getBluetooth(context);
		btController.registerReceiver(this);
	}

	@Override
	public void initiateBusConnection() {
		wiFiController.waitForSSID(BUS_SSID, BUS_WIFI_SCAN_RETRIES);
		Log.i("T4Y", "Initiating bus connection...");
	}

	@Override
	public void synchronize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerComponent(ICommunicationListener component) {
		subscribers.add(component);
	}

	@Override
	public void receive(BluetoothEnvelope message) {
			
		try {
			
			if (message == null) throw new BluetoothTransmissionException("Received null message - socket crashed?");
			
			BluetoothData btData = message.getData();
			
			if(btData == null) {
				Log.i("T4Y", "Received btMessage: null");
				return;
			} else if(btData instanceof KeyExchangePublicKeyMessage) {
				initializeKeyGenerator(((KeyExchangePublicKeyMessage) btData).getDhSpec());
				//send own public key
				KeyExchangePublicKeyMessage bluetoothReplyData = new KeyExchangePublicKeyMessage();
				
				SignedPublicKey pk = dhGenerator.getOwnPublicKey(null);
				SignedPublicKey foreignSignedPubKey = ((KeyExchangePublicKeyMessage) btData).getSignedPublicKey();
				
				bluetoothReplyData.setSignedPublicKey(pk);
				BluetoothEnvelope reply = new BluetoothEnvelope(bluetoothReplyData);
								
				sessionKey = dhGenerator.getSessionKey(foreignSignedPubKey, publicRSAKey);
				
				btController.send(reply);
				Log.i("T4Y", "Received btMessage: KeyExchangePublicKeyMessage => Session key established");
			} else if(btData instanceof ProtocolExceptionMessage) {
				Log.i("T4Y", "Received btMessage: ProtocolExceptionMessage");
				mainApplication.resetConnection("A protocol error occured");
				mainApplication.sendStatusBarNotification("T4Y: Connection failed","A protocol error occured");
			} else if(btData instanceof KeyExchangeFinishMessage) {
				Log.i("T4Y", "Received btMessage: KeyExchangeFinishMessage => Bus connected");
				mainApplication.sendStatusBarNotification("T4Y: Connected to bus","The bus connection is established");
				busConnected = true;
				mainApplication.startETicketExchange();
				startKeepAlive();
			} else if(btData instanceof DataMessage) {
				Log.i("T4Y", "Received btMessage: DataMessage");
				processData((DataMessage)btData);
			} else {
				Log.i("T4Y", "Received btMessage: undefined Message");
			}
			
		} catch (KeyAgreementException e) {
			Log.i("T4Y", "Received btMessage: Key Agreement failed");
			mainApplication.sendStatusBarNotification("T4Y: Connection failed","Key agreement with the bus failed");
			mainApplication.resetConnection("A protocol error occured");
			busConnected = false;
		} catch (BluetoothTransmissionException e) {
			Log.i("T4Y", "Bluetooth transmission failed (receiving failed)");
			mainApplication.sendStatusBarNotification("T4Y: Connection failed","Bluetooth connection with the bus failed");
			mainApplication.resetConnection("A protocol error occured (receiving failed)");
			busConnected = false;
		}
		
	}

	/**
	 * Handles the keep-alive signal transmission and tears down connection if a problem occurs
	 */
	private void startKeepAlive() {
		/*currently not supported by bus system
		
		if(!busConnected) return;
		KeepAliveSender keepAliveSender = new KeepAliveSender(this);
		keepAliveSender.run();
		*/
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Initializes the Diffie-Hellman generator and returns own public dh key.
	 * @param dhSpec The dh parameters, needed to initialize the generator.
	 * @return Returns own public dh key
	 */
	private SignedPublicKey initializeKeyGenerator(DHParameter dhSpec) {
		
		SignedPublicKey ownPublicKey = null;
		
		try {
			dhGenerator = new KeyExchange(dhSpec);
			ownPublicKey = dhGenerator.getOwnPublicKey(null);
		} catch (KeyAgreementException e) {
			Log.i("T4Y", "Key Agreement failed");
			mainApplication.resetConnection("A session key error occured");
		}
		
		return ownPublicKey;
	}

	@Override
	public void send(BluetoothEnvelope message) throws BluetoothConnectionLostException {
		if (!busConnected) return;
		
		DataMessage data;
		try {
			//if received null kill connection
			if(message == null) {
				btController.send(null);
				return;
			}
			
			data = (DataMessage) message.getData();
			data.encryptData(sessionKey);
			btController.send(message);
		} catch (InvalidKeyException e) {
			busConnected = false;
			Log.i("T4Y", "Invalid session key to encrypt DataMessage");
			throw new BluetoothConnectionLostException("Invalid session key to encrypt DataMessage");
		} catch (BluetoothTransmissionException e) {
			busConnected = false;
			Log.i("T4Y", "Bluetooth transmission failed (sending failed)");
			throw new BluetoothConnectionLostException("Connection to the bus has been lost (sending failed)");
		} 
		
	}
	
	/**
	 * Process the data coming from the bluetooth controller and decrypt it if necessary
	 * @param datamsg
	 */
	private void processData(DataMessage datamsg) {
		if (datamsg == null) {
			return;
		} 
		
		//check if datamessage is encrypted and decrypt ist
		if (datamsg.isEncrypted()) {
			try {
				datamsg.decryptData(sessionKey);
			} catch (InvalidKeyException e) {
				Log.i("T4Y", "The provided session key is invalid - did you use an old one?");
			} 
		}
		
		mainApplication.processData(datamsg);
		
	}

	@Override
	public void resetConnection() {
		busConnected = false;
		btController.reset();
	}

	/* (non-Javadoc)
	 * @see de.tukl.score.transport4you.mobile.communication.connectionmanager.impl.IConnectionBroadcast#getBUS_SSID()
	 */
	@Override
	public String getBUS_SSID() {
		return BUS_SSID;
	}

	/* (non-Javadoc)
	 * @see de.tukl.score.transport4you.mobile.communication.connectionmanager.impl.IConnectionBroadcast#getBUS_BT_NAME()
	 */
	@Override
	public String getBUS_BT_NAME() {
		return BUS_BT_NAME;
	}

	/* (non-Javadoc)
	 * @see de.tukl.score.transport4you.mobile.communication.connectionmanager.impl.IConnectionBroadcast#getMainApplication()
	 */
	@Override
	public IMainApplication getMainApplication() {
		return mainApplication;
	}

	public static Integer getBusBtScanRetries() {
		return BUS_BT_SCAN_RETRIES;
	}

	public static Integer getBusWifiScanRetries() {
		return BUS_WIFI_SCAN_RETRIES;
	}
}

