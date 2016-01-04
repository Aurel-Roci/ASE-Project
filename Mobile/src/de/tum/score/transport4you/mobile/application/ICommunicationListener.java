package de.tum.score.transport4you.mobile.application;

import de.tum.score.transport4you.shared.mobilebusweb.data.impl.BlobEnvelope;

public interface ICommunicationListener {

	/**
	 * Callback function to receive blob and handle further processing.
	 * @param blob The received blob, containing eticket etc.
	 */
	public void receiveBlob(BlobEnvelope blob);
}
