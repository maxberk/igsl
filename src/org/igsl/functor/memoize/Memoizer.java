/**
 * Implicit Graph Search Library(C), 2009, 2010, 2011 
 */

package org.igsl.functor.memoize;

import java.lang.reflect.Proxy;

import org.igsl.functor.NodeGenerator;
import org.igsl.functor.CostFunction;
import org.igsl.functor.HeuristicFunction;

/** Memoizer class
 */
public class Memoizer {

	/**
	 * Create a memoizer for an object implementing <interface>NodeGenerator</interface>
	 * interface
	 * 
	 * @param generator generator to be memoized
	 * @return generator helper
	 */
	public static <T> NodeGenerator<T> memoize(NodeGenerator<T> generator) {
		Handler handler = new Handler(generator, NodeGenerator.class);
		
		return (NodeGenerator<T>) Proxy.newProxyInstance(
			generator.getClass().getClassLoader(),
			generator.getClass().getInterfaces(),
			handler);
	}
	
	/**
	 * Create a memoizer for a class implementing <class>CostFunction</class> interface
	 * 
	 * @param function cost function to be memoized
	 * @return cost function helper
	 */
	public static <T,C> CostFunction<T,C> memoize(CostFunction<T,C> function) {
		Handler handler = new Handler(function, CostFunction.class);
		
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
	public static <T,C> HeuristicFunction<T,C> memoize(HeuristicFunction<T,C> heuristics) {
		Handler handler = new Handler(heuristics, HeuristicFunction.class);
		
		return (HeuristicFunction<T,C>) Proxy.newProxyInstance(
			heuristics.getClass().getClassLoader(),
			heuristics.getClass().getInterfaces(),
			handler);
	}

}
