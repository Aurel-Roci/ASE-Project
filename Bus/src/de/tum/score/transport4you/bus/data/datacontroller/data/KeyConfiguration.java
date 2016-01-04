package de.tum.score.transport4you.bus.data.datacontroller.data;

import java.security.PrivateKey;
import java.security.PublicKey;


/**
 * A class that represents all needed keys for encryption,etc.
 * @author hoerning
 *
 */
public class KeyConfiguration {
	
	/* The attributes */
	private PrivateKey keyAgreementPrivateKey;
	private PrivateKey blobPrivateKey;
	private PublicKey blobPublicKey;
	
	/* Getters / Setters */
	public PrivateKey getKeyAgreementPrivateKey() {
		return keyAgreementPrivateKey;
	}
	
	public void setKeyAgreementPrivateKey(PrivateKey keyAgreementPrivateKey) {
		this.keyAgreementPrivateKey = keyAgreementPrivateKey;
	}

	public PrivateKey getBlobPrivateKey() {
		return blobPrivateKey;
	}

	public void setBlobPrivateKey(PrivateKey blobPrivateKey) {
		this.blobPrivateKey = blobPrivateKey;
	}

	public PublicKey getBlobPublicKey() {
		return blobPublicKey;
	}

	public void setBlobPublicKey(PublicKey blobPublicKey) {
		this.blobPublicKey = blobPublicKey;
	}

}
