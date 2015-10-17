package org.igsl.test.eightqueens2;

/**
 * Implicit Graph Search Library(C), 2009, 2015 
 */

import org.igsl.algorithm.Direct;
import org.igsl.app.eightqueens2.EQPSolver;
import org.igsl.functor.iterator.path.BackwardPathIterator;
import org.igsl.traversal.linear.finite.FiniteSetTreeTraversal;

public class EightQueensTest {
	
	/**
	 */
	public static void main(String[] args) {
		EQPSolver solver = new EQPSolver(8);
		
		System.out.println("=====Eight Queens Problem for FiniteSetTreeTraversal.=====");
		
		FiniteSetTreeTraversal<Integer> tr = new FiniteSetTreeTraversal<Integer>(solver);
		Direct.searchForward(tr);
		BackwardPathIterator<Integer> path = tr.getPath();
		
		while(path.hasPreviousNode()) {
			Integer i = path.previousNode();
			String toPrint = (path.hasPreviousNode()) ? i.intValue() + "->" : i.intValue() + "";
			System.out.print(toPrint);
		}
		
		System.out.println();
		
		tr.backtrack();
		
		Direct.searchForward(tr);
		path = tr.getPath();
		
		while(path.hasPreviousNode()) {
			Integer i = path.previousNode();
			String toPrint = (path.hasPreviousNode()) ? i.intValue() + "->" : i.intValue() + "";
			System.out.print(toPrint);
		}
		
		System.out.println();
	}

}