package de.tum.score.transport4you.shared.mobilebus.data;

import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.SecretKey;

import de.tum.score.transport4you.shared.mobilebus.data.error.KeyAgreementException;
import de.tum.score.transport4you.shared.mobilebus.data.impl.keyexchange.DHParameter;
import de.tum.score.transport4you.shared.mobilebus.data.impl.keyexchange.SignedPublicKey;

public interface IKeyExchange {

	/**
	 * Returns own Diffie-Hellman public key together with a RSA signature if needed.
	 * 
	 * @param privateRSAKey The RSA private key that shall be used to sign the D-H public key.
	 * @return Returns SignedPublicKey object including own public key for the key exchange together with a signature, if a RSA key was provided.
	 * @throws KeyAgreementException
	 */
	public SignedPublicKey getOwnPublicKey(PrivateKey privateRSAKey) throws KeyAgreementException;
	
	/**
	 * Generates a new session key for the provided foreign public key and check signatures if available and RSA public key available.
	 * 
	 * @param foreignPublicKey The public key of the party a session key should be established with.
	 * @param publicRSAKey Needed if signature should be checked.
	 * @return Returns a new session key as byte array.
	 * @throws KeyAgreementException
	 */
	public SecretKey getSessionKey(SignedPublicKey foreignSignedPublicKey, PublicKey publicRSAKey) throws KeyAgreementException;

	/**
	 * Returns the generated Diffie-Hellman specs
	 * @return Returns a DHParameterSpec object.
	 */
	public DHParameter getDHParameters();
}
