package wildfour;

import bot.Field;

public class PlayField { 
	
	public static final int EMPTY = 0;
	public static final int ME = 1;
	public static final int OTHER = 2;
	
	public static final int WIDTH = 7;
	public static final int HEIGHT = 6;
		
	private int[][] field;
	
	private PlayField (int[][] field) {
		this.field = field;
	}
	
	/**
	 * Creates a normalized copy of the play field, where my bot 
	 * always has ID 1.
	 */
	public static PlayField fromBotField (Field botfield, int myId) {
		final int otherId = 3-myId;
		int[][] grid = new int[WIDTH][HEIGHT];
		for (int x=0; x<WIDTH; x++) {
			for (int y=0; y<HEIGHT; y++) {
				int disc = botfield.getDisc(x, y);
				if (disc == myId) {
					grid[x][y] = ME;
				} else if (disc == otherId) {
					grid[x][y] = OTHER;
				}
			}
		}
		return new PlayField(grid);
	}
	
	public static PlayField emptyField () {
		int[][] grid = new int[WIDTH][HEIGHT];
		return new PlayField(grid);
	}
	
	public boolean addDisc(int column, int disc) {
		if (column > WIDTH) {
			throw new IllegalStateException("Illegal column: " + column);
		}
		for (int y = HEIGHT-1; y >= 0; y--) { // From bottom up
			if (field[column][y] == 0) {
				field[column][y] = disc;
				return true;
			}
		}
		return false;
	}
	
	public boolean removeDisc(int column) {
		if (column > WIDTH) {
			throw new IllegalStateException("Illegal column: " + column);
		}
		for (int y = 0; y < HEIGHT; y++) { // From top down
			if (field[column][y] !=EMPTY) {
				field[column][y] = EMPTY;
				return true;
			}
		}
		return false;
	}
	
	public int getDisc(int column, int row) {
		return field[column][row];
	}
	
	public void print () {
		for (int y=0; y<HEIGHT; y++ ) {
			for (int x=0; x<WIDTH; x++) {
				int disc = field[x][y];
				if (disc == EMPTY) {
					System.err.print(" ");
				} else {
					System.err.print(disc);
				}
			}
			System.err.println();
		}
	}
	
	public double[] encodeAsNetworkInput() {
		double[] input = new double[84];
		int c = 0;
		for (int y=0; y<HEIGHT; y++ ) {
			for (int x=0; x<WIDTH; x++) {
				int disc = field[x][y];
				if (disc == ME) {
					input[c] = 1;
				} else if (disc == OTHER) {
					input[c+1] = 1;
				}
				c += 2;
			}
		}
		return input;
	}
	
	public PlayField getInverted () {
		int[][] grid = new int[WIDTH][HEIGHT];
		for (int x=0; x<WIDTH; x++) {
			for (int y=0; y<HEIGHT; y++) {
				int disc = field[x][y];
				if (disc == OTHER) {
					grid[x][y] = ME;
				} else if (disc == ME) {
					grid[x][y] = OTHER;
				}
			}
		}
		return new PlayField(grid);
	}
	
	/**
	 * @return if player 1 has 4 in a row
	 */
	public boolean haveIWon () {
		// vertical
		for (int x=0; x<WIDTH; x++) {
			int n = 0;
			for (int y=0; y<HEIGHT; y++) {
				int disc = field[x][y];
					if (disc == ME) {
						n++;
						if (n==4) {
							return true;
						}
					} else {
						n = 0;
					}
			}
		}
		// TODO: vertical, diagonal
		return false;
	}

}
