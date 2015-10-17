package org.igsl.functor.iterator.path;

/**
 * Implicit Graph Search Library(C), 2009, 2015 
 */

/**
 * Interface to iterate over a current path in a search tree, namely from
 * a cursor leaf node to a root node.
 * 
 * @param <T> - type of value in a search tree
 */
public interface BackwardPathIterator<T> {
	
	/**
	 * Means a node is not a search tree root
	 * @return true - if it is not a root node, false - if it is a root node.
	 */
	public boolean hasPreviousNode();

	/**
	 * 
	 */
	public T previousNode();
}