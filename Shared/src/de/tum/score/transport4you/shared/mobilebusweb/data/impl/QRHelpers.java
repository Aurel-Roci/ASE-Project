package de.tum.score.transport4you.shared.mobilebusweb.data.impl;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.Security;

import javax.xml.bind.DatatypeConverter;

import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.openssl.PEMDecryptorProvider;
import org.spongycastle.openssl.PEMEncryptedKeyPair;
import org.spongycastle.openssl.PEMKeyPair;
import org.spongycastle.openssl.PEMParser;
import org.spongycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.spongycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;
//import org.spongycastle.util.encoders.Base64;
import org.spongycastle.util.io.pem.PemReader;
//import org.bouncycastle.openssl.PEMReader;

import com.google.gson.Gson;

public class QRHelpers {
	
	public static byte[] createQRCodeForETicketNotEncrypted (ETicket eticket) throws IOException {
		ETicketDummy dummy = new ETicketDummy(eticket);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream( baos );
		oos.writeObject(dummy);
		oos.close();
		return baos.toByteArray();
	}
	public static String createQRCodeForETicket(ETicket eticket) throws IOException {
		// Should be stored seperately 
				String pem = "-----BEGIN RSA PRIVATE KEY-----\n"
					+"MIIEpQIBAAKCAQEAwXUffSp3HjYVGw4d+Ggba82nh2VBHT5wlRmwbQKXzvDCrEaL\n"
					+"5cJtcdELlhnTUQbqlQF+eIQl3os6RGHHI75SMHz+QBjxMxOs2C2EcbycqXaqRrD5\n"
					+"h//7MpzbgWV7hCap7kph++LBxNyhNsm2ZMKjpY71stkRNHrZzsywRgFuwQFdEqiv\n"
					+"ZiWSvu4OIAhIQP8VhOq0wC5BckS0q421WHcRXIbBACH0VNcNP3b2Qs6urfMIpqjg\n"
					+"UYjT1kxXwXc36G6JRKCOXaQIblEc7EKCHVE+/2DaAKGylY8swqeIVXm+1x4hWnGU\n"
					+"71NFfAFicRjFCgJNnLgdgtMEdmckZeSzN4DVFwIDAQABAoIBAQC0qsDhj4r29+L2\n"
					+"BVUP64nQP5s44uLQgMN7OyZ8Z3OGm2nyoV99DvpO1L9RSoUCosboqSCHREJpdvTP\n"
					+"b9EeHFt9VP3Mtn5rCPDeYoPSBCb6TAvxQM2IqRLazYphaXjUjZcdJvIi3j2/r5wP\n"
					+"Ionnx640qzHh+L4MmL5YVug4OJZKP2ySWZlyUHW7NPlKOHkRwostj35NL8KzseJg\n"
					+"Xlock0khKhcfnfhC28ANpYF5wa8bgi+twjMU/meu6Kn7prGrS6Sj6/LjKnvp6Bx/\n"
					+"KYHqCgkaxhukxL+7/Eyhq/zcFyHpTu0s0pmORhfHx5FHCy5R0ZyyYxXnhtwJDFdT\n"
					+"6n4xf/BBAoGBAP1ta1hR5uUzozEflKvWWEWIu9bmJhmryEtSC2Le7dsvmiFXRIqD\n"
					+"aZjvMNjKmWdtahEDYoxLFKLDSVunIx+CIfs74zLPYaNEyej2lG7hwUWyXg2Lkdeo\n"
					+"6vSGRLbxYq2BOobB83KlTm3tK4SGHXg511Avfg69kE4XRfLhEOGtVKNvAoGBAMNr\n"
					+"3DOCbUGyqm2k4DbmlEmRftdOmrgD1frzFEoHtaiYsQbh4RyEiDp4CWtBN5EdOjOV\n"
					+"v46ahDNLQnVM5z0D6mlUCwtZ751GsKb0VUeRpKxvfyZKVFl5XX7VCVSPqFE6pcoY\n"
					+"iRUvZsa8S8x9dYOJLcpMBEzMRIVq0rC5Gp1wjHTZAoGBAJU9lJOEV9buG9JX6LNx\n"
					+"HLaGGSgqjJFdiixg+neVFLmZRMkRnTl8vfjkEv34AXLZCjdOqQA5TsOzAUZKHPL3\n"
					+"LY/H6roHSlZdshHQ9ASASdMDgUO0x4Qa45JwZ5Lcf+HxUkf9e6IuGwu9OX1nhX9B\n"
					+"gLyl2zRPCeYS6oxnYgukiU1dAoGACbJsdtHeAgiPlGk+BvtiGFRz6tMnskHeeFlf\n"
					+"hFzlkrwg7KqAtR2OdPhH316ZF0ZQAQdJPhZEwRbW8WMjhk+PbjKRabrIvREo6t/s\n"
					+"62Q6u6O8t3Wwwc/X59dCY0PNolo6p9CX3MlBXFMzn64KCsDf2M302Kq6K7SlR8en\n"
					+"nnBbR+ECgYEA71rF0yKxDLi5sElROAJsepqrG1tGRLfWWTncac4XJKoMgnLQDkjF\n"
					+"Ehc9+pPi5r3j/xVCQHRbid+wgwTidPTLklP/wdKV4/SUvCUuvPUMVE7+kus7EXDv\n"
					+"r5Rmu651XXnwwzHdS3coeknhbmzfZbhC8AnUBSphRrA/GMYgWLvTbCM=\n"
					+"-----END RSA PRIVATE KEY-----\n";
				Security.addProvider(new BouncyCastleProvider());
				
				//Use PEMParser of spongycastle instead of PEMReader of bouncycastle library
				PEMParser pemParser = null;
				pemParser = new PEMParser(new PemReader(new StringReader(pem)));
				
				Object object = pemParser.readObject();
				JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
				KeyPair kp;
				// Unencrypted key - no password needed
				PEMKeyPair ukp = (PEMKeyPair) object;
				kp = converter.getKeyPair(ukp);
				PrivateKey qrEncryptionPrivateKey = kp.getPrivate();
				// RSA
//				KeyFactory keyFac = KeyFactory.getInstance("RSA");
//				RSAPrivateCrtKeySpec privateKey = keyFac.getKeySpec(kp.getPrivate(), RSAPrivateCrtKeySpec.class);

				QRCodedObject qr = null;
				String serializedStr = "";
				try {
					//sign the ETicket
					qr = new QRCodedObject(eticket, qrEncryptionPrivateKey);
					//Serialize the signed object
					serializedStr = qr.serialize();
				} catch (Exception e) {
					System.err.println("Error creating QR-coded object");
					e.printStackTrace();
				}
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream( baos );
				oos.writeObject(qr);
				oos.close();
				byte[] bArr = baos.toByteArray();
			
				//return DatatypeConverter.printBase64Binary(baos.toByteArray()); 
     			//return java.util.Base64.getEncoder().encodeToString(baos.toByteArray());
				
				return serializedStr;
	}
}
