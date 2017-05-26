package wildfour;

import org.junit.Test;

import bot.Field;
import junit.framework.Assert;
import wildfour.MoveFinder.BestMove;

public class MinMaxIntegrationTest {
	
	@Test
	public void test() {
		Field field = new Field(7, 6);
		MaxMinMoveFinder finder = new MaxMinMoveFinder(11);
		int round = 1;
		int player = 2;
		while (!field.isFull()) {
			long start = System.currentTimeMillis();
			finder.updateMaxDepth(round, 10000);
			BestMove move = finder.findBestMove(PlayField.fromBotField(field, player));
			long time = System.currentTimeMillis() - start;
			System.out.println("found move in " + time + "ms");
			Assert.assertTrue(time<1300);
			field.addDisc(move.move, player);
			//PlayField.fromBotField(field, 1).print();
			round++;
			player = 3-player;
		}

	}

}
