package org.igsl.traversal;

/**
 * Implicit Graph Search Library(C), 2009, 2015
 */

/**
 * Interface to access nodes of traversal in a random order
 * 
 * @param <T> - type of value in a search tree
 */
public interface RandomAccess<T> {
	
	/**
	 *
	 */
	public int length();

	/**
	 * 
	 */
	public T get(int idx) throws IndexOutOfBoundsException;
	
}