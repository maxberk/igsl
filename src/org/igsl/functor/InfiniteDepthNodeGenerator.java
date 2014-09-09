/**
 * Implicit Graph Search Library(C), 2009, 2014
 */

package org.igsl.functor;

/**
 * Interface InfiniteDepthNodeGenerator
 */
public interface InfiniteDepthNodeGenerator<T> {

	ValuesIterator<T> createValues(BackwardPathIterator<T> iterator);
	
	boolean isValidTransition(T value, BackwardPathIterator<T> iterator);
	
	boolean isGoal(BackwardPathIterator<T> bpi);

}
