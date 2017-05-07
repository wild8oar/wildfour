package wildfour;

import static wildfour.PlayField.ME;
import static wildfour.PlayField.OTHER;

/**
 * Minimax with alpha-beta pruning.
 *
 */
public class MaxMinMoveFinder implements MoveFinder {
	
	private static final int[] DEFAULT_ORDER = new int[] {3,2,4,5,1,0,6};
	private static final int[] ALT_ORDER = new int[] {3,4,2,6,0,1,5};
	
	private final int maxDepth;

	public MaxMinMoveFinder(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	@Override
	public BestMove findBestMove(PlayField myField, int searchDepth) {
		return findMiniMax(myField, searchDepth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, ME);
	}
	
	private BestMove findMiniMax (PlayField myField, int searchDepth, double alpha, double beta, char player) {
		int bestMove = -1;
		if (player == ME) {
			// Maximizing
			double bestScore = Integer.MIN_VALUE;
			for (int move: DEFAULT_ORDER) {
				if (myField.addDisc(move, player)) {
					double score = computeMiniMaxScore(myField, move, searchDepth, alpha, beta, player);
					myField.removeDisc(move);
					if (score > bestScore) {
						bestScore = score;
						bestMove = move;
					}
					if (score > alpha) {
						alpha = score;
					}
					if (beta <= alpha) {
						break;
					}
				}
			}
			return new BestMove(bestMove, bestScore);
		} else {
			// Minimizing
			double bestScore = Integer.MAX_VALUE;
			for (int move: ALT_ORDER) {
				if (myField.addDisc(move, player)) {
					double score = computeMiniMaxScore(myField, move, searchDepth, alpha, beta, player);
					myField.removeDisc(move);
					if (score < bestScore) {
						bestScore = score;
						bestMove = move;
					}
					if (score < beta) {
						beta = score;
					}
					if (beta <= alpha) {
						break;
					}
				}
			}
			return new BestMove(bestMove, bestScore);
		}
	}	
	
	private double computeMiniMaxScore (PlayField myField, int move, int depth, double alpha, double beta, char player) {
		if (myField.hasPlayerWon(player)) {
			return player == ME ? 10000-depth : -(10000-depth);
		}
		if (depth == maxDepth) {
			int[] features = myField.countThrees();
			return features[0]-features[1];
		}
		return findMiniMax(myField, depth+1, alpha, beta, player == ME ? OTHER : ME).score;		
	}


}
