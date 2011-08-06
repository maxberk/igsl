/**
 * Implicit Graph Search Library(C), 2009, 2010, 2011 
 */
package org.igsl.algorithm;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

import org.igsl.cost.Addable;
import org.igsl.traversal.CostTreeTraversal;
import org.igsl.traversal.TreeTraversal;

/**
 * Class containing direct graph search methods. All these methods return <code>void</code> and usually modify
 * a traversal state until a goal is reached. To access a solution one should exploit <code>getPath</code> method
 * that is common for <code>TreeTraversal</code> interface.
 */
public final class Direct {
	
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
	 * Search for all possible solutions based on <code>searchForward</code> call.
	 * 
	 * @param <T> type of the node
	 * @param tr search tree traversal
	 * @return an iterator to a list of solutions
	 */
	public static <T> Iterator<Enumeration<T>> findAllSolutions(TreeTraversal<T> tr) {
		ArrayList<Enumeration<T>> result = new ArrayList<Enumeration<T>>();
		
		while(!tr.isEmpty()) {
			searchForward(tr);
			if(!tr.isEmpty()) {
				result.add(tr.getPath());
			}
			tr.backtrack();
		}
		
		return result.iterator();
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
	 * Search for all optimal solutions based on <code>branchAndBound</code> techniques.
	 * 
	 * @param <T> type of the node
	 * @param <C> type of the cost
	 * @param tr search tree traversal
	 * @return an iterator to a list of solutions
	 */
	public static <T,C extends Addable<C> & Comparable<C>> Iterator<Enumeration<T>> findAllSolutions(CostTreeTraversal<T,C> tr) {
		ArrayList<Enumeration<T>> result = new ArrayList<Enumeration<T>>();
		
		searchForward(tr);
		
		if(tr.isEmpty()) {
			return result.iterator();
		}
		
		Enumeration<T> path = tr.getPath();
		C thresh = tr.getCost();
		result.add(path);
		
		tr.backtrack();
		
		while(!tr.isEmpty()) {
			if(tr.getNodeGenerator().isGoal(tr.getCursor())) {
				int compareResult = tr.getCost().compareTo(thresh);
				
				if(compareResult < 0)	{
					path = tr.getPath();
					thresh = tr.getCost();
	
					result.clear();
					result.add(path);
				} else if(compareResult == 0) {
					result.add(path);
				}
				
				tr.backtrack();
			} else {
				tr.moveForward();
			}
		}
		
		return result.iterator();
	}

}