package org.igsl.app.eightknights;

public class Board {
	int i, j;
	int level;
	Board parent;
	
	public Board() {
		this.i = 0;
		this.j = 0;
		this.level = 0;
		this.parent = null;
	}

	public Board(int i, int j, Board parent) {
		this.i = i;
		this.j = j;
		this.level = parent.level + 1;
		this.parent = parent;
	}
	
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
	
	public int getLevel() {
		return level;
	}
	
	public String toString() {
		return "(" + i + "," + j + ")";
	}
	
}
