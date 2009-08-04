/**
 * Implicit Graph Search Library(C), 2009 
 */
package org.igsl.app.fifteens;

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import org.igsl.algorithm.Direct;
import org.igsl.functor.CostFunction;
import org.igsl.functor.HeuristicFunction;
import org.igsl.functor.NodeGenerator;
import org.igsl.traversal.exponential.AStarTreeTraversal;

/**
 * Fifteens Puzzle solver is based <code>AStarTreeTraversal</code> library class which utilizes an A* algorithm.
 * The solver implements <code>NodeGenerator</code>, <code>CostFunction</code> and <code>HeuristicFunction</code>
 * for Fifteens Puzzle. The solver uses a <code>Position</code> and the <code>AddableInteger</code> classes
 * as node and cost instances in the template initialization.
 *
 */
public class FifteensSolver implements NodeGenerator<Position>,
	CostFunction<Position,AddableInteger>, HeuristicFunction<Position,AddableInteger> {
	
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
	
	public static void main(String[] args) {
		int[][] terminal = new int[][] {{0, 1, 2, 3}, {4, 5, 6, 7}, {8, 9, 10, 11}, {12, 13, 14, 15}};
		int[][] initial = new int[][] {{0, 9, 2, 3}, {5, 4, 6, 7}, {12, 1, 10, 11}, {13, 8, 14, 15}};
		
		FifteensSolver solver = new FifteensSolver(new Position(terminal));
		
		System.out.println("=====Fifteen Puzzle Solver. Initial -> terminal positions.=====");
		for(int i = 0; i < 4; ++i) {
			for(int j1 = 0; j1 < 4; ++j1) {
				String prefix1 = initial[i][j1] < 10 ? "  " : " ";
				System.out.print(prefix1 + initial[i][j1]);
			}
			
			System.out.print(" -> ");
			
			for(int j2 = 0; j2 < 4; ++j2) {
				String prefix2 = initial[i][j2] < 10 ? "  " : " ";
				System.out.print(prefix2 + terminal[i][j2]);
			}
			
			System.out.println();
		}
		
		AStarTreeTraversal<Position, AddableInteger> tr =
			new AStarTreeTraversal<Position, AddableInteger>(
				new Position(initial),
				new AddableInteger(0),
				solver, solver, solver
			);
		
		Direct.searchForward(tr);
		Enumeration<Position> path = tr.getPath();

		System.out.print("Path found (from terminal to initial position): ");
		while(path.hasMoreElements()) {
			Position p = path.nextElement();
			String toPrint = (path.hasMoreElements()) ? p.toString() + "->" : p.toString();
			System.out.print(toPrint);
		}
	}

}
