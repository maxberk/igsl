/**
 * Implicit Graph Search Library(C), 2009, 2010, 2011 
 */

package org.igsl.app.eightqueens;

import java.util.LinkedList;
import java.util.List;

import org.igsl.functor.NodeGenerator;
import org.igsl.functor.exception.DefaultValuesUnsupportedException;

/**
 * Eight Queens problem solver presented to demonstrate constraint-satisfaction techniques. The problem
 * is formulated as follows: eight queens should be placed on a chess board with a condition not to attack
 * each other. The class uses a <code>Board</code> class as node instance in template initialization.
 */
public class EQPSolver implements NodeGenerator<Board> {

	/**
	 * Node expansion algorithm. It uses <code>isValid</code> method of <code>Board</code> to check
	 * if a new position for a queen is admissable.
	 */
	public List<Board> expand(Board board) {
		List<Board> result = new LinkedList<Board>();
		int i = board.getLevel() + 1;
		
		for(int j = 1; j <= 8; ++j) {
			if(board.isValid(i, j)) {
				result.add(new Board(i, j, board));
			}
		}
		
		return result;
	}

	/**
	 * Terminal condition - board level attribute is 8.
	 */
	public boolean isGoal(Board board) {
		return (board.getLevel() == 8);
	}
	
	/**
	 * Defines default root node value
	 * @throws DefaultValuesUnsupportedException is not thrown here
	 * @return default start node value
	 */
	public Board getDefaultRootNode() throws DefaultValuesUnsupportedException {
		return new Board();
	}
	
}
