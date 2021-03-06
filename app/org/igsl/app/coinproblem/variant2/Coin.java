package org.igsl.app.coinproblem.variant2;

/**
 * Implicit Graph Search Library(C), 2009, 2010, 2011 
 */

public class Coin {
	
	private int denominationIndex;
	private Coin coin;
	
	public Coin() {
		this.denominationIndex = -1;
		this.coin = null;
	}
	
	public Coin(int denominationIndex, Coin coin) {
		this.denominationIndex = denominationIndex;
		this.coin = coin;
	}
	
	public int getIndex() {
		return denominationIndex;
	}
	
	public Coin getParent() {
		return coin;
	}
	
	public String toString() {
		return "" + denominationIndex;
	}
	
}
