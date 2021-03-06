package wildfour;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import wildfour.MoveFinder.BestMove;

public class AvoidLosingMove {
		
	private static final String MOVE = "000000B@000B00";
	private static final int DEPTH = 29;
	private static final MaxMinMoveFinder FINDER = new MaxMinMoveFinder(DEPTH-2);
	private static final int[] ORDER1 = new int[] {2};
	private static final int[] ORDER2 = new int[] {4,3,5,2,1,6,0}; 
	
	public static void main(String[] args) {
		System.out.println("Trying to avoid loss for move " + MOVE + " with depth " + DEPTH + "...");
		PlayField field = MapMoveFinder.decodeField(MOVE);
		field.print();
		for (int m1: ORDER1) {
			if (field.addDisc(m1, PlayField.ME)) {
				System.out.println("Trying move " + m1 + "...");
				double minScore = Integer.MAX_VALUE;
				Map<String, Integer> map = new HashMap<>();
				for (int m2: ORDER2) {
					if (field.addDisc(m2, PlayField.OTHER)) {
						System.out.println("   Opponent moves " + m2 + "...");
						long start = System.currentTimeMillis();
						BestMove best = FINDER.findBestMove(field);
						//BestMove best = new BestMove(m2,10);
						long time = System.currentTimeMillis()-start;
						System.out.println("     Best move (after " + TimeUnit.MILLISECONDS.toMinutes(time) + "mins): " +
								best.move + " with score " + best.score);
						map.put(MapMoveFinder.encodeField(field), best.move);
						if (best.score < minScore) {
							minScore = best.score;
						}
						field.removeDisc(m2);
						if (minScore < 0) {
							System.out.println("  Opponent found winning move");
							break;
						}
					}
				}
				field.removeDisc(m1);
				System.out.println("Best opponent score: " + minScore);
				if (minScore >= 0) {
					System.out.println("Found move to avoid loss: " + m1);
					System.out.println("MAP.put(\"" + MOVE + "\", " + m1 + ");");
					for (Entry<String, Integer> e: map.entrySet()) {
						System.out.println("  MAP.put(\"" + e.getKey() + "\", " + e.getValue() + ");");
					}
					break;
				}
			}
		}
		
	}

}
