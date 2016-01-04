package de.tum.score.transport4you.mobile.presentation.presentationmanager;


public interface IPresentation {
	
	/**
	 * Tells the Activity to release all its ressources and shut down.
	 */
	public void shutdown();

	/**
	 * Updates a progressdialog if available, containg the provided title and message. 
	 * Also specifies if the progressdialog should be visible or not and the increment to the status indicator.
	 * @param title
	 * @param message
	 * @param enabled
	 * @param increment
	 */
	public void updateProgessDialog(String title, String message, boolean visible, Integer increment);
}
