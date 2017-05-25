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
		int bestScore = Integer.MIN_VALUE;
		int alpha = Integer.MIN_VALUE;
		int bestMove = -1;
		for (int move: DEFAULT_ORDER) {
			if (myField.addDisc(move, ME)) {
				if (myField.hasPlayerWon(ME)) {
					return new BestMove(move, 10000);
				}
				int score = findMiniMax(myField, 1, alpha, Integer.MAX_VALUE, OTHER);
				myField.removeDisc(move);
				if (score > alpha) {
					alpha = score;
				}
				if (score > bestScore) {
					bestScore = score;
					bestMove = move;
				}
				//System.out.println("Move " + move + ": " + score);
			}
		}
		return new BestMove(bestMove, bestScore);
	}
	
	private int findMiniMax (PlayField myField, int searchDepth, int alpha, int beta, char player) {
		if (player == ME) {
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
					int score = computeMiniMaxScore(myField,searchDepth, alpha, beta, player);
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
			return player == ME ? 10000-depth : -(10000-depth);
		}
		if (myField.isFull()) {
			return 0;
		}
		if (depth == maxDepth) {
			int[] features = myField.countThrees();
			return features[0]-features[1];
		}
		return findMiniMax(myField, depth+1, alpha, beta, player == ME ? OTHER : ME);		
	}


}
