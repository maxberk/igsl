/**
 * Implicit Graph Search Library(C), 2011 
 */

package org.igsl.test.tsp;

import java.util.Enumeration;
import java.util.Iterator;

import org.igsl.algorithm.Direct;
import org.igsl.algorithm.Iterative;
import org.igsl.app.tsp.Route;
import org.igsl.app.tsp.TSPSolver;
import org.igsl.app.tsp.TSPSolver.Waypoint;
import org.igsl.cost.AddableDouble;
import org.igsl.functor.memoize.CostTreeTraversalMemoizer;
import org.igsl.functor.memoize.TreeTraversalMemoizer;
import org.igsl.traversal.exponential.BreadthFirstTreeTraversal;
import org.igsl.traversal.linear.DepthFirstCostTreeTraversal;
import org.igsl.traversal.linear.RecursiveBestFirstTreeTraversal;

public class TSPTest {
	
	/**
	 * TSP test based on DepthFirstCostTreeTraversal <code>searchForward</code>, <code>branchAndBound</code>
	 * and <code>deepenIteratively</code> algorithms 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		TSPSolver solver = new TSPSolver();
		
		solver.addWaypoint("a", 0.3, 0.9);
		solver.addWaypoint("b", 1.1, 1.1);
		solver.addWaypoint("c", 1.0, -0.2);
		solver.addWaypoint("d", 0.1, 0.2);
		solver.addWaypoint("e", 1.5, 0.5);
		solver.addWaypoint("f", 0.5, 0.5);
		solver.addWaypoint("g", -0.8, 0.1);
		solver.addWaypoint("h", -0.8, 0.8);
		
		System.out.println("=====Traveling Salesman Problem. Waypoints. Variant No. 1=====");
		Iterator<String> waypointsIterator1 = solver.getWaypoints().iterator();
		while(waypointsIterator1.hasNext()) {
			String waypointName = waypointsIterator1.next();
			Waypoint waypoint = solver.getWaypoint(waypointName);
			System.out.println(waypointName + ": x = " + waypoint.x + "; y = " + waypoint.y + ".");
		}
		
		// Depth-first search tree traversal using TSPSolver class
		// both for NodeGenerator and CostFunction implementations
		DepthFirstCostTreeTraversal<Route,AddableDouble> tr =
			new DepthFirstCostTreeTraversal<Route,AddableDouble>(
				new Route(solver.getWaypoints()), new AddableDouble(0), solver
			);

		// Find a solution without cost preference
		Direct.searchForward(tr);
		System.out.print("Admissable(non-optimal) path found while searching forward: ");
		Enumeration<Route> path = tr.getPath();
		while(path.hasMoreElements()) {
			Route r = path.nextElement();
			String toPrint = (path.hasMoreElements()) ? r.toString() + "->" : r.toString();
			System.out.print(toPrint);
		}
		System.out.println();

		// Initialize a second instance of depth-first tree traversal
		DepthFirstCostTreeTraversal<Route,AddableDouble> tr2 =
			new DepthFirstCostTreeTraversal<Route,AddableDouble>(
				new Route(solver.getWaypoints()), new AddableDouble(0), solver
			);

		// Find an optimal(minimal cost) solution with a branch-and-bound technique
		Enumeration<Route> path2 = Direct.branchAndBound(tr2);
		System.out.print("Optimal path found with branch-and-bound method: ");
		while(path2.hasMoreElements()) {
			Route r = path2.nextElement();
			String toPrint = (path2.hasMoreElements()) ? r.toString() + "->" : r.toString();
			System.out.print(toPrint);
		}
		System.out.println();

		// Initialize a third instance of depth-first tree traversal
		DepthFirstCostTreeTraversal<Route,AddableDouble> tr3 =
			new DepthFirstCostTreeTraversal<Route,AddableDouble>(
				new Route(solver.getWaypoints()), new AddableDouble(0),
				(new CostTreeTraversalMemoizer<Route,AddableDouble>()).memoize(solver)
			);

		// Find an optimal(minimal cost) solution with iterative deepening technique
		Enumeration<Route> path3 = Iterative.deepenIteratively(tr3);
		System.out.print("The optimal path found with iterative deepening: ");
		while(path3.hasMoreElements()) {
			Route r = path3.nextElement();
			String toPrint = (path3.hasMoreElements()) ? r.toString() + "->" : r.toString();
			System.out.print(toPrint);
		}
		System.out.println();
		
		// Initialize an instance of recursive best-first tree traversal
		RecursiveBestFirstTreeTraversal<Route,AddableDouble> tr4 =
			new RecursiveBestFirstTreeTraversal<Route,AddableDouble>(
				new Route(solver.getWaypoints()), new AddableDouble(0),
				(new CostTreeTraversalMemoizer<Route,AddableDouble>()).memoize(solver)
			);
		
		// Find an optimal(minimal cost) solution with recursive best-first tree traversal
		Direct.searchForward(tr4);
		
		Enumeration<Route> path4 = tr4.getPath();
		System.out.print("The optimal path found with recursive best-first search: ");
		while(path4.hasMoreElements()) {
			Route r = path4.nextElement();
			String toPrint = (path4.hasMoreElements()) ? r.toString() + "->" : r.toString();
			System.out.print(toPrint);
		}
		System.out.println("cost is " + tr4.getCost());
		
		// Initialize an instance of breadth-first tree traversal
		BreadthFirstTreeTraversal<Route> tr5 =
			new BreadthFirstTreeTraversal<Route>(
				new Route(solver.getWaypoints()),
				(new TreeTraversalMemoizer<Route>()).memoize(solver)
			);
		
		// Find an admissible solution with breadth-first tree traversal
		Direct.searchForward(tr5);
		
		Enumeration<Route> path5 = tr5.getPath();
		System.out.print("The path found with breadth-first search: ");
		while(path5.hasMoreElements()) {
			Route r = path5.nextElement();
			String toPrint = (path5.hasMoreElements()) ? r.toString() + "->" : r.toString();
			System.out.print(toPrint);
		}
	}

}
