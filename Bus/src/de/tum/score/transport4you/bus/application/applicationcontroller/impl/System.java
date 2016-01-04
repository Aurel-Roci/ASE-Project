package de.tum.score.transport4you.bus.application.applicationcontroller.impl;

import java.io.File;
import java.security.Security;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import de.tum.score.transport4you.bus.application.applicationcontroller.ApplicationControllerInterfaceCoordinator;
import de.tum.score.transport4you.bus.application.applicationcontroller.ISystem;
import de.tum.score.transport4you.bus.communication.connectionmanager.ConnectionManagerInterfaceCoordinator;
import de.tum.score.transport4you.bus.data.datacontroller.DataControllerInterfaceCoordinator;
import de.tum.score.transport4you.bus.data.datacontroller.error.DataControllerInitializingException;

/**
 * This class is the startup class of the Bus System.
 * It initializes all needed components
 * @author hoerning
 *
 */
public class System implements ISystem{
	
	/* Used for logging */
	private static Logger logger;
	
	/* The singleton instance */
	private static System instance;
	
	/* Executor for Thread Controlling */
	private SortedMap<String,Executor> executorMap = new TreeMap<String,Executor>();
	
	/**
	 * Returns the singleton instance
	 * @return
	 */
	public static System getInstance(){
		if(instance==null){
			instance= new System();
		}
		return instance;
	}

	/**
	 * The main method beeing called on startup
	 * @param args Parameter list:<br>1 - path to configuration file<br>2 - path to logging property file
	 */
	public static void main(String[] args) {
		
		//Check parameter size
		if(args.length!= 2) {
			throw new RuntimeException("Not enough parameters");
		}
		File configurationFile = new File(args[0]);
		File loggingConfigurationFile = new File(args[1]);
		
		//Initialize Logging
		PropertyConfigurator.configure(loggingConfigurationFile.getAbsolutePath());
		logger = Logger.getLogger("System");
		
		logger.info("Loading System");
		logger.debug("Logging system is loaded");
		
		//Load additional libraries
		logger.debug("Loading Security Provider");
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		logger.debug("Security Provider loaded");
				
		//Initialize Data Controller Component with configuration file
		try {
			logger.info("Initialize Data Controller Component");
			DataControllerInterfaceCoordinator.getStartup().init(configurationFile);
		} catch (DataControllerInitializingException e) {
			logger.error("Error while initializing the Data Controller");
			throw new RuntimeException("Error while initializing the Data Controller: "+e.getMessage());
		}
		logger.info("Data Controller Component initialized");
		
		//Initialize the Connection Manager Component
		logger.info("Initialize Connection Manager Component");
		ConnectionManagerInterfaceCoordinator.getStartup().init();
		logger.info("Connection Manager Component initialized");
		
		//Initialize the Application Controller Component
		logger.info("Initialize Application Controller Component");
		ApplicationControllerInterfaceCoordinator.getStartup().init();
		logger.info("Application Controller Component initialized");
		
		
	}

	@Override
	public Executor getExecutor(String category) {
		
		//Create a new Executor if no executor exists
		if(this.executorMap.get(category) == null) {
			int threadPoolSize = DataControllerInterfaceCoordinator.getSettingsDataController().getSystemConfiguration().getThreadPoolSize();
			logger.debug("Creating new Thread Pool ("+category+") with "+String.valueOf(threadPoolSize)+" threads");
			Executor executor = Executors.newFixedThreadPool(threadPoolSize);
			this.executorMap.put(category, executor);
		}
		
		return this.executorMap.get(category);
	}

}
