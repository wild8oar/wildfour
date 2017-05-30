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
		if (disc == PlayField.EMPTY) {
			return 0;
		}
		return disc == PlayField.ME ? 1 : 2;
	}

}
