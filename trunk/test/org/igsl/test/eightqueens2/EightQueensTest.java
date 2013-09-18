/**
 * Implicit Graph Search Library(C), 2009, 2013 
 */

package org.igsl.test.eightqueens2;

import org.igsl.algorithm.Direct;
import org.igsl.app.eightqueens2.EQPSolver;
import org.igsl.functor.BackwardPathIterator;
import org.igsl.traversal.linear.finiteset.DepthFirstTreeTraversal;

public class EightQueensTest {
	
	/**
	 */
	public static void main(String[] args) {
		EQPSolver solver = new EQPSolver(8);
		
		System.out.println("=====Eight Queens Problem 2. Direct search.=====");
		
		DepthFirstTreeTraversal<Integer> tr = new DepthFirstTreeTraversal<Integer>(solver);
		Direct.searchForward(tr);
		BackwardPathIterator<Integer> path = tr.getPath();
		
		while(path.hasPreviousNode()) {
			Integer i = path.previousNode();
			String toPrint = (path.hasPreviousNode()) ? i.intValue() + "->" : i.intValue() + "";
			System.out.print(toPrint);
		}
		
		System.out.println();
	}

}