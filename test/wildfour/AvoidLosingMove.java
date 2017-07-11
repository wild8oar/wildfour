package wildfour;

import java.util.concurrent.TimeUnit;

import wildfour.MoveFinder.BestMove;

public class AvoidLosingMove {
	
	// TODO: 000000B@0B0000,000000B@000B00 (d=29)
	
	private static final String MOVE = "000000B@0B0000";
	private static final int DEPTH = 29;
	private static final MaxMinMoveFinder FINDER = new MaxMinMoveFinder(DEPTH-2);
	private static final int[] ORDER = new int[] {4,2,5,1,3,0,6};
	
	public static void main(String[] args) {
		System.out.println("Trying to avoid loss for move " + MOVE + " with depth " + DEPTH + "...");
		PlayField field = MapMoveFinder.decodeField(MOVE);
		field.print();
		for (int m1: ORDER) {
			if (field.addDisc(m1, PlayField.ME)) {
				System.out.println("Trying move " + m1 + "...");
				double minScore = Integer.MAX_VALUE;
				for (int m2: ORDER) {
					if (field.addDisc(m2, PlayField.OTHER)) {
						System.out.println("   Opponent moves " + m2 + "...");
						long start = System.currentTimeMillis();
						BestMove best = FINDER.findBestMove(field);
						long time = System.currentTimeMillis()-start;
						System.out.println("   Best move (after " + TimeUnit.MILLISECONDS.toMinutes(time) + "mins): " +
								best.move + " with score " + best.score);
						if (best.score < minScore) {
							minScore = best.score;
						}
						field.removeDisc(m2);
						if (minScore < 0) {
							System.out.println(   "Opponent found winning move");
							break;
						}
					}
				}
				field.removeDisc(m1);
				System.out.println("Best opponent score: " + minScore);
				if (minScore >= 0) {
					System.out.println("Found move to avoid loss: " + m1);
					break;
				}
			}
		}
		
	}

}
