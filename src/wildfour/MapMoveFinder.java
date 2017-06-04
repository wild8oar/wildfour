package wildfour;

import static wildfour.PlayField.EMPTY;
import static wildfour.PlayField.ME;
import static wildfour.PlayField.OTHER;

import java.util.Map;
import java.util.Optional;

public class MapMoveFinder {
	
	private final Map<String, Integer> map;
	
	public MapMoveFinder(Map<String, Integer> map) {
		this.map = map;
	}

	public Optional<Integer> findMove (PlayField field) {
		String key = encodeField(field);
		Integer direct = map.get(key);
		if (direct != null) {
			return Optional.of(direct);
		}
		String mirror = encodeMirroredField(field);
		Integer fromMirror = map.get(mirror);
		if (fromMirror != null) {
			return Optional.of(6-fromMirror);
		}
		return Optional.empty();
	}
	
	public static String encodeField (PlayField field) {
		char[] enc = new char[14];
		int p = 0;
		for (int x=0; x<PlayField.WIDTH; x++) {
			int lower = 48+(toNum(field.getDisc(x, 0)) + 3*toNum(field.getDisc(x, 1)) + 9*toNum(field.getDisc(x, 2)));
			int upper = 48+(toNum(field.getDisc(x, 3)) + 3*toNum(field.getDisc(x, 4)) + 9*toNum(field.getDisc(x, 5)));
			enc[p++] = (char) lower;
			enc[p++] = (char) upper;
		}
		return new String(enc);
	}
	
	private static String encodeMirroredField (PlayField field) {
		char[] enc = new char[14];
		int p = 0;
		for (int x=PlayField.WIDTH-1; x >=0; x--) {
			int lower = 48+(toNum(field.getDisc(x, 0)) + 3*toNum(field.getDisc(x, 1)) + 9*toNum(field.getDisc(x, 2)));
			int upper = 48+(toNum(field.getDisc(x, 3)) + 3*toNum(field.getDisc(x, 4)) + 9*toNum(field.getDisc(x, 5)));
			enc[p++] = (char) lower;
			enc[p++] = (char) upper;
		}
		return new String(enc);
	}
	
	private static int toNum (char disc) {
		if (disc == EMPTY) {
			return 0;
		}
		return disc == ME ? 1 : 2;
	}
	
	public static PlayField decodeField (String enc) {
		PlayField field = PlayField.emptyField();
		for (int p=0; p<14; p+=2) {
			char[] lower = decode(enc.charAt(p)-48);
			char[] upper = decode(enc.charAt(p+1)-48);
			add(field, p/2, upper);
			add(field, p/2, lower);
		}
		// verify
		if (!enc.equals(encodeField(field))) {
			System.out.println("Original: " + enc);
			System.out.println("Decoded:  " + encodeField(field));
			field.print();
			throw new IllegalStateException("failed to decode");
		}
		return field;
	}
	
	public static String normalize (String enc) {
		PlayField field = decodeField(enc);
		String mirrored = encodeMirroredField(field);
		if (enc.compareTo(mirrored) > 0) {
			return mirrored;
		}
		return enc;
	}
	
	private static void add (PlayField field, int col, char[] decs) {
		for (int i = 2; i>=0; i--) {
			if (decs[i] != EMPTY) {
				field.addDisc(col, decs[i]);
			}
		}
	}

	private static char[] decode (int enc) {
		char[] res = new char[3];
		if (enc >= 9*2) {
			res[2] = OTHER;
			enc -= 18;
		} else if (enc >= 9) {
			res[2] = ME;
			enc -= 9;
		} else {
			res[2] = EMPTY;
		}
		if (enc >= 3*2) {
			res[1] = OTHER;
			enc -= 6;
		} else if (enc >= 3) {
			res[1] = ME;
			enc -= 3;
		} else {
			res[1] = EMPTY;
		}
		if (enc == 2) {
			res[0] = OTHER;
		} else if (enc == 1) {
			res[0] = ME;
		} else {
			res[0] = EMPTY;
		}
		return res;
	} 

}
