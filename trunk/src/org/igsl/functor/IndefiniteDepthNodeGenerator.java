/**
 * Implicit Graph Search Library(C), 2009, 2015
 */

package org.igsl.functor;

/**
 * Interface IndefiniteDepthNodeGenerator
 */
public interface IndefiniteDepthNodeGenerator<T> {

	RandomAccessValuesIterator<T> createValues(RandomAccess<T> tr);
	
	boolean isValidTransition(T value, RandomAccess<T> tr);
	
	boolean isGoal(RandomAccess<T> tr);

}
