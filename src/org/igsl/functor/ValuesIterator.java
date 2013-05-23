/**
 * Implicit Graph Search Library(C), 2009, 2013
 */

package org.igsl.functor;

/**
 * Interface ValuesIterator.
 */
public interface ValuesIterator<T> {
	
	void update(BackwardPathIterator<T> iterator);

	boolean hasNext();

	T next();
	
	T getValue();

}
