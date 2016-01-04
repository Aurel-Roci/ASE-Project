package de.tum.score.transport4you.bus.data.datacontroller;

import java.util.List;

import de.tum.score.transport4you.bus.data.datacontroller.error.PersistenceException;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.AbstractPersistenceObject;

/**
 * This interface provides generic methods for managing the persistence objects, eg. transactional commit
 * @author hoerning
 *
 */
public interface IPersistenceController {
	
	/**
	 * Saves the object by performing a transaction
	 * @param persistenceObjectList The already managed persistence objects
	 */
	public void save(List<AbstractPersistenceObject> persistenceObjectList) throws PersistenceException;

}
