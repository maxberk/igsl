package org.igsl.test.hamiltonianpath;

/**
 * Implicit Graph Search Library(C), 2009, 2015
 */

import org.igsl.algorithm.Direct;
import org.igsl.functor.iterator.path.BackwardPathIterator;
import org.igsl.traversal.linear.finite.FiniteSetTreeTraversal;
import org.igsl.app.hamiltonianpath.HamiltonianPathSolver;

public class HamiltonianPathTest {
	
	/**
	 * TSP test based on DepthFirstCostTreeTraversal <code>searchForward</code> algorithm 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		HamiltonianPathSolver solver = new HamiltonianPathSolver();
		
		solver.addWaypoint("a", 0.3, 0.9);
		solver.addWaypoint("b", 1.1, 1.1);
		solver.addWaypoint("c", 1.0, -0.2);
		solver.addWaypoint("d", 0.1, 0.2);
		solver.addWaypoint("e", 1.5, 0.5);
		solver.addWaypoint("f", 0.5, 0.5);
		solver.addWaypoint("g", -0.8, 0.1);
		solver.addWaypoint("h", -0.8, 0.8);

		solver.addWaypoint("i", -0.2, 0.4);
		solver.addWaypoint("k", -0.7, 0.1);
		solver.addWaypoint("l", -0.9, 0.8);
		solver.addWaypoint("m", -0.3, 0.3);
		solver.addWaypoint("n", -0.6, 0.4);
		solver.addWaypoint("o", -0.2, 0.7);
		solver.addWaypoint("p", -0.8, 0.5);
		solver.addWaypoint("r", -0.7, 0.4);
		
		System.out.println("=====Hamiltonian Path Problem for FiniteSetTreeTraversal.=====");
		
		// Depth-first search tree traversal using TSPSolver class
		FiniteSetTreeTraversal<String> tr =
			new FiniteSetTreeTraversal<String>(solver);
		
		int i = 0;
		while(++i < 10) {

			// Find a solution
			Direct.searchForward(tr);
			BackwardPathIterator<String> path = tr.getPath();
			while(path.hasPreviousNode()) {
				String toPrint = path.previousNode()  + "->";
				System.out.print(toPrint);
			}
			System.out.println();
			
			tr.backtrack();
		}	
	}

}
