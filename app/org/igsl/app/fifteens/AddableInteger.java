package org.igsl.app.fifteens;

import org.igsl.cost.Addable;

public class AddableInteger implements Addable<AddableInteger>, Comparable<AddableInteger>{
	private int value;
	
	/**
	 * Assigning zero value
	 */
	public AddableInteger() {
		this.value = 0;
	}
	
	/**
	 * Assigning a defined value
	 */
	public AddableInteger(int value) {
		this.value = value;
	}
	
	public AddableInteger addTo(AddableInteger other) {
		return new AddableInteger(value + other.value);
	}
	
	public int compareTo(AddableInteger other) {
		if(value > other.value) {
			return 1;
		} else if (value < other.value) {
			return -1;
		} else {
			return 0;
		}
	}
	
	public String toString() {
		return String.valueOf(value);
	}
	
}
