/**
 * Implicit Graph Search Library(C), 2009, 2010 
 */

package org.igsl.functor.memoize;

/**
 * Indicates a method from <code>NodeGenerator</code>,
 * <code>CostFunction</code> or <code>HeuristicFunction</code>
 * functors should be memoized. If no method is put under this
 * marker, all methods from functor interface are memoized.
 */
public @interface Memoize {

}
