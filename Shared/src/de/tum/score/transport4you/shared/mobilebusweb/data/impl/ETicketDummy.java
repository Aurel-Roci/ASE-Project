package de.tum.score.transport4you.shared.mobilebusweb.data.impl;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class ETicketDummy implements Serializable {
	private long id;
	private String customerId;
	private Date validUntil;
	
	public ETicketDummy(ETicket ticket) {
		id = ticket.getId();
		customerId = ticket.getCustomerId();
		validUntil = ticket.getValidUntil();
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "ETicket id: " + id + "\n" +
				"Customer id: " + customerId + "\n" +
				"Valid until: " + validUntil;
	}

}
