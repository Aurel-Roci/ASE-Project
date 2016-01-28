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

import com.google.gson.Gson;

public class QRCodedObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5100384283853681871L;
	private SignedObject signedETicket;
    public QRCodedObject(ETicket eticket, PrivateKey privateKey) throws InvalidKeyException {
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
			this.signedETicket = new SignedObject(eticket, privateKey, Signature.getInstance("SHA1withRSA","BC"));
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
    
	public ETicket getPublicETicket() {
		try {
			return (ETicket) signedETicket.getObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean checkConsistency(PublicKey publicKey) throws InvalidKeyException, IOException, ClassNotFoundException {
		
		try {
			return signedETicket.verify(publicKey, Signature.getInstance("SHA1withRSA","BC"));
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
	
	public String serialize(){
	    return new Gson().toJson(this);
	}
	
	public static QRCodedObject deserialize(String jsonString){
	    return new Gson().fromJson(jsonString, QRCodedObject.class);
	}

}
