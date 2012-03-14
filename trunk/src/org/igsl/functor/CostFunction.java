/**
 * Implicit Graph Search Library(C), 2009, 2010, 2011 
 */
package org.igsl.functor;

import org.igsl.functor.exception.DefaultValuesUnsupportedException;

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
	C getTransitionCost(T from, T to);
	
	/**
	 * Defines root(start) node cost 
	 * @return default start node cost
	 * @throws DefaultValuesUnsupportedException if no default root cost value exist
	 */
	C getDefaultRootCost() throws DefaultValuesUnsupportedException;

}
