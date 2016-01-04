/**
 * 
 */
package de.tum.score.transport4you.mobile.application.synchronizationmanager.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;

import android.content.Context;
import android.util.Log;
import de.tum.score.transport4you.mobile.R;
import de.tum.score.transport4you.mobile.application.ICommunicationListener;
import de.tum.score.transport4you.mobile.application.applicationcontroller.IMainApplication;
import de.tum.score.transport4you.mobile.application.synchronizationmanager.ISynchronization;
import de.tum.score.transport4you.mobile.application.synchronizationmanager.error.BlobException;
import de.tum.score.transport4you.mobile.application.synchronizationmanager.error.SynchronizationException;
import de.tum.score.transport4you.mobile.communication.dataconnectioncontroller.IData;
import de.tum.score.transport4you.mobile.communication.dataconnectioncontroller.error.RESTException;
import de.tum.score.transport4you.mobile.communication.dataconnectioncontroller.impl.DataConnectionController;
import de.tum.score.transport4you.mobile.data.datacontroller.ISettingsDataController;
import de.tum.score.transport4you.mobile.data.datacontroller.ISynchronizationDataController;
import de.tum.score.transport4you.mobile.data.datacontroller.error.DataControllerException;
import de.tum.score.transport4you.mobile.data.datacontroller.impl.DataController;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.BlobEntry;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.BlobEnvelope;
import de.tum.score.transport4you.shared.mobileweb.impl.message.MobileSettings;

public class SynchronizationManager implements ISynchronization, ICommunicationListener {

	private IMainApplication mainApplication;
	private ISynchronizationDataController synchronizationDataController;
	private ISettingsDataController settingsDataController;
	private IData dataConnectionController;
	private Context context;
	
	/*
	 * for other sceanrios than an successful purchase process 
	 * replace with one of the predefined testblobs: 
	 * R.raw.testblob_purchase_success ; R.raw.testblob_purchase_fail ; R.raw.testblob_valid_eticket
	 */
	private final int TEST_ETICKET = R.raw.testblob_valid_eticket;
	
	public SynchronizationManager(Context context, IMainApplication mainApp) {
		this.mainApplication = mainApp;
		this.context = context;
		this.dataConnectionController = new DataConnectionController(context,mainApp);
		synchronizationDataController = new DataController(context,mainApp);
		settingsDataController = new DataController(context, mainApp);
	}

	@Override
	public void synchronize() throws SynchronizationException{
		try {
			MobileSettings mobileSettings = settingsDataController.loadMobileSettings();
			MobileSettings mobileSettingsSynchronized;
			
			BlobEnvelope blob;
			
			try{
				blob = synchronizationDataController.loadBlob();
			}catch (DataControllerException e) {
				blob = null;
			}
			
			BlobEnvelope blobSynchronized;
			
			mobileSettingsSynchronized = dataConnectionController.synchronizeSettings(mobileSettings.getUsername(), mobileSettings.getPassword(), mobileSettings);
			settingsDataController.storeMobileSettings(mobileSettingsSynchronized);
			
			blobSynchronized = dataConnectionController.synchronizeETickets(mobileSettings.getUsername(), mobileSettings.getPassword(), blob);
			synchronizationDataController.storeBlob(blobSynchronized);	
		} catch (RESTException e) {
			throw new SynchronizationException("DataConnectionController Exception - " + e.getMessage());
		} catch (DataControllerException e) {
			throw new SynchronizationException("DataController Exception - " + e.getMessage());
		}		
	}
	
	@Override
	public boolean verifyLogin(String username, String password) {	
		try {
			Boolean result = dataConnectionController.checkAuthentication(context, username, password);
			Log.i("T4Y", "Verification of login credentials successful");
			return result;
		} catch (RESTException e) {
			Log.e("T4Y", "Verify login failed - reason: "+e.getMessage());
			return false;
		}
	}

	@Override
	public void receiveBlob(BlobEnvelope blob) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BlobEnvelope getETicketBlob() throws BlobException {
		Log.i("T4Y", "Loading stored eTicket blob");
		BlobEnvelope blob;
		
		//if debug mode is set, load dummy blob instead of real blob
		if(mainApplication.isDebugModeEnabled()) {
			//read dummy blob and etickets for testing purposes
			InputStream blobStream = context.getResources().openRawResource(TEST_ETICKET);
			
			try {
				ObjectInputStream blobRaw = new ObjectInputStream(blobStream);
				blob = (BlobEnvelope) blobRaw.readObject();
				return blob;
			} catch (StreamCorruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		if(!synchronizationDataController.existBlob()) {
			Log.i("T4Y", "No eTicket blob found");
			throw new BlobException("Could not load blob - maybe synchronize first?");
		}
		
		try {
			blob = synchronizationDataController.loadBlob();
		} catch (DataControllerException e) {
			Log.i("T4Y", "Could not load eTicket blob");
			throw new BlobException("Could not load blob - maybe synchronize first?");
		}
		
		return blob;
	}

	@Override
	public void setETicketBlob(BlobEnvelope blob) throws BlobException {
		Log.i("T4Y", "Storing new eTicket blob");
		
		try {
			synchronizationDataController.storeBlob(blob);
		} catch (DataControllerException e) {
			throw new BlobException("Could not store blob - a data controller exception occured");
		}
		
	}

}
