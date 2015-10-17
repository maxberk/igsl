package org.igsl.traversal;

/**
 * Implicit Graph Search Library(C), 2009, 2012
 */

import org.igsl.functor.iterator.path.BackwardPathIterator;
import org.igsl.functor.exception.EmptyTraversalException;

/**
 * Interface TreeTravesal represents a search tree as a set of inner nodes, which are already expanded
 * and a set of front nodes, which are ready to be expanded. The expansion mechanism is customized
 * with presenting a <code>NodeGenerator</code> function. The TreeTraversal defines the changes in the search
 * as the new nodes are expanded and selection of the node(named <i>cursor</i>) from the search tree
 * to be expanded next. It qualifies a unified point for all types of traversals.
 * It is parameterized by a type of the node in a search tree.
 */
public interface TreeTraversal<T> {

	/**
	 * Calls NodeGenerator.expand() on a node from front set and accumulates node's children into the search
	 * tree structure. If there are no children, searches for a new node to be expanded performing tree pruning
	 * for nodes without successors. For an empty tree exits without any processing. If a terminal node is expanded,
	 * returns false not performing any changes in a search tree, otherwise - true.
	 */
	public boolean moveForward() throws EmptyTraversalException;
	
	/**
	 * Performs tree pruning for a best node in the front set and searches for a new node.
	 * For an empty tree exits without any processing with a false result, otherwise returns true.
	 */
	public void backtrack() throws EmptyTraversalException;

	/**
	 * Check if there are nodes to expand.
	 * @return true - if there are no front nodes, false - otherwise.
	 */
	public boolean isEmpty();

	/**
	 * Provides a path in a search tree from the cursor node to the root of a search tree.
	 * For an empty search tree returns an empty enumeration. Path iterator returned is
	 * a singleton in compare with <code>getPath</code> result
	 * @return -  node iterator
	 */
	public BackwardPathIterator<T> getPathIterator();
	
	/**
	 * Provides a path in a search tree from the cursor node to the root of a search tree.
	 * For an empty search tree returns an empty enumeration.
	 * @return -  node iterator
	 */
	public BackwardPathIterator<T> getPath();
	
	
}