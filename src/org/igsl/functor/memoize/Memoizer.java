/**
 * Implicit Graph Search Library(C), 2009, 2010, 2011 
 */

package org.igsl.functor.memoize;

import java.lang.reflect.Proxy;
import java.util.HashMap;

import org.igsl.functor.NodeGenerator;
import org.igsl.functor.CostFunction;
import org.igsl.functor.HeuristicFunction;

/** Memoizer class
 */
public class Memoizer {
	
	private static HashMap<Object, Object> proxies = new HashMap<Object, Object>();

	/**
	 * Create a memoizer for an object implementing <interface>NodeGenerator</interface>
	 * interface
	 * 
	 * @param generator generator to be memoized
	 * @return generator helper
	 */
	public static <T> NodeGenerator<T> memoize(NodeGenerator<T> generator) {
		NodeGenerator<T> proxy = (NodeGenerator<T>) proxies.get(generator);
		
		if(proxy == null) {
			proxy = (NodeGenerator<T>) Proxy.newProxyInstance(
				generator.getClass().getClassLoader(),
				generator.getClass().getInterfaces(),
				new Handler(generator, NodeGenerator.class));
			
			proxies.put(generator, proxy);
		}
		
		return proxy;
	}
	
	/**
	 * Create a memoizer for a class implementing <class>CostFunction</class> interface
	 * 
	 * @param function cost function to be memoized
	 * @return cost function helper
	 */
	public static <T,C> CostFunction<T,C> memoize(CostFunction<T,C> function) {
		CostFunction<T,C> proxy = (CostFunction<T,C>) proxies.get(function);
		
		if(proxy == null) {
			proxy = (CostFunction<T,C>) Proxy.newProxyInstance(
				function.getClass().getClassLoader(),
				function.getClass().getInterfaces(),
				new Handler(function, CostFunction.class));
			
			proxies.put(function, proxy);
		}
		
		return proxy;
	}
	
	/**
	 * Create a memoizer for a class implementing <class>HeuristicFunction</class> interface
	 * 
	 * @param heuristics heuristic function to be memoized
	 * @return heuristic function helper
	 */
	public static <T,C> HeuristicFunction<T,C> memoize(HeuristicFunction<T,C> heuristics) {
		HeuristicFunction<T,C> proxy = (HeuristicFunction<T,C>) proxies.get(heuristics);
		
		if(proxy == null) {
			proxy = (HeuristicFunction<T,C>) Proxy.newProxyInstance(
				heuristics.getClass().getClassLoader(),
				heuristics.getClass().getInterfaces(),
				new Handler(heuristics, CostFunction.class));
			
			proxies.put(heuristics, proxy);
		}
		
		return proxy;
	}

}
