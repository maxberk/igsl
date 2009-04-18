/**
 * Implicit Graph Search Library(C), 2009 
 */
package org.igsl.app.tsp;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.igsl.algorithm.Direct;
import org.igsl.algorithm.Iterative;
import org.igsl.functor.CostFunction;
import org.igsl.functor.NodeGenerator;
import org.igsl.traversal.linear.DepthFirstCostTreeTraversal;
import org.igsl.traversal.linear.DepthFirstTreeTraversal;

/**
 * NodeGenerator and CostFunction implementations for Traveling Salesman Problem.
 * The problem is formulated as a planar for a set of waypoints presented by (x,y) coordinates.
 * The TSPSolver uses a <code>Route</code> and the <code>AddableDouble</code> classes as node and
 * cost instances in the template initialization.
 *
 */
public class TSPSolver implements NodeGenerator<Route>, CostFunction<Route,AddableDouble>
{
	private HashMap<String, Waypoint> waypoints = new HashMap<String, Waypoint>();

	/**
	 * Adds a waypoint defined by name and a pair of x- and y-coordinates
	 * 
	 * @param name name of waypoint
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	public void addWaypoint(String name, double x, double y) {
		waypoints.put(name, new Waypoint(x, y));
	}
	
	public Waypoint getWaypoint(String name) {
		return waypoints.get(name);
	}
	
	/**
	 * Returns a set of names of all waypoints
	 * 
	 * @return a set of waypoints' names
	 */
	public Set<String> getWaypoints() {
		return waypoints.keySet();
	}
	
	/**
	 * Node expansion algorithm. It iterates over all not yet visited waypoints and adds them
	 * to a resulted list if they do not have any crosses with lags provided by already visited
	 * waypoints.
	 * 
	 */
	public List<Route> expand(Route r) {
		List<Route> result = new LinkedList<Route>();
		
		String fromName = r.getLast();
		Waypoint w11 = waypoints.get(fromName);
		
		Iterator<String> i = r.getNotVisited().iterator();
		do {
			String toName = i.hasNext() ? i.next() : r.getFirst();
			Waypoint w12 = waypoints.get(toName);
			
			double dx1 = w12.x - w11.x;
			double dy1 = w12.y - w11.y;
			
			boolean crossed = false;
			
			if(r.getVisitedNumber() > 2) {
				ListIterator<String> j = r.getVisited().listIterator(r.getVisitedNumber() - 1);
				
				String start = j.previous();
				while(j.hasPrevious()) {
					String finish = j.previous();
					
					if(toName.equals(finish)) {
						continue;
					}
					
					Waypoint w21 = waypoints.get(start);
					Waypoint w22 = waypoints.get(finish);
					
					double dx2 = w22.x - w21.x;
					double dy2 = w22.y - w21.y;
					
					double z = dy1 * dx2 - dy2 * dx1;
					
					if(Math.abs(z) > 0.01) {
						double x = (w21.y * dx1 * dx2 - w11.y * dx1 * dx2 + w11.x * dy1 * dx2 - w21.x * dy2 * dx1) / z;
						
						boolean contains1 = (x - w11.x) * (x - w12.x) <= 0;
						boolean contains2 = (x - w21.x) * (x - w22.x) <= 0;
						
						if(contains1 && contains2) {
							crossed = true;
							break;
						}
					}
					
					finish = start;
				}
			}
				
			if( !crossed ) {
				LinkedList<String> visited = new LinkedList<String>(r.getVisited());
				LinkedList<String> notvisited = new LinkedList<String>(r.getNotVisited());
				
				Route route = new Route(visited, notvisited);
				route.addVisited(toName);
					
				result.add(route);
			}
		} while(i.hasNext());
				
		return result;
	}
	
	/**
	 * The route is complete if the initial and final waypoints are the same.
	 */
	public boolean isGoal(Route t) {
		return (t.getVisitedNumber() > 1) && t.getFirst().equalsIgnoreCase(t.getLast());
	}

	/**
	 * Calculates transition cost between two waypoints.
	 */
	public AddableDouble getTransitionCost(Route from, Route to) {
		Waypoint w1 = waypoints.get(from.getLast());
		Waypoint w2 = waypoints.get(to.getLast());
		
		double dx = w2.x - w1.x;
		double dy = w2.y - w1.y;
		
		return new AddableDouble(Math.sqrt(dx * dx + dy * dy));
	}
	
	public class Waypoint {
		double x, y;
		
		Waypoint(double x, double y) {
			this.x = x;
			this.y = y;
		}
	}
	
	/**
	 * TSP test based on DepthFirstCostTreeTraversal <code>searchForward</code>, <code>branchAndBound</code>
	 * and <code>deepenIteratively</code> algorithms 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		TSPSolver solver = new TSPSolver();
		
		solver.addWaypoint("a", 0.3, 0.9);
		solver.addWaypoint("b", 1.0, -0.2);
		solver.addWaypoint("c", 1.1, 1.1);
		solver.addWaypoint("d", 0.1, 0.2);
		solver.addWaypoint("e", 1.5, 0.5);
		solver.addWaypoint("f", 0.5, 0.5);
		solver.addWaypoint("g", -0.8, 0.1);
		solver.addWaypoint("i", -0.8, 0.8);
		
		System.out.println("=====Traveling Salesman Problem. Waypoints.=====");
		Iterator<String> waypointsIterator = solver.getWaypoints().iterator();
		int id = 0;
		while(waypointsIterator.hasNext()) {
			String waypointName = waypointsIterator.next();
			Waypoint waypoint = solver.getWaypoint(waypointName);
			System.out.println(waypointName + ": x = " + waypoint.x + "; y = " + waypoint.y + ".");
		}

		// Depth-first search tree traversal using TSPSolver class
		// both for NodeGenerator and CostFunction implementations
		DepthFirstCostTreeTraversal<Route,AddableDouble> tr =
			new DepthFirstCostTreeTraversal<Route,AddableDouble>(
				new Route(solver.getWaypoints()), new AddableDouble(0),
				solver, solver);

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
				new Route(solver.getWaypoints()), new AddableDouble(0),
				solver, solver);

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
				solver, solver);

		// Find an optimal(minimal cost) solution with iterative deepening technique
		Enumeration<Route> path3 = Iterative.deepenIteratively(tr3);
		System.out.print("The optimal path found with iterative deepening: ");
		while(path3.hasMoreElements()) {
			Route r = path3.nextElement();
			String toPrint = (path3.hasMoreElements()) ? r.toString() + "->" : r.toString();
			System.out.print(toPrint);
		}
		System.out.println();
		
	}

}