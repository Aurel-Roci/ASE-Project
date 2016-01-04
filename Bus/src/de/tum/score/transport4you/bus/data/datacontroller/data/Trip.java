package de.tum.score.transport4you.bus.data.datacontroller.data;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import de.tum.score.transport4you.shared.mobilebusweb.data.impl.AbstractPersistenceObject;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.ETicket;

/**
 * The data class representing a Trip. Note that this is an persistence class.
 * @author hoerning
 *
 */
@Entity
@NamedQuery (name="getAllTrips", query="SELECT x FROM Trip x")
public class Trip extends AbstractPersistenceObject{

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long id;
	
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date start;
	
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date end;
	
	private String customerId;
	
	@OneToOne(fetch=FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH})
	private ETicket customerTicket;
	
	/* Getters/Setters */
	public long getId() {
		return id;
	}

	public Date getStart() {
		return start;
	}


	public void setStart(Date start) {
		this.start = start;
	}


	public Date getEnd() {
		return end;
	}


	public void setEnd(Date end) {
		this.end = end;
	}

	public ETicket getCustomerTicket() {
		return customerTicket;
	}

	public void setCustomerTicket(ETicket customerTicket) {
		this.customerTicket = customerTicket;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	@Override
	public long getPersistenceId() {
		return this.id;
	}
	
}
