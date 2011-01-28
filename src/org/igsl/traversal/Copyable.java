/**
 * Implicit Graph Search Library(C), 2009, 2010, 2011 
 */
package org.igsl.traversal;

/**
 * Interface for objects that could be duplicated
 *  
 * @param <O> type of object
 */
public interface Copyable<O extends Copyable<O>> {
	
	/**
	 * Creates a duplicated object
	 * @return copy of an object
	 */
	public O getCopyOf();

}
