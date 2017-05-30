/**
 * Minimax with alpha-beta pruning.
 *
 */
public class MaxMinMoveFinder {
	
	private static final int[] DEFAULT_ORDER = new int[] {3,2,4,5,1,0,6};
	private static final int[] ALT_ORDER = new int[] {3,4,2,6,0,1,5};
	
	private int maxDepth = 42;

	public MaxMinMoveFinder(int maxDepth) {
		this.maxDepth = maxDepth;
	}
	
	public void updateMaxDepth (int round, int time) {
		if (time < 1000) {
			maxDepth = 8;
		} else if (time < 8000){
			maxDepth = 11;
		} else if (round < 18){
			maxDepth = 13;
		} else {
			maxDepth = 42;
		}
		System.err.println("Round " + round + " (" + time + "s):  depth = " + maxDepth);
	}

	public BestMove findBestMove (PlayField myField) {
		int bestScore = Integer.MIN_VALUE;
		int alpha = Integer.MIN_VALUE;
		int bestMove = -1;
		for (int move: DEFAULT_ORDER) {
			if (myField.addDisc(move, PlayField.ME)) {
				if (myField.hasPlayerWon(PlayField.ME)) {
					myField.removeDisc(move);
					return new BestMove(move, 10000);
				}
				int score = findMiniMax(myField, 1, alpha, Integer.MAX_VALUE, PlayField.OTHER);
				myField.removeDisc(move);
				if (score > alpha) {
					alpha = score;
				}
				if (score > bestScore) {
					bestScore = score;
					bestMove = move;
				}
//				System.out.println("move " + move + ": " + score);
			}
		}
		if (bestScore > 9900) {
			System.err.println("Detected sure win in " + (10000-bestScore));
		} else if (bestScore < -9900) {
			System.err.println("Detected sure loss in " + (bestScore + 10000));
		}
		return new BestMove(bestMove, bestScore);
	}
	
	private int findMiniMax (PlayField myField, int searchDepth, int alpha, int beta, char player) {
		if (player == PlayField.ME) {
			// Maximizing
			int bestScore = Integer.MIN_VALUE;
			for (int move: DEFAULT_ORDER) {
				if (myField.addDisc(move, player)) {
					int score = computeMiniMaxScore(myField, searchDepth, alpha, beta, player);
					myField.removeDisc(move);
					if (score > bestScore) {
						bestScore = score;
					}
					if (score > alpha) {
						alpha = score;
					}
					if (beta <= alpha) {
						break;
					}
				}
			}
			return bestScore;
		} else {
			// Minimizing
			int bestScore = Integer.MAX_VALUE;
			for (int move: ALT_ORDER) {
				if (myField.addDisc(move, player)) {
					int score = computeMiniMaxScore(myField, searchDepth, alpha, beta, player);
					myField.removeDisc(move);
					if (score < bestScore) {
						bestScore = score;
					}
					if (score < beta) {
						beta = score;
					}
					if (beta <= alpha) {
						break;
					}
				}
			}
			return bestScore;
		}
	}	
	
	private int computeMiniMaxScore (PlayField myField, int depth, int alpha, int beta, char player) {
		if (myField.hasPlayerWon(player)) {
			return player == PlayField.ME ? 10000-depth : -(10000-depth);
		}
		if (myField.isFull() || depth == maxDepth) {
			return 0;
		}
		return findMiniMax(myField, depth+1, alpha, beta, player == PlayField.ME ? PlayField.OTHER : PlayField.ME);		
	}


}
