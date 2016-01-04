package de.tum.score.transport4you.shared.mobilebus.data.impl.keyexchange;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * The parameters for the Diffie Hellman key Exchange
 * 
 *
 */
public class DHParameter implements Serializable{

	private static final long serialVersionUID = 2704612153791912071L;

	private BigInteger p,g;
	private int l;
	
	/* Getters/Setters*/
	public BigInteger getP() {
		return p;
	}
	public void setP(BigInteger p) {
		this.p = p;
	}
	public BigInteger getG() {
		return g;
	}
	public void setG(BigInteger g) {
		this.g = g;
	}
	public int getL() {
		return l;
	}
	public void setL(int l) {
		this.l = l;
	}
	

}
