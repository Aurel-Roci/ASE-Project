package de.tum.score.transport4you.mobile.application.applicationcontroller.impl;

import de.tum.score.transport4you.mobile.application.applicationcontroller.IMainApplication;

public class ApplicationSingleton {
	private static IMainApplication mainApplication;
	
	public static IMainApplication getApplicationController() {
		if (mainApplication == null) {
			mainApplication = new ApplicationController();
			//new Thread(mainApplication);
		}
		
		return mainApplication;
	}
}
