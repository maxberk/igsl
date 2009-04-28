package org.igsl.app.fifteens;

public class Position {
	
	private int[][] tiles;
	private int i0, j0;
	private Position parent;
	
	public Position(int[][] tiles) {
		this.parent = null;
		this.tiles = tiles;
		
		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 3; ++j) {
				if(tiles[i][j] == 0) {
					i0 = i;
					j0 = j;
				}
			}
		}
	}
	
	private Position(Position parent, int di, int dj) {
		this.parent = parent;
		this.tiles = new int[4][4];

		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 3; ++j) {
				tiles[i][j] = parent.tiles[i][j];
			}
		}
		
		this.i0 = parent.i0;
		this.j0 = parent.j0;
		
		tiles[i0][j0] = tiles[i0 + di][j0 + dj];
		tiles[i0 + di][j0 + dj] = 0;
		
		i0 = i0 + di;
		j0 = j0 + dj;
	}
	
	public boolean canMoveUp() {
		return (i0 > 0) && ((parent == null) || (parent.i0 != i0 - 1));
	}
	
	public Position moveTileUp() {
		return new Position(this, i0 - 1, j0);
	}
	
	public boolean canMoveDown() {
		return (i0 < 3) && ((parent == null) || (parent.i0 != i0 + 1));
	}
	
	public Position moveTileDown() {
		return new Position(this, i0 + 1, j0);
	}
	
	public boolean canMoveLeft() {
		return (j0 > 0) && ((parent == null) || (parent.j0 != j0 - 1));
	}
	
	public Position moveTileLeft() {
		return new Position(this, i0, j0 - 1);
	}
	
	public boolean canMoveRight() {
		return (j0 < 3) && ((parent == null) || (parent.j0 != j0 + 1));
	}
	
	public Position moveTileRight() {
		return new Position(this, i0, j0 + 1);
	}
	
	public boolean isTerminal() {
		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 3; ++j) {
				if(tiles[i][j] != (j + i * 4)) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public int manhattanDistance() {
		return 0;
	}
	
	public String toString() {
		return "(" + i0 + "," + j0 + ")";
	}

}
