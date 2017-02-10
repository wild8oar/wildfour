package wildfour;

/**
 * No min-max, simply evaluate all options.
 *
 */
public class MaxMinMoveFinder implements MoveFinder {
	
	private final Evaluator evaluator;
	private final int maxDepth;

	public MaxMinMoveFinder(Evaluator evaluator, int maxDepth) {
		this.evaluator = evaluator;
		this.maxDepth = maxDepth;
	}

	@Override
	public BestMove findBestMove(PlayField myField, int searchDepth) {
		BestMove bestMove = BestMove.none();
		for (int move=0; move<PlayField.WIDTH; move++) {
			if (myField.addDisc(move, PlayField.ME)) {
				if (myField.haveIWon()) {
					return BestMove.victory(move, searchDepth);
				}
				double score = computeScore(myField, searchDepth);
				if (score > bestMove.score) {
					bestMove = new BestMove(move, score);
				}
				myField.removeDisc(move);
			}
		}
		return bestMove;
	}

	private double computeScore(PlayField myField, int depth) {
		if (depth == maxDepth) {
			return evaluator.evaluate(myField);
		}
		return -findBestMove(myField.getInverted(), depth+1).score;		
	}

}
