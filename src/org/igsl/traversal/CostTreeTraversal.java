/**
 * Implicit Graph Search Library(C), 2009, 2010, 2011, 2013
 */
package org.igsl.traversal;

import org.igsl.traversal.TreeTraversal;

/**
 * Interface CostTreeTravesal represents a traversal that accumulates cost while traversing the search tree.
 * It extends TreeTraversal with <code>CostFunction<code> functionality.
 * It is parameterized by a type of the node and a type of edge cost in a search tree.
 */
public interface CostTreeTraversal<T,C> extends TreeTraversal<T> {

	/**
	 * An accumulated cost of the cursor node in a serach tree
	 * 
	 * @return - cost of the cursor 
	 */
	public C getCost();
	
}
