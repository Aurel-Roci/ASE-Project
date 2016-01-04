package de.tum.score.transport4you.shared.mobilebus.data.impl.message;

import java.io.Serializable;
import java.util.List;

import de.tum.score.transport4you.shared.mobilebusweb.data.impl.ETicketType;

/**
 * The answer from the bus system to an invalid or empty eticket from the mobile system, 
 * to tell the mobile system which etickets can be purchased.
 */
public class AvailableETicketTypesMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7643019078486471408L;

	private List<ETicketType> availableETicketTypes;

	public List<ETicketType> getAvailableETicketTypes() {
		return availableETicketTypes;
	}

	public void setAvailableETicketTypes(
			List<ETicketType> newList) {
		this.availableETicketTypes = newList;
	}
}
