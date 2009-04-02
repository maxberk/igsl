/**
 * Implicit Graph Search Library(C), 2009 
 */
package org.igsl.algorithm;

import java.util.Enumeration;

import org.igsl.cost.Addable;
import org.igsl.traversal.CopyableCostTreeTraversal;
import org.igsl.traversal.CopyableTreeTraversal;
import org.igsl.traversal.CostTreeTraversal;
import org.igsl.traversal.TreeTraversal;

/**
 * Class containing sequential graph search algorithms.
 */
public final class Sequential {
	
	/**
	 * Basic constraint-satisfaction problem programming algorithm, no cost included. Algorithms expands nodes
	 * in a search tree according to the expansion order and terminates either by a goal condition or
	 * exhausting a search space - traversal becomes empty.
	 * 
	 * @param <T> type of the node
	 * @param tr search tree traversal
	 */
	public static <T> void searchForward(TreeTraversal<T> tr) {
		while(!tr.isEmpty() && !tr.getNodeGenerator().isGoal(tr.getCursor())) {
			tr.moveForward();
		}
	}

	/**
	 * Cost threshold algorithms - the solution found should not exceed a predefined cost value. Either founds
	 * the first matching solution and exits or the traversal becomes empty on exhausting the search space.
	 * 
	 * @param <T> type of the node
	 * @param <C> type of the cost
	 * @param tr search tree traversal
	 * @param thresh predefined cost value
	 */
	public static <T,C extends Addable<C> & Comparable<C>> void searchForward(CostTreeTraversal<T,C> tr, C thresh) {
		while(!tr.isEmpty()) {
			if( tr.getCost().compareTo(thresh) > 0) {
				tr.backtrack();
			} else if(tr.getNodeGenerator().isGoal(tr.getCursor())) {
				return;
			} else {
				tr.moveForward();
			}
		}
	}
	
	/**
	 * Branch-and-bound - a technique to find best-by-cost solution.
	 * 
	 * @param <T> type of the node
	 * @param <C> type of the cost
	 * @param tr search tree traversal
	 * @return an enumeration containing nodes on an optimal path from beginning to end
	 */
	public static <T,C extends Addable<C> & Comparable<C>> Enumeration<T> branchAndBound(CostTreeTraversal<T,C> tr) {
		searchForward(tr);
		
		if(tr.isEmpty()) {
			return tr.getPath();
		}
		
		Enumeration<T> result = tr.getPath();
		C thresh = tr.getCost();
		tr.backtrack();
		
		while(!tr.isEmpty()) {
			int compareResult = tr.getCost().compareTo(thresh);
			
			if(tr.getNodeGenerator().isGoal(tr.getCursor())	&& (compareResult < 0))	{
				result = tr.getPath();
				thresh = tr.getCost();
				tr.backtrack();
			} else if(compareResult >= 0) {
				tr.backtrack();
			} else {
				tr.moveForward();
			}
		}
		
		return result;
	}
	
	/**
	 * Iterative deepening limited by depth on each step
	 * 
	 * @param <T> type of the node
	 * @param tr search tree traversal
	 * @return an enumeration containing nodes on a path from beginning to end,
	 * null if no path found
	 */
	public static <T> Enumeration<T> deepenIteratively(
			CopyableTreeTraversal<T> tr) {
		if(tr.isEmpty()) {
			return null;
		}

		int depth = 0;
		do {
			TreeTraversal<T> tr1 = tr.getCopyOf();

			while(!tr1.isEmpty() && !tr1.getNodeGenerator().isGoal(tr1.getCursor())) {
				if(tr1.getDepth() < depth) {
					tr1.moveForward();
				} else {
					tr1.backtrack();
				}
			}
			
			if(!tr1.isEmpty()) {
				return tr1.getPath();
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
	 * @param tr search tree traversal
	 * @return an enumeration containing nodes on an optimal path from beginning to end,
	 * null if no path found
	 */
	public static <T,C extends Addable<C> & Comparable<C>> Enumeration<T> deepenIteratively(
			CopyableCostTreeTraversal<T,C> tr) {
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
				return tr1.getPath();
			} else if(nextThresh == null){
				return null;
			}
			
			thresh = nextThresh;
		} while(true);
	}

}