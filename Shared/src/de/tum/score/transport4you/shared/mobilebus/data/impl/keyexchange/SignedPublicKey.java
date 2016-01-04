package de.tum.score.transport4you.shared.mobilebus.data.impl.keyexchange;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.PublicKey;

/**
 * Used to store a public D-H key together with a RSA signature
 */
public class SignedPublicKey implements Serializable{
		
	private static final long serialVersionUID = -44639299013117102L;
	
	private byte[] publicKey;
	private byte[] signature;
	
	public void setPublicKey(PublicKey publicKey){
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(bos);
			oos.writeObject(publicKey);
		    oos.flush();
		    oos.close();
		    bos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	
	    this.publicKey = bos.toByteArray();
	}
	
	public PublicKey getPublicKey(){
		
		PublicKey pKey = null;
		ByteArrayInputStream bis = new ByteArrayInputStream(publicKey);
	    ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(bis);
			Object o = ois.readObject();
			
			bis.close();
			ois.close();
			
			pKey = (PublicKey) o;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return pKey;
		
	}
	
	public byte[] getPublicKeyByte(){
		
		return this.publicKey;
	}

	public byte[] getSignature() {
		return signature;
	}

	public void setSignature(byte[] signature) {
		this.signature = signature;
	}
	
	
	

}
