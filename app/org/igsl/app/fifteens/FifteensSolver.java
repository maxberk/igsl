/**
 * Implicit Graph Search Library(C), 2009, 2010, 2011
 */

package org.igsl.app.fifteens;

import java.util.LinkedList;
import java.util.List;

import org.igsl.cost.AddableInteger;
import org.igsl.functor.HeuristicFunction;
import org.igsl.functor.exception.DefaultValuesUnsupportedException;

/**
 * Fifteens Puzzle solver is based <code>AStarTreeTraversal</code> library class which utilizes an A* algorithm.
 * The solver implements <code>HeuristicFunction</code> for Fifteens Puzzle. The solver uses a <code>Position</code>
 * and the <code>AddableInteger</code> classes as node and cost instances in the template initialization.
 */
public class FifteensSolver implements HeuristicFunction<Position,AddableInteger> {
	
	private Position terminal;

	/**
	 * Constructor with a terminal position as parameter
	 *  
	 * @param terminal terminal position
	 */
	public FifteensSolver(Position terminal) {
		this.terminal = terminal;
	}
	
	/**
	 * Node expansion algorithm. According to <code>Position</code> moveTileXXX methods it could generate
	 * up to 4 possible successors. It does not allow to create a position similar to a <code>Position</code>
	 * parent thus preserving A* from primitive node duplication. 
	 * 
	 * @see Position
	 */
	public List<Position> expand(Position position) {
		List<Position> result = new LinkedList<Position>();
		
		Position p = position.moveTileUp();
		if(p != null) result.add(p);
		
		p = position.moveTileDown();
		if(p != null) result.add(p);
		
		p = position.moveTileLeft();
		if(p != null) result.add(p);
		
		p = position.moveTileRight();
		if(p != null) result.add(p);
		
		return result;
	}
	
	/**
	 * Search is completed when a terminal position is reached
	 */
	public boolean isGoal(Position position) {
		return position.equals(terminal);
	}
	
	/**
	 * Defines root(start) node value with all waypoints set as not-visited
	 * @throws DefaultValuesUnsupportedException is thrown here
	 * @return default root value
	 */
	public Position getDefaultRootNode() throws DefaultValuesUnsupportedException {
		throw new DefaultValuesUnsupportedException();
	}
	
	/**
	 * Defines zero root(start) node cost 
	 * @throws DefaultValuesUnsupportedException is not thrown here
	 * @return default start node cost
	 */
	public AddableInteger getDefaultRootCost() throws DefaultValuesUnsupportedException {
		return new AddableInteger(0);
	}
	
	/**
	 * Transition cost is 1 as a cost for a single move 
	 */
	public AddableInteger getTransitionCost(Position from, Position to) {
		return new AddableInteger(1);
	}
	
	/**
	 * Heuristic cost is acquired by a Manhattan distance calculation from a given position
	 * to a terminal one. 
	 */
	public AddableInteger getEstimatedCost(Position p) {
		return new AddableInteger(p.manhattanDistance(terminal));
	}

}