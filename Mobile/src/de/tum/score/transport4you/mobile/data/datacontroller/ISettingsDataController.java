package de.tum.score.transport4you.mobile.data.datacontroller;

import java.security.PublicKey;

import de.tum.score.transport4you.mobile.data.datacontroller.error.DataControllerException;
import de.tum.score.transport4you.shared.mobileweb.impl.message.MobileSettings;

public interface ISettingsDataController {
	
	/**
	 * Loads the mobile settings stored on the mobile device.
	 * 
	 * @return Returns a MobileSettings object.
	 * @throws DataControllerException
	 */
	public MobileSettings loadMobileSettings() throws DataControllerException;
	
	/**
	 * Stores the mobile settings on the mobile device.
	 * 
	 * @param mobileSettings Mobile Settings that shall be stored.
	 * @return Returns true if storing was successful, false otherwise.
	 * @throws DataControllerException
	 */
	public void storeMobileSettings(MobileSettings mobileSettings) throws DataControllerException;
	
	/**
	 * Tests if there are any mobile settings stored on the device 
	 * 
	 * @return
	 */
	public boolean existMobileSettings();

	/**
	 * Reads out the public RSa key of the bus company, stored on the mobile phone.
	 * @return Returns the loaded RSA key
	 */
	public PublicKey getPublicRSAKey();
}
