package de.tum.score.transport4you.shared.mobilebusweb.data.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Represents an ETicket Type
 * @author hoerning
 *
 */
@Entity
public class ETicketType extends AbstractPersistenceObject{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8280256021201295627L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long id;
	
	private String name;
	
	private int validMinutes;
	
	private Double price;
	
	private int amountTickets;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getValidMinutes() {
		return validMinutes;
	}

	public void setValidMinutes(int validMinutes) {
		this.validMinutes = validMinutes;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getAmountTickets() {
		return amountTickets;
	}

	public void setAmountTickets(int amountTickets) {
		this.amountTickets = amountTickets;
	}

	@Override
	public long getPersistenceId() {
		return this.id;
	}
	
}
