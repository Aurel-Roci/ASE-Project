package de.tum.score.transport4you.mobile.presentation.presentationmanager.impl;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import de.tum.score.transport4you.mobile.R;
import de.tum.score.transport4you.mobile.application.applicationcontroller.IMainApplication;
import de.tum.score.transport4you.mobile.application.applicationcontroller.impl.ApplicationSingleton;
import de.tum.score.transport4you.mobile.presentation.presentationmanager.IPresentation;
import de.tum.score.transport4you.shared.mobileweb.impl.message.MobileSettings;

public class SettingsScreen extends Activity implements IPresentation{
    private IMainApplication mainApplication;
	private CheckBox rememberLoginCheckBox;
	private CheckBox autoSynchronizationCheckBox;
	private CheckBox autoScanCheckBox;
	private CheckBox autoSMSNotificationCheckBox;
	private MobileSettings mobileSettings;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
        mainApplication = ApplicationSingleton.getApplicationController();
        mainApplication.registerActivity(this);
        
        autoSynchronizationCheckBox = (CheckBox) findViewById(R.id.chk_automaticsynchronization);
        autoScanCheckBox = (CheckBox) findViewById(R.id.chk_automaticscanning);
        autoSMSNotificationCheckBox = (CheckBox) findViewById(R.id.chk_automaticsmsnotification);
        
        OnClickListener clickListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				saveSettings();
			}
		};
        
        autoSynchronizationCheckBox.setOnClickListener(clickListener);
        autoScanCheckBox.setOnClickListener(clickListener);
        autoSMSNotificationCheckBox.setOnClickListener(clickListener);
    }
    
    public void onStart() {
    	super.onStart();
    	loadSettings();
    }
    
    public void onDestroy() {
    	super.onDestroy();
    }

	@Override
	public void shutdown() {
		this.finish();
	}
    
    /**
     * Update all changed settings
     */
    private void saveSettings() {
    	Log.i("T4Y-Settings", "Save mobile settings");
    	
    	mobileSettings.setAllowAutoSynchronization(autoSynchronizationCheckBox.isChecked());
        mobileSettings.setAllowAutoScan(autoScanCheckBox.isChecked());
        mobileSettings.setAllowAutoSMSNotification(autoSMSNotificationCheckBox.isChecked());
        
    	mainApplication.setMobileSettings(mobileSettings);
    }
    
    /**
     * Load all mobile settings 
     */
    private void loadSettings() {
    	Log.i("T4Y-Settings", "Load mobile settings");
    	mobileSettings = mainApplication.getMobileSettings();
    	
    	//update display
        autoSynchronizationCheckBox.setChecked(mobileSettings.isAllowAutoSynchronization());
        autoScanCheckBox.setChecked(mobileSettings.isAllowAutoScan());
        autoSMSNotificationCheckBox.setChecked(mobileSettings.isAllowAutoSMSNotification());
    }

	@Override
	public void updateProgessDialog(String title, String message, boolean visible, Integer increment) {
		// TODO Auto-generated method stub
		
	}

}