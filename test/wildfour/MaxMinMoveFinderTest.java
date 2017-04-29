package wildfour;
import static wildfour.PlayField.ME;
import static wildfour.PlayField.OTHER;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bot.Field;
import junit.framework.Assert;
import wildfour.MoveFinder.BestMove;

public class MaxMinMoveFinderTest {
	
	private int nEval;
	private long start;
	
	private static final int MAX_DEPTH = 10;
	private final Evaluator evaluator = new EqualEvaluator();
	
	@Test
	public void shouldFindWinIn1 () {
		MoveFinder finder = new MaxMinMoveFinder(evaluator, MAX_DEPTH);
		Field field = new Field(7,6);
		field.addDisc(4, 1);
		field.addDisc(4, 1);
		field.addDisc(4, 1);
		
		BestMove best = finder.findBestMove(PlayField.fromBotField(field, 1), 0);
		Assert.assertEquals(4, best.move);
		Assert.assertEquals(10000.0, best.score);
	}
	
	@Test
	public void shouldFindWinIn2 () {
		MoveFinder finder = new MaxMinMoveFinder(evaluator, MAX_DEPTH);
		Field field = new Field(7,6);
		field.addDisc(3, 1);
		field.addDisc(4, 1);
		
		BestMove best = finder.findBestMove(PlayField.fromBotField(field, 1), 0);
		Assert.assertEquals(2, best.move);
		Assert.assertEquals(9998.0, best.score);
	}
	
	@Test
	public void shouldFindWinIn3 () {
		MoveFinder finder = new MaxMinMoveFinder(evaluator, MAX_DEPTH);
		Field field = new Field(7,6);
		field.addDisc(2, 2);
		field.addDisc(4, 2);
		field.addDisc(5, 1);
		field.addDisc(2, 1);
		field.addDisc(4, 2);
		field.addDisc(5, 2);
		field.addDisc(2, 1);
		field.addDisc(4, 1);
		field.addDisc(5, 1);
		field.addDisc(2, 1);
		field.addDisc(4, 1);
		field.addDisc(5, 1);
		field.addDisc(2, 2);

		BestMove best = finder.findBestMove(PlayField.fromBotField(field, 1), 0);
		Assert.assertEquals(3, best.move);
		Assert.assertEquals(9996.0, best.score);
	}
	
	@Test
	public void shouldPreventLossIn1 () {
		MoveFinder finder = new MaxMinMoveFinder(evaluator, MAX_DEPTH);
		Field field = new Field(7,6);
		field.addDisc(4, 1);
		field.addDisc(4, 1);
		field.addDisc(4, 1);
		
		BestMove best = finder.findBestMove(PlayField.fromBotField(field, 2), 0);
		Assert.assertEquals(4, best.move);
		Assert.assertEquals(-1.0, best.score);
	}
	
	@Test
	public void shouldPreventLossIn2 () {
		MoveFinder finder = new MaxMinMoveFinder(evaluator, MAX_DEPTH);
		Field field = new Field(7,6);
		field.addDisc(3, 1);
		field.addDisc(4, 1);
		
		BestMove best = finder.findBestMove(PlayField.fromBotField(field, 2), 0);
		Assert.assertEquals(2, best.move);
	}
	
	@Test
	public void shouldJustPlay () {
		MoveFinder finder = new MaxMinMoveFinder(evaluator, MAX_DEPTH);
		Field field = new Field(7,6);
		
		finder.findBestMove(PlayField.fromBotField(field, OTHER), 0);
	}
	
	
	@Before
	public void start() {
		nEval = 0;
		start = System.currentTimeMillis();
	}
	
	@After
	public void done() {
		System.out.println(nEval + " evaluations in " + (System.currentTimeMillis() - start) + "ms");
	}
	
	private class EqualEvaluator implements Evaluator {

		@Override
		public double evaluate(PlayField field, int move) {
			nEval++;
			return 0;
		}
	}

}
