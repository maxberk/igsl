package org.igsl.app.fifteens;

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import org.igsl.algorithm.Direct;
import org.igsl.functor.CostFunction;
import org.igsl.functor.HeuristicFunction;
import org.igsl.functor.NodeGenerator;
import org.igsl.traversal.exponential.AStarTreeTraversal;

public class FifteensSolver implements NodeGenerator<Position>,
	CostFunction<Position,AddableInteger>, HeuristicFunction<Position,AddableInteger> {
	
	public List<Position> expand(Position position) {
		List<Position> result = new LinkedList<Position>();
		
		if(position.canMoveUp()) {
			result.add(position.moveTileUp());
		}
		
		if(position.canMoveDown()) {
			result.add(position.moveTileDown());
		}
		
		if(position.canMoveLeft()) {
			result.add(position.moveTileLeft());
		}
		
		if(position.canMoveRight()) {
			result.add(position.moveTileRight());
		}
		
		return result;
	}
	
	public boolean isGoal(Position position) {
		return position.isTerminal();
	}
	
	public AddableInteger getTransitionCost(Position from, Position to) {
		return new AddableInteger(1);
	}
	
	public AddableInteger getEstimatedCost(Position p) {
		return new AddableInteger(p.manhattanDistance());
	}
	
	public static void main(String[] args) {
		FifteensSolver solver = new FifteensSolver();
		
		AStarTreeTraversal<Position, AddableInteger> tr =
			new AStarTreeTraversal<Position, AddableInteger>(
				new Position(new int[][] {{0, 1, 2, 3}, {4, 5, 6, 7}, {8, 9, 10, 11}, {12, 13, 14, 15}}),
				new AddableInteger(0),
				solver, solver, solver
			);
		
		Direct.searchForward(tr);
		Enumeration<Position> path = tr.getPath();
		
		while(path.hasMoreElements()) {
			Position p = path.nextElement();
			String toPrint = (path.hasMoreElements()) ? p.toString() + "->" : p.toString();
			System.out.print(toPrint);
		}
	}

}
