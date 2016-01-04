package de.tum.score.transport4you.bus.data.datacontroller.data;


/**
 * A class that represents all needed keys the application
 * @author hoerning
 *
 */
public class ApplicationConfiguration {
	
	/* The attributes */
	private String prepayAccountRepresentation;
	private String postpayAccountRepresentation;
	
	/* Getters/Setters */
	public String getPrepayAccountRepresentation() {
		return prepayAccountRepresentation;
	}
	public void setPrepayAccountRepresentation(String prepayAccountRepresentation) {
		this.prepayAccountRepresentation = prepayAccountRepresentation;
	}
	public String getPostpayAccountRepresentation() {
		return postpayAccountRepresentation;
	}
	public void setPostpayAccountRepresentation(String postpayAccountRepresentation) {
		this.postpayAccountRepresentation = postpayAccountRepresentation;
	}

}
