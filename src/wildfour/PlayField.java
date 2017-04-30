package wildfour;

import java.util.regex.Pattern;

import bot.Field;

public class PlayField { 
	
	public static final char EMPTY = ' ';
	public static final char ME = '1';
	public static final char OTHER = '2';
	public static final char BORDER = 'X';
	
	private static final String WIN1 = "1111";
	private static final String WIN2 = "2222";
	private static final String OPENTHREE1 = " 111 ";
	private static final String OPENTHREE2 = " 222 ";
	private static final String ANYTHREE1L = " 111";
	private static final String ANYTHREE1R = "111 ";
	private static final String ANYTHREE2L = " 222";
	private static final String ANYTHREE2R = "222 ";
	
	public static final int WIDTH = 7;
	public static final int HEIGHT = 6;
	private static final int SIZE = WIDTH*HEIGHT+1;
		
	private final char[] field;
	
	private static final Pattern ANYTHREE1 = Pattern.compile("( 111|111 )");
	
	private String collectedField;
		
	private PlayField (char[] field) {
		this.field = field;
		this.collectedField = null;
	}
	
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
	
	private static int idx (int x, int y) {
		return 6*x+y;
	}
	
	private static int[] transform (int[][] in) {
		int[] out = new int[in.length];
		for (int i=0; i< in.length; i++) {
			if (in[i] == null) {
				out[i] = SIZE-1;
			} else {
				out[i] = idx(in[i][0], in[i][1]);
			}
		}
		return out;
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
	
	public boolean addDisc(int column, char disc) {
		if (column > WIDTH) {
			throw new IllegalStateException("Illegal column: " + column);
		}
		this.collectedField = null;
		for (int idx = idx(column, HEIGHT-1); idx >= idx(column, 0); idx--) { // From bottom up
			if (field[idx] == EMPTY) {
				field[idx] = disc;
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
		for (int y = idx(column, 0); y <= idx(column, HEIGHT-1); y++) { // From top down
			if (field[y] !=EMPTY) {
				field[y] = EMPTY;
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
		collectCollectibles();
		int[] features = new int[4];
		features[0] = collectedField.contains(OPENTHREE1) ? 1:0;
		features[1] = collectedField.contains(OPENTHREE2) ? 1:0;
		features[2] = collectedField.contains(ANYTHREE1L) || collectedField.contains(ANYTHREE1R) ? 1:0;
		features[3] = collectedField.contains(ANYTHREE2L) || collectedField.contains(ANYTHREE2R) ? 1:0;
		return features;
	}
	
	private int countFeatures (String feature) {
		int n=0;
		int ix = collectedField.indexOf(feature, 0);
		while (ix > -1) {
			n++;
			ix = collectedField.indexOf(feature, ix+1);
		}
		return n;
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
	
	/**
	 * @return if player 1 has 4 in a row
	 */
	public boolean hasPlayerWon (char player) {
		collectCollectibles();
		if (collectedContainsWin(player)) {
			return true;
		}
		return false;
	}

	private boolean collectedContainsWin (char player) {
		String toFind = player == ME ? WIN1 : WIN2;
		return collectedField.contains(toFind);
	}
	
	private void collectCollectibles () {
		if (collectedField != null) {
			return;
		}
		char[] collected = new char[COLLECTIBLES.length];
		for (int i=0; i<COLLECTIBLES.length; i++) {
				collected[i] = field[COLLECTIBLES[i]];
		}
		collectedField = new String(collected);
	}
	

}
