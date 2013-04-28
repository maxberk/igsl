/**
 * Implicit Graph Search Library(C), 2011 
 */

package org.igsl.test.knighttour2;

import java.util.Enumeration;
import java.util.Iterator;

import org.igsl.algorithm.Direct;
import org.igsl.traversal.linear.finiteset.FixedDepthTreeTraversal;
import org.igsl.functor.BackwardPathIterator;

import org.igsl.app.knighttour2.Position;
import org.igsl.app.knighttour2.KnightTourSolver;

public class KnightTourTest {

	/**
	 * Knight Tour test scenario based on DepthFirstTreeTraversal. 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		KnightTourSolver solver = new KnightTourSolver(5, new Position(2,2));
		
		System.out.println("=====Knight Tour 2. Direct search.=====");
		FixedDepthTreeTraversal<Position> tr = new FixedDepthTreeTraversal<Position>(solver);
		
		Direct.searchForward(tr);
		BackwardPathIterator<Position> path = tr.getPath();
		while(path.hasPreviousNode()) {
			String toPrint = path.previousNode()  + "->";
			System.out.print(toPrint);
		}
		System.out.println();
	}
	
}
