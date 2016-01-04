package de.tum.score.transport4you.mobile.data.datacontroller;

import de.tum.score.transport4you.mobile.data.datacontroller.error.DataControllerException;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.BlobEnvelope;

public interface ISynchronizationDataController {

	/**
	 * Loads the blob stored on the mobile device.
	 * 
	 * @return Returns a BlobEnvelope object, storing account balance, picture and tickets.
	 * @throws DataControllerException
	 */
	public BlobEnvelope loadBlob() throws DataControllerException;
	
	/**
	 * Stores the blob on the mobile device.
	 * 
	 * @param blob BlobEnvelope that shall be stored.
	 * @return Returns true if storing was successful, false otherwise.
	 * @throws DataControllerException
	 */
	public void storeBlob(BlobEnvelope blob) throws DataControllerException;
	
	/**
	 * Tests if there are any blobs stored on the device 
	 * 
	 * @return
	 */
	public boolean existBlob();
}
