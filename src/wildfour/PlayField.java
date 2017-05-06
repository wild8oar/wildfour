package wildfour;

import java.util.Arrays;

import bot.Field;

public class PlayField { 
	
	public static final char EMPTY = ' ';
	public static final char ME = '1';
	public static final char OTHER = '2';
	public static final char BORDER = 'X';
	
	private static final int ONE = 1;
	private static final int TWO = 10;
	private static final int FOUR1 = 4*ONE;
	private static final int FOUR2 = 4*TWO;
	private static final int THREE1 = 3*ONE;
	private static final int THREE2 = 3*TWO;
	
	public static final int WIDTH = 7;
	public static final int HEIGHT = 6;
	private static final int SIZE = WIDTH*HEIGHT+1;
	private static final int OUTSIDE = SIZE-1;
		
	private final char[] field;
	private final int[] counts = new int[168];
	
	
	private static final int[] COLLECTIBLES = transform(new int[][] {
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
	});
	
	private static final int[][] countmap = setupCountMap();
	
	private PlayField (char[] field) {
		this.field = field;
		initialCollect();
	}
	
	private static int idx (int x, int y) {
		return 6*x+y;
	}
	
	private static int[] transform (int[][] in) {

		int[] out = new int[in.length];
		for (int i=0; i< in.length; i++) {
			if (in[i] == null) {
				out[i] = OUTSIDE;
			} else {
				out[i] = idx(in[i][0], in[i][1]);
			}
		}
		return out;
	}
	
	private static int[][] setupCountMap () {
		int[][] map = new int[SIZE-1][];
		int start = 0;
		int index = 0;
		while (start < COLLECTIBLES.length-2) {
			int end = start+3;
			while (end < COLLECTIBLES.length-1 && COLLECTIBLES[end+1] < OUTSIDE) {end++;};
			index = addToCountMap(index, map, start, end);
			start = end+2;
		}
		return map;
	}
	
	private static int addToCountMap(int index, int[][] map, int start, int end) {
		for (int beg=start; beg<end-2; beg++) {
			for (int x=beg; x<beg+4; x++) {
				int mapi = COLLECTIBLES[x];
				map[mapi] = add(map[mapi], index);
			}
			index++;
		}
		return index;
	}

	private static int[] add (int[] in, int x) {
		if (in == null) {
			return new int[] {x};
		} else if (in.length == 1) {
			return new int[] {in[0], x};
		} else if (in.length == 2) {
			return new int[] {in[0], in[1], x};
		} else if (in.length == 3) {
			return new int[] {in[0], in[1], in[2], x};
		} else if (in.length == 4) {
			return new int[] {in[0], in[1], in[2], in[3], x};
		} else if (in.length == 5) {
			return new int[] {in[0], in[1], in[2], in[3], in[4], x};
		} else if (in.length == 6) {
			return new int[] {in[0], in[1], in[2], in[3], in[4], in[5], x};
		} else if (in.length == 7) {
			return new int[] {in[0], in[1], in[2], in[3], in[4], in[5], in[6], x};
		} else if (in.length == 8) {
			return new int[] {in[0], in[1], in[2], in[3], in[4], in[5], in[6], in[7], x};
		} else if (in.length == 9) {
			return new int[] {in[0], in[1], in[2], in[3], in[4], in[5], in[6], in[7], in[8], x};
		} else if (in.length == 10) {
			return new int[] {in[0], in[1], in[2], in[3], in[4], in[5], in[6], in[7], in[8], in[9], x};
		} else if (in.length == 11) {
			return new int[] {in[0], in[1], in[2], in[3], in[4], in[5], in[6], in[7], in[8], in[9], in[10], x};
		} else if (in.length == 12) {
			return new int[] {in[0], in[1], in[2], in[3], in[4], in[5], in[6], in[7], in[8], in[9], in[10], in[11], x};
		} 
		throw new IllegalStateException("in array too large: " + in.length);
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
		grid[SIZE-1] = BORDER;
		return new PlayField(grid);
	}
	
	public static PlayField emptyField () {
		char[] grid = new char[SIZE];
		for (int i=0; i<SIZE-1; i++) {
			grid[i] = EMPTY;
		}
		grid[SIZE-1] = BORDER;
		return new PlayField(grid);
	}
	
	public boolean addDisc (int column, char disc) {
		if (column > WIDTH) {
			throw new IllegalStateException("Illegal column: " + column);
		}
		for (int idx = idx(column, HEIGHT-1); idx >= idx(column, 0); idx--) { // From bottom up
			if (field[idx] == EMPTY) {
				setDisc(idx, disc);
				return true;
			}
		}
		return false;
	}
	
	private void setDisc (int index, char disc) {
		field[index] = disc;
		for (int i: countmap[index]) {
			counts[i] += (disc == ME ? ONE : TWO);
		}
	}
	
	private void unsetDisc (int index, char disc) {
		field[index] = EMPTY;
		for (int i: countmap[index]) {
			counts[i] -= (disc == ME ? ONE : TWO);
		}	
	}
	
	public boolean removeDisc(int column) {
		if (column > WIDTH) {
			throw new IllegalStateException("Illegal column: " + column);
		}
		for (int y = idx(column, 0); y <= idx(column, HEIGHT-1); y++) { // From top down
			if (field[y] !=EMPTY) {
				unsetDisc(y, field[y]);
				return true;
			}
		}
		return false;
	}
	
	public char getDisc(int column, int row) {
		return field[idx(column,row)];
	}
	
	public void print () {
		for (int y=0; y<HEIGHT; y++ ) {
			for (int x=0; x<WIDTH; x++) {
				char disc = field[idx(x,y)];
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

	public int[] getFeatureExistance () {
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
		for (int x=0; x<WIDTH; x++) {
			for (int y=0; y<HEIGHT; y++) {
				char disc = field[idx(x,y)];
				if (disc == OTHER) {
					grid[idx(x,y)] = ME;
				} else if (disc == ME) {
					grid[idx(x,y)] = OTHER;
				}
			}
		}
		return new PlayField(grid);
	}
	
	public boolean hasPlayerWon (char player) {
		int four = player == ME ? FOUR1 : FOUR2;
		for (int x: counts) {
			if (x == four) {
				return true;
			}
		}
		return false;
	}
	
	private void initialCollect () {
		for (int i=0; i<SIZE-1; i++) {
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
}
