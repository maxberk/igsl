/**
 * Implicit Graph Search Library(C), 2009 
 */
package org.igsl.app.tsp;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Inspected route for Traveling Salesman Problem. Contains two lists of names
 * for visited and not yet visited waypoints.
 */
public class Route {
	List<String> visited = new LinkedList<String>();
	List<String> notvisited = new LinkedList<String>();
	
	/**
	 * Constructor for a node in the route of search tree.
	 * 
	 * Extracts the first name from a set of names of waypoints on the map
	 * and places it into a list of visited waypoints, other waypoints are
	 * placed into the list of not yet visited waypoints.
	 * 
	 * @param all a set of names of waypoints.
	 */
	public Route(Set<String> all) {
		Iterator<String> i = all.iterator();
		
		if(i.hasNext()) {
			visited.add(i.next());
			
			while(i.hasNext()) {
				notvisited.add(i.next());
			}
		}
	}
	
	/**
	 * Constructor for a regular node in the search tree.
	 * 
	 * @param visited a list of names of visited waypoints
	 * @param notvisited a list of names of not yet visited waypoints
	 */
	public Route(List<String> visited, List<String> notvisited) {
		this.visited = visited;
		this.notvisited = notvisited;
	}
	
	/**
	 * Accessor for a list of names of visited waypoints
	 * 
	 * @return a list of visited nodes
	 */
	public List<String> getVisited() {
		return visited;
	}
	
	/**
	 * Accessor for a list of names of not yet visited waypoints
	 * 
	 * @return a list of not yet visited waypoints 
	 */
	public List<String> getNotVisited() {
		return notvisited;
	}
	
	/**
	 * Returns a number of visited waypoints  
	 * 
	 * @return a number of visited waypoints
	 */
	public int getVisitedNumber() {
		return visited.size();
	}
	
	/**
	 * Returns a name of a first waypoint from a visited list
	 * 
	 * @return name of the first point in the route, null - if no waypoints 
	 */
	public String getFirst() {
		return visited.isEmpty() ? null : visited.get(0);
	}
	
	/**
	 * Returns a name of a last waypoint from a visited list
	 * 
	 * @return name of the last point in the route, null - if no waypoints 
	 */
	public String getLast() {
		return visited.isEmpty() ? null : visited.get(visited.size()-1);
	}
	
	/**
	 * Removes a waypoint from a list of not yet visited waypoints and adds it
	 * to a list of visited. An initial route point is added while it is not
	 * contained in the not yet visited waypoints' list.
	 * 
	 * @param waypoint waypoint name
	 */
	public void addVisited(String waypoint) {
		boolean isRemoved = notvisited.remove(waypoint);
		
		if(isRemoved) {
			visited.add(waypoint);
		} else if(visited.indexOf(waypoint) == 0) {
			visited.add(waypoint);
		}
	}
	
	/**
	 * Return a name of a last waypoint from a visited list.
	 */
	public String toString() {
		return getLast();
	}
	
}