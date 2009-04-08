package org.igsl.algorithm;

import java.util.Enumeration;

import org.igsl.cost.Addable;
import org.igsl.traversal.CopyableCostTreeTraversal;
import org.igsl.traversal.CopyableTreeTraversal;
import org.igsl.traversal.CostTreeTraversal;
import org.igsl.traversal.TreeTraversal;

/**
 * Class containing iterative graph search methods.
 */
public final class Iterative {
	
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
