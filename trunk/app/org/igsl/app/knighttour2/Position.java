package org.igsl.app.knighttour2;

/**
 * Implicit Graph Search Library(C), 2009, 2013 
 */

/**
 *  Position for Knight Tour.
 */
public class Position {
	int i, j; // cell indices
	
	public Position() {}
	
	public Position(int i, int j) {
		this.i = i;
		this.j = j;
	}
	
	/**
	 * Creates a start position.
	 */
	public void assign(int i, int j) {
		this.i = i;
		this.j = j;
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
	
	public boolean equals(Object o) {
		Position other = (Position) o;
		return (i == other.i && j == other.j);
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