package de.tukl.score.transport4you.bus.data.datacontroller;


import static org.junit.Assert.*;

import java.io.File;

import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.tukl.score.transport4you.bus.data.datacontroller.DataControllerInterfaceCoordinator;
import de.tukl.score.transport4you.bus.data.datacontroller.ISettingsDataController;
import de.tukl.score.transport4you.bus.data.datacontroller.data.BluetoothConfiguration;
import de.tukl.score.transport4you.bus.data.datacontroller.data.KeyConfiguration;
import de.tukl.score.transport4you.bus.data.datacontroller.data.SystemConfiguration;
import de.tukl.score.transport4you.bus.data.datacontroller.error.ConfigurationLoadingException;

/**
 * This class checks the settings interface
 * @author hoerning
 *
 */
public class SettingsDataControllerTest{

	private static ISettingsDataController settingsDataController;
	private static final String CONFIG_FILE = "res/system.conf";
	private static final String LOGGING_FILE = "res/log4j.conf";
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		//Initialize
		DataControllerInterfaceCoordinator.getStartup().init(new File(CONFIG_FILE));
		PropertyConfigurator.configure(LOGGING_FILE);
		
		//Init Settings
		settingsDataController = DataControllerInterfaceCoordinator.getSettingsDataController();

	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	/**
	 * Tests the System Configuration
	 */
	@Test
	public void testSystemConfiguration(){
		
		SystemConfiguration systemConf = settingsDataController.getSystemConfiguration();
		assertNotNull(systemConf);
		systemConf.getThreadPoolSize();
		systemConf.getDaemonThreadTimer();
	}
	
	/**
	 * Tests the Bluetooth Configuration
	 */
	@Test
	public void testBluetoothConfiguration(){
		
		BluetoothConfiguration bluetoothConf = settingsDataController.getBluetoothConfiguration();
		assertNotNull(bluetoothConf);
		assertNotNull(bluetoothConf.getServerName());
		assertNotNull(bluetoothConf.getServiceName());
		assertNotNull(bluetoothConf.getServiceUUID());
	}
	
	/**
	 * Tests the Key Configuration
	 * @throws ConfigurationLoadingException 
	 */
	@Test
	public void testKeyConfiguration() throws ConfigurationLoadingException{
		
		KeyConfiguration keyConf = settingsDataController.getKeyConfiguration();
		assertNotNull(keyConf);
		assertNotNull(keyConf.getKeyAgreementPrivateKey());
	}

}
