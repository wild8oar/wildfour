package wildfour;

import static wildfour.PlayField.ME;

import java.util.Optional;

public class MappedEvaluator {
	
	public static Optional<Integer> findMove (PlayField field) {
		String key = encodeField(field);
		return Optional.ofNullable(TheMap.MAP.get(key));
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
	
	private static int toNum (char disc) {
		if (disc == PlayField.EMPTY) {
			return 0;
		}
		return disc == ME ? 1 : 2;
	}

}
