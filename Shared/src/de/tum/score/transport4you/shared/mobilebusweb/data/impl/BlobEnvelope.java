package de.tum.score.transport4you.shared.mobilebusweb.data.impl;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.SignedObject;

/**
 * Represents the envelope of the Blob synchronized between Mobile and Web
 * System and sended
 * 
 * @author hoerning
 * 
 */
public class BlobEnvelope implements Serializable {

	private static final long serialVersionUID = -2589647478403768025L;

	/* The entries of the Blob */
	private SignedObject signedBlobEntry;

	/**
	 * Creates a new Envelope with the public values of the Blob and encrypts the private part if a public RSA key is provided
	 * 
	 * @param publicBlobEntry
	 * @param encryptedBlobEntry
	 * @throws InvalidKeyException
	 */
	public BlobEnvelope(BlobEntry publicBlobEntry, PrivateKey privateKey) throws InvalidKeyException {
		if (privateKey == null) {
			//Testing workaround
			//TODO: remove in stable
			 KeyPairGenerator keyGen = null;
			try {
				keyGen = KeyPairGenerator.getInstance("RSA");
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 keyGen.initialize(512);
			 KeyPair keypair = keyGen.genKeyPair();
			 privateKey = keypair.getPrivate();
		}
		try {
			this.signedBlobEntry = new SignedObject(publicBlobEntry, privateKey, Signature.getInstance("SHA1withRSA","BC"));
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * Get the public values of the Blob
	 * 
	 * @return
	 */
	public BlobEntry getPublicBlobEntry() {
		try {
			return (BlobEntry) signedBlobEntry.getObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Checks that encrypted and public part are consistent.
	 * 
	 * @param privateKey
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 * @throws InvalidKeyException 
	 */
	public boolean checkConsistency(PublicKey publicKey) throws InvalidKeyException, IOException, ClassNotFoundException {
		
		try {
			return signedBlobEntry.verify(publicKey, Signature.getInstance("SHA1withRSA","BC"));
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
