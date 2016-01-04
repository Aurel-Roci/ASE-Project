package de.tum.score.transport4you.shared.mobilebus.data.impl.message;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.crypto.SecretKey;

import org.bouncycastle.openssl.PEMReader;
import org.junit.BeforeClass;
import org.junit.Test;

import de.tum.score.transport4you.shared.mobilebus.data.impl.keyexchange.KeyExchange;
import de.tum.score.transport4you.shared.mobilebus.data.impl.keyexchange.SignedPublicKey;
import de.tum.score.transport4you.shared.mobilebus.data.impl.message.DataMessage;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.BlobEntry;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.BlobEnvelope;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.ETicket;



public class DataMessageTest {
	
	
	@BeforeClass
	public static void before(){
		
		//initalize Security Provider
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}
	
	@Test
	public void testEncryption() throws Exception {
		
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
		
		SecretKey sessionKey = keyExchangeServer.getSessionKey(signedPublicKey, publicRSAKey);
		
		//First Test
		Object toEncrypt = new String("This is a test");
		Object toEncryptCheck = new String("This is a test");
		DataMessage message = new DataMessage(toEncrypt);
	
		message.encryptData(sessionKey);
		assertFalse(message.getData().equals(toEncryptCheck));
		
		message.decryptData(sessionKey);
		assertTrue(message.getData().equals(toEncryptCheck));
		
		//Second Test
		BlobEnvelope toEncryptBlobEnv = new BlobEnvelope(new BlobEntry(), null);
		
		toEncryptBlobEnv.getPublicBlobEntry().setAccountBalance(new Double("200.00"));
		toEncryptBlobEnv.getPublicBlobEntry().setAccountType("test");
		toEncryptBlobEnv.getPublicBlobEntry().seteTicketList(new ArrayList<ETicket>());
		
		toEncrypt = toEncryptBlobEnv;
		
		DataMessage message2= new DataMessage(toEncrypt);
		message.encryptData(sessionKey);
		message.decryptData(sessionKey);
		
	}

}
