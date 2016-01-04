package de.tum.score.transport4you.bus.data.datacontroller.data;

/**
 * Data class, which represents the parameters to configure Bluetooth
 * @author hoerning
 *
 */
public class BluetoothConfiguration {
	
	/* The data attributes */
	private String serviceName;
	
	private String serverName;
	
	private String serviceUUID;
	
	/* Getters and Setters */
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getServiceUUID() {
		return serviceUUID;
	}
	public void setServiceUUID(String serviceUUID) {
		this.serviceUUID = serviceUUID;
	}

}
