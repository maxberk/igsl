/**
 * Implicit Graph Search Library(C), 2011 
 */

package org.igsl.test.eightknights;

import java.util.Enumeration;

import org.igsl.algorithm.Direct;
import org.igsl.algorithm.Iterative;
import org.igsl.app.eightknights.Board;
import org.igsl.app.eightknights.EKPSolver;
import org.igsl.functor.memoize.Memoizer;
import org.igsl.traversal.linear.DepthFirstTreeTraversal;

public class EightKnightsTest {
	
	/**
	 * Eight Knights problem test scenarios based on DepthFirstTreeTraversal. First scenario demonstrates
	 * how to find all solutions with <code>searchForward</code> method while the second utilizes
	 * iterative <code>deepenIteratively</code> techniques. 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		EKPSolver solver = new EKPSolver();
		
		System.out.println("=====Eight Knights Problems. Direct search. All solutions=====");
		
		DepthFirstTreeTraversal<Board> tr = new DepthFirstTreeTraversal<Board>(new Board(), solver);
		int id = 0;
		
		while(!tr.isEmpty()) {
			Direct.searchForward(tr);
			if(!tr.isEmpty()) {
				System.out.print("Solution " + (++id) + ": ");
				Enumeration<Board> path = tr.getPath();
				while(path.hasMoreElements()) {
					Board r = path.nextElement();
					String toPrint = (path.hasMoreElements()) ? r.toString() + "->" : r.toString();
					System.out.print(toPrint);
				}
				System.out.println();
			
				tr.backtrack();
			}
		}
		
		System.out.println("=====Eight Knights Problems. Solution for iterative search=====");
		
		DepthFirstTreeTraversal<Board> tr2 = new DepthFirstTreeTraversal<Board>(
				new Board(), Memoizer.memoize(solver));
				
		Enumeration<Board> path = Iterative.deepenIteratively(tr2);
		System.out.print("Solution found iteratively: ");
		while(path.hasMoreElements()) {
			Board r = path.nextElement();
			String toPrint = (path.hasMoreElements()) ? r.toString() + "->" : r.toString();
			System.out.print(toPrint);
		}
		System.out.println();
	}

}
