package de.tum.score.transport4you.shared.mobilebus.data.impl.message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import org.bouncycastle.util.encoders.Base64;

/**
 * In this Message any data may be transfered.
 * @author hoerning
 *
 */
public class DataMessage extends BluetoothData {
	
	private static final long serialVersionUID = 7047224747928275745L;

	private Object data;
	
	private boolean encrypted = false;

	public DataMessage(Object data){
		this.data = data;
	}

	public Object getData() {
		return data;
	}

	public boolean isEncrypted() {
		return encrypted;
	}
	
	public void encryptData(SecretKey key) throws InvalidKeyException {
		
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("AES","BC");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			
			ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
			ObjectOutput out = new ObjectOutputStream(byteOutputStream);   
			out.writeObject(data);
			byte[] dataBytes = byteOutputStream.toByteArray(); 
			
			data = cipher.doFinal(Base64.encode(dataBytes));

			encrypted = true;
		} catch (NoSuchAlgorithmException e) {
			throw new InvalidKeyException();
		} catch (NoSuchPaddingException e) {
			throw new InvalidKeyException();
		} catch (IOException e) {
			throw new InvalidKeyException();
		} catch (IllegalBlockSizeException e) {
			throw new InvalidKeyException();
		} catch (BadPaddingException e) {
			throw new InvalidKeyException();
		} catch (NoSuchProviderException e) {
			throw new InvalidKeyException();
		}
		
		
	}
	
	public void decryptData(SecretKey key) throws InvalidKeyException {
		if(! encrypted) return;
		
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("AES","BC");
			cipher.init(Cipher.DECRYPT_MODE, key);	
			data = Base64.decode(cipher.doFinal((byte[]) data));
			
			ByteArrayInputStream byteInputStream = new ByteArrayInputStream((byte[])data);
			ObjectInput in;
			
			in = new ObjectInputStream(byteInputStream);
			data = in.readObject();
			
			encrypted = false;
			
		} catch (Exception e) {
			throw new InvalidKeyException(e.getMessage());
		}
		
	}
}
