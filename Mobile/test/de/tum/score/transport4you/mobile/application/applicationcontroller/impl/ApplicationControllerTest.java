package de.tukl.score.transport4you.mobile.application.applicationcontroller.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.app.NotificationManager;
import android.content.Context;

import com.xtremelabs.robolectric.RobolectricTestRunner;

import de.tukl.score.transport4you.mobile.application.synchronizationmanager.ISynchronization;
import de.tukl.score.transport4you.mobile.communication.connectionmanager.impl.ConnectionManager;
import de.tukl.score.transport4you.mobile.communication.connectionmanager.impl.ConnectionManagerSingleton;
import de.tukl.score.transport4you.mobile.data.datacontroller.ISettingsDataController;
import de.tukl.score.transport4you.mobile.presentation.presentationmanager.impl.MainScreen;

@RunWith(RobolectricTestRunner.class)
public class ApplicationControllerTest {
   
	private Context context = new MainScreen();
	
    @Test
    /**
     * Test if the initialization method of the applicationcontroller
     */
    public void initializeTest() throws Exception {
        ApplicationController applicationController = (ApplicationController) ApplicationSingleton.getApplicationController();
    	
        //NullPointerException not avoidable, because invoked filereader cannot find files while offline testing
        try {
        	applicationController.initialize(context);
        } catch (NullPointerException e) {
        	
        }
    	
    	assertTrue(applicationController != null);
    	assertTrue(applicationController.settingsDataController != null);
    	assertTrue(applicationController.synchronizationManager != null);
    	
    }
    
    @Test
    /**
     * Test if the login procedure of the applicationcontroller
     */
    public void loginTest() throws Exception {
        ApplicationController applicationController = (ApplicationController) ApplicationSingleton.getApplicationController();
    	
        boolean positiveTest1 = applicationController.login("test", "test", true);
        boolean positiveTest2 = applicationController.login("", "test", true);
        boolean positiveTest3 = applicationController.login("test", "", true);
        
        boolean negativeTest1 = applicationController.login(null, "test", true);
        boolean negativeTest2 = applicationController.login("", null, true);
        boolean negativeTest3 = applicationController.login(null, null, true);
        
    	assertTrue(positiveTest1);
    	assertTrue(positiveTest2);
    	assertTrue(positiveTest3);
    	
    	assertFalse(negativeTest1);
    	assertFalse(negativeTest2);
    	assertFalse(negativeTest3);
    	
    }
    
    @Test
    /**
     * Test if the start bus scan procedure of the applicationcontroller
     */
    public void busScanTest() throws Exception {
        ApplicationController applicationController = (ApplicationController) ApplicationSingleton.getApplicationController();
    	
        //NullPointerException not avoidable, because invoked filereader cannot find files while offline testing
        try {
			applicationController.connectionManager = ConnectionManagerSingleton.getCommunication(context, applicationController);
			applicationController.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			applicationController.startBusScan();
		} catch (NullPointerException e) {
		}
		
		applicationController.sendETicket();
		
		assertFalse(applicationController.eTicketExchangeFinished);
    	
    }
    
}

