package de.tum.score.transport4you.shared.mobilebusweb.data.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;

import javax.swing.ImageIcon;

/**
 * Represents an entry of the Blob. 
  * @author hoerning
 *
 */
public class BlobEntry implements Serializable{
	
	private static final long serialVersionUID = -3847716138927816711L;
	
	/* User Information */
	//private ImageIcon userPicture;
	private String userId;
	private String userName;
	private String userAddress;
	private byte[] userPicture;
	
	/* Account Information */
	private Double accountBalance;
	private String accountType;

	/* Tickets */
	private ArrayList<ETicket> eTicketList = new ArrayList<ETicket>();
	
	/* Modification Date */
	private Date modificationDate;
	
	/**
	 * Creates a new BlobEntry Data Object
	 */
	public BlobEntry(){
	}
	
	/* Getters and Setters */
	public byte[] getUserPicture() {
		return userPicture;
	}

	public void setUserPicture(byte[] userPicture) {
		this.userPicture = userPicture;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Double getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(Double accountBalance) {
		this.accountBalance = accountBalance;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public ArrayList<ETicket> geteTicketList() {
		return eTicketList;
	}

	public void seteTicketList(ArrayList<ETicket> eTicketList) {
		this.eTicketList = eTicketList;
	}

	public Date getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}
	

}
