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
	
	boolean isValidTransition(T value, BackwardPathIterator<T> iterator);
	
	boolean isGoal(BackwardPathIterator<T> bpi);

}
