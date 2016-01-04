package de.tukl.score.transport4you.mobile.presentation.presentationmanager.impl;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.app.ProgressDialog;

import com.xtremelabs.robolectric.RobolectricTestRunner;

import de.tukl.score.transport4you.mobile.R;

@RunWith(RobolectricTestRunner.class)
public class MainScreenTest {

    @Test
    /**
     * Tests if the mobile system has the right application name
     */
    public void appNameTest() throws Exception {
        String appName = new MainScreen().getResources().getString(R.string.app_name);
        assertTrue(appName.equals("Transport4You"));
    }
    
    @Test
    /**
     * Test if the MainScreen shuts down correctly
     */
    public void shutDownTest() throws Exception {
        MainScreen mainScreen = new MainScreen();
        mainScreen.shutdown();
        
        assertTrue(mainScreen.isFinishing());
    }
    
    @Test
    /**
     * Test if the notification dialog is started correctly
     */
    public void startNotificationDialogTest() throws Exception {
        MainScreen mainScreen = new MainScreen();
        
        Object output1 = mainScreen.onCreateDialog(0);
        Object output2 = mainScreen.onCreateDialog(1);
        Object output3 =  mainScreen.onCreateDialog(2);
        Object output4 = mainScreen.onCreateDialog(3);
        ;
        
        assertTrue(output1 != null);
        assertTrue(output2 != null);
        assertTrue(output3 == null);
        assertTrue(output4 == null);
        
        assertTrue(output1 instanceof ProgressDialog);
        assertTrue(output2 instanceof ProgressDialog);
    }
    
}

