package de.tukl.score.transport4you.mobile.application.applicationcontroller.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class ApplicationSingletonTest {
   
    @Test
    /**
     * Test if the application singleton works correctly and creates only one instance of an application controller
     */
    public void getApplicationControllerTest() throws Exception {
        ApplicationController firstTry = null;
        ApplicationController secondTry = null; 
    	
    	firstTry = (ApplicationController) ApplicationSingleton.getApplicationController();
    	secondTry = (ApplicationController) ApplicationSingleton.getApplicationController();
    	
    	ApplicationController counterTest = new ApplicationController();
    	
    	assertTrue(firstTry == secondTry);
    	assertFalse(counterTest == firstTry);
    	assertFalse(counterTest == secondTry);
    }
    
}

