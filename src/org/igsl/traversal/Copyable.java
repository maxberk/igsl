package org.igsl.traversal;

/**
 * Interface for objects that could be duplicated
 *  
 * @param <O> type of object
 */
public interface Copyable<O> {
	
	/**
	 * Creates a duplicated object
	 * @return copy of an object
	 */
	public O getCopyOf();

}
