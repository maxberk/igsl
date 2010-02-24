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
	public SudokuSolver(int dim) {
		this.dim = dim;
	}

	/**
	 * Node expansion algorithm. It uses <code>isValid</code> method of <code>Table</code> to check
	 * if a number is admissable for a given cell.
	 */
	public List<Table> expand(Table table) {
		List<Table> result = new LinkedList<Table>();
		
		int i = table.getI();
		int j = table.getJ();
		
		if(++j == dim * dim + 1) {
			if(++i == dim * dim + 1) return result;
			j = 1;
		}
		
		for(int n = 1; n <= (dim * dim); ++n) {
			if(table.isValid(i, j, n, dim)) {
				result.add(new Table(i, j, n, table));
			}
		}
		
		++expansions;
		
		if((expansions % 10) == 0) {
			System.out.println("expansions = " + expansions);
		}
		
		return result;
	}

	/**
	 * Terminal condition - table is filled.
	 */
	public boolean isGoal(Table table) {
		return (table.getI() == table.getJ()) && (table.getI() == (dim * dim)) ;
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
	
	private int dim; // problem dim
	private long expansions = 0;
	
	/**
	 * Sudoku test scenarios based on DepthFirstTreeTraversal. First scenario demonstrates
	 * how to find all solutions with <code>searchForward</code> method. 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SudokuSolver solver = new SudokuSolver(2);
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