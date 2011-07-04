package org.igsl.app.knighttour;

import java.util.LinkedList;
import java.util.List;

import org.igsl.functor.NodeGenerator;

/**
 * Knight Tour solver presented to demonstrate constraint-satisfaction techniques.
 * The class uses a <code>Position</code> class as node instance in template initialization.
 */
public class KnightTourSolver implements NodeGenerator<Position> {
	
	/**
	 * Constructor for Knight Tour solver
	 */
	public KnightTourSolver(int dim) {
		this.dim = dim;
	}
	
	/**
	 * Node expansion algorithm. It uses <code>isValid</code> method of <code>Position</code> to check
	 * if a knight move is admissible for a given cell.
	 */
	public List<Position> expand(Position position) {
		List<Position> result = new LinkedList<Position>();
		
		int[] deltas = {-2, -1, 1, 2};
		
		for(int di : deltas) {
			for(int dj : deltas){
				if(Math.abs(di) + Math.abs(dj) == 3){
					int i = position.getI() + di;
					int j = position.getJ() + dj;
					
					if(i < dim && i > -1 && j < dim && j > -1 && position.isFree(i, j)) {
						result.add(new Position(i, j, position));
					}
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Terminal condition - table is filled.
	 */
	public boolean isGoal(Position position) {
		return position.getSize() == (dim * dim) ;
	}
	
	private int dim; // problem dim

}
