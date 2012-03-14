/**
 * Implicit Graph Search Library(C), 2011 
 */

package org.igsl.test.knighttour;

import java.util.Enumeration;
import java.util.Iterator;

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
		
		Iterator<Enumeration<Position>> iterator = Direct.findAllSolutions(tr);
		int id = 0;
		
		while(iterator.hasNext()) {
			System.out.print("Solution " + (++id) + ": ");
			Enumeration<Position> path = iterator.next();
			while(path.hasMoreElements()) {
				Position r = path.nextElement();
				String toPrint = (path.hasMoreElements()) ? r.toString() + "->" : r.toString();
				System.out.print(toPrint);
			}
			System.out.println();
		}
	}
	
}
