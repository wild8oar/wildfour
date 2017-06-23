package wildfour;

import static wildfour.PlayField.EMPTY;
import static wildfour.PlayField.ME;

import java.util.Map;
import java.util.Optional;

public class LongMapMoveFinder {
	
	private static final int[] COLMAP = {0, 1, 2, -1, 3, 5, -1, 4, 6, -1, -1, -1, -1, 7, 11, 
			-1, 9, 13, -1, -1, -1, -1, 8, 12, -1, 10, 14, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
			-1, -1, -1, -1, -1, 22, -1, 18, 26, -1, -1, -1, -1, 16, 24, -1, 20, 28, -1, -1, -1, 
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 15, 23, -1, 19, 27, -1, -1, -1, -1, 17, 25,
			-1, 21, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
			-1, -1, -1, -1, 35, 49, -1, -1, -1, -1, 31, 45, -1, 39, 53, -1, -1, -1, -1, -1, -1, 
			-1, -1, -1, -1, -1, -1, -1, 29, 43, -1, 37, 51, -1, -1, -1, -1, 33, 47, -1, 41, -1, 
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 42, 
			-1, 36, 50, -1, -1, -1, -1, 32, 46, -1, 40, 54, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
			-1, -1, -1, -1, 30, 44, -1, 38, 52, -1, -1, -1, -1, 34, 48, -1, -1, -1, -1, -1, -1, 
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
			-1, 59, 85, -1, 73, 99, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 55, 81, 
			-1, 69, 95, -1, -1, -1, -1, 63, 89, -1, 77, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 79, -1, 67, 93, -1, -1, -1, -1, 61, 87, 
			-1, 75, 101, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 57, 83, -1, 71, 97, 
			-1, -1, -1, -1, 65, 91, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
			-1, -1, -1, -1, -1, -1, -1, 66, 92, -1, -1, -1, -1, 60, 86, -1, 74, 100, -1, -1, -1, 
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 56, 82, -1, 70, 96, -1, -1, -1, -1, 64, 90, 
			-1, 78, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
			-1, -1, 80, -1, 68, 94, -1, -1, -1, -1, 62, 88, -1, 76, 102, -1, -1, -1, -1, -1, -1, 
			-1, -1, -1, -1, -1, -1, -1, 58, 84, -1, 72, 98};
	
	private static final int[] REVMAP = {0, 1, 2, 4, 7, 5, 8, 13, 22, 16, 25, 14, 23, 17, 26, 67, 
			49, 76, 43, 70, 52, 79, 41, 68, 50, 77, 44, 71, 53, 148, 229, 130, 211, 157, 238, 124, 
			205, 151, 232, 133, 214, 160, 203, 149, 230, 131, 212, 158, 239, 125, 206, 152, 233, 
			134, 215, 391, 634, 472, 715, 373, 616, 454, 697, 400, 643, 481, 610, 448, 691, 394, 
			637, 475, 718, 376, 619, 457, 700, 403, 646, 446, 689, 392, 635, 473, 716, 374, 617, 
			455, 698, 401, 644, 482, 611, 449, 692, 395, 638, 476, 719, 377, 620, 458, 701};

	private static final long[] POW103 = {1, 103, 10609, 1092727, 112550881, 11592740743L, 1194052296529L};
	private static final int[] POW3 = {1, 3, 9, 27, 81, 243};

	private final Map<Long, Integer> map;
	
	public LongMapMoveFinder(Map<Long, Integer> map) {
		this.map = map;
	}

	public Optional<Integer> findMove (PlayField field) {
		long key = encodeField(field);
		Integer direct = map.get(key);
		if (direct != null) {
			return Optional.of(direct);
		}
		long mirror = encodeMirroredField(field);
		Integer fromMirror = map.get(mirror);
		if (fromMirror != null) {
			return Optional.of(6-fromMirror);
		}
		return Optional.empty();
	}
	
	public static long encodeField (PlayField field) {
		long enc = 0;
		for (int x=0; x<PlayField.WIDTH; x++) {
			int col = encodeColumn(new char[] {field.getDisc(x, 5), field.getDisc(x, 4), field.getDisc(x, 3),
					field.getDisc(x, 2), field.getDisc(x, 1), field.getDisc(x, 0)});
			enc += col*POW103[x];
		}
		return enc;
	}
	
	private static int encodeColumn(char[] col) {
		int x = 0;
		for (int i=0; i<6; i++) {
			x += toNum(col[i]) * POW3[i];
		}
		if (COLMAP[x] == -1) {
			throw new IllegalStateException("Colmap -1: " + new String(col));
		}
		return COLMAP[x];
	}	
	
	private static int toNum (char disc) {
		if (disc == EMPTY) {
			return 0;
		}
		return disc == ME ? 1 : 2;
	}
	

	private static long encodeMirroredField (PlayField field) {
		long enc = 0;
		for (int x=PlayField.WIDTH-1; x >=0; x--) {
			int col = encodeColumn(new char[] {field.getDisc(x, 5), field.getDisc(x, 4), field.getDisc(x, 3),
					field.getDisc(x, 2), field.getDisc(x, 1), field.getDisc(x, 0)});
			enc += col*POW103[6-x];
		}
		return enc;
	}
	
	public static PlayField decodeField (long enc) {
		PlayField field = PlayField.emptyField();
		long rem = enc;
		for (int p= PlayField.WIDTH-1; p>=0; p--) {
			int colenc = (int) (rem / POW103[p]);
			char[] col = decodeCol(colenc);
			add(field, p, col);
			rem -= colenc * POW103[p];
		}
		// verify
		if (enc != encodeField(field)) {
			System.out.println("Original: " + enc);
			System.out.println("Decoded:  " + encodeField(field));
			field.print();
			throw new IllegalStateException("failed to decode");
		}
		return field;
	}
	
	private static char[] decodeCol (int colenc) {
		char[] col = new char[PlayField.HEIGHT];
		int rem = REVMAP[colenc];
		for (int h=PlayField.HEIGHT-1; h>=0; h--) {
			int dn = rem / POW3[h];
			col[5-h] = toDisc (dn);
			rem -= dn * POW3[h];
		}
		return col;
	}

	private static char toDisc (int dn) {
		if (dn == 1) {
			return PlayField.ME;
		}
		if (dn == 2) {
			return PlayField.OTHER;
		}
		if (dn == 0) {
			return PlayField.EMPTY;
		}
		throw new IllegalStateException("disc num: " + dn);
	}

	public static long mirror (long enc) {
		PlayField field = decodeField(enc);
		return encodeMirroredField(field);
	}
	
	public static long normalize (long enc) {
		long mirrored = mirror(enc);
		if (mirrored < enc) {
			return mirrored;
		}
		return enc;
	}
	
	private static void add (PlayField field, int col, char[] decs) {
		for (int i = decs.length-1; i>=0; i--) {
			if (decs[i] != EMPTY) {
				field.addDisc(col, decs[i]);
			}
		}
	}
	
}
