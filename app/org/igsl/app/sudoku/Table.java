package org.igsl.app.sudoku;

/**
 * Implicit Graph Search Library(C), 2010 
 */

/**
 *  Table of cells for Sudoku. Location of filled cells with values is based on a single cell location and
 *  a reference to table of cells already filled.
 */
public class Table {
	int i, j; // cell indices
	int value; // cell value
	Table parent; // parent cell
	
	/**
	 * Creates an empty Table.
	 */
	public Table(int i, int j, int value) {
		this.i = i;
		this.j = j;
		this.value = value;
		this.parent = null;
	}

	/**
	 * Creates Table from a given parent exemplar and a new cell
	 * 
	 * @param i vertical index of the cell
	 * @param j horizontal index of the cell
	 * @param parent reference to <code>Table</code> with cells 
	 */
	public Table(int i, int j, int value, Table parent) {
		this.i = i;
		this.j = j;
		this.value = value;
		this.parent = parent;
	}

	/**
	 * Checks if a cell is free
	 * 
	 * @param i vertical index
	 * @param j horizontal index
	 * @return true, if a cell is free, false - otherwise
	 */
	public boolean isFree(int i, int j) {
		if((this.i == i) && (this.j == j)) {
			return false;
		} else if(parent != null) {
			return parent.isFree(i, j);
		} else {
			return true;
		}
	}
	
	/**
	 * Checks if a cell could contain a given value
	 * 
	 * @param i vertical index
	 * @param j horizontal index
	 * @param value value to be placed
	 * @return true, if a value is valid, false - otherwise
	 */
	public boolean isValid(int i, int j, int value, int dim) {
		if((this.value == value) && ((this.i == i) || (this.j == j))) {
			return false;
		} else if((this.value == value) &&
			(((this.i - 1) / dim) == ((i - 1) / dim)) &&
			(((this.j - 1) / dim) == ((j - 1) / dim))) {
			return false;
		} else if(parent != null) {
			return parent.isValid(i, j, value, dim);
		} else {
			return true;
		}
	}
	
	/**
	 * Returns horizontal index
	 * 
	 * @return horizontal index
	 */
	public int getI() {
		return i;
	}

	/**
	 * Returns vertical index
	 * 
	 * @return vertical index
	 */
	public int getJ() {
		return j;
	}
	
	/**
	 * Returns vertical and horizontal indices along with a value in a cell 
	 * 
	 * @return contents of a cell
	 */
	public String toString() {
		if(parent != null) {
			return "(" + i + "," + j + "," + value + ")";
		} else {
			return "initial";
		}
	}
	
}
