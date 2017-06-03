package wildfour;

import java.util.Arrays;

import bot.Field;

public final class PlayField { 
	
	public static final char EMPTY = ' ';
	public static final char ME = '1';
	public static final char OTHER = '2';
	
	private static final int ONE = 1;
	private static final int TWO = 10;
	private static final int FOUR1 = 4*ONE;
	private static final int FOUR2 = 4*TWO;
	private static final int THREE1 = 3*ONE;
	private static final int THREE2 = 3*TWO;
	
	public static final int WIDTH = 7;
	public static final int HEIGHT = 6;
	public static final int SIZE = WIDTH*HEIGHT;
		
	private final char[] field;
	private final int[] counts = new int[69];
	private final int[] top = new int[WIDTH];
	private char winner;
	
	private static final int[][] countmap = {{0, 24, 62}, {4, 24, 25, 64}, {8, 24, 25, 26, 65}, 
			{12, 24, 25, 26, 56}, {16, 25, 26, 54}, {20, 26, 45}, {0, 1, 27, 59}, 
			{4, 5, 27, 28, 61, 62}, {8, 9, 27, 28, 29, 56, 63, 64}, {12, 13, 27, 28, 29, 54, 55, 65}, 
			{16, 17, 28, 29, 45, 46}, {20, 21, 29, 48}, {0, 1, 2, 30, 67}, {4, 5, 6, 30, 31, 56, 58, 59}, 
			{8, 9, 10, 30, 31, 32, 54, 55, 60, 61, 62}, {12, 13, 14, 30, 31, 32, 45, 46, 47, 63, 64}, 
			{16, 17, 18, 31, 32, 48, 49, 65}, {20, 21, 22, 32, 51}, {0, 1, 2, 3, 33, 56, 68}, 
			{4, 5, 6, 7, 33, 34, 54, 55, 66, 67}, {8, 9, 10, 11, 33, 34, 35, 45, 46, 47, 57, 58, 59}, 
			{12, 13, 14, 15, 33, 34, 35, 48, 49, 50, 60, 61, 62}, {16, 17, 18, 19, 34, 35, 51, 52, 63, 64}, 
			{20, 21, 22, 23, 35, 53, 65}, {1, 2, 3, 36, 55}, {5, 6, 7, 36, 37, 46, 47, 68}, 
			{9, 10, 11, 36, 37, 38, 48, 49, 50, 66, 67}, {13, 14, 15, 36, 37, 38, 51, 52, 57, 58, 59}, 
			{17, 18, 19, 37, 38, 53, 60, 61}, {21, 22, 23, 38, 63}, {2, 3, 39, 47}, {6, 7, 39, 40, 49, 50}, 
			{10, 11, 39, 40, 41, 51, 52, 68}, {14, 15, 39, 40, 41, 53, 66, 67}, {18, 19, 40, 41, 57, 58}, 
			{22, 23, 41, 60}, {3, 42, 50}, {7, 42, 43, 52}, {11, 42, 43, 44, 53}, {15, 42, 43, 44, 68}, 
			{19, 43, 44, 66}, {23, 44, 57}};

	
	private PlayField (char[] field) {
		this.field = field;
		initialCollect();
		collectTops();
	}
	
	static final int idx (int x, int y) {
		return 6*x+y;
	}
		
	/**
	 * Creates a normalized copy of the play field, where my bot 
	 * always has ID 1.
	 */
	public static PlayField fromBotField (Field botfield, int myId) {
		final int otherId = 3 - myId;
		char[] grid = new char[SIZE];
		for (int x=0; x<WIDTH; x++) {
			for (int y=0; y<HEIGHT; y++) {
				int disc = botfield.getDisc(x, y);
				if (disc == myId) {
					grid[idx(x,y)] = ME;
				} else if (disc == otherId) {
					grid[idx(x,y)] = OTHER;
				} else {
					grid[idx(x,y)] = EMPTY;
				}
			}
		}
		return new PlayField(grid);
	}
	
