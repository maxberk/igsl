/**
 * Implicit Graph Search Library(C), 2011 
 */

package org.igsl.traversal;

/**
 * Interface for objects that could be split
 *  
 * @param <O> type of object
 */
public interface Splitable<O extends Splitable<O>> {

	/**
	 * Creates a separate object and modifies current as a result of split
	 * @return a split object
	 */
	public O split();

}
