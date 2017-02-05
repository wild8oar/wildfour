package wildfour;

import org.junit.Assert;
import org.junit.Test;

import bot.Field;

public class PlayFieldTest {
	
	@Test
	public void shouldLoadFieldForBot1() {
		PlayField field = PlayField.fromBotField(setupTestField(), 1);
		Assert.assertEquals(PlayField.ME, field.getDisc(4,5));
		Assert.assertEquals(PlayField.OTHER, field.getDisc(4,4));
	}
	
	@Test
	public void shouldLoadFieldForBot2() {
		PlayField field = PlayField.fromBotField(setupTestField(), 2);
		Assert.assertEquals(PlayField.OTHER, field.getDisc(4,5));
		Assert.assertEquals(PlayField.ME, field.getDisc(4,4));
	}
	
	@Test
	public void shouldAddDisc () {
		PlayField field = PlayField.fromBotField(setupTestField(), 1);
		boolean res = field.addDisc(2, PlayField.ME);
		Assert.assertEquals(PlayField.ME, field.getDisc(2,4));
		Assert.assertTrue(res);
	}
	
	@Test
	public void shouldOnlyAdd6Discs () {
		PlayField field = PlayField.fromBotField(setupTestField(), 1);
		for (int i=0; i<6; i++) {
			boolean res = field.addDisc(1, PlayField.ME);
			Assert.assertTrue(res);
		}
		boolean res = field.addDisc(1, PlayField.ME);
		Assert.assertFalse(res);
	}
	
	@Test
	public void shouldRemoveDisc () {
		PlayField field = PlayField.fromBotField(setupTestField(), 1);
		boolean res = field.removeDisc(2);
		Assert.assertEquals(PlayField.EMPTY, field.getDisc(2,5));
		Assert.assertTrue(res);
	}
	
	@Test
	public void shouldNotRemoveDisc () {
		PlayField field = PlayField.fromBotField(setupTestField(), 1);
		boolean res = field.removeDisc(1);
		Assert.assertFalse(res);
	}


	@Test
	public void shouldPrintField() {
		PlayField.fromBotField(setupTestField(), 1).print();
	}
	
	@Test
	public void shouldFindVerticalWin() {
		PlayField field = PlayField.emptyField();
		Assert.assertFalse(field.haveIWon());
		field.addDisc(4, 1);
		Assert.assertFalse(field.haveIWon());
		field.addDisc(4, 1);
		Assert.assertFalse(field.haveIWon());
		field.addDisc(4, 1);
		Assert.assertFalse(field.haveIWon());
		field.addDisc(4, 1);
		Assert.assertTrue(field.haveIWon());
	}
	
	@Test
	public void shouldNotFindVerticalWin() {
		PlayField field = PlayField.emptyField();
		Assert.assertFalse(field.haveIWon());
		field.addDisc(4, 1);
		Assert.assertFalse(field.haveIWon());
		field.addDisc(4, 2);
		Assert.assertFalse(field.haveIWon());
		field.addDisc(4, 1);
		Assert.assertFalse(field.haveIWon());
		field.addDisc(4, 1);
		Assert.assertFalse(field.haveIWon());
	}
	
	@Test
	public void shouldFindHorizontalWin() {
		PlayField field = PlayField.emptyField();
		Assert.assertFalse(field.haveIWon());
		field.addDisc(4, 1);
		Assert.assertFalse(field.haveIWon());
		field.addDisc(2, 1);
		Assert.assertFalse(field.haveIWon());
		field.addDisc(3, 1);
		Assert.assertFalse(field.haveIWon());
		field.addDisc(5, 1);
		Assert.assertTrue(field.haveIWon());
	}
	
	@Test
	public void shouldNotFindHorizontalWin() {
		PlayField field = PlayField.emptyField();
		Assert.assertFalse(field.haveIWon());
		field.addDisc(4, 1);
		Assert.assertFalse(field.haveIWon());
		field.addDisc(2, 1);
		Assert.assertFalse(field.haveIWon());
		field.addDisc(3, 2);
		Assert.assertFalse(field.haveIWon());
		field.addDisc(5, 1);
		Assert.assertFalse(field.haveIWon());
	}
	
	@Test
	public void shouldFindDiagonalFromLeftToToRight() {
		PlayField field = PlayField.emptyField();
		Assert.assertFalse(field.haveIWon());
		field.addDisc(1, 2);
		field.addDisc(1, 1);
		Assert.assertFalse(field.haveIWon());
		field.addDisc(2, 2);
		field.addDisc(2, 2);
		field.addDisc(2, 1);
		Assert.assertFalse(field.haveIWon());
		field.addDisc(3, 2);
		field.addDisc(3, 2);
		field.addDisc(3, 2);
		field.addDisc(3, 1);
		Assert.assertFalse(field.haveIWon());
		field.addDisc(4, 2);
		field.addDisc(4, 2);
		field.addDisc(4, 2);
		field.addDisc(4, 2);
		field.addDisc(4, 1);
		Assert.assertTrue(field.haveIWon());
	}
	
	@Test
	public void shouldFindDiagonalFromBottomToToRight() {
		PlayField field = PlayField.emptyField();
		Assert.assertFalse(field.haveIWon());
		field.addDisc(3, 2);
		field.addDisc(3, 1);
		Assert.assertFalse(field.haveIWon());
		field.addDisc(4, 2);
		field.addDisc(4, 2);
		field.addDisc(4, 1);
		Assert.assertFalse(field.haveIWon());
		field.addDisc(5, 2);
		field.addDisc(5, 2);
		field.addDisc(5, 2);
		field.addDisc(5, 1);
		Assert.assertFalse(field.haveIWon());
		field.addDisc(6, 2);
		field.addDisc(6, 2);
		field.addDisc(6, 2);
		field.addDisc(6, 2);
		field.addDisc(6, 1);
		Assert.assertTrue(field.haveIWon());
	}
	
	private Field setupTestField () {
		Field field = new Field(7,6);
		field.addDisc(4, 1);
		field.addDisc(4, 2);
		field.addDisc(2, 1);
		return field;
	}
}
