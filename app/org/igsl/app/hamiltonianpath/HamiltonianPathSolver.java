/**
 * Implicit Graph Search Library(C), 2009, 2010, 2011, 2013
 */

package org.igsl.app.hamiltonianpath;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.igsl.cost.AddableDouble;
import org.igsl.functor.FiniteSetNodeGenerator;
import org.igsl.functor.BackwardPathIterator;
import org.igsl.functor.exception.DefaultValuesUnsupportedException;

/**
 * NodeGenerator and CostFunction implementations for Traveling Salesman Problem.
 * The problem is formulated as a planar for a set of waypoints presented by (x,y) coordinates.
 * The TSPSolver uses a <code>String</code> and the <code>AddableDouble</code> classes as node and
 * cost instances in the template initialization.
 *
 */
public class HamiltonianPathSolver implements FiniteSetNodeGenerator<String>
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
	
	/**
	 * !!!
	 */
	public String[] getAllValues() {
		String[] result = new String[waypoints.size()];	
		int i = 0;
	
		Set<String> set = waypoints.keySet();
		
		Iterator<String> iterator = set.iterator();
		while(iterator.hasNext()) {
			result[i++] = iterator.next();
		}
		
		return result;
	}
	
	/**
	 * Node expansion algorithm. It iterates over all not yet visited waypoints and adds them
	 * to a resulted list if they do not have any crosses with lags provided by already visited
	 * waypoints.
	 * 
	 */
	public boolean isValidTransition(String newName, BackwardPathIterator<String> iterator) {
		if(!iterator.hasPreviousNode()) {
			return true;
		}	
		String lastName = iterator.previousNode();
		
		if(!iterator.hasPreviousNode()) {
			return true;
		}	
		String startName = iterator.previousNode();

		Waypoint w11 = waypoints.get(newName);
		Waypoint w12 = waypoints.get(lastName);
		
		double dx1 = w12.x - w11.x;
		double dy1 = w12.y - w11.y;
		
		while(iterator.hasPreviousNode()) {
			Waypoint w21 = waypoints.get(startName);
			String finishName = iterator.previousNode();
			Waypoint w22 = waypoints.get(finishName);

			double dx2 = w22.x - w21.x;
			double dy2 = w22.y - w21.y;
			
			double z = dy1 * dx2 - dy2 * dx1;
			
			if(Math.abs(z) > 0.01) {
				double x = (w21.y * dx1 * dx2 - w11.y * dx1 * dx2 + w11.x * dy1 * dx2 - w21.x * dy2 * dx1) / z;
				
				boolean contains1 = (x - w11.x) * (x - w12.x) <= 0;
				boolean contains2 = (x - w21.x) * (x - w22.x) <= 0;
				
				if(contains1 && contains2) {
					return false;
				}
			}
			
			startName = finishName;		
		}
				
		return true;
	}
	
	/**
	 * Defines zero root(start) node cost 
	 * @throws DefaultValuesUnsupportedException is not thrown here
	 * @return default start node cost
	 */
	public AddableDouble getDefaultRootCost() throws DefaultValuesUnsupportedException {
		return new AddableDouble(0);
	}

	/**
	 * Calculates transition cost between two waypoints.
	 */
	public AddableDouble getTransitionCost(String from, String to) {
		Waypoint w1 = waypoints.get(from);
		Waypoint w2 = waypoints.get(to);
		
		double dx = w2.x - w1.x;
		double dy = w2.y - w1.y;
		
		return new AddableDouble(Math.sqrt(dx * dx + dy * dy));
	}
	
	class Waypoint {
		public double x, y;
		
		Waypoint(double x, double y) {
			this.x = x;
			this.y = y;
		}
	}
	
}