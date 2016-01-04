package de.tum.score.transport4you.mobile.presentation.presentationmanager.impl;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import de.tum.score.transport4you.mobile.R;
import de.tum.score.transport4you.mobile.application.applicationcontroller.IMainApplication;
import de.tum.score.transport4you.mobile.application.applicationcontroller.impl.ApplicationSingleton;
import de.tum.score.transport4you.mobile.presentation.presentationmanager.IPresentation;

public class CustomerInformationScreen extends Activity implements IPresentation{
    private IMainApplication mainApplication;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customerinformation);
        
        mainApplication = ApplicationSingleton.getApplicationController();
        mainApplication.registerActivity(this);
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