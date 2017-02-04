package wildfour;

import org.junit.Test;

import junit.framework.Assert;
import wildfour.MoveFinder.BestMove;

public class MaxMinEvaluatorTest {

	@Test
	public void shouldFind0 () {
		MoveFinder finder = new MaxMinMoveFinder(new TestEvaluator(), 0);
		BestMove best = finder.findBestMove(PlayField.emptyField(), 0);
		Assert.assertEquals(0, best.move);
		Assert.assertEquals(1.0, best.score);
	}
	
	@Test
	public void shouldFind1 () {
		MoveFinder finder = new MaxMinMoveFinder(new TestEvaluator(), 1);
		BestMove best = finder.findBestMove(PlayField.emptyField(), 0);
		Assert.assertEquals(2, best.move);
		Assert.assertEquals(-1.0, best.score);
	}

	private class TestEvaluator implements Evaluator {

		@Override
		public double evaluate(PlayField field) {
			for (int i=0; i<7; i++) {
				if (field.getDisc(i, 5) == 0) {
					System.out.println(i);
					return i;
				}
			}
			return -1;
		}
	}

}
