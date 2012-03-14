/**
 * Implicit Graph Search Library(C), 2009, 2010 
 */

package org.igsl.functor.memoize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates a method from <code>NodeGenerator</code>,
 * <code>CostFunction</code> or <code>HeuristicFunction</code>
 * functors should be memoized. If no method is put under this
 * marker, all methods from functor interface are memoized.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Memoize {
}
