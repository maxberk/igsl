/**
 * Implicit Graph Search Library(C), 2009, 2013
 */

package org.igsl.functor;

/**
 * Interface AccumulatedPathNodeGenerator should be implemented to initialize a tree traversal
 * with accumulated value of nodes on a path.
 */
public interface AccumulatedPathNodeGenerator<T> extends FiniteSetNodeGenerator<T> {

	/**
	 * Updates accumulator value with a value add-on.
	 * 
	 * @param accumulator value to be updated
	 * @param value value to be added
	 * 
	 */
	void updateAccumulatedValue(T accumulator, T value);

}