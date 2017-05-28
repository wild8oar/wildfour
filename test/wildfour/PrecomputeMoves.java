package wildfour;
import static wildfour.PlayField.ME;
import static wildfour.PlayField.OTHER;
import static wildfour.MappedEvaluator.encodeField;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PrecomputeMoves {
	
	private static final int MAX_ROUNDS = 4;
	private static final int MAX_DEPTH = 16;
	
	private static final int ENTRIES_PER_METHOD = 4000;
	
	private static final MaxMinMoveFinder finder = new MaxMinMoveFinder(MAX_DEPTH);
	private static Map<String, Integer> map = new HashMap<>();
	
	private static int nComputed = 0;
	private static int nStored = 0;
	
	private static int[] nEval = new int[MAX_ROUNDS+1];
	private static long[] timeSum = new long[MAX_ROUNDS+1];
	
	private static void precomputeForPlayer1 (PlayField field, int round) {
		int move = findBestMove(field, round);
		field.addDisc(move, ME);
		round++;
		if (round >= MAX_ROUNDS || field.hasPlayerWon(ME) || field.isFull()) {
			field.removeDisc(move);
			return;
		}
		for (int i=0; i<PlayField.WIDTH; i++) {
			if (field.addDisc(i, OTHER)) {
				if (field.hasPlayerWon(OTHER)) {
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
			if (field.addDisc(i, ME)) {
				if (field.hasPlayerWon(ME)) {
					throw new IllegalStateException("Other player must not win!");
				}
				if (field.isFull()) {
					field.removeDisc(i);
					return;
				}
				PlayField inv = field.getInverted();
				int move = findBestMove(inv, round+1);
				field.addDisc(move, OTHER);
				if (!field.hasPlayerWon(OTHER) && !field.isFull()) {
					precomputeForPlayer2(field, round+2);
				}
				field.removeDisc(move);
				field.removeDisc(i);
			}			
		}
	}
	
	private static int findBestMove (PlayField field, int round) {
		nComputed++;
		String key = encodeField(field);
		Integer stored = map.get(key);
		if (stored != null) {
			return stored;
		}
		nEval[round]++;
		long start = System.currentTimeMillis();
		int move = finder.findBestMove(field).move;
		timeSum[round] += (System.currentTimeMillis()-start);
		if (move == -1) {
			throw new IllegalStateException("Illegal move: -1");
		}
		map.put(key, move);
		nStored++;
		if (nStored % 10 == 0) {
			System.out.println(nComputed + " / " + nStored);
		}
//		System.out.println("== Store move ==");
//		field.print();
//		System.out.println("Move: " + move);
		return move;
	}
	
	private static void saveMap() throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(new File("/home/adrian/aigames/wildfour/src/wildfour/TheMap.java"));
		writer.println("package wildfour;");
		writer.println("/**");	
		writer.println(" * Generated on " + new Date());
		writer.println(" * Max. rounds " + MAX_ROUNDS);
		writer.println(" * Max. depth " + MAX_DEPTH);
		writer.println("**/");	
		writer.println();
		writer.println("import java.util.HashMap;");
		writer.println("import java.util.Map;");
		writer.println();
		writer.println("public class TheMap {");
		writer.println();
		writer.println("    public static final Map<String, Integer> MAP = new HashMap<>();");
		writer.println();
		int meth = 0;
		int n = 0;
		for (Map.Entry<String, Integer> e: map.entrySet()) {
			if (n % ENTRIES_PER_METHOD == 0) {
				if (meth > 0) {
					writer.println("    }");
					writer.println();
				}
				meth++;
				writer.println("    private static void setup" + meth + "() {");
			}
			writer.println("      MAP.put(\"" + e.getKey() + "\", " + e.getValue() + ");");
			n++;
		}
		writer.println("    }");
		writer.println();
		writer.println("    static {");
		for (int i=1; i<=meth; i++) {
			writer.println("        setup" + i + "();");
		}
		writer.println("    }");
		writer.println("}");
		writer.close();
	}

	public static void main (String[] args) throws Exception {
		map = TheMap.MAP;
		nComputed = nStored = 0;
		long start = System.currentTimeMillis();
		PlayField field = PlayField.emptyField();
		precomputeForPlayer1(field, 1);
		System.out.println("Finished player 1 in:   " + (System.currentTimeMillis() - start) + "ms");
		precomputeForPlayer2(field, 1);
		saveMap();
		System.out.println("Moves computed: " + nComputed);
		System.out.println("Moves stored:   " + nStored);
		System.out.println("Completed in:   " + (System.currentTimeMillis() - start)/1000 + "s");
		System.out.println("Timing stats:");
		for (int i=1; i<=MAX_ROUNDS; i++) {
			if (nEval[i] > 0) {
				System.out.println(String.format("Round %2d: %6d evals, avg %dms", i, nEval[i], timeSum[i]/nEval[i]));
			}
		}
	}



}
