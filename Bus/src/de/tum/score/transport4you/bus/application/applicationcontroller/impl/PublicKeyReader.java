package de.tum.score.transport4you.bus.application.applicationcontroller.impl;
import java.io.*;
import java.security.*;
import java.security.spec.*;

public class PublicKeyReader {
	 public static PublicKey get(String filename)
			    throws Exception {

			    File f = new File(filename);
			    FileInputStream fis = new FileInputStream(f);
			    DataInputStream dis = new DataInputStream(fis);
			    byte[] keyBytes = new byte[(int)f.length()];
			    dis.readFully(keyBytes);
			    dis.close();

			    X509EncodedKeySpec spec =
			      new X509EncodedKeySpec(keyBytes);
			    KeyFactory kf = KeyFactory.getInstance("RSA");
			    return kf.generatePublic(spec);
			  }
}
