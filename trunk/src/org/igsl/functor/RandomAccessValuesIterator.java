/**
 * Implicit Graph Search Library(C), 2009, 2015
 */

package org.igsl.functor;

/**
 * Interface ValuesIterator for RandomAccess traversal.
 */
public interface RandomAccessValuesIterator<T> {
	
	void update(RandomAccess<T> tr);

	boolean hasNext();

	T next();
	
	T getValue();

}
