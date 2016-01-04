package de.tum.score.transport4you.bus.data.datacontroller;


import de.tum.score.transport4you.bus.data.datacontroller.data.ApplicationConfiguration;
import de.tum.score.transport4you.bus.data.datacontroller.data.BluetoothConfiguration;
import de.tum.score.transport4you.bus.data.datacontroller.data.KeyConfiguration;
import de.tum.score.transport4you.bus.data.datacontroller.data.SystemConfiguration;
import de.tum.score.transport4you.bus.data.datacontroller.error.ConfigurationLoadingException;

public interface ISettingsDataController {
	
	/**
	 * Returns the Bluetooth Configuration stored in the configuration.
	 * @return
	 */
	public BluetoothConfiguration getBluetoothConfiguration();
	
	/**
	 * Returns the System Configuration stored in the configuration.
	 * @return
	 */
	public SystemConfiguration getSystemConfiguration();

	/**
	 * Returns the Key Configuration stored.
	 * @return
	 * @throws ConfigurationLoadingException 
	 */
	public KeyConfiguration getKeyConfiguration() throws ConfigurationLoadingException;
	
	/**
	 * Returns the ApplicationConfiguration stored
	 * @return
	 */
	public ApplicationConfiguration getApplicationConfiguration();

}
