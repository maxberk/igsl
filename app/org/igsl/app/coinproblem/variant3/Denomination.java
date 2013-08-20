package org.igsl.app.coinproblem.variant3;

/**
 * Implicit Graph Search Library(C), 2009, 2013
 */

public class Denomination {
	
	private int index;
	private int amount;
	
	public Denomination(int index) {
		this.index = index;
		this.amount = 0;
	}
	
	public int getIndex() {
		return index;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public Denomination decAmount() {
		--amount;
		return this;
	}
	
	public String toString() {
		return index + "(" + amount + ")";
	}

}
