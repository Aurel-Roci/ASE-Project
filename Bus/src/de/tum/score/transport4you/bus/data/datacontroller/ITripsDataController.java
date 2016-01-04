package de.tum.score.transport4you.bus.data.datacontroller;

import java.util.ArrayList;

import de.tum.score.transport4you.bus.data.datacontroller.data.Trip;

/**
 * Offers method to manipulate Trips
 * @author hoerning
 *
 */
public interface ITripsDataController {
	
	
	/**
	 * Removes the specified trips
	 * @param trip
	 */
	public void removeTripList(ArrayList<Trip> trip);
	
	/**
	 * Returns only active trips
	 */
	public ArrayList<Trip> getActiveTrips();
	
	/**
	 * Returns the whole list of trips
	 */
	public ArrayList<Trip> getTripList();

}
