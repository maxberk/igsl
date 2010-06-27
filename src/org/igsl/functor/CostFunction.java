/**
 * Implicit Graph Search Library(C), 2009 
 */
package org.igsl.functor;

/**
 * Cost function generator allows to calculate cost
 * of transition between neighbor nodes on a search tree.
 *
 * @param <T> type of node
 * @param <C> type of cost
 */
public interface CostFunction<T,C> extends NodeGenerator<T> {
	
	/**
	 * The cost of the edge between two nodes on a problem graph
	 * 
	 * @param from start node
	 * @param to final node
	 * @return transition cost between nodes
	 */
	public C getTransitionCost(T from, T to);

}
