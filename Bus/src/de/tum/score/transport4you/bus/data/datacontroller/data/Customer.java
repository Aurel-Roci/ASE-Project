package de.tum.score.transport4you.bus.data.datacontroller.data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import de.tum.score.transport4you.shared.mobilebusweb.data.impl.AbstractPersistenceObject;

/**
 * Represents a customer
 * @author hoerning
 *
 */
@Entity
public class Customer extends AbstractPersistenceObject{
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long id;
	
	private long systemId;
	
	@OneToOne(mappedBy="customer", cascade = CascadeType.ALL)
	private Trip trip;

	public long getSystemId() {
		return systemId;
	}

	public void setSystemId(long systemId) {
		this.systemId = systemId;
	}

	public long getId() {
		return id;
	}

	public Trip getTrip() {
		return trip;
	}

	@Override
	public long getPersistenceId() {
		return this.id;
	}
	
}
