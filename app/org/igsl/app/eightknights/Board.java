/**
 * Implicit Graph Search Library(C), 2009, 2010 
 */

package org.igsl.app.eightknights;

/**
 *  Chess board for Eight Knights problem. Location of knights' is based on a single knight location and
 *  a reference to position of knights already placed on the board.
 */
public class Board {
	int i, j;
	int level;
	Board parent;
	
	/**
	 * Creates an empty board without knights.
	 */
	public Board() {
		this.i = 0;
		this.j = 0;
		this.level = 0;
		this.parent = null;
	}

	/**
	 * Creates a new position on a board
	 * 
	 * @param i vertical index on the board
	 * @param j horizontal index on the board
	 * @param parent reference to <code>Board</code> with knights already placed on the board 
	 */
	public Board(int i, int j, Board parent) {
		this.i = i;
		this.j = j;
		this.level = parent.level + 1;
		this.parent = parent;
	}
	
	/**
	 * Checks if a knight could be placed on a demanded position
	 * 
	 * @param i vertical index
	 * @param j horizontal index
	 * @return true, if other knights do not attack the demande position, false - otherwise
	 */
	public boolean isValid(int i, int j) {
		if((this.i == i) ||	(this.j == j) ||
			(Math.abs(this.i - i) == Math.abs(this.j - j))
		) {
			return false;
		} else if(parent != null) {
			return parent.isValid(i, j);
		} else {
			return true;
		}
	}
	
	/**
	 * Returns number of knights on a board
	 * 
	 * @return <code>int</code> value of knights
	 */
	public int getLevel() {
		return level;
	}
	
	/**
	 * Returns vertical and horizontal indices of a knight 
	 */
	public String toString() {
		return "(" + i + "," + j + ")";
	}
	
	/**
	 * Return hashCode
	 * 
	 * @return <code>int</code> hashCode
	 */
	public int hashCode() {
		int result = 31 * i + j;
		result = 31 * result + level;
		
		return result;
	}
	
}
