package de.tum.score.transport4you.mobile.application.synchronizationmanager;

import android.content.Context;
import de.tum.score.transport4you.mobile.application.synchronizationmanager.error.BlobException;
import de.tum.score.transport4you.mobile.application.synchronizationmanager.error.SynchronizationException;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.BlobEnvelope;

public interface ISynchronization {

	/**
	 * Invokes a synchronization process with the web system
	 * @throws SynchronizationException
	 */
	public void synchronize() throws SynchronizationException;
	
	/**
	 * Verifies the provided user credentials with the web system 
	 * @param username
	 * @param password
	 * @return Returns true if credentials are correct, false otherwise
	 */
	public boolean verifyLogin(String username, String password);
	
	/**
	 * Loads the eTicket blob stored on the phone
	 * @return Returns a BlobEnvelope blob object, containing all tickets and user information.
	 * @throws BlobException
	 */
	public BlobEnvelope getETicketBlob() throws BlobException;
	
	/**
	 * Saves a modified or new blob to the file system
	 * @param blob
	 */
	public void setETicketBlob(BlobEnvelope blob) throws BlobException;
}
