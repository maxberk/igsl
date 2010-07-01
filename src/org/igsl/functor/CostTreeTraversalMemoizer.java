package org.igsl.functor;

import java.lang.reflect.Proxy;

/**
 * CostTreeTraversalMemoizer creates helper classes for functors implementing
 * <code>CostFunction</code> or descendant interface to minimize
 * a calculation time for corresponding functors
 *
 * @param <T> type of node
 * @param <C> type of cost
 */
public class CostTreeTraversalMemoizer<T,C> extends TreeTraversalMemoizer<T> {
	
	/**
	 * Create a memoizer for a class implementing <class>CostFunction</class> interface
	 * 
	 * @param function cost function to be memoized
	 * @return cost function helper
	 */
	public CostFunction<T,C> memoize(CostFunction<T,C> function) {
		//Handler handler = new Handler(function, new String[] {"expand", "isGoal", "getTransitionCost"});
		Handler handler = new Handler(function, new String[] { "expand" });
		
		return (CostFunction<T,C>) Proxy.newProxyInstance(
			function.getClass().getClassLoader(),
			function.getClass().getInterfaces(),
			handler);
	}
	
	/**
	 * Create a memoizer for a class implementing <class>HeuristicFunction</class> interface
	 * 
	 * @param heuristics heuristic function to be memoized
	 * @return heuristic function helper
	 */
	public HeuristicFunction<T,C> memoize(HeuristicFunction<T,C> heuristics) {
		return heuristics;
	}

}
