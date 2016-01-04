package de.tum.score.transport4you.bus.communication.connectionmanager.impl.protocol.mobilesystem;

import java.util.ArrayList;
import java.util.concurrent.Executor;

import org.apache.log4j.Logger;

import de.tum.score.transport4you.bus.application.applicationcontroller.ApplicationControllerInterfaceCoordinator;
import de.tum.score.transport4you.bus.communication.bluetoothcontroller.data.BluetoothConnection;
import de.tum.score.transport4you.bus.data.datacontroller.DataControllerInterfaceCoordinator;

/**
 * This Thread takes care of handling managing issues for established mobile system connections.
 * @author hoerning
 *
 */
public class MobileSystemConnectionManager extends Thread{
	
	/* The amount of time to wait between checking */
	private static int DAEMON_TIMER = DataControllerInterfaceCoordinator.getSettingsDataController().getSystemConfiguration().getDaemonThreadTimer();
	
	/* The running status of the thread */
	private boolean started = false;
	
	/* The request to stop the thread; */
	private boolean stopThread = false;
	
	/* Logger */
	private Logger logger = Logger.getLogger("Mobile System Connection");
	
	/* Active Connections and management variables */
	private ArrayList<MobileSystemConnection> connectionList = new ArrayList<MobileSystemConnection>();
	private ArrayList<MobileSystemConnection> connectionsToAdd = new ArrayList<MobileSystemConnection>();
	private ArrayList<MobileSystemConnection> connectionsToDelete = new ArrayList<MobileSystemConnection>();
	
	/**
	 * Starts the thread.
	 */
	public void run(){
		
		this.started = true;
		synchronized(this) {
			this.stopThread = false;
		}
		
		logger.debug("Mobile System Connection Manager Thread started");
		
		//Loop and peform management tasks
		while(!stopThread){
			
			synchronized(this){
			
				// Add connections
				this.connectionList.addAll(connectionsToAdd);
				connectionsToAdd.clear();
				
				// Delete connections
				this.connectionList.removeAll(connectionsToDelete);
				connectionsToDelete.clear();
				
				//Stop Thread if needed
				if(this.connectionList.isEmpty()){
					
						this.stopThread = true;
						this.started = false;
						
				} else {
				
					logger.debug("Checking active connections");
							
					// Check active connections
					for(MobileSystemConnection mobileSystemConnection: this.connectionList){
						checkConnection(mobileSystemConnection);
					}
					
					logger.debug("Checking active connections done");
					
					}
					
			}
			
			//Sleep predefined time
			try {
				Thread.sleep(DAEMON_TIMER);
			} catch (InterruptedException e) {
			}
		}
		
		logger.debug("Mobile System Connection Manager Thread stopped");
		
	}
	
	private void checkConnection(MobileSystemConnection mobileSystemConnection) {
		
		//*1* Check whether connections is still active: if not => remove
		if(!mobileSystemConnection.isActive()){
			logger.debug("Detected inactive connection, preparing to remove connection");
			this.connectionsToDelete.add(mobileSystemConnection);
		}
	}

	/**
	 * Opens a new Mobile System Connection with the specified Bluetooth connection
	 * @param connection
	 */
	public synchronized void openConnection(BluetoothConnection connection) {
		
		//Create a new mobile system connection and start listening
		MobileSystemConnection mobileSystemConnection = new MobileSystemConnection(connection);
		Executor executor = ApplicationControllerInterfaceCoordinator.getSystem().getExecutor("MobileSystemConnections");
		this.connectionsToAdd.add(mobileSystemConnection);
		
		//Start manager thread if needed
		if(this.started == false) {
			ApplicationControllerInterfaceCoordinator.getSystem().getExecutor("Daemons").execute(this);
		}
		
		//Start listening
		executor.execute(mobileSystemConnection);
	}
	
	/**
	 * Closes the connection by removing it from the connection list
	 * @param mobileSystemConnection
	 */
	public synchronized void closeConnection(MobileSystemConnection mobileSystemConnection){
		
		//Remove from connection list
		this.connectionsToDelete.add(mobileSystemConnection);
		
	}
	
	/**
	 * Stops the thread after the execution of the current work
	 */
	public synchronized void stopThread(){
		
		this.stopThread = true;
	}

	
	
}
