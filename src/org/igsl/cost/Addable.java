/**
 * Implicit Graph Search Library(C), 2009 
 */
package org.igsl.cost;

/**
 * This interface is used for cost value accumulated while moving from
 * start to the end point of search.
 * This cost value should confirm summary operation through addTo method. 
 */

public interface Addable<C> {

	/**
	 * The method defines the result of summation of a cost value with the other instance
	 * of the same type. New value is expected to be a new instance. For example, the normal
	 * behavior of the method for Integer values in the scope of Addable interface should
	 * look like
	 * 
	 * public AddableInteger addTo(AddableInteger other) {
	 * return new AddableInteger(value + other.value);
	 * }
	 * 
	 * @param other - second operand
	 * @return C - new instance of the cost value
	 */
	public C addTo(C other);
	
}
