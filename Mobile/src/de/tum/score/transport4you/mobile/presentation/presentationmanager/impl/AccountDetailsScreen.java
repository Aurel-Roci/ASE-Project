package de.tum.score.transport4you.mobile.presentation.presentationmanager.impl;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import de.tum.score.transport4you.mobile.R;
import de.tum.score.transport4you.mobile.application.applicationcontroller.IMainApplication;
import de.tum.score.transport4you.mobile.application.applicationcontroller.impl.ApplicationSingleton;
import de.tum.score.transport4you.mobile.presentation.presentationmanager.IPresentation;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.BlobEntry;

public class AccountDetailsScreen extends Activity implements IPresentation{
    private IMainApplication mainApplication;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accountdetails);
        
        mainApplication = ApplicationSingleton.getApplicationController();
        mainApplication.registerActivity(this);
        
        //get blob from device
        BlobEntry entry = mainApplication.getStoredBlobEntry();
        
    	//set account settings according to blob content if not null
        if(entry != null) {       	
        	//lookup views for data population
            TextView userName = (TextView) findViewById(R.id.lblUsername);
            TextView address = (TextView) findViewById(R.id.lblAddress); 
            TextView accountType = (TextView) findViewById(R.id.lblAccountType);
            TextView accountBalance = (TextView) findViewById(R.id.lblAccountBalance);
                       
            //update views
            userName.setText(entry.getUserName() != null ? entry.getUserName() : "n/a");
            address.setText(entry.getUserAddress()  != null ? entry.getUserAddress() : "n/a");
            accountType.setText(entry.getAccountType()  != null ? entry.getAccountType() : "n/a");
            accountBalance.setText(entry.getAccountBalance()  != null ? entry.getAccountBalance().toString() : "n/a");
        }
        	
    }
	
    public void onDestroy() {
    	super.onDestroy();
    }

	@Override
	public void shutdown() {
		this.finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.mn_exit:
	        mainApplication.shutdownSystem();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}

	@Override
	public void updateProgessDialog(String title, String message, boolean visible, Integer increment) {
		// TODO Auto-generated method stub
		
	}
}