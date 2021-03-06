/**
 * Implicit Graph Search Library(C), 2011 
 */

package org.igsl.test.fifteens;

import java.util.Enumeration;

import org.igsl.algorithm.Direct;
import org.igsl.app.fifteens.FifteensSolver;
import org.igsl.app.fifteens.Position;
import org.igsl.cost.AddableInteger;
import org.igsl.functor.exception.DefaultValuesUnsupportedException;
import org.igsl.traversal.TreeTraversal.PathIterator;
import org.igsl.traversal.exponential.AStarTreeTraversal;

import static org.igsl.functor.memoize.Memoizer.*;

public class FifteensTest {
	
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
		
		try {
			AStarTreeTraversal<Position, AddableInteger> tr =
				new AStarTreeTraversal<Position, AddableInteger>(new Position(initial), memoize(solver));
			
			Direct.searchForward(tr);
			PathIterator<Position> path = tr.getPath();
	
			System.out.print("Path found (from terminal to initial position): ");
			while(path.hasPreviousNode()) {
				Position p = path.previousNode();
				String toPrint = (path.hasPreviousNode()) ? p.toString() + "->" : p.toString();
				System.out.print(toPrint);
			}
		} catch (DefaultValuesUnsupportedException e) {
		}
	}

}
