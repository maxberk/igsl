/**
 * Implicit Graph Search Library(C), 2009, 2015
 */

package org.igsl.functor;

/**
 * Interface IndefiniteDepthNodeGenerator
 */
public interface IndefiniteDepthNodeGenerator<T> {

	//ValuesIterator<T> createValues(BackwardPathIterator<T> iterator);
	RandomAccessValuesIterator<T> createValues(RandomAccess<T> tr);
	
	//boolean isValidTransition(T value, BackwardPathIterator<T> iterator);
	boolean isValidTransition(T value, RandomAccess<T> tr);
	
	//boolean isGoal(BackwardPathIterator<T> bpi);
	boolean isGoal(RandomAccess<T> tr);

}
