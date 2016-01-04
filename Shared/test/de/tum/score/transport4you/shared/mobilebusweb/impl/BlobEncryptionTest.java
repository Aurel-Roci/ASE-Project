package de.tum.score.transport4you.shared.mobilebusweb.impl;

import java.io.FileReader;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.X509Certificate;

import junit.framework.Assert;

import org.bouncycastle.openssl.PEMReader;
import org.junit.BeforeClass;
import org.junit.Test;

import de.tum.score.transport4you.shared.mobilebus.data.impl.keyexchange.KeyExchange;
import de.tum.score.transport4you.shared.mobilebus.data.impl.keyexchange.SignedPublicKey;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.BlobEntry;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.BlobEnvelope;


public class BlobEncryptionTest {
	@BeforeClass
	public static void before(){
		
		//initalize Security Provider
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}
	
	@Test
	public void testInit() throws Exception {
		BlobEntry blobEntry = new BlobEntry();
		BlobEnvelope blobEnvelope = new BlobEnvelope(blobEntry, null);
		
	}
	
	@Test(expected=java.security.InvalidKeyException.class)
	public void testSignatureFail() throws Exception {
		BlobEnvelope blobEnvelope = new BlobEnvelope(null,null);
		blobEnvelope.checkConsistency(null);
	}
	
	@Test
	public void testGetBlob() throws Exception {
		BlobEnvelope blobEnvelope = new BlobEnvelope(null,null);
		Object obj = blobEnvelope.getPublicBlobEntry();
		Assert.assertEquals(obj, null);
	}
	
	
	
	@Test
	public void testSignatures() throws Exception {
		
		//Load Public/Private RSAKey
		FileReader fileReader = new FileReader("test_res/BlobEncryptionKey-private.pem");
		PEMReader pemReader = new PEMReader(fileReader);
		KeyPair keyPair = (KeyPair) pemReader.readObject();
		PrivateKey privateRSAKey = keyPair.getPrivate();
		fileReader = new FileReader("test_res/BlobEncryptionKey-cert.pem");
		pemReader = new PEMReader(fileReader);
		X509Certificate certificate = (X509Certificate) pemReader.readObject();
		PublicKey publicRSAKey = certificate.getPublicKey();
		
		BlobEntry blobEntry = new BlobEntry();
		BlobEnvelope blobEnvelope = new BlobEnvelope(blobEntry, privateRSAKey);
		Assert.assertEquals(blobEnvelope.checkConsistency(publicRSAKey), true);
	}
	
}
