/**
 * Implicit Graph Search Library(C), 2009, 2010, 2011, 2013
 */

package org.igsl.functor;

import java.util.List;

import org.igsl.functor.exception.DefaultValuesUnsupportedException;

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
	 * @param iterator path iterator from a root node  to a currently expanded node
	 * 
	 * @return default start node value
	 */
	boolean isValidTransition(T value, FiniteSetPathIterator<T> iterator);

}
