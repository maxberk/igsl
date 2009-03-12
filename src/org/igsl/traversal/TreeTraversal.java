package org.igsl.traversal;

import java.util.Collection;
import java.util.Enumeration;

/**
 * Implicit Graph Search Library(C), 2009 
 */
import org.igsl.functor.NodeGenerator;

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
	 * for nodes without successors. For an empty tree exits without any processing.
	 */
	public void moveForward();
	
	/**
	 * Performs tree pruning for a best node in the front set and searches for a new node.
	 * For an empty tree exits without any processing.
	 */
	public void backtrack();

	/**
	 * Returns a value of the node to be expanded next
	 * @return - cursor value
	 */
	public T getCursor();

	/**
	 * Returns a NodeGenerator function
	 * @return - reference to a <code>NodeGenerator</code> instance
	 * @see NodeGenerator
	 */
	public NodeGenerator<T> getNodeGenerator();
	
	/**
	 * Check if there are nodes to expand.
	 * @return true - if there are no front nodes, false - otherwise.
	 */
	public boolean isEmpty();

	/**
	 * Provides a path in a search tree from the cursor node to the root of a search tree.
	 * For an empty search tree returns an empty enumeration.
	 * @return - enumeration of nodes
	 */
	public Enumeration<T> getPath();

	/**
	 * Returns a collection of frontier nodes in a search tree.
	 * @return - <code>Collection</code> of node values
	 */
	public Collection<T> getLeafs();

	/**
	 * Returns a number of edges in a search tree from a root node to a cursor.
	 * @return - int value, -1 for an empty traversal
	 * @see #isEmpty()
	 */
	public int getDepth();

}