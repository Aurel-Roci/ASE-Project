package de.tum.score.transport4you.mobile.communication.connectionmanager.impl;

import de.tum.score.transport4you.mobile.application.applicationcontroller.IMainApplication;

public interface IConnectionBroadcast {

	public abstract String getBUS_SSID();

	public abstract String getBUS_BT_NAME();

	public abstract IMainApplication getMainApplication();

}