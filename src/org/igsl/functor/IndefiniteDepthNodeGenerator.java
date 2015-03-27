/**
 * Implicit Graph Search Library(C), 2009, 2015
 */

package org.igsl.functor;

/**
 * Interface IndefiniteDepthNodeGenerator
 */
public interface IndefiniteDepthNodeGenerator<T> {

	ValuesIterator<T> createValues(BackwardPathIterator<T> iterator);
	
	boolean isValidTransition(T value, BackwardPathIterator<T> iterator);
	
	boolean isGoal(BackwardPathIterator<T> bpi);

}
