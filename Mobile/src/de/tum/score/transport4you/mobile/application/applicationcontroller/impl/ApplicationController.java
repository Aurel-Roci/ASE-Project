package de.tum.score.transport4you.mobile.application.applicationcontroller.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.security.PublicKey;
import java.security.Security;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import de.tum.score.transport4you.mobile.R;
import de.tum.score.transport4you.mobile.application.ICommunicationListener;
import de.tum.score.transport4you.mobile.application.applicationcontroller.IMainApplication;
import de.tum.score.transport4you.mobile.application.synchronizationmanager.ISynchronization;
import de.tum.score.transport4you.mobile.application.synchronizationmanager.error.BlobException;
import de.tum.score.transport4you.mobile.application.synchronizationmanager.error.SynchronizationException;
import de.tum.score.transport4you.mobile.application.synchronizationmanager.impl.SynchronizationManager;
import de.tum.score.transport4you.mobile.communication.connectionmanager.ICommunication;
import de.tum.score.transport4you.mobile.communication.connectionmanager.error.BluetoothConnectionLostException;
import de.tum.score.transport4you.mobile.communication.connectionmanager.impl.ConnectionManagerSingleton;
import de.tum.score.transport4you.mobile.data.datacontroller.ISettingsDataController;
import de.tum.score.transport4you.mobile.data.datacontroller.error.DataControllerException;
import de.tum.score.transport4you.mobile.data.datacontroller.impl.DataController;
import de.tum.score.transport4you.mobile.presentation.presentationmanager.IPresentation;
import de.tum.score.transport4you.mobile.presentation.presentationmanager.impl.AvailableETicketListScreen;
import de.tum.score.transport4you.mobile.presentation.presentationmanager.impl.MainScreen;
import de.tum.score.transport4you.mobile.shared.MobileIntents;
import de.tum.score.transport4you.shared.mobilebus.data.impl.message.AvailableETicketTypesMessage;
import de.tum.score.transport4you.shared.mobilebus.data.impl.message.BluetoothEnvelope;
import de.tum.score.transport4you.shared.mobilebus.data.impl.message.DataMessage;
import de.tum.score.transport4you.shared.mobilebus.data.impl.message.ETicketPurchaseFailedMessage;
import de.tum.score.transport4you.shared.mobilebus.data.impl.message.PurchaseETicketFinishMessage;
import de.tum.score.transport4you.shared.mobilebus.data.impl.message.PurchaseETicketTypeMessage;
import de.tum.score.transport4you.shared.mobilebus.data.impl.message.ValidETicketMessage;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.BlobEntry;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.BlobEnvelope;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.ETicket;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.ETicketType;
import de.tum.score.transport4you.shared.mobileweb.impl.message.MobileSettings;

/**
 * This class handles all application logic, like modification and exchange of data and management of gui data. 
 *
 */
public class ApplicationController extends Thread implements IMainApplication, ICommunicationListener {
	ISettingsDataController settingsDataController;
	ISynchronization synchronizationManager;
	ICommunication connectionManager;
	Context context;
	NotificationManager notificationManager;
	ArrayList<IPresentation> registeredActivities = new ArrayList<IPresentation>();
	
	private final int MAX_RECONNECT_RETRIES = 2;
	
	private final boolean DEBUG = false;
	
	int reconnectRetries;
	boolean eTicketExchangeFinished = false;
	
	protected ApplicationController() {

	}
	
	@Override
	public void registerActivity(Activity activity) {
		this.registeredActivities.add((IPresentation)activity);
		
		if(activity instanceof MainScreen) {
		}
	}
	
	public BlobEntry getStoredBlobEntry()
	{
		BlobEntry entry = null;
		
		try {
			BlobEnvelope blob = synchronizationManager.getETicketBlob();
			entry = blob.getPublicBlobEntry();
		} catch (BlobException e) {
			Log.i("T4Y", "ApplicationController: Error loading blob!");
		}
		
		return entry;
	}
	
