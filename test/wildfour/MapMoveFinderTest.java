package wildfour;

import org.junit.Assert;
import org.junit.Test;

import bot.Field;

public class MapMoveFinderTest {
	
	private MapMoveFinder finder = new MapMoveFinder(TheMap.MAP);
	
	@Test
	public void shouldFindWinForPlayer1 () {
		Field field = new Field(7,6);
		field.addDisc(3, 1);
		field.addDisc(3, 1);
		field.addDisc(3, 1);
		field.addDisc(1, 2);
		field.addDisc(5, 2);
		field.addDisc(5, 2);
		
		int best = finder.findMove(PlayField.fromBotField(field, 1)).get();
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
		
		int best = finder.findMove(PlayField.fromBotField(field, 2)).get();
		Assert.assertEquals(3, best);
	}
	
	@Test
	public void shouldAvoidLossForPlayer2 () {
		Field field = new Field(7,6);
		field.addDisc(3, 1);
		Assert.assertEquals(3, findMove(PlayField.fromBotField(field, 2)));
		field.addDisc(3, 2);
		field.addDisc(2, 1);
		Assert.assertEquals(4, findMove(PlayField.fromBotField(field, 2)));
	}
	
	@Test
	public void shouldAvoidLossForPlayer2Mirrored () {
		Field field = new Field(7,6);
		field.addDisc(3, 1);
		Assert.assertEquals(3, findMove(PlayField.fromBotField(field, 2)));
		field.addDisc(3, 2);
		field.addDisc(4, 1);
		Assert.assertEquals(2, findMove(PlayField.fromBotField(field, 2)));
	}
	
	private int findMove(PlayField field) {
		return finder.findMove(field).get();
	}

}
