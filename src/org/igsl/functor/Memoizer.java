/**
 * Implicit Graph Search Library(C), 2009, 2010 
 */

package org.igsl.functor;

/**
 * Memoizer creates helper classes for all functors to minimize
 * a calculation time for a functor
 *
 * @param <T> type of node
 * @param <C> type of cost
 */
public class Memoizer<T,C> {
	
	/**
	 * Create a memoizer for a <interface>NodeGenerator</interface>
	 * 
	 * @param generator generator to be memoized
	 * @return generator helper
	 */
	public NodeGenerator<T> memoize(NodeGenerator<T> generator) {
		return generator;
	}
	
	/**
	 * Create a memoizer for a <class>CostFunction</class>
	 * 
	 * @param function cost function to be memoized
	 * @return cost function helper
	 */
	public CostFunction<T,C> memoize(CostFunction<T,C> function) {
		return function;
	}
	
	/**
	 * Create a memoizer for a <class>HeuristicFunction</class>
	 * 
	 * @param heuristics heuristic function to be memoized
	 * @return heuristic function helper
	 */
	public HeuristicFunction<T,C> memoize(HeuristicFunction<T,C> heuristics) {
		return heuristics;
	}

}