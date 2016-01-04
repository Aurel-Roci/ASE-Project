package de.tum.score.transport4you.bus.data.datacontroller.data;

/**
 * Data class, which represents the parameters to configure the System
 * @author hoerning
 *
 */
public class SystemConfiguration {
	
	/* The data attributes */
	private int threadPoolSize;
	
	private int daemonThreadTimer;

	/* Getters and Setters */
	public int getThreadPoolSize() {
		return threadPoolSize;
	}

	public void setThreadPoolSize(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}

	public void setDaemonThreadTimer(int daemonThreadTimer) {
		this.daemonThreadTimer = daemonThreadTimer;
	}

	public int getDaemonThreadTimer() {
		return daemonThreadTimer;
	}
	

}
