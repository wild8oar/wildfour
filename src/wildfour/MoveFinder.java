package wildfour;

public interface MoveFinder {
	
	BestMove findBestMove (PlayField myField, int searchDepth);
	
	
	public static class BestMove {
		
		public final int move;
		public final double score;
		
		public BestMove(int move, double score) {
			this.move = move;
			this.score = score;
		}
		
		public static BestMove none () {
			return new BestMove(-1, -100000);
		}
		
		public static BestMove victory (int move) {
			return new BestMove(move, 100000);
		}
		
		public boolean isNone () {
			return move == -1;
		}
	}

}
