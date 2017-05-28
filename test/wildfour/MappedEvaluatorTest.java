package wildfour;

import org.junit.Assert;
import org.junit.Test;

import bot.Field;

public class MappedEvaluatorTest {
	
		
	@Test
	public void shouldFindWinForPlayer1 () {
		Field field = new Field(7,6);
		field.addDisc(3, 1);
		field.addDisc(3, 1);
		field.addDisc(3, 1);
		field.addDisc(1, 2);
		field.addDisc(5, 2);
		field.addDisc(5, 2);
		
		int best = MappedEvaluator.findMove(PlayField.fromBotField(field, 1)).get();
		Assert.assertEquals(3, best);
	}
	
	@Test
	public void shouldFindWinForPlayer2 () {
		Field field = new Field(7,6);
		field.addDisc(0, 1);
		field.addDisc(5, 1);
		field.addDisc(5, 1);
		field.addDisc(0, 1);
		field.addDisc(3, 2);
		field.addDisc(3, 2);
		field.addDisc(3, 2);
		
		int best = MappedEvaluator.findMove(PlayField.fromBotField(field, 2)).get();
		Assert.assertEquals(3, best);
	}
	
	@Test
	public void shouldAvoidLossForPlayer2 () {
		Field field = new Field(7,6);
		field.addDisc(3, 1);
		Assert.assertEquals(3, findMove(PlayField.fromBotField(field, 2)));
		field.addDisc(3, 2);
		field.addDisc(2, 1);
		
	}
	
	private int findMove(PlayField field) {
		return MappedEvaluator.findMove(field).get();
	}

}
