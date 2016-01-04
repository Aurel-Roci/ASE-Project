package de.tum.score.transport4you.shared.mobilebusweb.data.impl;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Represents an ETicket
 * @author hoerning
 *
 */
@Entity
public class ETicket extends AbstractPersistenceObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4865268647836014207L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long id;
	
	private boolean invalidated;
	
	private String customerId;
	
	private long validTime;
	
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date validUntil;
	
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date invalidatedAt;
	
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date sellingDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isInvalidated() {
		return invalidated;
	}

	public void setInvalidated(boolean invalidated) {
		this.invalidated = invalidated;
	}

	public long getValidTime() {
		return validTime;
	}

	public void setValidTime(long validTime) {
		this.validTime = validTime;
	}

	public Date getValidUntil() {
		return validUntil;
	}

	public void setValidUntil(Date validUntil) {
		this.validUntil = validUntil;
	}

	public Date getSellingDate() {
		return sellingDate;
	}

	public void setSellingDate(Date sellingDate) {
		this.sellingDate = sellingDate;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Date getInvalidatedAt() {
		return invalidatedAt;
	}

	public void setInvalidatedAt(Date invalidatedAt) {
		this.invalidatedAt = invalidatedAt;
	}
	
	public boolean equals(Object o)  {
		
		if(!(o instanceof ETicket)){
			return false;
		}
		
		ETicket toCheck = (ETicket) o;
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
