package org.igsl.app.sudoku;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.igsl.algorithm.Direct;
import org.igsl.functor.NodeGenerator;
import org.igsl.traversal.linear.DepthFirstTreeTraversal;

/**
 * Sudoku solver presented to demonstrate constraint-satisfaction techniques.
 * The class uses a <code>Table</code> class as node instance in template initialization.
 */
public class SudokuSolver implements NodeGenerator<Table> {
	
	/**
	 * Constructor for Sudoku solver
	 */
	public SudokuSolver() {
		this.size = 9;
	}

	/**
	 * Node expansion algorithm. It uses <code>isValid</code> method of <code>Table</code> to check
	 * if a number is admissable for a given cell.
	 */
	public List<Table> expand(Table table) {
		List<Table> result = new LinkedList<Table>();
		
		int i = table.getI();
		int j = table.getJ();
		
		if(++j == size + 1) {
			if(++i == size + 1) return result;
			j = 1;
		}
		
		for(int n = 1; n <= size; ++n) {
			if(table.isValid(i, j, n)) {
				result.add(new Table(i, j, n, table));
			}
		}
		
		return result;
	}

	/**
	 * Terminal condition - table is filled.
	 */
	public boolean isGoal(Table table) {
		return (table.getI() == table.getJ()) && (table.getI() == size) ;
	}
	
	/**
	 * Generate a cell in Sudoku table
	 * 
	 * @param i horizontal index of cell
	 * @param j vertical index of cell 
	 * @param value cell value
	 * 
	 * @return table
	 */
	public Table fillCell(int i, int j, int value) {
		return new Table(i, j, value);
	}
	
	private int size; // table size
	
	/**
	 * Sudoku test scenarios based on DepthFirstTreeTraversal. First scenario demonstrates
	 * how to find all solutions with <code>searchForward</code> method while the second utilizes
	 * iterative <code>deepenIteratively</code> techniques. 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SudokuSolver solver = new SudokuSolver();
		Table table = solver.fillCell(1, 1, 1);
		
		System.out.println("=====Sudoku. Direct search.=====");
		
		DepthFirstTreeTraversal<Table> tr = new DepthFirstTreeTraversal<Table>(table, solver);
		
		Direct.searchForward(tr);
		if(!tr.isEmpty()) {
			Enumeration<Table> path = tr.getPath();
			while(path.hasMoreElements()) {
				Table r = path.nextElement();
				String toPrint = (path.hasMoreElements()) ? r.toString() + "->" : r.toString();
				System.out.print(toPrint);
			}
			System.out.println();
		}
	}
	
}

