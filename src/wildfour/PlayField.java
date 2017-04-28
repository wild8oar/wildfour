package wildfour;

import bot.Field;

public class PlayField { 
	
	public static final int EMPTY = 0;
	public static final int ME = 1;
	public static final int OTHER = 2;
	public static final int BORDER = -1;
	
	public static final int WIDTH = 7;
	public static final int HEIGHT = 6;
		
	private int[][] field;
	
	int[] collectedField;
		
	private PlayField (int[][] field) {
		this.field = field;
		this.collectedField = null;
	}
	
	private static final int[][] COLLECTIBLES = new int[][] {
		// horizontal
		{0,0}, {1,0}, {2,0}, {3,0}, {4,0}, {5,0}, {6,0}, null,
		{0,1}, {1,1}, {2,1}, {3,1}, {4,1}, {5,1}, {6,1}, null,
		{0,2}, {1,2}, {2,2}, {3,2}, {4,2}, {5,2}, {6,2}, null,
		{0,3}, {1,3}, {2,3}, {3,3}, {4,3}, {5,3}, {6,3}, null,
		{0,4}, {1,4}, {2,4}, {3,4}, {4,4}, {5,4}, {6,4}, null,
		{0,5}, {1,5}, {2,5}, {3,5}, {4,5}, {5,5}, {6,5}, null,
		// vertical
		{0,0}, {0,1}, {0,2}, {0,3}, {0,4}, {0,5}, null,
		{1,0}, {1,1}, {1,2}, {1,3}, {1,4}, {1,5}, null,
		{2,0}, {2,1}, {2,2}, {2,3}, {2,4}, {2,5}, null,
		{3,0}, {3,1}, {3,2}, {3,3}, {3,4}, {3,5}, null,
		{4,0}, {4,1}, {4,2}, {4,3}, {4,4}, {4,5}, null,
		{5,0}, {5,1}, {5,2}, {5,3}, {5,4}, {5,5}, null,
		{6,0}, {6,1}, {6,2}, {6,3}, {6,4}, {6,5}, null,
		// bottom to top right
		{0,5}, {1,4}, {2,3}, {3,2}, {4,1}, {5,0}, null,
		{1,5}, {2,4}, {3,3}, {4,2}, {5,1}, {6,0}, null,
		{2,5}, {3,4}, {4,3}, {5,2}, {6,1}, null,
		{3,5}, {4,4}, {5,3}, {6,2}, null,
		// left to top right
		{0,4}, {1,3}, {2,2}, {3,1}, {4,0}, null,
		{0,3}, {1,2}, {2,1}, {3,0}, null,
		// bottom to top left
		{6,5}, {5,4}, {4,3}, {3,2}, {2,1}, {1,0}, null,
		{5,5}, {4,4}, {3,3}, {2,2}, {1,1}, {0,0}, null,
		{4,5}, {3,4}, {2,3}, {1,2}, {0,1}, null,
		{3,5}, {2,4}, {1,3}, {0,2}, null,
		// right to top left
		{6,4}, {5,3}, {4,2}, {3,1}, {2,0}, null,
		{6,3}, {5,2}, {4,1}, {3,0}
	};
	
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
		this.collectedField = null;
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
		this.collectedField = null;
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
	
	public double[] encodeFieldAsNetworkInput() {
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
	
	public double[] encodeFeaturesAsNetworkInput() {
		return null;
	}
//	public double[] encodeFeaturesAsNetworkInput() {
//		// (my,other) x (1open,2open) x (2,3) = 8
//		double[] input = new double[8];
//		for (int[] row: collectAll()) {
//			int previous = BORDER;
//			int current = row[0];
//			int len = 1;
//			for (int i=1; i<=row.length; i++) {
//				int disc = i == row.length ? BORDER: row[i];
//				if (disc == current) {
//					len++;
//				} else {
//					int index = getIndex(current, len, previous, i >= row.length-1 ? BORDER: row[i+1]);
//					if (index > -1) {
//						input[index]++;
//					}
//					previous = current;
//					current = disc;
//					len = 1;
//				}
//			}
//		}
//		// normalize (max=5)
//		for (int i=0; i<input.length; i++) {
//			input[i] /= 5;
//			if (input[i] > 1.0) {
//				input[i] = 1.0;
//			}
//		}
//		return input;
//	}
//	
	private int getIndex(int color, int len, int previous, int next) {
		//System.out.println("color=" + color + ", len=" + len + ", prev=" + previous + ", next=" + next);
		if (len < 2 || color == EMPTY || color == BORDER) {
			return -1;
		}
		int open = (previous == EMPTY ? 1:0) + (next == EMPTY ? 1:0);
		if (open == 0) {
			return -1;
		}
		int index = color == ME ? 0 : 4;
		index += len == 2 ? 0 : 2;
		index += open == 1 ? 0 : 1;
		//System.out.println(" ==> " + index);
		return index;
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
	public boolean hasPlayerWon (int player) {
		if (containsWin(collectAll(), player)) {
			return true;
		}
		return false;
	}

	private boolean containsWin (int[] collected, int player) {
		int n = 0;
		for (int i=0; i<collected.length; i++) {
			if (collected[i] == player) {
				n++;
				if (n==4) {
					return true;
				}
			} else {
				n = 0;
			}
		}
		return false;
	}
	
	private void collectCollectibles () {
		collectedField = new int[COLLECTIBLES.length];
		for (int i=0; i<COLLECTIBLES.length; i++) {
			if (COLLECTIBLES[i] == null) {
				collectedField[i] = PlayField.BORDER;
			} else {
				collectedField[i] = field[COLLECTIBLES[i][0]][COLLECTIBLES[i][1]];
			}
		}
	}
	
	int[] collectAll () {
		if (collectedField != null) {
			return collectedField;
		}
		collectCollectibles();
		return collectedField;
	}

}
