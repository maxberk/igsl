/**
 * Implicit Graph Search Library(C), 2009 
 */
package org.igsl.functor;

import java.util.List;

/**
 * This interface NodeGenerator should be implemented to initialize any tree traversal.
 * It presents two functions that define a process of graph search, namely node expansion
 * procedure and termination on a goal node.
 */
public interface NodeGenerator<T> {

	/**
	 * Expansion procedure for a node on the graph.
	 * Returns a list of child nodes.
	 * If a node has no successors returning resulting list is empty.
	 * 
	 * @param t value of the node to be expanded
	 * @return list of new nodes' values in a search tree
	 */
	List<T> expand(T t);
	
	/**
	 * Determines if the node on the graph is terminal in graph search process
	 * @param t node value
	 * @return true if the node is a goal
	 */
	boolean isGoal(T t);

}
