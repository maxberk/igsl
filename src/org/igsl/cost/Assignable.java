/**
 * Implicit Graph Search Library(C), 2011
 */
package org.igsl.cost;

/**
 * This interface is used for cost value shared between different traversal instances
 * This cost value should confirm assign operation through assign method. 
 */
public interface Assignable<C> {
	
	public void assign(C other);

}
