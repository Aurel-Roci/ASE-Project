package de.tum.score.transport4you.mobile.presentation.presentationmanager.impl;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import de.tum.score.transport4you.mobile.R;
import de.tum.score.transport4you.mobile.application.applicationcontroller.IMainApplication;
import de.tum.score.transport4you.mobile.application.applicationcontroller.impl.ApplicationSingleton;
import de.tum.score.transport4you.mobile.presentation.presentationmanager.IPresentation;
import de.tum.score.transport4you.shared.mobilebus.data.impl.message.AvailableETicketTypesMessage;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.ETicketType;

public class AvailableETicketListScreen extends ListActivity implements IPresentation{
    private IMainApplication mainApplication;
	private ArrayAdapter<String> availableETicketTypes;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainApplication = ApplicationSingleton.getApplicationController();
        mainApplication.registerActivity(this);
        
        if (this.getIntent().getExtras() != null) {
            Object obj =  this.getIntent().getExtras().get("eTicketTypes");
            
            if ( obj != null && obj instanceof AvailableETicketTypesMessage) {
            	final List<ETicketType> eTicketTypes = ((AvailableETicketTypesMessage)obj).getAvailableETicketTypes();
            
            	ArrayList<String> eTicketTypesStrings = new ArrayList<String>();  
            	for(ETicketType ett : eTicketTypes) {
            		eTicketTypesStrings.add("Name: " +ett.getName() + " Validity: " + ett.getValidMinutes() + " Price: " + ett.getPrice() +" Amount: "+ett.getAmountTickets());
            	}
            	
		    	setListAdapter(new ArrayAdapter<String>(this, R.layout.available_etickettypes, eTicketTypesStrings));
		    	ListView eTicketListView = getListView();
		
		    	eTicketListView.setOnItemClickListener(new OnItemClickListener() {
		    	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		                
		            	//TODO select right etickettype name
		                // Get the name of the selected eticket type
		            	//TextView eTicketTypeNameView = (TextView) view;
		                //String eTicketTypeName = eTicketTypeNameView.getText().toString().split(" ")[1];
		
		                ETicketType selectedETicketType = eTicketTypes.get(position);
		                
		                mainApplication.buyETicket(selectedETicketType);
		                finish();
		    	    }
			    });

            }
        }

    }
	
	/**
	 * Updates the list of available eTicket
	 * @param ticketTypes
	 */
	public void setAvailableETicketTypes(ArrayList<ETicketType> ticketTypes) {
		
		//this.availableETicketTypes = ticketTypes;
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
	public void onBackPressed() {
	    return;
	}

	@Override
	public void updateProgessDialog(String title, String message, boolean visible, Integer increment) {
		// TODO Auto-generated method stub
		
	}
}