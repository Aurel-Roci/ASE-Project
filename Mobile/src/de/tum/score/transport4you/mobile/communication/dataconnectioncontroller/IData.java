package de.tum.score.transport4you.mobile.communication.dataconnectioncontroller;

import java.io.IOException;
import java.io.Serializable;

import org.restlet.resource.ResourceException;

import android.content.Context;

import de.tum.score.transport4you.mobile.communication.dataconnectioncontroller.error.RESTException;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.BlobEnvelope;
import de.tum.score.transport4you.shared.mobileweb.impl.message.MobileSettings;

public interface IData {
	
	/**
	 * Checks the authentication with the web system using the provided credentials.
	 * 
	 * @param username The username of the customer for authentification purposes
	 * @param password The password of the customer for authentification purposes
	 * @return Returns true if the authentication was successful, false otherwise
	 * @throws RESTException 
	 * @throws IOException 
	 * @throws ResourceException 
	 */
	public boolean checkAuthentication(Context context, String username, String password) throws RESTException;
	
	/**
	 * Synchronizes the blob on the mobile system with the web system.
	 * 
	 * @param username The username of the customer for authentification purposes
	 * @param password The password of the customer for authentification purposes
	 * @param blob The blob that should be updated
	 * @return Returns an updated blob if changes are necessary, an empty one otherwise
	 * @throws RESTException 
	 */
	public BlobEnvelope synchronizeETickets(String username, String password, BlobEnvelope blob) throws RESTException;
	
	/**
	 * Synchronizes the mobile settings with the web system.
	 * 
	 * @param username The username of the customer for authentification purposes
	 * @param password The password of the customer for authentification purposes
	 * @param mobileSettings The blob that should be updated
	 * @return Returns an updated MobileSettings object if changes are necessary, an empty one otherwise
	 * @throws RESTException 
	 */
	public MobileSettings synchronizeSettings(String username, String password, MobileSettings mobileSettings) throws RESTException;

}
