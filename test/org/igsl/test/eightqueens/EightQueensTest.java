/**
 * Implicit Graph Search Library(C), 2009, 2013 
 */

package org.igsl.test.eightqueens;

import org.igsl.algorithm.Direct;
import org.igsl.app.eightqueens.Queen;
import org.igsl.app.eightqueens.EQPSolver;
import org.igsl.functor.BackwardPathIterator;
import org.igsl.traversal.linear.finiteset.FixedDepthTreeTraversal;

public class EightQueensTest {
	
	/**
	 */
	public static void main(String[] args) {
		EQPSolver solver = new EQPSolver(8);
		
		System.out.println("=====Eight Queens Problem. Direct search.=====");
		
		FixedDepthTreeTraversal<Queen> tr = new FixedDepthTreeTraversal<Queen>(solver);
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