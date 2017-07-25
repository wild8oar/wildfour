package wildfour;

import java.util.HashMap;
import java.util.Map;

import maps.MapR19D24AP1;

public class CombineMaps {
	
	private static final Map<String, Integer> MAP1 = MapR19D24AP1.MAP;
	private static final Map<String, Integer> MAP2 = new HashMap<>();
	private static final String OUTPUT_MAP = "MapA";

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
