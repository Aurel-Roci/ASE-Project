package de.tum.score.transport4you.shared.mobilebus.data.impl.message;


import de.tum.score.transport4you.shared.mobilebus.data.impl.keyexchange.DHParameter;
import de.tum.score.transport4you.shared.mobilebus.data.impl.keyexchange.SignedPublicKey;

/**
 * This Message represents the sending of the public key
 * @author hoerning
 *
 */
public class KeyExchangePublicKeyMessage extends BluetoothData {
	
	private static final long serialVersionUID = 3489502969448714018L;
	
	private DHParameter dhSpec;
	private SignedPublicKey signedPublicKey;
	
	/* Getters/Setters*/
	public SignedPublicKey getSignedPublicKey() {
		return signedPublicKey;
	}
	public void setSignedPublicKey(SignedPublicKey signedPublicKey) {
		this.signedPublicKey = signedPublicKey;
	}
	public DHParameter getDhSpec() {
		return dhSpec;
	}
	public void setDhSpec(DHParameter dhSpec) {
		this.dhSpec = dhSpec;
	}
	
}
