package de.tum.score.transport4you.shared.mobileweb.impl.message;

import java.io.Serializable;
import java.security.PublicKey;

public class MobileSettings implements Serializable{
	private static final long serialVersionUID = 7367131207440089093L;
	
	private String username;
	private String password;
	private boolean rememberLogin;
	private boolean allowAutoScan;
	private boolean allowAutoSynchronization;
	private boolean allowAutoSMSNotification;
	private PublicKey publicRSAkey;
	private String busSSID;
	private String busBTName;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isRememberLogin() {
		return rememberLogin;
	}
	public void setRememberLogin(boolean rememberLogin) {
		this.rememberLogin = rememberLogin;
	}
	public boolean isAllowAutoScan() {
		return allowAutoScan;
	}
	public void setAllowAutoScan(boolean allowAutoScan) {
		this.allowAutoScan = allowAutoScan;
	}
	public boolean isAllowAutoSynchronization() {
		return allowAutoSynchronization;
	}
	public void setAllowAutoSynchronization(boolean allowAutoSynchronization) {
		this.allowAutoSynchronization = allowAutoSynchronization;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public boolean isAllowAutoSMSNotification() {
		return allowAutoSMSNotification;
	}
	public void setAllowAutoSMSNotification(boolean allowAutoSMSNotification) {
		this.allowAutoSMSNotification = allowAutoSMSNotification;
	}
	public PublicKey getPublicRSAkey() {
		return publicRSAkey;
	}
	public void setPublicRSAkey(PublicKey publicRSAkey) {
		this.publicRSAkey = publicRSAkey;
	}
	public String getBusSSID() {
		return busSSID;
	}
	public void setBusSSID(String busSSID) {
		this.busSSID = busSSID;
	}
	public String getBusBTName() {
		return busBTName;
	}
	public void setBusBTName(String busBTName) {
		this.busBTName = busBTName;
	}
}
