package org.igsl.app.golombruler;

/**
 * Implicit Graph Search Library(C), 2009, 2015
 */

public class MutableInteger {
	
	private int value;
	
	public MutableInteger(int value) {
		this.value = value;
	}
	
	public void assignValue(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public MutableInteger inc() {
		++value;
		return this;
	}
	
	public String toString() {
		return value + "";
	}

}
