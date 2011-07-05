/**
 * Implicit Graph Search Library(C), 2011 
 */

package org.igsl.test.knighttour;

import java.util.Enumeration;

import org.igsl.algorithm.Direct;
import org.igsl.traversal.linear.DepthFirstTreeTraversal;

import org.igsl.app.knighttour.Position;
import org.igsl.app.knighttour.KnightTourSolver;

public class KnightTourTest {

	/**
	 * Knight Tour test scenario based on DepthFirstTreeTraversal. 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		KnightTourSolver solver = new KnightTourSolver(5);
		
		System.out.println("=====Knight Tour. Direct search.=====");
		DepthFirstTreeTraversal<Position> tr = new DepthFirstTreeTraversal<Position>(
			new Position(2,2), solver);
		
		int solutionId = 0;
		
		while(!tr.isEmpty()) {
			Direct.searchForward(tr);
			
			if(!tr.isEmpty()) {
				Enumeration<Position> path = tr.getPath();
				
				System.out.print("Solution " + (++solutionId) + ": ");
				
				while(path.hasMoreElements()) {
					Position p = path.nextElement();
					String toPrint = (path.hasMoreElements()) ? p.toString() + "->" : p.toString();
					System.out.print(toPrint);
				}
				
				System.out.println();
				tr.backtrack();
			} else {
				System.out.println("No more solutions found");
			}
		}
	}
	
}
