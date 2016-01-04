package de.tukl.score.transport4you.bus.data.datacontroller;


import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.tukl.score.transport4you.bus.data.datacontroller.DataControllerInterfaceCoordinator;
import de.tukl.score.transport4you.bus.data.datacontroller.data.ETicketType;

/**
 * This class checks the ETicketType interface
 * @author hoerning
 *
 */
public class ETicketDataControllerTest{

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
	public void testRetrieveETicketTypeList(){
		
		List<ETicketType> eTicketTypeList = DataControllerInterfaceCoordinator.getETicketTypeDataController().getAllETicketTypes();
		
		assertTrue(eTicketTypeList.size() != 0);
		
	}
	

}
