package wildfour;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MapWriter {
	
	private static final File PATH = new File("test/maps");
	private static final int ENTRIES_PER_CLASS = 4000;
	
	public static void writeMap (String mapName, Map<String, Integer> map) throws FileNotFoundException {
		List<String> keys = new ArrayList<>();
		keys.addAll(map.keySet());
		Collections.sort(keys);
		if (keys.size() <= ENTRIES_PER_CLASS) {
			writeMap(mapName, map, keys, 0, keys.size()-1);
			return;
		}
		// have to split
		int num = 0;
		for (int i=0; i<keys.size(); i+=ENTRIES_PER_CLASS) {
			num++;
			int to = i+ENTRIES_PER_CLASS-1;
			if (to >= keys.size()) {
				to = keys.size()-1;
			}
			writeMap(mapName(mapName, num), map, keys, i, to);
		}
		writeSummary(mapName, num);
	}
	
	private static String mapName (String name, int idx) {
		return String.format("%s_%02d", name, idx);
	}
	
	private static void writeSummary(String name, int num) throws FileNotFoundException {
		try (PrintWriter writer = new PrintWriter(new File(PATH, name + ".java"))) {
			writeClassHeader(writer, name);
			for (int i=1; i<=num; i++) {
				writer.println("MAP.putAll(" + mapName(name, i) + ".MAP);");
			}
			writer.println("}");
			writer.println("}");
			writer.close();
		}
	}

	private static void writeMap (String name, Map<String, Integer> map, List<String> keys, int from, int to) throws FileNotFoundException {
		try (PrintWriter writer = new PrintWriter(new File(PATH, name + ".java"))) {
			writeClassHeader(writer, name);
			for (int i=from; i<=to; i++) {
				String key = keys.get(i);
				writer.println("MAP.put(\"" + key + "\"," + map.get(key) + ");");
			}
			writer.println("}");
			writer.println("}");
			writer.close();
		}
	}
	
	private static void writeClassHeader (PrintWriter writer, String className) {
		writer.println("package maps;");
		writer.println("/**");	
		writer.println(" * Written on " + new Date());
		writer.println("**/");	
		writer.println();
		writer.println("import java.util.HashMap;");
		writer.println("import java.util.Map;");
		writer.println();
		writer.println("public class " + className + " {");
		writer.println();
		writer.println("public static final Map<String, Integer> MAP = new HashMap<>();");
		writer.println();
		writer.println("static {");
	}


}
