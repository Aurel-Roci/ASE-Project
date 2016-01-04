package de.tum.score.transport4you.shared.mobilebus.data.impl.message;

import java.io.Serializable;
import java.util.ArrayList;

import de.tum.score.transport4you.shared.mobilebusweb.data.impl.ETicketType;

/**
 * The answer form the mobile system which eticket type should be purchased.
 */
public class PurchaseETicketTypeMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7643019078486471408L;

	private ETicketType selectedETicketType;

	public ETicketType getSelectedETicketType() {
		return selectedETicketType;
	}

	public void setSelectedETicketType(ETicketType selectedETicketType) {
		this.selectedETicketType = selectedETicketType;
	}

}
