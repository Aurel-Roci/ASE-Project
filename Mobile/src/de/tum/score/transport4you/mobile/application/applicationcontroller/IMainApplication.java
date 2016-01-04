package de.tum.score.transport4you.mobile.application.applicationcontroller;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import de.tum.score.transport4you.shared.mobilebus.data.impl.message.DataMessage;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.BlobEntry;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.ETicket;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.ETicketType;
import de.tum.score.transport4you.shared.mobileweb.impl.message.MobileSettings;

public interface IMainApplication extends Runnable{
	
	/**
	 * Returns whether the app is running in debug mode or not
	 * @return
	 */
	public boolean isDebugModeEnabled();
	
	/**
	 * Returns the entry of the currently stored blob on that device
	 * 
	 * @return
	 */
	public BlobEntry getStoredBlobEntry();
	
	/**
	 * Registers an new activity to enable callbacks.
	 * 
	 * @param activity The activity that should be registered.
	 */
	public void registerActivity(Activity activity);
	
	/**
	 * This checks whether the provided credentials are valid.
	 * 
	 * @param username The username of the customer.
	 * @param password The password of the customer.
	 * @return
	 */
	public boolean login(String username, String password, boolean rememberLogin);
	
	/**
	 * Returns the stored mobile settings of the customer.
	 * 
	 * @return Returns a MobileSettings object
	 */
	public MobileSettings getMobileSettings();
	
	/**
	 * Sets the changed mobile settings
	 * 
	 * @param mobileSettings
	 */
	public void setMobileSettings(MobileSettings mobileSettings);
	
	/**
	 * Initializes all necessary components and passes context
	 * 
	 * @param context The current context
	 */
	public void initialize(Context context);
	
	/**
	 * Start to scan for the next bus.
	 */
	public void startBusScan();	
	
	/**
	 * Buys the selected eticket type from the bus system
	 * @param ETicketType The eticket type of the to-be purchased ticket
	 */
	public void buyETicket(ETicketType eTicketTypeName);
	
	/**
	 * Starts the synchronization process with the web system.
	 */
	public void synchronize();
	
	/**
	 * Tells the application manager to start the eticket exchange.
	 */
	public void startETicketExchange();
	
	/**
	 * Tells the application controller to release all resources and kill all open activities.
	 */
	public void shutdownSystem();
	
	/**
	 * Sends the specified message as status bar notification
	 * @param title
	 * @param message
	 */
	public void sendStatusBarNotification(String title, String message);

	/**
	 * Read out all stored etickets and sends them to the bus within a blob.
	 */
	public void sendETicket();
	
	/**
	 * Distinguish which type of DataMessage was received and delegate further actions
	 * @param data
	 */
	public void processData(DataMessage datamsg);
	
	/**
	 * Tells the ApplicationController to reset the whole bus connection after an failed connectiob attempt,
	 * in order to allow a retry
	 * @param reason
	 */
	public void resetConnection(String reason);
}
