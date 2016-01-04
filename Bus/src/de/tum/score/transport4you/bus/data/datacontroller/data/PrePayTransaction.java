package de.tum.score.transport4you.bus.data.datacontroller.data;


import javax.persistence.Entity;

/**
 * Represents an Transaction on PrePay Basis
 * @author hoerning
 *
 */
@Entity
public class PrePayTransaction extends PaymentTransaction{
		
	private double oldAccountBalance;
	
	private double newAccountBalance;

	public double getOldAccountBalance() {
		return oldAccountBalance;
	}

	public void setOldAccountBalance(double oldAccountBalance) {
		this.oldAccountBalance = oldAccountBalance;
	}

	public double getNewAccountBalance() {
		return newAccountBalance;
	}

	public void setNewAccountBalance(double newAccountBalance) {
		this.newAccountBalance = newAccountBalance;
	}
	
	
	
}
