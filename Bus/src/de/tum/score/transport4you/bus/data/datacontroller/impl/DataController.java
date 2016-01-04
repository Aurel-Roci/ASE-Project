package de.tum.score.transport4you.bus.data.datacontroller.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.bouncycastle.openssl.PEMReader;

import de.tum.score.transport4you.bus.data.datacontroller.ISettingsDataController;
import de.tum.score.transport4you.bus.data.datacontroller.data.ApplicationConfiguration;
import de.tum.score.transport4you.bus.data.datacontroller.data.BluetoothConfiguration;
import de.tum.score.transport4you.bus.data.datacontroller.data.KeyConfiguration;
import de.tum.score.transport4you.bus.data.datacontroller.data.SystemConfiguration;
import de.tum.score.transport4you.bus.data.datacontroller.error.ConfigurationLoadingException;
import de.tum.score.transport4you.bus.data.datacontroller.error.DataControllerInitializingException;

/**
 * The class beeing capable of handling data/persistent related issues
 * @author hoerning
 *
 */
public class DataController implements ISettingsDataController{
	
	/* Store the information if this class was initialized */
	private boolean initialized = false;
	
	/* The used configuration */
	private PropertiesConfiguration propertiesConfiguration;
	
	/* The Key Configuration */
	private KeyConfiguration keyConfiguration;
	
	/* Logging */
	private Logger logger = Logger.getLogger("Data");
	
	private static DataController instance = null;
	
	/**
	 * The method to return the singleton instance of the Data Controller
	 * @return
	 */
	public static DataController getInstance(){
		if(instance==null) {
			instance = new DataController();
		}
		
		return instance;
	}

	/**
	 * This method needs to be called to initialize the Data Controller correctly.<br>Note that this method must only be called once.
	 * @param configurationFile
	 * @throws DataControllerInitializingException 
	 */
	public void init(File configurationFile) throws DataControllerInitializingException {
		logger.debug("Initializing Data Controller");
		
		if(initialized) {
			//Class was already initialized
			logger.error("Controller was already initialized");
			throw new DataControllerInitializingException("Controller was already initialized");
		} else {
			//Initialize
			try {
				propertiesConfiguration = new PropertiesConfiguration(configurationFile);
			} catch (ConfigurationException e) {
				logger.error("Error while loading the properties");
				throw new DataControllerInitializingException(e.getMessage());
			}		
			logger.debug("Initializing Data Controller finished");
			this.initialized = true;
		}
	}

	@Override
	public BluetoothConfiguration getBluetoothConfiguration() {

		//Create a new BluetoothConfiguration by reading out the parameters in the config file
		logger.trace("Loading Bluetooth Configuration");
		BluetoothConfiguration bluetoothConfiguration = new BluetoothConfiguration();
		bluetoothConfiguration.setServerName(propertiesConfiguration.getString(PropertiesConfigurationEntries.BLUETOOTH_SERVER));
		bluetoothConfiguration.setServiceName(propertiesConfiguration.getString(PropertiesConfigurationEntries.BLUETOOTH_SERVICE));
		bluetoothConfiguration.setServiceUUID(propertiesConfiguration.getString(PropertiesConfigurationEntries.BLUETOOTH_UUID));
		
		return bluetoothConfiguration;
	}

	@Override
	public SystemConfiguration getSystemConfiguration() {
		
		//Create a new SystemConfiguration by reading out the parameters in the config file
		logger.trace("Loading System Configuration");
		SystemConfiguration systemConfiguration = new SystemConfiguration();
		systemConfiguration.setThreadPoolSize(propertiesConfiguration.getInt(PropertiesConfigurationEntries.THREAD_POOL_SIZE));
		systemConfiguration.setDaemonThreadTimer(propertiesConfiguration.getInt(PropertiesConfigurationEntries.SYSTEM_DAEMON_TIMER));
		
		return systemConfiguration;
	}

	@Override
	public KeyConfiguration getKeyConfiguration() throws ConfigurationLoadingException {

		
		if(keyConfiguration==null) {
			//Load values out of configuration
			logger.trace("Loading Key Configuration");
			keyConfiguration = new KeyConfiguration();
			
			FileReader fileReader;
			try {
				
				//Load Key Agreement Private Key
				fileReader = new FileReader(propertiesConfiguration.getString(PropertiesConfigurationEntries.SECURITY_KEYAGREEMENT_KEYPATH));
				PEMReader pemReader = new PEMReader(fileReader);
				KeyPair keyPair = (KeyPair) pemReader.readObject();
				keyConfiguration.setKeyAgreementPrivateKey(keyPair.getPrivate());
				pemReader.close();
				fileReader.close();
				
				//Load Blob Private Key
				fileReader = new FileReader(propertiesConfiguration.getString(PropertiesConfigurationEntries.SECURITY_BLOB_KEYPATH_PRIVATE));
				pemReader = new PEMReader(fileReader);
				keyPair = (KeyPair) pemReader.readObject();
				keyConfiguration.setBlobPrivateKey(keyPair.getPrivate());
				pemReader.close();
				fileReader.close();
				
				//Load Blob Public Key
				fileReader = new FileReader(propertiesConfiguration.getString(PropertiesConfigurationEntries.SECURITY_BLOB_KEYPATH_PUBLIC));
		        pemReader = new PEMReader(fileReader);
		        X509Certificate cert = (X509Certificate) pemReader.readObject();
		        PublicKey publicKey = (PublicKey) cert.getPublicKey();
		        keyConfiguration.setBlobPublicKey(publicKey);
		    	pemReader.close();
				fileReader.close();
				
			} catch (FileNotFoundException e) {
				logger.error("Error loading the Key Configuration: "+e.getMessage());
				keyConfiguration = null;
				throw new ConfigurationLoadingException("Error loading the Key Configuration: "+e.getMessage());
			} catch (IOException e) {
				logger.error("Error loading the Key Configuration: "+e.getMessage());
				keyConfiguration = null;
				throw new ConfigurationLoadingException("Error loading the Key Configuration: "+e.getMessage());
			} catch (Exception e) {
				logger.error("Error loading the Key Configuration: "+e.getMessage());
				throw new RuntimeException(e.getMessage());
			}
		} else {
			logger.trace("Key Configuration already loaded");
		}
		
		return keyConfiguration;
	}

	@Override
	public ApplicationConfiguration getApplicationConfiguration() {
		
		logger.trace("Loading Application Configuration");
		ApplicationConfiguration appConf = new ApplicationConfiguration();
		//TODO: This configuration entries should be changeable from the outside
		appConf.setPostpayAccountRepresentation("POSTPAY");
		appConf.setPrepayAccountRepresentation("PREPAY");
		return appConf;
	}

}
