/**
 * Implicit Graph Search Library(C), 2009 
 */
package org.igsl.cost;

/**
 * Implementation of Addable interface for double type.
 */
public class AddableDouble implements Addable<AddableDouble>, Comparable<AddableDouble>, Assignable<AddableDouble>
{
	protected double value;
	
	/**
	 * Assigning zero value
	 */
	public AddableDouble() {
		this.value = 0;
	}
	
	/**
	 * Assigning a defined value
	 */
	public AddableDouble(double value) {
		this.value = value;
	}
	
	public AddableDouble addTo(AddableDouble other) {
		return new AddableDouble(value + other.value);
	}
	
	public int compareTo(AddableDouble other) {
		if(value > other.value) {
			return 1;
		} else if (value < other.value) {
			return -1;
		} else {
			return 0;
		}
	}
	
	public void assign(AddableDouble other) {
		this.value = other.value;
	}
	
	public String toString() {
		return String.valueOf(value);
	}
	
}