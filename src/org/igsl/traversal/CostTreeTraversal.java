/**
 * Implicit Graph Search Library(C), 2009 
 */
package org.igsl.traversal;

import org.igsl.functor.CostFunction;

/**
 * Interface CostTreeTravesal represents a traversal that accumulates cost while traversing the search tree.
 * It extends TreeTraversal with <code>CostFunction<code> functionality.
 * It is parameterized by a type of the node and a type of edge cost in a search tree.
 */
public interface CostTreeTraversal<T,C> extends TreeTraversal<T> {

	/**
	 * A cost of the cursor
	 * 
	 * @return - cost of the cursor 
	 */
	public C getCost();
	
	/**
	 * Returns a CostFunction function
	 * 
	 * @return - reference to a <code>CostFunction</code> instance
	 * @see CostFunction
	 */
	public CostFunction<T,C> getCostFunction();	

}
