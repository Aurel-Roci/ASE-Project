package de.tum.score.transport4you.bus.data.datacontroller.impl;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import de.tum.score.transport4you.bus.data.datacontroller.IETicketTypeDataController;
import de.tum.score.transport4you.bus.data.datacontroller.IPersistenceController;
import de.tum.score.transport4you.bus.data.datacontroller.ITransactionDataController;
import de.tum.score.transport4you.bus.data.datacontroller.ITripsDataController;
import de.tum.score.transport4you.bus.data.datacontroller.data.PaymentTransaction;
import de.tum.score.transport4you.bus.data.datacontroller.data.Trip;
import de.tum.score.transport4you.bus.data.datacontroller.error.PersistenceException;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.AbstractPersistenceObject;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.ETicketType;

/**
 * This controller handles all tasks related to data persistence with use of the JPA
 * @author hoerning
 *
 */
public class PersistentDataController implements ITripsDataController, IETicketTypeDataController, ITransactionDataController, IPersistenceController{
	
	/* The Singleton instance */
	private static PersistentDataController instance;
	
	/* Looger */
	private Logger logger = Logger.getLogger("Data");
	
	/* JPA Managers */
	private EntityManagerFactory factory;
	private EntityManager entityManager;
	
	/**
	 * Returns the singleton instance
	 * @return
	 */
	public static PersistentDataController getInstance(){
		if(instance==null){
			instance = new PersistentDataController();
		}
		return instance;
	}
	
	private PersistentDataController(){
		
		//Initialize JPA related objects
		logger.debug("Initializing the EclipseLink JPA 2.0 system");
		factory = Persistence.createEntityManagerFactory("T4Y_BUS");
		entityManager = factory.createEntityManager();
		
		logger.debug("Persistence API successful initialized");
	}

	@Override
	public void removeTripList(ArrayList<Trip> trip) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<Trip> getActiveTrips() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Trip> getTripList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(List<AbstractPersistenceObject> loggingList) throws PersistenceException{
		
		try {
		// Merge all objects and save them
		entityManager.getTransaction().begin();
		for(AbstractPersistenceObject object : loggingList){
			if(object.getPersistenceId()==0){
				//Not managed yet
				entityManager.persist(object);
			} else {
				entityManager.merge(object);
			}
		}
		entityManager.flush();
		entityManager.getTransaction().commit();
		
		} catch (RuntimeException e) {
			logger.error("Error while accessing the db: "+e.getMessage());
			throw new PersistenceException("Error while accessing the db: "+e.getMessage());
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ETicketType> getAllETicketTypes() {
		
		logger.debug("Retrieving ETicketTypes from Persistence");

		entityManager.getTransaction().begin();
		Query query = entityManager.createQuery("SELECT x FROM ETicketType x");
		List<ETicketType> results = query.getResultList();
		entityManager.getTransaction().commit();
		
		for(ETicketType o : results){
			entityManager.detach(o);
		}
		
		return results;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PaymentTransaction> getAllTransactions() {
		
		logger.debug("Retrieving Transactions from Persistence");

		entityManager.getTransaction().begin();
		Query query = entityManager.createNamedQuery("getAllTransactions");
		List<PaymentTransaction> results = query.getResultList();
		entityManager.getTransaction().commit();
		
		for(PaymentTransaction o : results){
			entityManager.detach(o);
		}
		
		return results;
	}


}
