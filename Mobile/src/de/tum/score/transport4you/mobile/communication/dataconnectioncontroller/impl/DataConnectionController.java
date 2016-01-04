package de.tum.score.transport4you.mobile.communication.dataconnectioncontroller.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.bouncycastle.util.encoders.Base64;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.w3c.dom.Document;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import de.tum.score.transport4you.mobile.application.applicationcontroller.IMainApplication;
import de.tum.score.transport4you.mobile.communication.dataconnectioncontroller.IData;
import de.tum.score.transport4you.mobile.communication.dataconnectioncontroller.error.RESTException;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.BlobEnvelope;
import de.tum.score.transport4you.shared.mobileweb.impl.message.MobileSettings;

@TargetApi(Build.VERSION_CODES.FROYO)
public class DataConnectionController implements IData {

	private static final String baseURL = "http://score-1042.appspot.com/rest/";
	private IMainApplication mainApplication;
	private Context context;
	
	public DataConnectionController(Context context, IMainApplication mainApp) {
		this.mainApplication = mainApp;
		this.context = context;
	}
	
	@Override
	public boolean checkAuthentication(Context context, String username, String password) throws RESTException {

	    try {
	    	String md5 = computeMD5(password);
	    	
	    	//if debug mode is set, fix credentials for testing
			if(mainApplication.isDebugModeEnabled()) {
				username = "hans@example.com";
				md5 = "5ebe2294ecd0e0f08eab7690d2a6ee69";
			}
			
	    	String output = new ClientResource(baseURL + "user/" + username + "/" + md5).get().getText();  
	    	
	    	if(output.toLowerCase().contains("wrong password"))	{
	    		return false;	    		
	    	} 
	    	else
	    	{
	    		return true;
	    	}	        
	    } catch (ResourceException e) {
	    	throw new RESTException("REST request failed (checkAuthentication)");
	    } catch (IOException e) {
	    	throw new RESTException("REST request failed (checkAuthentication)");
		}
	}
	
	@Override
	public BlobEnvelope synchronizeETickets(String username, String password, BlobEnvelope blob) throws RESTException {
	    try {
			ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
			ObjectOutput out = new ObjectOutputStream(byteOutputStream);   
			out.writeObject(blob);
			byte[] dataBytes = byteOutputStream.toByteArray(); 
	    	
	    	byte[] outputObject = Base64.encode(dataBytes);
	    	
    		String md5 = computeMD5(password);
	    	
    		//if debug mode is set, fix credentials for testing
			if(mainApplication.isDebugModeEnabled()) {
				username = "hans@example.com";
				md5 = "5ebe2294ecd0e0f08eab7690d2a6ee69";
			}
	    	
	    	String xml = new ClientResource(baseURL + "user/" + username + "/" + md5).get().getText();  
	        
	        InputStream is = new ByteArrayInputStream(xml.getBytes("UTF-8"));

	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        Document document = builder.parse(is);

	        XPath xpath = XPathFactory.newInstance().newXPath();

	        String ticketString = (String) xpath.evaluate("/user/ticket", document, XPathConstants.STRING);
	        	        
			ByteArrayInputStream byteInputStream = new ByteArrayInputStream(Base64.decode(ticketString.getBytes()));
			ObjectInput in = new ObjectInputStream(byteInputStream);
			BlobEnvelope result = (BlobEnvelope) in.readObject();
	        
	        return result;
	    } catch (Exception e) {
	    	throw new RESTException("REST request failed (synchronizeETickets)");
	    }
	}

	@Override
	public MobileSettings synchronizeSettings(String username, String password,	MobileSettings mobileSettings) throws RESTException {
		//for the time being do not sync settings with server and just return local settings
		return mobileSettings;		
	}
	
	private static final String computeMD5(final String s) {
	    final String MD5 = "MD5";
	    try {
	        // Create MD5 Hash
	        MessageDigest digest = java.security.MessageDigest
	                .getInstance(MD5);
	        digest.update(s.getBytes());
	        byte messageDigest[] = digest.digest();

	        // Create Hex String
	        StringBuilder hexString = new StringBuilder();
	        for (byte aMessageDigest : messageDigest) {
	            String h = Integer.toHexString(0xFF & aMessageDigest);
	            while (h.length() < 2)
	                h = "0" + h;
	            hexString.append(h);
	        }
	        return hexString.toString();

	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    return "";
	}
}
