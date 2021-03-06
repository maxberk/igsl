/**
 * Implicit Graph Search Library(C), 2009, 2013 
 */

package org.igsl.app.eightqueens;

/**
*
*/
public class Queen {
	int i, j;
	
	/**
	 * 
	 */
	public Queen(int i) {
		this.i = i;
		this.j = 1;
	}
	
	/**
	 * 
	 */
	public boolean canBeat(Queen q) {
		return (j == q.j) || (Math.abs(i - q.i) == Math.abs(j - q.j));
	}
	
	public int getI() {
		return i;
	}
	
	public int getJ() {
		return j;
	}

	public void firstPosition() {
		j = 1;
	}
	
	public Queen nextPosition() {
		++j;
		return this;
	}
	
	/**
	 * Returns vertical and horizontal indices of a knight 
	 */
	public String toString() {
		return j + "";
	}
	
}