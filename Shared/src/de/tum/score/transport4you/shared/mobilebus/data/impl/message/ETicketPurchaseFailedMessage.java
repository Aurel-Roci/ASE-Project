package de.tum.score.transport4you.shared.mobilebus.data.impl.message;

import java.io.Serializable;

/*
 * Signals that the eticket purchase process has failed
 */
public class ETicketPurchaseFailedMessage implements Serializable{

	private static final long serialVersionUID = 107949476984418671L;
	
	private String reason;
	
	public ETicketPurchaseFailedMessage(String reason) {
		this.reason = reason;
	}
	
	public String getReason() {
		return this.reason;
	}
}
