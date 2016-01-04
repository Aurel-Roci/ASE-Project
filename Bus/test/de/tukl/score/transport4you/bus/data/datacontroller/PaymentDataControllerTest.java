package de.tukl.score.transport4you.bus.data.datacontroller;


import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.tukl.score.transport4you.bus.data.datacontroller.DataControllerInterfaceCoordinator;
import de.tukl.score.transport4you.bus.data.datacontroller.data.AbstractPersistenceObject;
import de.tukl.score.transport4you.bus.data.datacontroller.data.ETicket;
import de.tukl.score.transport4you.bus.data.datacontroller.data.PaymentTransaction;
import de.tukl.score.transport4you.bus.data.datacontroller.data.PrePayTransaction;
import de.tukl.score.transport4you.bus.data.datacontroller.error.PersistenceException;

/**
 * This class checks the storing of the payment transactions
 * @author hoerning
 *
 */
public class PaymentDataControllerTest{

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
	 * Tests the Payment transactions
	 * @throws PersistenceException 
	 */
	@Test
	public void testInsertPaymentTransaction() throws PersistenceException{
		
		//Create some sample values
		PrePayTransaction prePayTrans = new PrePayTransaction();
		ETicket ticket = new ETicket();
		ArrayList<ETicket> ticketList = new ArrayList<ETicket>();
		ticketList.add(ticket);
		
		prePayTrans.setAmount(4.00);
		prePayTrans.setNewAccountBalance(5.00);
		prePayTrans.setOldAccountBalance(9.00);
		prePayTrans.setSoldETickets(ticketList);
		prePayTrans.setSuccess(false);
		prePayTrans.setUserID("Testuser");
		prePayTrans.setTime(new Date());
		
		ticket.setCustomerId("Testuser");
		ticket.setInvalidated(true);
		ticket.setInvalidatedAt(new Date());
		ticket.setSellingDate(new Date());
		ticket.setValidTime(60);
		ticket.setValidUntil(new Date());
		
		//Save
		List<AbstractPersistenceObject> savingList = new ArrayList<AbstractPersistenceObject>();
		savingList.add(ticket);
		savingList.add(prePayTrans);
		
		DataControllerInterfaceCoordinator.getPersistentDataController().save(savingList);
		
		//Retrieve
		List<PaymentTransaction> resultList = DataControllerInterfaceCoordinator.getTransactionDataController().getAllTransactions();
		
		assertTrue(resultList.contains(prePayTrans));
		assertTrue(prePayTrans.getSoldETickets().get(0).equals(ticket));
		
		prePayTrans.setSuccess(true);
		
		//Add again
		DataControllerInterfaceCoordinator.getPersistentDataController().save(savingList);
		
		//Retrieve again => the prepay transaction should not be twice in the list
		resultList = DataControllerInterfaceCoordinator.getTransactionDataController().getAllTransactions();
		int found = 0;
		for(PaymentTransaction ppt : resultList){
			if(ppt.equals(prePayTrans)){
				found++;
			}
		}
		
	}
}
