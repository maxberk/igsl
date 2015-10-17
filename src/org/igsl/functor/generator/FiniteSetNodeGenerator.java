package org.igsl.functor.generator;

/**
 * Implicit Graph Search Library(C), 2009, 2015
 */

import org.igsl.functor.iterator.path.BackwardPathIterator;

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
	 * Limits the search depth by a value between 1 and T[].length
	 * 
	 * @return maximal depth of the search tree
	 */
	int getMaxDepth();
	
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
