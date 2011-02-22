/**
 * Implicit Graph Search Library(C), 2009, 2010, 2011 
 */

package org.igsl.functor.memoize;

import java.lang.reflect.Proxy;

import org.igsl.functor.NodeGenerator;

/**
 * TreeTraversalMemoizer creates helper class for solvers implementing
 * <code>NodeGenerator</code> interface to minimize a calculation time
 * for corresponding functors
 *
 * @param <T> type of node
 */
public class TreeTraversalMemoizer<T> {
	
	/**
	 * Create a memoizer for an object implementing <interface>NodeGenerator</interface>
	 * interface
	 * 
	 * @param generator generator to be memoized
	 * @return generator helper
	 */
	public NodeGenerator<T> memoize(NodeGenerator<T> generator) {
		Handler handler = new Handler(generator, NodeGenerator.class);
				
		return (NodeGenerator<T>) Proxy.newProxyInstance(
			generator.getClass().getClassLoader(),
			generator.getClass().getInterfaces(),
			handler);
	}

}