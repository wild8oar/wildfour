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
	
	int[][] collectedField;
	
	private PlayField (int[][] field) {
		this.field = field;
		this.collectedField = null;
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
	
	private static final int ME_1OPEN_2 = 0;
	private static final int ME_2OPEN_2 = 1;
	private static final int ME_1OPEN_3 = 2;
	private static final int ME_2OPEN_3 = 3;
	private static final int OTHER_1OPEN_2 = 4;
	private static final int OTHER_2OPEN_2 = 5;
	private static final int OTHER_1OPEN_3 = 6;
	private static final int OTHER_2OPEN_3 = 7;
	
	
	public double[] encodeFeaturesAsNetworkInput() {
		// (my,other) x (1open,2open) x (2,3) = 8
		double[] input = new double[8];
		for (int[] row: collectAll()) {
			int previous = BORDER;
			int current = row[0];
			int len = 1;
			for (int i=1; i<=row.length; i++) {
				int disc = i == row.length ? BORDER: row[i];
				if (disc == current) {
					len++;
				} else {
					int index = getIndex(current, len, previous, i >= row.length-1 ? BORDER: row[i+1]);
					if (index > -1) {
						input[index]++;
					}
					previous = current;
					current = disc;
					len = 1;
				}
			}
		}
		// normalize (max=5)
		for (int i=0; i<input.length; i++) {
			input[i] /= 5;
			if (input[i] > 1.0) {
				input[i] = 1.0;
			}
		}
		return input;
	}
	
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

	private boolean containsWin (int[][] collected, int player) {
		for (int x=0; x<collected.length; x++) {
			int n = 0;
			for (int y=0; y<collected[x].length; y++) {
				int disc = collected[x][y];
				if (disc == player) {
					n++;
					if (n==4) {
						return true;
					}
				} else {
					n = 0;
				}
			}
		}
		return false;
	}
	
	private int[][] collectVertical () {
		return field;
	}
	
	private int[][] collectHorizontal () {
		int[][] collected = new int[HEIGHT][WIDTH];
		for (int y=0; y<HEIGHT; y++) {
			for (int x=0; x<WIDTH; x++) {
				collected[y][x] = field[x][y];
			}
		}
		return collected;
	}

	private int[][] collectToTopRight () {
		int[][] collected = new int[6][];
		int n = 0;
		// from left to top right
		for (int y=5; y>2; y--) {
			int[] diag = new int[y+1];
			for (int x=0; x<y+1; x++) {
				diag[x] = field[x][y-x];
			}
			collected[n++] = diag;
		}
		// from bottom to top right
		for (int x=1; x<4; x++) {
			int[] diag = new int[7-x];
			for (int y=5; y>=x-1; y--) {
				diag[5-y] = field[x-y+5][y];
			}
			collected[n++] = diag;
		}
		return collected;
	}
	
	private int[][] collectToTopLeft () {
		int[][] collected = new int[6][];
		int n = 0;
		// from right to top left
		for (int y=5; y>2; y--) {
			int[] diag = new int[y+1];
			for (int x=6; x>5-y; x--) {
				diag[6-x] = field[x][x+y-6];
			}
			collected[n++] = diag;
		}
		// from bottom to top left
		for (int x=5; x>2; x--) {
			int[] diag = new int[x+1];
			for (int y=5; y>=5-x; y--) {
				diag[5-y] = field[x+y-5][y];
			}
			collected[n++] = diag;
		}
		return collected;
	}
	
	int[][] collectAll () {
		if (collectedField != null) {
			return collectedField;
		}
		collectedField = new int[25][];
		int n=0;
		for (int[] row: collectHorizontal()) {
			collectedField[n++] = row;
		}
		for (int[] row: collectVertical()) {
			collectedField[n++] = row;
		}
		for (int[] row: collectToTopRight()) {
			collectedField[n++] = row;
		}
		for (int[] row: collectToTopLeft()) {
			collectedField[n++] = row;
		}
		return collectedField;
	}

}
