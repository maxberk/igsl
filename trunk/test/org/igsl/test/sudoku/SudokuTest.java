/**
 * Implicit Graph Search Library(C), 2011 
 */

package org.igsl.test.sudoku;

import java.util.Enumeration;

import org.igsl.algorithm.Direct;
import org.igsl.app.sudoku.SudokuSolver;
import org.igsl.app.sudoku.Table;
import org.igsl.traversal.TreeTraversal.PathIterator;
import org.igsl.traversal.linear.DepthFirstTreeTraversal;

public class SudokuTest {
	
	/**
	 * Sudoku test scenarios based on DepthFirstTreeTraversal. First scenario demonstrates
	 * how to find all solutions with <code>searchForward</code> method. 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SudokuSolver solver = new SudokuSolver(3);
		/*
		Table table = solver.fillCell(1, 1, 5);
		table = solver.fillCell(1, 2, 3, table);
		table = solver.fillCell(1, 5, 7, table);
		
		table = solver.fillCell(2, 1, 6, table);
		table = solver.fillCell(2, 4, 1, table);
		table = solver.fillCell(2, 5, 9, table);
		table = solver.fillCell(2, 6, 5, table);
		
		table = solver.fillCell(3, 2, 9, table);
		table = solver.fillCell(3, 3, 8, table);
		table = solver.fillCell(3, 8, 6, table);
		
		table = solver.fillCell(4, 1, 8, table);
		table = solver.fillCell(4, 5, 6, table);
		table = solver.fillCell(4, 9, 3, table);
		
		table = solver.fillCell(5, 1, 4, table);
		table = solver.fillCell(5, 4, 8, table);
		table = solver.fillCell(5, 6, 3, table);
		table = solver.fillCell(5, 9, 1, table);
		
		table = solver.fillCell(6, 1, 7, table);
		table = solver.fillCell(6, 5, 2, table);
		table = solver.fillCell(6, 9, 6, table);
		
		table = solver.fillCell(7, 2, 6, table);
		table = solver.fillCell(7, 7, 2, table);
		table = solver.fillCell(7, 8, 8, table);
		
		table = solver.fillCell(8, 4, 4, table);
		table = solver.fillCell(8, 5, 1, table);
		table = solver.fillCell(8, 6, 9, table);
		table = solver.fillCell(8, 9, 5, table);
		
		table = solver.fillCell(9, 5, 8, table);
		table = solver.fillCell(9, 8, 7, table);
		table = solver.fillCell(9, 9, 9, table);
		*/
		
		Table table = solver.fillCell(1, 5, 8);
	
		table = solver.fillCell(2, 3, 4, table);
		table = solver.fillCell(2, 4, 9, table);
		table = solver.fillCell(2, 6, 5, table);
		table = solver.fillCell(2, 7, 3, table);
		
		table = solver.fillCell(3, 2, 6, table);
		table = solver.fillCell(3, 4, 3, table);
		table = solver.fillCell(3, 6, 7, table);
		table = solver.fillCell(3, 8, 2, table);
		
		table = solver.fillCell(4, 2, 4, table);
		table = solver.fillCell(4, 3, 1, table);
		table = solver.fillCell(4, 7, 2, table);
		table = solver.fillCell(4, 8, 9, table);
		
		table = solver.fillCell(5, 1, 9, table);
		table = solver.fillCell(5, 9, 7, table);
		
		table = solver.fillCell(6, 2, 7, table);
		table = solver.fillCell(6, 3, 2, table);
		table = solver.fillCell(6, 7, 4, table);
		table = solver.fillCell(6, 8, 6, table);
		
		table = solver.fillCell(7, 2, 5, table);
		table = solver.fillCell(7, 4, 2, table);
		table = solver.fillCell(7, 6, 9, table);
		table = solver.fillCell(7, 8, 1, table);
		
		table = solver.fillCell(8, 3, 9, table);
		table = solver.fillCell(8, 4, 1, table);
		table = solver.fillCell(8, 6, 4, table);
		table = solver.fillCell(8, 7, 8, table);
		
		table = solver.fillCell(9, 5, 7, table);
		
		System.out.println("=====Sudoku. Direct search.=====");
		
		DepthFirstTreeTraversal<Table> tr = new DepthFirstTreeTraversal<Table>(table, solver);
		
		Direct.searchForward(tr);
		if(!tr.isEmpty()) {
			PathIterator<Table> path = tr.getPath();
			while(path.hasPreviousNode()) {
				Table r = path.previousNode();
				String toPrint = (path.hasPreviousNode()) ? r.toString() + "->" : r.toFullString();
				System.out.print(toPrint);
			}
			System.out.println();
		}
	}

}
