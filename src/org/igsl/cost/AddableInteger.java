/**
 * Implicit Graph Search Library(C), 2009 
 */
package org.igsl.cost;


/**
 * Implementation of Addable interface for an integer type.
 */
public class AddableInteger implements Addable<AddableInteger>, Comparable<AddableInteger>, Assignable<AddableInteger> {
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
	
	public void assign(AddableInteger other) {
		this.value = other.value;
	}
	
	public String toString() {
		return String.valueOf(value);
	}
	
}
