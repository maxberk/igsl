/**
 * Implicit Graph Search Library(C), 2009, 2013
 */

package org.igsl.functor;

/**
 * Interface ValuesIterator.
 */
public interface ValuesIterator<T> {

	boolean hasNext();

	T next();
	
	T getValue();
	
	void update(BackwardPathIterator<T> bpi);

}
