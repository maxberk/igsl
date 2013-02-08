/**
 * Implicit Graph Search Library(C), 2009, 2010, 2011 
 */
package org.igsl.algorithm;

import java.util.Enumeration;
import java.util.Stack;

import org.igsl.cost.Addable;
import org.igsl.traversal.Copyable;
import org.igsl.traversal.TreeTraversal;
import org.igsl.traversal.CostTreeTraversal;
import org.igsl.traversal.TreeTraversal.PathIterator;

/**
 * Class containing iterative graph search methods. Iterative schemas return a path found while a traversal
 * does not change its state. It means that only traversals that implement <code>Copyable</code> interface
 * could be used in iterative schemas.
 */
public final class Iterative {
	
	/**
	 * Iterative deepening limited by depth on each step.
	 * 
	 * @param <T> type of the node
	 * @param <Tr> type of the traversal
	 * @param tr search tree traversal
	 * @return an enumeration containing nodes on a path from beginning to end,
	 * null if no path found
	 */
	public static <T,Tr extends TreeTraversal<T> & Copyable<Tr>> Enumeration<T> deepenIteratively(Tr tr)
	{
		if(tr.isEmpty()) {
			return null;
		}

		int depth = 0;
		do {
			Tr tr1 = tr.getCopyOf();
			
			while(!tr1.isEmpty() && !tr1.getNodeGenerator().isGoal(tr1.getCursor())) {
				if(tr1.getDepth() <= depth) {
					tr1.moveForward();
				} else {
					tr1.backtrack();
				}
			}
			
			if(!tr1.isEmpty()) {
				return convertTo(tr1.getPath());
			}
			
			++depth;
		} while(true);
	}
	
	/**
	 * Iterative deepening by acquiring minimal cost
	 * exceeding current threshold value on each iteration
	 * 
	 * @param <T> type of the node
	 * @param <C> type of the cost
	 * @param <Tr> type of the traversal
	 * @param tr search tree traversal
	 * @return an enumeration containing nodes on an optimal path from beginning to end,
	 * null if no path found
	 */
	public static <T,C extends Addable<C> & Comparable<C>,Tr extends CostTreeTraversal<T,C> & Copyable<Tr>>
		Enumeration<T> deepenIteratively(Tr tr)
	{
		if(tr.isEmpty()) {
			return null;
		}
		
		C thresh = tr.getCost();
		do {
			CostTreeTraversal<T,C> tr1 = tr.getCopyOf();
			C nextThresh = null;

			while(!tr1.isEmpty() && !tr1.getNodeGenerator().isGoal(tr1.getCursor())) {
				if(tr1.getCost().compareTo(thresh) < 1) { // <= thresh
					tr1.moveForward();
				} else {
					if(nextThresh == null || tr1.getCost().compareTo(nextThresh) == -1) { // <nextThresh
						nextThresh = tr1.getCost();
					}

					tr1.backtrack();
				}
			}
			
			if(!tr1.isEmpty()) {
				return convertTo(tr1.getPath());
			} else if(nextThresh == null){
				return null;
			}
			
			thresh = nextThresh;
		} while(true);
	}
	
	private static<T> Enumeration<T> convertTo(PathIterator<T> pi) {
		Stack<T> result = new Stack<T>();
		
		while(pi.hasPreviousNode()) {
			result.add(pi.previousNode());
		}
		
		return result.elements();	
	}	

}
