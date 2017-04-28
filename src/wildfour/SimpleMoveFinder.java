package wildfour;

/**
 * No min-max, simply evaluate all options.
 *
 */
public class SimpleMoveFinder implements MoveFinder {
	
	private final Evaluator evaluator;

	public SimpleMoveFinder(Evaluator evaluator) {
		this.evaluator = evaluator;
	}

	@Override
	public BestMove findBestMove(PlayField myField, int searchDepth) {
		BestMove bestMove = BestMove.none();
		for (int move=0; move<PlayField.WIDTH; move++) {
			if (myField.addDisc(move, PlayField.ME)) {
				double score = evaluator.evaluate(myField, move);
				if (score > bestMove.score) {
					bestMove = new BestMove(move, score);
				}
				myField.removeDisc(move);
			}
		}
		if (bestMove.isNone()) {
			throw new IllegalStateException("No move found!");
		}
		return bestMove;
	}

}
