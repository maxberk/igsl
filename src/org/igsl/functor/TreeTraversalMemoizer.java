/**
 * Implicit Graph Search Library(C), 2009, 2010 
 */

package org.igsl.functor;

import java.lang.reflect.Proxy;

/**
 * TreeTraversalMemoizer creates helper class for solvers implementing
 * <code>NodeGenerator</code> interface to minimize a calculation time
 * for corresponding functors
 *
 * @param <T> type of node
 */
public class TreeTraversalMemoizer<T> {
	
	/**
	 * Create a memoizer for a <interface>NodeGenerator</interface>
	 * 
	 * @param generator generator to be memoized
	 * @return generator helper
	 */
	public NodeGenerator<T> memoize(NodeGenerator<T> generator) {
		//Handler handler = new Handler(generator, new String[] {"expand", "isGoal"});
		Handler handler = new Handler(generator, new String[] {"expand"});
		
		return (NodeGenerator<T>) Proxy.newProxyInstance(
			generator.getClass().getClassLoader(),
			generator.getClass().getInterfaces(),
			handler);
	}

}