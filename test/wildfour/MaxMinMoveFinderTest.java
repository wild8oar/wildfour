package wildfour;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import bot.Field;
import junit.framework.Assert;
import wildfour.MoveFinder.BestMove;

public class MaxMinMoveFinderTest {
	
	private int nEval;
	private long start;
	
	private static final int MAX_DEPTH = 11;
	
	@Test
	public void shouldFindWinIn1 () {
		MaxMinMoveFinder finder = new MaxMinMoveFinder(MAX_DEPTH);
		Field field = new Field(7,6);
		field.addDisc(4, 1);
		field.addDisc(4, 1);
		field.addDisc(4, 1);
		field.addDisc(5, 2);
		
		BestMove best = finder.findBestMove(PlayField.fromBotField(field, 1));
		Assert.assertEquals(4, best.move);
		Assert.assertEquals(10000.0, best.score);
	}
	
	@Test
	public void shouldFindWinIn2 () {
		MaxMinMoveFinder finder = new MaxMinMoveFinder(MAX_DEPTH);
		Field field = new Field(7,6);
		field.addDisc(3, 1);
		field.addDisc(4, 1);
		
		BestMove best = finder.findBestMove(PlayField.fromBotField(field, 1));
		Assert.assertEquals(2, best.move);
		Assert.assertEquals(9998.0, best.score);
	}
	
	@Test
	public void shouldFindWinIn3 () {
		MaxMinMoveFinder finder = new MaxMinMoveFinder(MAX_DEPTH);
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

		BestMove best = finder.findBestMove(PlayField.fromBotField(field, 1));
		Assert.assertEquals(3, best.move);
		Assert.assertEquals(9996.0, best.score);
	}
	
	@Test @Ignore
	public void shouldMake3 () {
		MaxMinMoveFinder finder = new MaxMinMoveFinder(MAX_DEPTH);
		Field field = new Field(7,6);
		field.addDisc(1, 1);
		field.addDisc(1, 1);

		BestMove best = finder.findBestMove(PlayField.fromBotField(field, 1));
		Assert.assertEquals(1, best.move);
		Assert.assertEquals(1.0, best.score);
	}
	
	@Test
	public void shouldPreventLossIn1 () {
		MaxMinMoveFinder finder = new MaxMinMoveFinder(MAX_DEPTH);
		Field field = new Field(7,6);
		field.addDisc(1, 1);
		field.addDisc(1, 1);
		field.addDisc(1, 1);

		BestMove best = finder.findBestMove(PlayField.fromBotField(field, 2));
		Assert.assertEquals(1, best.move);
		Assert.assertEquals(0.0, best.score);
	}
	
	@Test
	public void shouldPreventLossIn2 () {
		MaxMinMoveFinder finder = new MaxMinMoveFinder(MAX_DEPTH);
		Field field = new Field(7,6);
		field.addDisc(1, 1);
		field.addDisc(2, 1);
		
		BestMove best = finder.findBestMove(PlayField.fromBotField(field, 2));
		Assert.assertEquals(3, best.move);
	}
	
	@Test
	public void shouldJustPlay () {
		MaxMinMoveFinder finder = new MaxMinMoveFinder(MAX_DEPTH);
		Field field = new Field(7,6);
		
		finder.findBestMove(PlayField.fromBotField(field, 2));
	}
	
	@Test
	public void shouldPlayFast () {
		MaxMinMoveFinder finder = new MaxMinMoveFinder(MAX_DEPTH);
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
		BestMove best = finder.findBestMove(PlayField.fromBotField(field, 2));
		Assert.assertEquals(6, best.move);
	}
	
	@Test
	public void shouldPlayFastMirrored () {
		MaxMinMoveFinder finder = new MaxMinMoveFinder(MAX_DEPTH);
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
		
		BestMove best = finder.findBestMove(PlayField.fromBotField(field, 2));
		Assert.assertEquals(0, best.move);
	}
	
	@Test
	public void shouldPreventLossIfAlmostFull () {
		MaxMinMoveFinder finder = new MaxMinMoveFinder(MAX_DEPTH);
		Field field = new Field(7,6);
		field.addDisc(3, 1);
		field.addDisc(4, 2);
		field.addDisc(3, 1);
		field.addDisc(3, 2);
		field.addDisc(3, 1);
		field.addDisc(4, 2);
		field.addDisc(4, 1);
		field.addDisc(4, 2);
		field.addDisc(1, 1);
		field.addDisc(1, 2);
		field.addDisc(1, 1);
		field.addDisc(3, 2);
		
		field.addDisc(3, 1);
		field.addDisc(5, 2);
		field.addDisc(2, 1);
		field.addDisc(0, 2);
		field.addDisc(2, 1);
		field.addDisc(4, 2);
		field.addDisc(4, 1);
		field.addDisc(0, 2);
		field.addDisc(5, 1);
		field.addDisc(6, 2);
		field.addDisc(5, 1);
		field.addDisc(5, 2);
		
		field.addDisc(5, 1);
		field.addDisc(5, 2);
		field.addDisc(0, 1);
		field.addDisc(0, 2);
		field.addDisc(0, 1);
		field.addDisc(1, 2);
		field.addDisc(1, 1);
		field.addDisc(2, 2);
		
		BestMove best = finder.findBestMove(PlayField.fromBotField(field, 1));
		Assert.assertEquals(2, best.move);
	}
	
	
	@Test
	public void shouldNotBeStupid () {
		MaxMinMoveFinder finder = new MaxMinMoveFinder(MAX_DEPTH);
		Field field = new Field(7,6);
		field.addDisc(3, 1);
		field.addDisc(3, 2);
		field.addDisc(3, 1);
		field.addDisc(3, 2);
		field.addDisc(3, 1);
		field.addDisc(3, 2);
		
		field.addDisc(2, 1);
		field.addDisc(4, 2);
		field.addDisc(4, 1);
		field.addDisc(2, 2);
		field.addDisc(4, 1);
		field.addDisc(2, 2);
		
		field.addDisc(2, 1);
		field.addDisc(5, 2);
		field.addDisc(0, 1);
		field.addDisc(1, 2);
		field.addDisc(0, 1);
		
		BestMove best = finder.findBestMove(PlayField.fromBotField(field, 2));
		Assert.assertNotSame(1, best.move);
	}
	
	@Test
	public void shouldNotBeStupid2 () {
		MaxMinMoveFinder finder = new MaxMinMoveFinder(MAX_DEPTH);
		Field field = new Field(7,6);
		field.addDisc(3, 1);
		field.addDisc(3, 2);
		field.addDisc(3, 1);
		field.addDisc(3, 2);
		field.addDisc(3, 1);
		field.addDisc(3, 2);
		
		field.addDisc(4, 1);
		field.addDisc(2, 2);
		field.addDisc(2, 1);
		field.addDisc(2, 2);
		field.addDisc(2, 1);
		
		PlayField.fromBotField(field, 1);
		
		BestMove best = finder.findBestMove(PlayField.fromBotField(field, 2));
		Assert.assertEquals(4, best.move);
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
		Assert.assertTrue("took too long: " + time, time<1350);
	}
	

}
