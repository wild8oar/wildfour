import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PrecomputeMoves {
	
	private static final int MAX_ROUNDS = 2;
	private static final int MAX_DEPTH = 20;
	
	private static final int ENTRIES_PER_CLASS = 4000;
	
	private static final MaxMinMoveFinder finder = new MaxMinMoveFinder(MAX_DEPTH);
	private static final Map<String, Integer> map = TheMap.MAP; //new HashMap<>();
	private static final MapMoveFinder mapFinder = new MapMoveFinder(map);;
	private static final Map<String, Integer> quickMap = new HashMap<>();
	private static final MapMoveFinder quickFinder = new MapMoveFinder(quickMap);
	
	private static int nComputed = 0;
	private static int nStored = 0;
	private static int nQuick = 0;
	
	private static int[] nEval = new int[MAX_ROUNDS+1];
	private static long[] timeSum = new long[MAX_ROUNDS+1];
	
	private static void precomputeForPlayer1 (PlayField field, int round) {
		int move = findBestMove(field, round);
		field.addDisc(move, PlayField.ME);
		round++;
		if (round >= MAX_ROUNDS || field.hasPlayerWon(PlayField.ME) || field.isFull()) {
			field.removeDisc(move);
			return;
		}
		for (int i=0; i<PlayField.WIDTH; i++) {
			if (field.addDisc(i, PlayField.OTHER)) {
				if (field.hasPlayerWon(PlayField.OTHER)) {
					throw new IllegalStateException("Other player must not win!");
				}
				if (!field.isFull()) {
					precomputeForPlayer1(field, round+1);
				}
				field.removeDisc(i);
			}			
		}
		field.removeDisc(move);
	}
	
	private static void precomputeForPlayer2 (PlayField field, int round) {
		if (round >= MAX_ROUNDS) {
			return;
		}
		for (int i=0; i<PlayField.WIDTH; i++) {
			if (field.addDisc(i, PlayField.ME)) {
				if (field.hasPlayerWon(PlayField.ME)) {
					throw new IllegalStateException("Other player must not win!");
				}
				if (field.isFull()) {
					field.removeDisc(i);
					return;
				}
				PlayField inv = field.getInverted();
				int move = findBestMove(inv, round+1);
				field.addDisc(move, PlayField.OTHER);
				if (!field.hasPlayerWon(PlayField.OTHER) && !field.isFull()) {
					precomputeForPlayer2(field, round+2);
				}
				field.removeDisc(move);
				field.removeDisc(i);
			}			
		}
	}
	
	private static int findBestMove (PlayField field, int round) {
		nComputed++;
		Optional<Integer> stored = mapFinder.findMove(field);
		if (stored.isPresent()) {
			return stored.get();
		}
		Optional<Integer> quick = quickFinder.findMove(field);
		if (quick.isPresent()) {
			return quick.get();
		}
		nEval[round]++;
		long start = System.currentTimeMillis();
		int move = finder.findBestMove(field).move;
		long time = System.currentTimeMillis()-start;
		System.out.println("Round " + round + " move: " + time + "ms");
		timeSum[round] += time;
		if (move == -1) {
			throw new IllegalStateException("Illegal move: -1");
		}
		String key = MapMoveFinder.encodeField(field);
		if (time < 500) {
			nQuick++;
			quickMap.put(key, move);
			return move;
		}
		map.put(key, move);
		nStored++;
		if (nStored % 10 == 0) {
			System.out.println(nComputed + " / " + nStored);
		}
		return move;
	}
	
	private static void saveMap() throws FileNotFoundException {
		if (map.isEmpty()) {
			return;
		}
		File path = new File("/home/adrian/git/wildfour/perfect");
		File mapsPath = new File(path, "maps");
		PrintWriter writer = null;
		int classNum = 0;
		int n = 0;
		for (String k: map.keySet()) {
			if (n % ENTRIES_PER_CLASS == 0) {
				if (classNum > 0) {
					writer.println("}");
					writer.println("}");
					writer.close();
				}
				classNum++;
				String mapName = String.format("Map%02d", classNum);
				writer = new PrintWriter(new File(mapsPath, mapName + ".java"));
				writeClassHeader(writer, "maps", mapName);
			}
			writer.println("MAP.put(\"" + k + "\"," + map.get(k) + ");");
			n++;
		}
		writer.println("}");
		writer.println("}");
		writer.close();
		writer = new PrintWriter(new File(path, "TheMap.java"));
		writeClassHeader(writer, null, "TheMap");
		for (int i=1; i<=classNum; i++) {
			String mapName = String.format("maps.Map%02d", classNum);
			writer.println("MAP.putAll(" + mapName + ".MAP);");
		}
		writer.println("}");
		writer.println("}");
		writer.close();
	}
	
	private static void writeClassHeader (PrintWriter writer, String packName, String className) {
		if (packName != null) writer.println("package " + packName + ";");
		writer.println("/**");	
		writer.println(" * Generated on " + new Date());
		writer.println(" * Max. rounds " + MAX_ROUNDS);
		writer.println(" * Max. depth " + MAX_DEPTH);
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

	public static void main (String[] args) throws Exception {
		nComputed = nStored = 0;
		long start = System.currentTimeMillis();
		PlayField field = PlayField.emptyField();
		precomputeForPlayer1(field, 1);
		System.out.println("Finished player 1 in:   " + (System.currentTimeMillis() - start)/1000 + "s");
		precomputeForPlayer2(field, 1);
		saveMap();
		System.out.println("Moves computed:           " + nComputed);
		System.out.println("Moves stored:             " + nStored);
		System.out.println("Quick finds not stored:   " + nQuick);
		System.out.println("Completed in:             " + (System.currentTimeMillis() - start)/1000 + "s");
		System.out.println("Timing stats:");
		for (int i=1; i<=MAX_ROUNDS; i++) {
			if (nEval[i] > 0) {
				System.out.println(String.format("Round %2d: %6d evals, avg %5dms", i, nEval[i], timeSum[i]/nEval[i]));
			}
		}
	}



}
