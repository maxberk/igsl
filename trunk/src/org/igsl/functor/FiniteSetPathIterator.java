/**
 * Implicit Graph Search Library(C), 2009, 2010, 2011, 2013 
 */

package org.igsl.functor;

/**
 * Interface to random iterate over a current path in a search tree, namely from
 * a cursor leaf node to a root node.
 * 
 * @param <T> - type of value in a search tree
 */
public interface FiniteSetPathIterator<T> extends PathIterator<T> {
	
	/**
	 * Access to a root node in a search tree
	 * @return a root node value, null - if tree is empty
	 */
	public T rootNode();

	/**
	 * Access to a leaf node in a search tree
	 * @return a leaf node value, null - if tree is empty
	 */
	public T leafNode();
}