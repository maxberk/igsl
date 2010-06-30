/**
 * Implicit Graph Search Library(C), 2009, 2010 
 */

package org.igsl.functor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;

/**
 * Memoizer creates helper classes for functors to minimize
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
		//Handler handler = new Handler(function, new String[] {"expand", "isGoal", "getTransitionCost"});
		Handler handler = new Handler(function, new String[] { "expand" });
		
		return (CostFunction<T,C>) Proxy.newProxyInstance(
			function.getClass().getClassLoader(),
			function.getClass().getInterfaces(),
			handler);
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
	
	
	class Handler implements InvocationHandler {
		
		Object obj;
		String[] methodNames;
		HashMap[] maps = null; 
		
		Handler(Object obj, String[] methodNames) {
			this.obj = obj;
			this.methodNames = methodNames;
			
			maps = new HashMap[methodNames.length];
			for(int i = 0; i < methodNames.length; ++i) {
				maps[i] = new HashMap();
			}
		}
		
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			String methodName = method.getName();
			Object result = null;
			boolean isFound = false;
			
			for(int i = 0; i < methodNames.length; ++i) {
				if(methodName.equals(methodNames[i])) {
					result = maps[i].get(args[0]);
					
					if(result == null) {
						result = method.invoke(obj, args);
						maps[i].put(args[0], result);
						//System.out.println("Filter: methodName = " + methodName);
					} else {
						//System.out.println("Found: methodName = " + methodName);
					}
					
					isFound = true;
					break;
				}
			}
			
			if(isFound == false) {
				result = method.invoke(obj, args);				
				//System.out.println("Invoked: methodName = " + method.getName());
			}
			
			return result;
		}
		
	}

}