/**
 * Implicit Graph Search Library(C), 2011 
 */

package org.igsl.test.eightqueens;

import java.util.Enumeration;

import org.igsl.algorithm.Direct;
import org.igsl.algorithm.Iterative;
import org.igsl.app.eightqueens.Board;
import org.igsl.app.eightqueens.EQPSolver;
import org.igsl.traversal.linear.DepthFirstTreeTraversal;

import static org.igsl.functor.memoize.Memoizer.*;

public class EightQueensTest {
	
	/**
	 * Eight Queens problem test scenarios based on DepthFirstTreeTraversal. First scenario demonstrates
	 * how to find all solutions with <code>searchForward</code> method while the second utilizes
	 * iterative <code>deepenIteratively</code> techniques. 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		EQPSolver solver = new EQPSolver();
		
		System.out.println("=====Eight Queens Problems. Direct search. All solutions=====");
		
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
		
		System.out.println("=====Eight Queens Problems. Solution for iterative search=====");
		
		DepthFirstTreeTraversal<Board> tr2 = new DepthFirstTreeTraversal<Board>(
				new Board(), memoize(solver));
				
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
