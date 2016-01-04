/**
 * Code partly taken from http://www.exampledepot.com/egs/javax.crypto/KeyAgree.html
 */
package de.tum.score.transport4you.shared.mobilebus.data.impl.keyexchange;

import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import de.tum.score.transport4you.shared.mobilebus.data.IKeyExchange;
import de.tum.score.transport4you.shared.mobilebus.data.error.KeyAgreementException;

public class KeyExchange implements IKeyExchange {

	private PrivateKey privateKey;
	private PublicKey ownPublicKey;
	private DHParameter dhParameter;

	public KeyExchange(DHParameter dhParameter) throws KeyAgreementException {
		try {
		    // Use the values to generate a key pair
		    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DH");
		    DHParameterSpec dhSpec = new DHParameterSpec(dhParameter.getP(), dhParameter.getG(), dhParameter.getL());
		    keyGen.initialize(dhSpec);
		    KeyPair keypair = keyGen.generateKeyPair();

		    // Get the generated public and private keys
		    privateKey = keypair.getPrivate();
		    ownPublicKey = keypair.getPublic();

		} catch (java.security.InvalidAlgorithmParameterException e) {
			throw new KeyAgreementException("DH parameters wrong!");			
		} catch (java.security.NoSuchAlgorithmException e) {
			throw new KeyAgreementException("No DH algorithm found!");
		}

	}
	
	public KeyExchange() throws Exception {
		// Create the parameter generator for a 1024-bit DH key pair
        AlgorithmParameterGenerator paramGen = AlgorithmParameterGenerator.getInstance("DH");
        paramGen.init(512);

        // Generate the parameters
        AlgorithmParameters params = paramGen.generateParameters();
        DHParameterSpec dhSpec = (DHParameterSpec)params.getParameterSpec(DHParameterSpec.class);
        this.dhParameter = new DHParameter();
        this.dhParameter.setG(dhSpec.getG());
        this.dhParameter.setP(dhSpec.getP());
        this.dhParameter.setL(dhSpec.getL());
        
	    // Use the values to generate a key pair
	    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DH");
	    keyGen.initialize(dhSpec);
	    KeyPair keypair = keyGen.generateKeyPair();

	    // Get the generated public and private keys
	    privateKey = keypair.getPrivate();
	    ownPublicKey = keypair.getPublic();
	}

	/* (non-Javadoc)
	 * @see de.tukl.score.transport4you.shared.data.IKeyExchange#getOwnPublicKey(java.security.PrivateKey)
	 */
	@Override
	public SignedPublicKey getOwnPublicKey(PrivateKey privateRSAKey) throws KeyAgreementException {
		SignedPublicKey signedPublicKey = new SignedPublicKey();
		byte[] signature = null;
		
		signedPublicKey.setPublicKey(ownPublicKey);
		
		//fixme: removed signing for the time being -> has some RSA issues
		/*if(privateRSAKey != null) {
			try {
				Signature signer = Signature.getInstance("SHA1withRSA");
				signer.initSign(privateRSAKey);
				signer.update(signedPublicKey.getPublicKeyByte());
				signature = signer.sign();
				
			} catch (NoSuchAlgorithmException e) {
				throw new KeyAgreementException("No RSA algorithm found!");
			} catch (InvalidKeyException e) {
				throw new KeyAgreementException("Invalid RSA key!");
			} catch (SignatureException e) {
				throw new KeyAgreementException("Failed to sign key!");
			}
		}*/
		
		signedPublicKey.setSignature(signature);
		
		return signedPublicKey;
	}
	
		
	/* (non-Javadoc)
	 * @see de.tukl.score.transport4you.shared.data.IKeyExchange#getSessionKey(java.security.PublicKey)
	 */
	@Override
	public SecretKey getSessionKey(SignedPublicKey foreignSignedPublicKey, PublicKey publicRSAKey) throws KeyAgreementException {
		
		if(foreignSignedPublicKey.getSignature() != null) {
			
			try {
				Signature signer = Signature.getInstance("SHA1withRSA");
				signer.initVerify(publicRSAKey);
				signer.update(foreignSignedPublicKey.getPublicKeyByte());
				
				if (!signer.verify(foreignSignedPublicKey.getSignature())) {
					throw new KeyAgreementException("Signature check failed!");
				}
				
			} catch (NoSuchAlgorithmException e) {
				throw new KeyAgreementException("No RSA algorithm found!");
			} catch (InvalidKeyException e) {
				throw new KeyAgreementException("Invalid RSA key!");
			} catch (SignatureException e) {
				throw new KeyAgreementException("Failed to sign key!");
			}
		}
		
	    try {
	    	// Prepare to generate the secret key with the private key and public key of the other party
		    KeyAgreement ka;
			ka = KeyAgreement.getInstance("DH");
			ka.init(privateKey);
		    
			PublicKey fpk = foreignSignedPublicKey.getPublicKey();
			
			ka.doPhase(fpk, true);

		    // Specify the type of key to generate;
		    String algorithm = "AES";
		    
		    // Generate the secret key by deriving it via a sha256 hash on the dh key as seed
		    byte[] dhSessionKey = ka.generateSecret(algorithm).getEncoded(); 
		    
		    byte[] smallerKey = new byte[16];
		    
		    for(int i=0; i<= 15; i++) {
		    	smallerKey[i] = dhSessionKey[i];
		    }
		    
		    SecretKeySpec sessionKey = new SecretKeySpec(smallerKey,"AES-256");
		    
		    /* Alternative key generation technique that requires more computation
		    PBEKeySpec keySpec = new PBEKeySpec(new String(messageDigest.digest(),"UTF-8").toCharArray());
		    SecretKeyFactory kf = SecretKeyFactory.getInstance("AES");
		    SecretKey sessionKey = kf.generateSecret(keySpec);
		    */

		       
		    return sessionKey;
		    
		} catch (NoSuchAlgorithmException e) {
			throw new KeyAgreementException("No RSA algorithm found!");
		} catch (InvalidKeyException e) {
			throw new KeyAgreementException("Invalid key genreated!");
		}	
	}

	/* (non-Javadoc)
	 * @see de.tukl.score.transport4you.shared.data.IKeyExchange#getDHParameters()
	 */
	public DHParameter getDHParameters() {
		return this.dhParameter;
	}
	
}
