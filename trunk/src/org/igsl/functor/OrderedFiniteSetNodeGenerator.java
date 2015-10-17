/**
 * Implicit Graph Search Library(C), 2009, 2014
 */

package org.igsl.functor;

/**
 * Interface OrderedFiniteSetNodeGenerator should be implemented to initialize a tree traversal
 * on a limited set of ordered node values.
 */
public interface OrderedFiniteSetNodeGenerator<T>
	extends FiniteSetNodeGenerator<T> {

	/**
	 * Validate if a new node is valid in expansion procedure
	 * 
	 * @param value1 value for a first node
	 * @param value2 value for a first node
	 * @param iterator path iterator from a currently expanded node to a root node
	 * 
	 * @return -1, 0 ,1 - if, correspondingly, value1<value2, value1=value2, value1>value2
	 */
	int compareValues(T value1, T value2, BackwardPathIterator<T> iterator);

}
