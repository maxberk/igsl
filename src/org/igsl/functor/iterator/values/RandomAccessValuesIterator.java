package org.igsl.functor.iterator.values;

/**
 * Implicit Graph Search Library(C), 2009, 2015
 */

import org.igsl.traversal.RandomAccess;

/**
 * Interface ValuesIterator for RandomAccess traversal.
 */
public interface RandomAccessValuesIterator<T> {
	
	void update(RandomAccess<T> tr);

	boolean hasNext();

	T next();
	
	T getValue();

}
