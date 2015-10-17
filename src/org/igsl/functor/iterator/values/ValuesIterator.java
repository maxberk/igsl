package org.igsl.functor.iterator.values;

/**
 * Implicit Graph Search Library(C), 2009, 2015
 */

import org.igsl.functor.iterator.path.BackwardPathIterator;

/**
 * Interface ValuesIterator.
 */
public interface ValuesIterator<T> {
	
	void update(BackwardPathIterator<T> iterator);

	boolean hasNext();

	T next();
	
	T getValue();

}
