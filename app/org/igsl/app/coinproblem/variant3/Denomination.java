package org.igsl.app.coinproblem.variant3;

/**
 * Implicit Graph Search Library(C), 2009, 2010, 2011, 2012
 */

public class Denomination {
	
	private int index;
	private int amount;
	
	public Denomination() {
		this.index = -1;
		this.amount = 0;
	}
	
	public Denomination(int index, int amount) {
		this.index = index;
		this.amount = amount;
	}
	
	public int getIndex() {
		return index;
	}
	
	public int getAmount() {
		return amount;
	}	

}
