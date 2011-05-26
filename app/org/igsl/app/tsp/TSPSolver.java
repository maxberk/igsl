/**
 * Implicit Graph Search Library(C), 2009, 2010, 2011 
 */
package org.igsl.app.tsp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.PriorityQueue;
import java.util.Set;

import org.igsl.cost.AddableDouble;
import org.igsl.functor.HeuristicFunction;
import org.igsl.functor.memoize.Memoize;

/**
 * NodeGenerator and CostFunction implementations for Traveling Salesman Problem.
 * The problem is formulated as a planar for a set of waypoints presented by (x,y) coordinates.
 * The TSPSolver uses a <code>Route</code> and the <code>AddableDouble</code> classes as node and
 * cost instances in the template initialization.
 *
 */
public class TSPSolver implements HeuristicFunction<Route,AddableDouble>
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
	@Memoize()
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
	
	/**
	 * Heuristics based on a 1-tree estimation.
	 * 
	 * @param t node in a search tree
	 * @return cost estimated value for a cost
	 */
	@Memoize
	public AddableDouble getEstimatedCost(Route t) {
		PriorityQueue<Edge> q = new PriorityQueue<Edge>();

		String sName = t.getFirst();
		Waypoint s = waypoints.get(sName);
		Iterator<String> i1 = t.getNotVisited().iterator();
		while(i1.hasNext()) {
			String wName = i1.next();
			Waypoint w = waypoints.get(wName);
			
			double dx = w.x - s.x;
			double dy = w.y - s.y;
			
			q.offer(new Edge(sName, wName, Math.sqrt(dx * dx + dy * dy)));
		}
		
		double delta = 0.0;
		if(t.getVisitedNumber() > 1) {
			String fName = t.getLast();
			Waypoint f = waypoints.get(fName);
			
			double dx1 = f.x - s.x;
			double dy1 = f.y - s.y;
			delta = Math.sqrt(dx1 * dx1 + dy1 * dy1);
			
			Iterator<String> i2 = t.getNotVisited().iterator();
			while(i2.hasNext()) {
				String wName = i2.next();
				Waypoint w = waypoints.get(wName);
				
				double dx = w.x - f.x;
				double dy = w.y - f.y;
				
				q.offer(new Edge(fName, wName, Math.sqrt(dx * dx + dy * dy)));
			}
			
			double dx = s.x - f.x;
			double dy = s.y - f.y;

			q.offer(new Edge(sName, fName, Math.sqrt(dx * dx + dy * dy)));
		}

		int idx = 0;
		Iterator<String> i3 = t.getNotVisited().iterator();
		while(i3.hasNext()) {
			String w1Name = i3.next();
			Waypoint w1 = waypoints.get(w1Name);
			
			ListIterator<String> i4 = t.getNotVisited().listIterator(++idx);
			while(i4.hasNext()) {
				String w2Name = i4.next();
				Waypoint w2 = waypoints.get(w2Name);

				double dx = w1.x - w2.x;
				double dy = w1.y - w2.y;
					
				q.offer(new Edge(w1Name, w2Name, Math.sqrt(dx * dx + dy * dy)));
			}
		}
		
		if(q.isEmpty()) {
			return new AddableDouble(0);
		} else {
			// code extracting a vertex with a minimum cost in a tree
			// two of edge cost values accumulated in the result
			Edge e = q.poll();

			String eName = e.name1;
			double result = e.length;
			
			boolean nextMinLengthFound = false;
			double nextMinLength = 100;
			ArrayList<Edge> removed = new ArrayList<Edge>();
			
			Iterator<Edge> i = q.iterator();
			while(i.hasNext()) {
				e = i.next();
				
				if(e.name1.equalsIgnoreCase(eName) || e.name2.equalsIgnoreCase(eName)) {
					if(e.length < nextMinLength) {
						nextMinLengthFound = true;
						nextMinLength = e.length;
					}
					
					removed.add(e);
				}
			}
			
			q.removeAll(removed);
			
			if(nextMinLengthFound) {
				result += nextMinLength;
			}
			//...
			
			if(!q.isEmpty()) {
				e = q.poll();
				HashSet<String> used = new HashSet<String>();
				used.add(e.name1);
				used.add(e.name2);
				result += e.length;
	
				int numOfWaypoints = t.getNotVisited().size() + (t.getVisitedNumber() > 1 ? 2 : 1) - 1;
				while(used.size() < numOfWaypoints) {
					e = q.poll();
					
					if(used.contains(e.name1) && used.contains(e.name2)) {
						continue;
					} 
					
					if(!used.contains(e.name1) && !used.contains(e.name2)) {
						continue;
					}
	
					if(used.contains(e.name1)) {
						used.add(e.name2);
					} else {
						used.add(e.name1);
					}
					
					result += e.length;
				}
			}
			
			return new AddableDouble(result - delta);
		}
	}
	
	public class Waypoint {
		public double x, y;
		
		Waypoint(double x, double y) {
			this.x = x;
			this.y = y;
		}
	}
	
	class Edge implements Comparable<Edge> {
		String name1, name2;
		double length;
		
		Edge(String name1, String name2, double length) {
			this.name1 = name1;
			this.name2 = name2;
			this.length = length;
		}
		
		public int compareTo(Edge other) {
			if(length > other.length) {
				return 1;
			} else if(length < other.length) {
				return -1;
			} else {
				return 0;
			}
		}
	}

}