	public static PlayField emptyField () {
		char[] grid = new char[SIZE];
		for (int i=0; i<SIZE; i++) {
			grid[i] = EMPTY;
		}
		return new PlayField(grid);
	}
	
	public boolean addDisc (int column, char disc) {
		if (column > WIDTH) {
			throw new IllegalStateException("Illegal column: " + column);
		}
		if (top[column] == 0) {
			return false;
		}
		top[column]--;
		setDisc(idx(column, top[column]), disc);
		return true;
	}
	
	private void collectTops () {
		for (int col=0; col<WIDTH; col++) {
			for (int y= HEIGHT-1; y >= 0; y--) { // From bottom up
				if (field[idx(col, y)] == EMPTY) {
					top[col] = y+1;
					break;
				}
			}
		}
	}
	
	private void setDisc (int index, char disc) {
		field[index] = disc;
		int val = disc == ME ? ONE : TWO;
		int four = disc == ME ? FOUR1 : FOUR2;
		for (int i: countmap[index]) {
			counts[i] += val;
			if (counts[i] == four) {
				winner = disc;
			}
		}
	}
	
	private void unsetDisc (int index) {
		int val = field[index] == ME ? ONE : TWO;
		field[index] = EMPTY;
		for (int i: countmap[index]) {
			counts[i] -= val;
		}
		winner = 0;
	}
	
	public boolean removeDisc (int column) {
		if (column > WIDTH) {
			throw new IllegalStateException("Illegal column: " + column);
		}
		if (top[column] == HEIGHT) {
			return false;
		}
		unsetDisc(idx(column, top[column]));
		top[column]++;
		return true;
	}
	
	public char getDisc(int column, int row) {
		return field[idx(column,row)];
	}
	
	public void print () {
		for (int y=0; y<HEIGHT; y++ ) {
			for (int x=0; x<WIDTH; x++) {
				char disc = field[idx(x,y)];
				if (disc == EMPTY) {
					System.out.print(" ");
				} else {
					System.out.print(disc);
				}
			}
			System.out.println();
		}
	}
	
	public double[] encodeFieldAsNetworkInput() {
		double[] input = new double[84];
		int c = 0;
		for (int y=0; y<HEIGHT; y++ ) {
			for (int x=0; x<WIDTH; x++) {
				int disc = field[idx(x,y)];
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
	
	@Deprecated
	public double[] encodeFeaturesAsNetworkInput() {
		return null;
	}

	public int[] countThrees () {
		int t1 = 0;
		int t2 = 0;
		for (int x: counts) {
			if (x == THREE1) {
				t1++;
			} else if (x == THREE2) {
				t2++;
			}
		}
		return new int[] {t1, t2};
	}

	public PlayField getInverted () {
		char[] grid = new char[SIZE];
		for (int i=0; i<SIZE; i++) {
			char disc = field[i];
			if (disc == OTHER) {
				grid[i] = ME;
			} else if (disc == ME) {
				grid[i] = OTHER;
			} else {
				grid[i] = EMPTY;
			}
		}
		return new PlayField(grid);
	}
	
	public boolean hasPlayerWon (char player) {
		return winner == player;
	}
	
	public boolean isFull () {
		for (int i: top) {
			if (i > 0) {
				return false;
			}
		}
		return true;
	}
	
	private void initialCollect () {
		for (int i=0; i<SIZE; i++) {
			if (field[i] != EMPTY) {
				for (int x: countmap[i]) {
					counts[x] += (field[i] == ME ? ONE : TWO);
				}
			}
		}
	}
	
	public void printCounts () {
		System.out.println(Arrays.toString(counts));
	}
	
	public int rounds () {
		int r = 0;
		for (int i=0; i<field.length; i++) {
			if (field[i] != EMPTY) {
				r++;
			}
		}
		return r;
	}

	public char[] getField() {
		return field;
	}

}
