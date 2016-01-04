package de.tum.score.transport4you.shared.mobilebus.data.impl.keyexchange;

import java.io.FileReader;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.X509Certificate;

import org.bouncycastle.openssl.PEMReader;
import org.junit.BeforeClass;
import org.junit.Test;

import de.tum.score.transport4you.shared.mobilebus.data.impl.keyexchange.KeyExchange;
import de.tum.score.transport4you.shared.mobilebus.data.impl.keyexchange.SignedPublicKey;



public class KeyExchangeTest {
	
	
	@BeforeClass
	public static void before(){
		
		//initalize Security Provider
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}
	
	@Test
	public void testInit() throws Exception {
		
		KeyExchange keyExchange;
		
		keyExchange = new KeyExchange();
		
	}
	
	@Test
	public void testSignatures() throws Exception {
		
		KeyExchange keyExchangeClient,keyExchangeServer;
		
		keyExchangeClient = new KeyExchange();
		keyExchangeServer = new KeyExchange(keyExchangeClient.getDHParameters());
		
		//Load Public/Private RSAKey
		FileReader fileReader = new FileReader("test_res/BusMobileDH-private.pem");
		PEMReader pemReader = new PEMReader(fileReader);
		KeyPair keyPair = (KeyPair) pemReader.readObject();
		PrivateKey privateRSAKey = keyPair.getPrivate();
		fileReader = new FileReader("test_res/BusMobileDH-cert.pem");
		pemReader = new PEMReader(fileReader);
		X509Certificate certificate = (X509Certificate) pemReader.readObject();
		PublicKey publicRSAKey = certificate.getPublicKey();
		
			
		SignedPublicKey signedPublicKey = keyExchangeClient.getOwnPublicKey(privateRSAKey);
		
		keyExchangeServer.getSessionKey(signedPublicKey, publicRSAKey);
	}

}
