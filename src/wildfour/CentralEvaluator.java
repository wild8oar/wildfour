package wildfour;

public class CentralEvaluator implements Evaluator {

	@Override
	public double evaluate(PlayField field, int move) {
		if (move<=3) {
			return move-3;
		}
		return 3-move;
	}

}
