package de.tukl.score.transport4you.bus.communication.connectionmanager.impl.protocol;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.tukl.score.transport4you.bus.communication.bluetoothcontroller.BluetoothStub;
import de.tukl.score.transport4you.bus.communication.bluetoothcontroller.data.BluetoothConnection;
import de.tukl.score.transport4you.bus.communication.connectionmanager.impl.protocol.mobilesystem.MobileSystemListeningProtocol;
import de.tukl.score.transport4you.bus.data.datacontroller.SettingsDataControllerTest;

/**
 * This class tests the functionality of the Mobile System Connection Framework
 * @author hoerning
 *
 */
public class MobileSystemConnectionTest {
	
	private static BluetoothStub bluetooth;
	
	private MobileSystemListeningProtocol mobileSystemListeningProtocol;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
		
		//Init Data Controller
		SettingsDataControllerTest.setUpBeforeClass();
		bluetooth = new BluetoothStub();
	}
	
	@Before
	public void setUp() throws Exception{
		
		mobileSystemListeningProtocol = new MobileSystemListeningProtocol();
		mobileSystemListeningProtocol.startProtocol();
		
	}
	
	@Test
	public void testInitialization(){
		
		assertNotNull(bluetooth.getCallback());
		assertNotNull(bluetooth.getBluetoothConfiguration());
		
	}

	@Test
	public void testConnectionEstablishment(){
		
		new BluetoothConnection(null);
		
	}
}
