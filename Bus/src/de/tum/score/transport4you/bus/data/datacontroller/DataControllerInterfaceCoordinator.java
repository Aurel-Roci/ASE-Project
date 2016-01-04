package de.tum.score.transport4you.bus.data.datacontroller;

import de.tum.score.transport4you.bus.data.datacontroller.impl.DataController;
import de.tum.score.transport4you.bus.data.datacontroller.impl.PersistentDataController;
import de.tum.score.transport4you.bus.data.datacontroller.impl.Startup;

/**
 * This class allows other components to retrieve interfaces probided by this class
 * @author hoerning
 *
 */
public class DataControllerInterfaceCoordinator {
	
	/* Initialization possibilities */
	private static ISettingsDataController settingsDataController  = null;
	
	/**
	 * Retrieve SettingsDataController
	 * @return
	 */
	public static ISettingsDataController getSettingsDataController() {
		if(DataControllerInterfaceCoordinator.settingsDataController== null){
			return DataController.getInstance();
		} else {
			return DataControllerInterfaceCoordinator.settingsDataController;
		}
	}
	
	public static IStartup getStartup(){
		return Startup.getInstance();
	}
	
	/**
	 * Initializes the coordinator with a non standard interface
	 * @param controller
	 */
	protected static void setSettingsDataController(ISettingsDataController controller){
		settingsDataController = controller;
	}
	
	/**
	 * Returns the interface to access the ETicketTypeList
	 * @return
	 */
	public static IETicketTypeDataController getETicketTypeDataController() {
		return PersistentDataController.getInstance();
	}
	
	/**
	 * Returns the interface to access the Persistence Managing Interface
	 * @return
	 */
	public static IPersistenceController getPersistentDataController(){
		return PersistentDataController.getInstance();
	}
	
	/**
	 * Returns the interface to access the Transaction Interface
	 * @return
	 */
	public static ITransactionDataController getTransactionDataController(){
		return PersistentDataController.getInstance();
	}

}
