package de.tukl.score.transport4you.mobile.application.synchronizationmanager.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.app.NotificationManager;
import android.content.Context;

import com.xtremelabs.robolectric.RobolectricTestRunner;

import de.tukl.score.transport4you.mobile.application.applicationcontroller.impl.ApplicationController;
import de.tukl.score.transport4you.mobile.application.applicationcontroller.impl.ApplicationSingleton;
import de.tukl.score.transport4you.mobile.application.synchronizationmanager.ISynchronization;
import de.tukl.score.transport4you.mobile.application.synchronizationmanager.error.SynchronizationException;
import de.tukl.score.transport4you.mobile.communication.connectionmanager.impl.ConnectionManager;
import de.tukl.score.transport4you.mobile.communication.connectionmanager.impl.ConnectionManagerSingleton;
import de.tukl.score.transport4you.mobile.data.datacontroller.ISettingsDataController;
import de.tukl.score.transport4you.mobile.presentation.presentationmanager.impl.MainScreen;

@RunWith(RobolectricTestRunner.class)
public class SynchronizationManagerTest {
   
	private Context context = new MainScreen();
	
    @Test (expected=SynchronizationException.class)
    /**
     * Test if the synchronize method of the synchronization manager
     */
    public void synchronizeTest() throws Exception {
        ApplicationController applicationController = (ApplicationController) ApplicationSingleton.getApplicationController();
    	
        SynchronizationManager synchronizationManager = new SynchronizationManager(context, applicationController);
    	
        //synchronization is currently not completely implemented => currently it must throw a SynchronizationException
        synchronizationManager.synchronize();
        
    }
  
    @Test 
    /**
     * Test if the login verification method of the synchronization manager
     */
    public void verifyLoginTest() throws Exception {
        ApplicationController applicationController = (ApplicationController) ApplicationSingleton.getApplicationController();
    	
        SynchronizationManager synchronizationManager = new SynchronizationManager(context, applicationController);
    	
        //login credentials verification is currently not implemented completely => method must alsway return true
        
        assertTrue(synchronizationManager.verifyLogin("test", "test"));
        
    }
}

