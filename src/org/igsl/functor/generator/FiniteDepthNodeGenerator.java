package org.igsl.functor.generator;

/**
 * Implicit Graph Search Library(C), 2009, 2015
 */

import org.igsl.functor.iterator.values.ValuesIterator;
import org.igsl.functor.iterator.path.BackwardPathIterator;


/**
 * Interface FiniteDepthNodeGenerator should be implemented to initialize a tree traversal
 * on a limited set of node values.
 */
public interface FiniteDepthNodeGenerator<T> {

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
