/**
 * Implicit Graph Search Library(C), 2011 
 */
package org.igsl.cost;

/**
 * This interface is used for cost value that could be incremented by one.
 */

public interface Incrementable<C> {

	/**
	 * The method defines the result of summation of a cost value and one.
	 * New value is expected to be a new instance. For example, the normal
	 * behavior of the method for Integer values in the scope of Incrementable
	 * interface should look like
	 * 
	 * public AddableInteger inc() {
	 * return new AddableInteger(value + 1);
	 * }
	 * 
	 * @return C - new instance of the cost value
	 */
	public C inc();
	
}