package de.tum.score.transport4you.mobile.presentation.presentationmanager.impl;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import de.tum.score.transport4you.mobile.R;
import de.tum.score.transport4you.mobile.application.applicationcontroller.IMainApplication;
import de.tum.score.transport4you.mobile.application.applicationcontroller.impl.ApplicationSingleton;
import de.tum.score.transport4you.mobile.presentation.presentationmanager.IPresentation;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.BlobEntry;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.ETicket;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.ETicketType;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.QRHelpers;

public class ETicketListScreen extends Activity implements IPresentation {
	private IMainApplication mainApplication;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eticketlist);

		mainApplication = ApplicationSingleton.getApplicationController();
		mainApplication.registerActivity(this);

		BlobEntry entry = mainApplication.getStoredBlobEntry();

		ArrayList<ETicket> tickets = new ArrayList<ETicket>();

		// get etickets from blob
		if (entry != null) {
			tickets = entry.geteTicketList();
		}

		// Create the adapter to convert the array to views
		TicketAdapter adapter = new TicketAdapter(this, 0, tickets);

		// Attach the adapter to a ListView
		final ListView listView = (ListView) findViewById(R.id.list_etickets);
		listView.setAdapter(adapter);
		// clicking a single item displays the qr code
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(parent.getContext(), QRCodeScreen.class);
				ETicket selected = (ETicket) listView.getItemAtPosition(position);
				///Ticket Encryption
				try {
					String ticketJsonEncrypted  = QRHelpers.createQRCodeForETicket(selected); //defined in Shared
					String ticketJson = selected.serialize(); 
					
					intent.putExtra("ETicket", ticketJson); 
					intent.putExtra("Enc", ticketJsonEncrypted);
					//Test for ticketJsonEncrypted
					//intent.putExtra("ETicket", ticketJsonEncrypted);
					startActivity(intent);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
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