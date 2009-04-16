package org.igsl.app.eightknights;

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import org.igsl.algorithm.Direct;
import org.igsl.algorithm.Iterative;
import org.igsl.functor.NodeGenerator;
import org.igsl.traversal.linear.DepthFirstTreeTraversal;

public class EKPSolver implements NodeGenerator<Board> {
	
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
	
	public boolean isGoal(Board board) {
		return (board.getLevel() == 8);
	}
	
	public static void main(String[] args) {
		EKPSolver solver = new EKPSolver();
		
		System.out.println("Eight Knights Problem");
		
		DepthFirstTreeTraversal<Board> tr = new DepthFirstTreeTraversal<Board>(new Board(), solver);
		Direct.searchForward(tr);
		System.out.print("An admissable path found is: ");
		Enumeration<Board> path = tr.getPath();
		while(path.hasMoreElements()) {
			Board r = path.nextElement();
			String toPrint = (path.hasMoreElements()) ? r.toString() + "->" : r.toString();
			System.out.print(toPrint);
		}
		System.out.println();
		
		tr.backtrack();
		
		Direct.searchForward(tr);
		System.out.print("Next solution found: ");
		path = tr.getPath();
		while(path.hasMoreElements()) {
			Board r = path.nextElement();
			String toPrint = (path.hasMoreElements()) ? r.toString() + "->" : r.toString();
			System.out.print(toPrint);
		}
		System.out.println();
		
		tr.backtrack();

		path = Iterative.deepenIteratively(tr);
		System.out.print("Solution found iteratively: ");
		while(path.hasMoreElements()) {
			Board r = path.nextElement();
			String toPrint = (path.hasMoreElements()) ? r.toString() + "->" : r.toString();
			System.out.print(toPrint);
		}
		System.out.println();
	}
	
}
