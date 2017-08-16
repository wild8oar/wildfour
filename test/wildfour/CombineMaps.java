package wildfour;

import java.util.Map;

import maps.MapAP1X;
import maps.MapAP2X;

public class CombineMaps {
	
	private static final Map<String, Integer> MAP1 = MapAP1X.MAP;
	private static final Map<String, Integer> MAP2 = MapAP2X.MAP;
	private static final String OUTPUT_MAP = "MapAX";

	public static void main(String[] args) throws Exception {
		int s1 = MAP1.size();
		int s2 = MAP2.size();
		System.out.println("Input map sizes: " + s1 + "/" + s2);
		MAP1.putAll(MAP2);
		if (MAP1.size() != s1+s2) {
			throw new IllegalStateException("Overlap: s1=" + s1 + ", s2=" + s2+ ", combined=" + MAP1.size());
		}
		MapWriter.writeMap(OUTPUT_MAP, MAP1);
	}
}
