package org.igsl.test.eightqueens;

/**
 * Implicit Graph Search Library(C), 2009, 2015 
 */

import org.igsl.algorithm.Direct;
import org.igsl.app.eightqueens.Queen;
import org.igsl.app.eightqueens.EQPSolver;
import org.igsl.functor.iterator.path.BackwardPathIterator;
import org.igsl.traversal.linear.finite.FiniteDepthTreeTraversal;

public class EightQueensTest {
	
	/**
	 */
	public static void main(String[] args) {
		EQPSolver solver = new EQPSolver(8);
		
		System.out.println("=====Eight Queens Problem for FiniteDepthTreeTraversal.=====");
		
		FiniteDepthTreeTraversal<Queen> tr = new FiniteDepthTreeTraversal<Queen>(solver);
		Direct.searchForward(tr);
		BackwardPathIterator<Queen> path = tr.getPath();
		
		while(path.hasPreviousNode()) {
			Queen q = path.previousNode();
			String toPrint = (path.hasPreviousNode()) ? q.toString() + "->" : q.toString();
			System.out.print(toPrint);
		}
		
		System.out.println();
	}

}