	public class BestMove {
		
		public final int move;
		public final double score;
		
		public BestMove(int move, double score) {
			this.move = move;
			this.score = score;
		}
		
		public static BestMove none () {
			return new BestMove(-1, -1000000);
		}
		
		public static BestMove victory (int move, int depth) {
			return new BestMove(move, 100000-depth);
		}
		
		public boolean isNone () {
			return move == -1;
		}
	}
