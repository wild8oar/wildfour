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
	
	@Test
	public void shouldFindWinIn1 () {
		MoveFinder finder = new MaxMinMoveFinder(MAX_DEPTH);
		Field field = new Field(7,6);
		field.addDisc(4, 1);
		field.addDisc(4, 1);
		field.addDisc(4, 1);
		field.addDisc(5, 2);
		
		BestMove best = finder.findBestMove(PlayField.fromBotField(field, 1), 0);
		Assert.assertEquals(4, best.move);
		Assert.assertEquals(10000.0, best.score);
	}
	
	@Test
	public void shouldFindWinIn2 () {
		MoveFinder finder = new MaxMinMoveFinder(MAX_DEPTH);
		Field field = new Field(7,6);
		field.addDisc(3, 1);
		field.addDisc(4, 1);
		
		BestMove best = finder.findBestMove(PlayField.fromBotField(field, 1), 0);
		Assert.assertEquals(2, best.move);
		Assert.assertEquals(9998.0, best.score);
	}
	
	@Test
	public void shouldFindWinIn3 () {
		MoveFinder finder = new MaxMinMoveFinder(MAX_DEPTH);
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
		MoveFinder finder = new MaxMinMoveFinder(MAX_DEPTH);
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
		MoveFinder finder = new MaxMinMoveFinder(MAX_DEPTH);
		Field field = new Field(7,6);
		field.addDisc(3, 1);
		field.addDisc(4, 1);
		
		BestMove best = finder.findBestMove(PlayField.fromBotField(field, 2), 0);
		Assert.assertEquals(2, best.move);
	}
	
	@Test
	public void shouldJustPlay () {
		MoveFinder finder = new MaxMinMoveFinder(MAX_DEPTH);
		Field field = new Field(7,6);
		
		finder.findBestMove(PlayField.fromBotField(field, 2), 0);
	}
	
	@Test
	public void shouldPlayFast () {
		MoveFinder finder = new MaxMinMoveFinder(MAX_DEPTH);
		Field field = new Field(7,6);
		field.addDisc(3, 1);
		field.addDisc(3, 2);
		field.addDisc(4, 1);
		field.addDisc(2, 2);
		field.addDisc(6, 1);
		field.addDisc(5, 2);
		field.addDisc(4, 1);
		field.addDisc(4, 2);
		field.addDisc(6, 1);
		field.addDisc(3, 2);
		field.addDisc(6, 1);
		
		BestMove best = finder.findBestMove(PlayField.fromBotField(field, 2), 0);
		Assert.assertEquals(6, best.move);
	}
	
	@Test
	public void shouldPlayFastMirrored () {
		MoveFinder finder = new MaxMinMoveFinder(MAX_DEPTH);
		Field field = new Field(7,6);
		field.addDisc(3, 1);
		field.addDisc(3, 2);
		field.addDisc(2, 1);
		field.addDisc(4, 2);
		field.addDisc(0, 1);
		field.addDisc(1, 2);
		field.addDisc(2, 1);
		field.addDisc(2, 2);
		field.addDisc(0, 1);
		field.addDisc(3, 2);
		field.addDisc(0, 1);
		
		BestMove best = finder.findBestMove(PlayField.fromBotField(field, 2), 0);
		Assert.assertEquals(0, best.move);
	}
	
	
	
	@Before
	public void start() {
		nEval = 0;
		start = System.currentTimeMillis();
	}
	
	@After
	public void done() {
		long time = System.currentTimeMillis() - start;
		System.out.println(nEval + " evaluations in " + time + "ms");
		Assert.assertTrue("took too long: " + time, time<1000);
	}
	

}
