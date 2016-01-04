package de.tum.score.transport4you.shared.mobilebus.data.impl.message;

import java.io.Serializable;

import de.tum.score.transport4you.shared.mobilebusweb.data.impl.BlobEnvelope;

/**
 * Represents the message that is sent by the Bus System to the Mobile System to notify the Mobile System about the success of an valid ETicket.
 * A message with an empty blob means that blob was not changed (e.g. season tickets) and nothing has to be saved on the mobile phone.
 * @author root
 *
 */
public class ValidETicketMessage implements Serializable{

	private static final long serialVersionUID = 5337559830685666594L;
	
	private BlobEnvelope blob;

	public BlobEnvelope getBlob() {
		return blob;
	}

	public void setBlob(BlobEnvelope blob) {
		this.blob = blob;
	}
}
