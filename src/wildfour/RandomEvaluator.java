package wildfour;

import java.util.Random;

public class RandomEvaluator implements Evaluator {
	
	private static final Random RAND = new Random();


	@Override
	public double evaluate(PlayField field, int move) {
		return RAND.nextDouble();
	}

}
