package org.igsl.app.knighttour;

/**
 * Implicit Graph Search Library(C), 2011 
 */

/**
 *  Position for Knight Tour. Location of visited cells is based on a single cell location and
 *  a reference to position of cells already filled.
 */
public class Position {
	int i, j; // cell indices
	Position parent; // parent position
	int size; // number of filled cells
	
	/**
	 * Creates a start position.
	 */
	public Position(int i, int j) {
		this.i = i;
		this.j = j;
		this.parent = null;
		this.size = 1;
	}
	
	/**
	 * Creates a child for parent postion.
	 */
	public Position(int i, int j, Position parent) {
		this.i = i;
		this.j = j;
		this.parent = parent;
		this.size = parent.size + 1;
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
	 * Checks if a knight could be placed in a given position
	 * 
	 * @param i vertical index
	 * @param j horizontal index
	 * @return true, if it is possible, false - otherwise
	 */
	public boolean isValid(int i, int j) {
		if((this.i == i) && (this.j == j)) {
			return false;
		} else if(parent != null) {
			return parent.isValid(i, j);
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
	 * Returns number of filled cells
	 * 
	 * @return number of filled cells
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Returns vertical and horizontal indices 
	 * 
	 * @return contents of a position
	 */
	public String toString() {
		return "(" + i + "," + j + ")";
	}

}
