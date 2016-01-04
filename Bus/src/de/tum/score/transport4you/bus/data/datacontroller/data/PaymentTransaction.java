package de.tum.score.transport4you.bus.data.datacontroller.data;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import de.tum.score.transport4you.shared.mobilebusweb.data.impl.AbstractPersistenceObject;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.ETicket;

/**
 * Represents an Transaction
 * @author hoerning
 *
 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@NamedQuery (name="getAllTransactions", query="SELECT x FROM PaymentTransaction x")
public class PaymentTransaction extends AbstractPersistenceObject{
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long id;
	
	private boolean success;
	
	private String userID;
	
	private Double amount;
	
	@OneToMany(fetch=FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH})
	private List<ETicket> soldETickets;
	
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date time;
	
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public List<ETicket> getSoldETickets() {
		return soldETickets;
	}

	public void setSoldETickets(List<ETicket> soldETickets) {
		this.soldETickets = soldETickets;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	public boolean equals(Object o)  {
	
		if(!(o instanceof PaymentTransaction)){
			return false;
		}
		
		PaymentTransaction toCheck = (PaymentTransaction) o;
		if(toCheck.id == this.id) {
			return true;
		}
		
		return false;
	}

	@Override
	public long getPersistenceId() {
		return this.id;
	}

	
}
