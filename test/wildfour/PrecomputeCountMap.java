package wildfour;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrecomputeCountMap {
	private static final int OUTSIDE = -1;

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
		int[][] map = new int[PlayField.SIZE][];
		int start = 0;
		int index = 0;
		while (start < COLLECTIBLES.length-2) {
			int end = start+3;
			while (end < COLLECTIBLES.length-1 && COLLECTIBLES[end+1] != OUTSIDE) {end++;};
			index = addToCountMap(index, map, start, end);
			start = end+2;
		}
		System.out.println("last index: " + index);
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
	
	public static void main(String[] args) {
		List<String> maps = new ArrayList<>();
		for (int x=0; x<countmap.length; x++) {
			String t = Arrays.toString(countmap[x]);
			maps.add("{" + t.substring(1, t.length()-1) + "}");
		}
		System.out.println("{" + String.join(", ", maps) + "}");
	}
}
