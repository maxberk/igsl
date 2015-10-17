package org.igsl.functor.generator;

/**
 * Implicit Graph Search Library(C), 2009, 2015
 */

import org.igsl.traversal.RandomAccess;
import org.igsl.functor.iterator.values.RandomAccessValuesIterator;

/**
 * Interface IndefiniteDepthNodeGenerator
 */
public interface IndefiniteDepthNodeGenerator<T> {

	RandomAccessValuesIterator<T> createValues(RandomAccess<T> tr);
	
	boolean isValidTransition(T value, RandomAccess<T> tr);
	
	boolean isGoal(RandomAccess<T> tr);

}
