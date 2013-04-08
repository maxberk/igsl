/**
 * Implicit Graph Search Library(C), 2009, 2010, 2011, 2013 
 */

package org.igsl.functor;

/**
 * Interface to random iterate over a current path in a search tree, namely from
 * a root node to a cursor leaf node.
 * 
 * @param <T> - type of value in a search tree
 */
public interface ForwardPathIterator<T> {
	
	/**
	 * Means a node is not a search tree leaf
	 * @return true - if it is not a leaf node, false - if it is a leaf node.
	 */
	public boolean hasNextNode();

	/**
	 * 
	 */
	public T nextNode();
	
}