	public void initialize(Context cntxt) {
		this.context = cntxt;
		//initialize controllers
		Log.i("T4Y", "Initializing ApplicationController");
		
		//Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		Security.insertProviderAt(new org.bouncycastle.jce.provider.BouncyCastleProvider(), 1);
		
		settingsDataController = new DataController(context,this);
		synchronizationManager = new SynchronizationManager(context,this);
		
		//TODO synchronize blob and settings for the first time
		//create mobileSettings and blob dummies if run for the first time
		if (!settingsDataController.existMobileSettings()) {
			MobileSettings newSettings;
			
			try {
				//try to synchronize with web system for the first time
				Log.i("T4Y", "Try to synchronize with web system");
				synchronizationManager.synchronize();
				
			} catch (SynchronizationException e) {
							
				try {
					
					Log.i("T4Y", "Synchronization failed => store default mobile settings ");
					
					newSettings = new MobileSettings();
					
					newSettings.setUsername("");
					newSettings.setPassword("");
					newSettings.setBusSSID("BUS");
					newSettings.setBusBTName("BUS");
					newSettings.setRememberLogin(false);
					newSettings.setAllowAutoScan(false);
					newSettings.setAllowAutoSynchronization(false);
					newSettings.setAllowAutoSMSNotification(false);
					PublicKey publicRSAKey = settingsDataController.getPublicRSAKey();
					newSettings.setPublicRSAkey(publicRSAKey);
					settingsDataController.storeMobileSettings(newSettings);
				} catch (DataControllerException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		connectionManager = ConnectionManagerSingleton.getCommunication(context, this);
		connectionManager.registerComponent(this);
		
		notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	@Override
	public boolean login(String username, String password, boolean rememberLogin) {
		
		if(synchronizationManager.verifyLogin(username, password)) {
			Log.i("T4Y", "Logging in");
			
			//store changed login data
			try {
				MobileSettings mobileSettings;
				mobileSettings = settingsDataController.loadMobileSettings();
				mobileSettings.setPassword(password);
				mobileSettings.setUsername(username);
				mobileSettings.setRememberLogin(rememberLogin);
				settingsDataController.storeMobileSettings(mobileSettings);
				
				if(mobileSettings.isAllowAutoSynchronization()) {
					synchronizationManager.synchronize();
					mobileSettings = settingsDataController.loadMobileSettings();
				}
				
				if(mobileSettings.isAllowAutoScan()) {
					startBusScan();
				}
				
			} catch (NullPointerException e) {
				return false;
			} catch (DataControllerException e) {
				return false;
			} catch (SynchronizationException e) {
				return false;
			}
			return true;
		} else {
			AlertDialog.Builder incorrectCredentialAlert = new AlertDialog.Builder(context);
            incorrectCredentialAlert.setMessage("Incorrect credentials!");
            incorrectCredentialAlert.setCancelable(true);
            incorrectCredentialAlert.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

            incorrectCredentialAlert.create().show();
		}			
		
		return false;
	}

	@Override
	public MobileSettings getMobileSettings() {
		MobileSettings mobileSettings = new MobileSettings();
		
		try {
			mobileSettings = settingsDataController.loadMobileSettings();
		} catch (DataControllerException e) {
			//if mobile settings can not be loaded, set default settings
			Log.i("T4Y", "Could not load mobile settings => shutdown application");
			shutdownSystem();
		}
		
		return mobileSettings;
	}

	@Override
	public void setMobileSettings(MobileSettings mobileSettings) {
		//store changed mobile settings
		try {
			settingsDataController.storeMobileSettings(mobileSettings);
		} catch (DataControllerException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void startBusScan() {
		eTicketExchangeFinished = false;
		reconnectRetries = MAX_RECONNECT_RETRIES;
		connectionManager.initiateBusConnection();		
		sendProgessDialogUpdate("Connecting to bus system...", true, 1000);
	}
	
	/**
	 * Send progressdialog updates to all activities
	 * @param message
	 * @param visible
	 * @param time
	 */
	private void sendProgessDialogUpdate(String message, boolean visible, Integer increment) {
		
		for(IPresentation act : registeredActivities) {
			act.updateProgessDialog("T4Y", message, visible, increment);
		}
	}

	@Override
	public void sendETicket() {
		
		BlobEnvelope blob = null;
		Log.i("T4Y", "Sending eTicket");
		sendStatusBarNotification("T4Y: eTicket sent","The eTicket was sent successfully!");
		
		try {
			blob = synchronizationManager.getETicketBlob();
		} catch (BlobException e) {
			e.printStackTrace();
		}
		
		//encapsulate blob into an bluetoothenvelope and send it to the bus
		DataMessage bluetoothData = new DataMessage(blob);
		BluetoothEnvelope blobMessage = new BluetoothEnvelope(bluetoothData);
		
		sendProgessDialogUpdate("Sending eTicket...",true,100);
		
		try {
			connectionManager.send(blobMessage);
		} catch (BluetoothConnectionLostException e) {
			resetConnection("Bluetooth connection lost");
		}
		
	}

	/**
	 * Receives a list of available eticket from the bus system
	 * @param availableETickets 
	 */
	private void receiveETicketList(AvailableETicketTypesMessage availableETickets) {
		if(availableETickets == null) return;
		
		//TODO Fill list in activity with available tickets
		sendStatusBarNotification("T4Y: Please select eTicket","No eTickets available on phone - please select eTicket type to be purchased!");
		
		Intent showAvailableETicketTypes = new Intent(context, AvailableETicketListScreen.class);
		showAvailableETicketTypes.putExtra("eTicketTypes", availableETickets);
		
		context.startActivity(showAvailableETicketTypes);
	}

	@Override
	public void buyETicket(ETicketType selectedETicketType) {
		if(selectedETicketType == null) return;
		
		PurchaseETicketTypeMessage purchaseMessage = new PurchaseETicketTypeMessage();
		purchaseMessage.setSelectedETicketType(selectedETicketType);
		
		//encapsulate PurchaseETicketTypeMessage into an bluetoothenvelope and send it to the bus
		DataMessage bluetoothData = new DataMessage(purchaseMessage);
		BluetoothEnvelope blobMessage = new BluetoothEnvelope(bluetoothData);
		
		Log.i("T4Y", "Sending purchase request");
		
		try {
			connectionManager.send(blobMessage);
		} catch (BluetoothConnectionLostException e) {
			resetConnection("Bluetooth connection lost");
		}
		
	}

	@Override
	public void synchronize() {
		try {
			sendProgessDialogUpdate("Starting synchronization...", true , 1000);
			synchronizationManager.synchronize();
		} catch (SynchronizationException e) {
			sendProgessDialogUpdate("Synchronization failed...", true , 1000);
			Log.i("T4Y", "Synchronization failed");
			try {
				sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			sendProgessDialogUpdate("", false , 1000);
		}
		sendProgessDialogUpdate("Synchronization successful!", true , 1000);
		sendProgessDialogUpdate("Synchronization successful!", false , 1000);
	}

	@Override
	public void receiveBlob(BlobEnvelope blob) {
		Log.i("T4Y", "Received blob");
		
		try {
			if(blob != null) {
				Log.i("T4Y", "T4Y: ETicket purchase completed - a new ticket has been purchased");
				sendStatusBarNotification("T4Y: ETicket purchase completed","A new ticket has been purchased");
				synchronizationManager.setETicketBlob(blob);
				
				//Send a PurchaseETicketFinishMessage to the bus system to ack purchase
				DataMessage bluetoothData = new DataMessage(new PurchaseETicketFinishMessage());
				BluetoothEnvelope blobMessage = new BluetoothEnvelope(bluetoothData);
				
				connectionManager.send(blobMessage);
				
				//signal successfull transmission
				sendProgessDialogUpdate("A new ticket has been purchased", true, 1000);
				sleep(3000);
				sendProgessDialogUpdate("", false, 1000);
				tearDown();
				
			} else {
				Log.i("T4Y", "ETicket sent - bus system accepted eTicket"); 
				sendStatusBarNotification("T4Y: ETicket sent","The bus system accepted the eTicket");
				
				//signal successfull transmission
				sendProgessDialogUpdate("The bus system accepted the eTicket", true, 1000);
				sleep(3000);
				sendProgessDialogUpdate("", false, 1000);
				tearDown();
			}

		} catch (BlobException e) {
			resetConnection("Blob exception occured");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BluetoothConnectionLostException e) {
			resetConnection("Sending PurchaseETicketFinishMessage failed");
		}
		
	}

	@Override
	public void startETicketExchange() {
		sendProgessDialogUpdate("Started eTicket exchange...", true , 1000);
		try {
			sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("T4Y", "Starting eTicket handling");
		sendETicket();		
	}

	@Override
	public void shutdownSystem() {
		Log.i("T4Y", "Shutting system down");
		
		tearDown();
		
		for (IPresentation a : registeredActivities) {
			a.shutdown();
		}
		
		//kills all opended broadcast receivers
    	Intent tearDown = new Intent(MobileIntents.TEAR_DOWN);
    	context.sendBroadcast(tearDown);
		
	}

	@Override
	public void sendStatusBarNotification(String title, String message) {
		int icon = R.drawable.dialog;
				
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(context)
		        .setSmallIcon(icon)
		        .setContentTitle(title)
		        .setContentText(message);
		
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(1337, mBuilder.build());
	}

	@Override
	public void processData(DataMessage datamsg) {
		
		if (datamsg.getData() instanceof AvailableETicketTypesMessage) {
			receiveETicketList((AvailableETicketTypesMessage) datamsg.getData());
		} else if (datamsg.getData() instanceof ValidETicketMessage) {
			receiveBlob(((ValidETicketMessage) datamsg.getData()).getBlob());
		} else if (datamsg.getData() instanceof ETicketPurchaseFailedMessage) {
			String reason = ((ETicketPurchaseFailedMessage)datamsg.getData()).getReason();
			Log.i("T4Y", "ETicket purchase failed - reason: " + reason);
			sendStatusBarNotification("T4Y: ETicket purchase failed","Reason: " + reason);
			tearDown();
		}  else {
			Log.i("T4Y", "Unknown DataMessage type received");
		}
		
	}

	@Override
	public void resetConnection(String reason) {
		
		if(eTicketExchangeFinished) return;
		
		notificationManager.cancel(1);
		
		//try to reconnect if maximum retries are not reached
		if (reconnectRetries > 0) {
			connectionManager.resetConnection();
			connectionManager.initiateBusConnection();
			sendProgessDialogUpdate("Retry [" + reconnectRetries + "]", true, 1000);
			Log.i("T4Y", "Retry to establish bluetooth connection [" + reconnectRetries + "]");
			
			reconnectRetries--;
		} else {
			sendProgessDialogUpdate("Resetting connection - reason: " + reason, true, 1000);
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sendProgessDialogUpdate("", false, 1000);
			
			Log.i("T4Y", "Tear down bus connection reason: " + reason);
			tearDown();
		}
	}

	public void run() {
		Log.i("T4Y", "Starting ApplicationController thread");
	}
	
	public void tearDown() {
		eTicketExchangeFinished = true;
		notificationManager.cancel(1);
		sendProgessDialogUpdate("", false, 1000);
		try {
			Log.i("T4Y", "BT Connection killed ");
			connectionManager.send(null);
		} catch (BluetoothConnectionLostException e) {
		}
		

		//TODO signal that eTicket exchange was successful
	}

	@Override
	public boolean isDebugModeEnabled() {
		return DEBUG;
	}
	
}
