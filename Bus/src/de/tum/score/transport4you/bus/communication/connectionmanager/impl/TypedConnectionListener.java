package de.tum.score.transport4you.bus.communication.connectionmanager.impl;

import de.tum.score.transport4you.bus.communication.connectionmanager.CommunicationType;
import de.tum.score.transport4you.bus.communication.connectionmanager.IConnectionListener;

/**
 * The representation of a IConnectionListener with a specific Communication Type
 * @author hoerning
 *
 */
public class TypedConnectionListener {

	/* The communication type */
	private CommunicationType communicationType;
	
	/* The listener */
	private IConnectionListener connectionListener;

	@Override
	public boolean equals(Object o){
		
		//*1* Same class
		if(!(o instanceof TypedConnectionListener)) {
			return false;
		}
		TypedConnectionListener toCompare = (TypedConnectionListener) o;
		
		//*2* Same Type
		if(!(toCompare.getCommunicationType() == this.getCommunicationType())){
			return false;
		}
		
		//*3* Same Listener
		if(toCompare.getConnectionListener()!= this.getConnectionListener()){
			return false;
		}
		
		return true;
	}
	
	/* Getters/Setters*/
	public CommunicationType getCommunicationType() {
		return communicationType;
	}

	public void setCommunicationType(CommunicationType communicationType) {
		this.communicationType = communicationType;
	}

	public IConnectionListener getConnectionListener() {
		return connectionListener;
	}

	public void setConnectionListener(IConnectionListener connectionListener) {
		this.connectionListener = connectionListener;
	}
	
	
	
}
