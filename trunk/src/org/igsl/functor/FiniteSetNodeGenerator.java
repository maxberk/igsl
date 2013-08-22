/**
 * Implicit Graph Search Library(C), 2009, 2010, 2011, 2013
 */

package org.igsl.functor;

/**
 * Interface FiniteSetNodeGenerator should be implemented to initialize a tree traversal
 * on a limited set of node values.
 */
public interface FiniteSetNodeGenerator<T> {

	/**
	 * Presents all possible node values.
	 * 
	 * @return array of possible node values
	 */
	T[] getAllValues();
	
	/**
	 * Validate if a new node is valid in expansion procedure
	 * 
	 * @param value value for a new node
	 * @param iterator path iterator from a currently expanded node to a root node
	 * 
	 * @return true - if transition to value is valid
	 */
	boolean isValidTransition(T value, BackwardPathIterator<T> iterator);

}
