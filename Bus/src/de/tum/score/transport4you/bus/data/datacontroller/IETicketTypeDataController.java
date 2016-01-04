package de.tum.score.transport4you.bus.data.datacontroller;


import java.util.List;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.ETicketType;


/**
 * Offers methods to acces the E-Ticket type list
 * @author hoerning
 *
 */
public interface IETicketTypeDataController {
	
	public List<ETicketType> getAllETicketTypes();

}
