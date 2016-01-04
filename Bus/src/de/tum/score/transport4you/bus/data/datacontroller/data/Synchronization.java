package de.tum.score.transport4you.bus.data.datacontroller.data;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import de.tum.score.transport4you.shared.mobilebusweb.data.impl.AbstractPersistenceObject;

/**
 * The data class representing a Synchronization. Note that this is an persistence class.
 * @author hoerning
 *
 */
@Entity
public class Synchronization extends AbstractPersistenceObject{

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long id;
	
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date start;
	
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date end;
	
	
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

	@Override
	public long getPersistenceId() {
		return this.id;
	}
}
