/**
 * Implicit Graph Search Library(C), 2009 
 */
package org.igsl.functor;

/**
 * Heuristic cost function interface. Heuristics are used in best-first search
 * to estimate a "distance" between a search tree node and a goal node.
 *
 * @param <T> node type
 * @param <C> cost type
 */
public interface HeuristicFunction<T,C> {
	
	/**
	 * Estimates a cost between a node in a search tree and a goal node
	 * 
	 * @param t node in a search tree
	 * @return cost estimated value for a cost
	 */
	public C getEstimatedCost(T t);

}
