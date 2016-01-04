package de.tum.score.transport4you.shared.mobilebusweb.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.Security;
import java.util.ArrayList;
import java.util.Date;

import javax.imageio.ImageIO;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;

import de.tum.score.transport4you.shared.mobilebusweb.data.impl.BlobEntry;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.BlobEnvelope;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.ETicket;

public class BlobGenerator {

	/**
	 * @param args
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws InvalidKeyException
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException, InvalidKeyException {
		//Add Bouncy Castle
		Security.addProvider(new BouncyCastleProvider());
		
		//Read Certificate file for encryption
		PEMReader pemReader = null;
		pemReader = new PEMReader(new FileReader("test_res/BlobEncryptionKey-private.pem"));
		
		PrivateKey blobEncryptionPrivateKey = ((KeyPair) pemReader.readObject()).getPrivate();

		//create new blob entry
		BlobEntry blobEntry = new BlobEntry();
		
		blobEntry.setAccountType("prePayAccount");
		blobEntry.setUserId(Integer.toString(1));
		blobEntry.setUserAddress("Building 32, Room 414 67653 Kaiserslautern");

		//Add image to blob
		BufferedImage bufferedImage = null;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		bufferedImage = ImageIO.read(new FileInputStream("test_res/kai.jpg"));
		ImageIO.write(bufferedImage, "JPG", byteArrayOutputStream);
		blobEntry.setUserPicture(byteArrayOutputStream.toByteArray());
		//output to see quality
		File f = new File ("test_res/kai_sml.jpg");
		ImageIO.write(bufferedImage, "JPG", f);

		blobEntry.setAccountBalance(new Double("20.00"));
		blobEntry.seteTicketList(new ArrayList<ETicket>());
		blobEntry.setModificationDate(new Date());

		//create envelope and add blob entry
		BlobEnvelope blobEnvelope = null;
		blobEnvelope = new BlobEnvelope(blobEntry, blobEncryptionPrivateKey);

		//Write blob envelope to file
		ObjectOutput out = new ObjectOutputStream(new FileOutputStream("test_res/test.blob"));
		out.writeObject(blobEnvelope);
		out.close();

	}

}
