package de.tum.score.transport4you.mobile.data.datacontroller.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import android.content.Context;
import android.util.Log;
import de.tum.score.transport4you.mobile.R;
import de.tum.score.transport4you.mobile.application.applicationcontroller.IMainApplication;
import de.tum.score.transport4you.mobile.data.datacontroller.ISettingsDataController;
import de.tum.score.transport4you.mobile.data.datacontroller.ISynchronizationDataController;
import de.tum.score.transport4you.mobile.data.datacontroller.error.DataControllerException;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.BlobEnvelope;
import de.tum.score.transport4you.shared.mobileweb.impl.message.MobileSettings;

public class DataController implements ISettingsDataController, ISynchronizationDataController {

	private final String MOBILE_SETTINGS_FILENAME = "mobileSettings.t4y";
	private final String BLOB_FILENAME = "blob.t4y";
	private Context context;
	private IMainApplication mainApplication;
	
	public DataController(Context context, IMainApplication mainApp) {
		this.context = context;
		this.mainApplication = mainApp;
	}
	
	@Override
	public MobileSettings loadMobileSettings() throws DataControllerException {
		MobileSettings mobileSettings;
		
		try {
			FileInputStream fileInputStream;
			fileInputStream = context.openFileInput(MOBILE_SETTINGS_FILENAME);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			
			mobileSettings = (MobileSettings)objectInputStream.readObject();
			
			objectInputStream.close();
			fileInputStream.close();
			
			//storing successful
			return mobileSettings;
		} catch (FileNotFoundException e) {
			//storing failed
			throw new DataControllerException("Mobile Settings file not found!");
		} catch (IOException e) {
			//storing failed
			throw new DataControllerException("Mobile Settings I/O exception!");
		} catch (ClassNotFoundException e) {
			//storing failed
			throw new DataControllerException("Mobile settings class not found exception!");
		}
	}

	@Override
	public void storeMobileSettings(MobileSettings mobileSettings) throws DataControllerException {
		
		try {
			FileOutputStream fileOutputStream;
			fileOutputStream = context.openFileOutput(MOBILE_SETTINGS_FILENAME, Context.MODE_PRIVATE);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(mobileSettings);
			objectOutputStream.close();
			fileOutputStream.close();
			
		} catch (FileNotFoundException e) {
			//storing failed
			throw new DataControllerException("Mobile Settings file not found!");
		} catch (IOException e) {
			//storing failed
			throw new DataControllerException("Mobile Settings I/O exception!");
		}
		
	}

	@Override
	public BlobEnvelope loadBlob() throws DataControllerException {
		BlobEnvelope blob;
		
		try {
			FileInputStream fileInputStream;
			fileInputStream = context.openFileInput(BLOB_FILENAME);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			
			blob = (BlobEnvelope)objectInputStream.readObject();
			
			objectInputStream.close();
			fileInputStream.close();
			
			//storing successful
			return blob;
			
		} catch (FileNotFoundException e) {
			//storing failed
			throw new DataControllerException("Blob file not found!");
		} catch (IOException e) {
			//storing failed
			throw new DataControllerException("Blob I/O exception!");
		} catch (ClassNotFoundException e) {
			//storing failed
			throw new DataControllerException("BlobEnvelope class not found exception!");
		}
	}

	@Override
	public void storeBlob(BlobEnvelope blob) throws DataControllerException {
		try {
			FileOutputStream fileOutputStream;
			fileOutputStream = context.openFileOutput(BLOB_FILENAME, Context.MODE_PRIVATE);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(blob);
			objectOutputStream.close();
			fileOutputStream.close();			
		} catch (FileNotFoundException e) {
			//storing failed
			throw new DataControllerException("Blob file not found!");
		} catch (IOException e) {
			//storing failed
			throw new DataControllerException("Blob I/O exception!");
		}
	}

	@Override
	public boolean existBlob() {
		try {
			FileInputStream fileInputStream = context.openFileInput(BLOB_FILENAME);
			
			return true;
		} catch (FileNotFoundException e) {
			return false;
		}
	}

	@Override
	public boolean existMobileSettings() {
		try {
			FileInputStream fileInputStream = context.openFileInput(MOBILE_SETTINGS_FILENAME);
			return true;
		} catch (FileNotFoundException e) {
			return false;
		}
	}
	
	@Override
	public PublicKey getPublicRSAKey() {
		PublicKey pubKey = null;
		InputStream certificate = context.getResources().openRawResource(R.raw.bus_dh_rsa_cert);
		InputStreamReader certReader = new InputStreamReader(certificate);
		
        try {
        	CertificateFactory factory = CertificateFactory.getInstance("X.509");       	
            X509Certificate cert = (X509Certificate) factory.generateCertificate(certificate);        	
       	
            pubKey = (PublicKey) cert.getPublicKey();
            Log.i("T4Y", "Loaded publicRSAKey of bus");
            certReader.close();
            certificate.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
			e.printStackTrace();
		}

		return pubKey;
	}
}
