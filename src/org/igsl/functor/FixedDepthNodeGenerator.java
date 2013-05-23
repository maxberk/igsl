/**
 * Implicit Graph Search Library(C), 2009, 2013
 */

package org.igsl.functor;

/**
 * Interface FixedDepthNodeGenerator should be implemented to initialize a tree traversal
 * on a limited set of node values.
 */
public interface FixedDepthNodeGenerator<T> {

	/**
	 * Returns maximal depth
	 */
	int getMaxDepth();
	
	/**
	 * Create <code>ValuesIterator</code> instance for nodes on a given depth
	 * 
	 * @param idx depth value index
	 */
	ValuesIterator<T> createValues(int idx);
	
	/**
	 * Validate if a new node is valid in expansion procedure
	 * 
	 * @param value value for a new node
	 * @param iterator path iterator from a currently expanded node to a root node
	 * 
	 * @return true - if transition to value is valid
	 */
	boolean isValidTransition(T value, BackwardPathIterator<T> bpi);

}
