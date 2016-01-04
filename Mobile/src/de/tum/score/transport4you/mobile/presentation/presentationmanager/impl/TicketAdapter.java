package de.tum.score.transport4you.mobile.presentation.presentationmanager.impl;

import java.util.Date;
import java.util.List;

import de.tum.score.transport4you.mobile.R;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.ETicket;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

public class TicketAdapter extends ArrayAdapter<ETicket> {

	public TicketAdapter(Context context, int resource, List<ETicket> tickets) {
		super(context, 0, tickets);
	}

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
       // Get the data item for this position
		ETicket ticket = getItem(position);    
		
       // Check if an existing view is being reused, otherwise inflate the view
       if (convertView == null) {
          convertView = LayoutInflater.from(getContext()).inflate(R.layout.eticket, parent, false);
       }

       // Lookup view for data population
       TextView ticketInvalidationDate = (TextView) convertView.findViewById(R.id.txt_invalidation);
       TextView ticketExpirationDate = (TextView) convertView.findViewById(R.id.txt_expiration);
       
       // Populate the data into the template view using the data object
       ticketInvalidationDate.setText(ticket.getInvalidatedAt() != null ? ticket.getInvalidatedAt().toLocaleString() : "unknown");
       ticketExpirationDate.setText(ticket.getValidUntil()  != null ? ticket.getValidUntil().toLocaleString() : "unknown");
       
       //check if ticket is already invalidated, expired or fresh and change color accordingly
       TableLayout tab = (TableLayout) convertView.findViewById(R.id.tab_ticket);
       if(ticket.isInvalidated() && new Date().compareTo(ticket.getValidUntil()) <= 0) {
    	   tab.setBackgroundColor(Color.YELLOW);
       } else if(ticket.isInvalidated() && new Date().compareTo(ticket.getValidUntil()) > 0) {
    	   tab.setBackgroundColor(Color.RED);
       } else if(!ticket.isInvalidated()) {
    	   tab.setBackgroundColor(Color.GREEN);
       }
       
       // Return the completed view to render on screen
       return convertView;
   }
	
}
