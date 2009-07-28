package org.igsl.app.fifteens;

public class Position {
	
	private int[][] tiles;
	private int i0, j0;
	private Position parent;
	
	public Position(int[][] tiles) {
		this.parent = null;
		this.tiles = tiles;
		
		boolean found = false;
		
		for(int i = 0; i < 4; ++i) {
			for(int j = 0; j < 4; ++j) {
				if(tiles[i][j] == 0) {
					i0 = i;
					j0 = j;
					found = true;
					break;
				}
				
				if(found) break;
			}
		}
	}
	
	private Position(Position parent, int i0, int j0) {
		this.parent = parent;
		this.tiles = new int[4][4];

		for(int i = 0; i < 4; ++i) {
			for(int j = 0; j < 4; ++j) {
				tiles[i][j] = parent.tiles[i][j];
			}
		}
		
		this.i0 = i0;
		this.j0 = j0;
		
		tiles[parent.i0][parent.j0] = tiles[this.i0][this.j0];
		tiles[this.i0][this.j0] = 0;
	}
	
	public Position moveTileUp() {
		if((i0 > 0) && ((parent == null) || (parent.i0 != i0 - 1))) {
			return new Position(this, i0 - 1, j0);
		} else {
			return null;
		}
	}
	
	public Position moveTileDown() {
		if((i0 < 3) && ((parent == null) || (parent.i0 != i0 + 1))) {
			return new Position(this, i0 + 1, j0);
		} else {
			return null;
		}
	}
	
	public Position moveTileLeft() {
		if((j0 > 0) && ((parent == null) || (parent.j0 != j0 - 1))) {
			return new Position(this, i0, j0 - 1);
		} else {
			return null;
		}
	}
	
	public Position moveTileRight() {
		if((j0 < 3) && ((parent == null) || (parent.j0 != j0 + 1))) {
			return new Position(this, i0, j0 + 1);
		} else {
			return null;
		}
	}
	
	public boolean isTerminal(Position p) {
		for(int i = 0; i < 4; ++i) {
			for(int j = 0; j < 4; ++j) {
				if(tiles[i][j] != p.tiles[i][j]) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public int manhattanDistance(Position p) {
		int result = 0;
		
		for(int i = 0; i < 4; ++i) {
			for(int j = 0; j < 4; ++j) {
				boolean found = false;
				
				for(int i1 = 0; i1 < 4; ++i1) {
					for(int j1 = 0; j1 < 4; ++j1) {
						if(p.tiles[i][j] == tiles[i1][j1]) {
							result += Math.abs(i1 - i) + Math.abs(j1 - j);
							found = true;
							break;
						}
					}
					
					if(found) break;
				}
			}
		}
		
		return result;
	}
	
	public String toString() {
		return "(" + i0 + "," + j0 + ")";
	}

}