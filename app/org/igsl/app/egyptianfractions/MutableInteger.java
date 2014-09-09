package org.igsl.app.egyptianfractions;

/**
 * Implicit Graph Search Library(C), 2009, 2014
 */

public class MutableInteger {
	
	private long value;
	
	public MutableInteger(long value) {
		this.value = value;
	}
	
	public void assignValue(long value) {
		this.value = value;
	}
	
	public long getValue() {
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
