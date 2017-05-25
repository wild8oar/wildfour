package wildfour;
import static wildfour.PlayField.ME;
import static wildfour.PlayField.OTHER;

import java.util.Arrays;

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
		boolean res = field.addDisc(2, ME);
		Assert.assertEquals(ME, field.getDisc(2,4));
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
		PlayField field = PlayField.fromBotField(setupTestField(), 1);
		field.print();
	}
	
	@Test
	public void shouldFindVerticalWin() {
		PlayField field = PlayField.emptyField();
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(4, PlayField.ME);
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(4, PlayField.ME);
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(4, PlayField.ME);
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(4, PlayField.ME);
		Assert.assertTrue(field.hasPlayerWon(PlayField.ME));
		
		field = PlayField.emptyField();
		field.addDisc(6, PlayField.ME);
		field.addDisc(6, PlayField.ME);
		field.addDisc(6, PlayField.ME);
		field.addDisc(6, PlayField.ME);
		Assert.assertTrue(field.hasPlayerWon(PlayField.ME));
		
		field = PlayField.emptyField();
		field.addDisc(6, PlayField.OTHER);
		field.addDisc(6, PlayField.OTHER);
		field.addDisc(6, PlayField.OTHER);
		field.addDisc(6, PlayField.OTHER);
		Assert.assertTrue(field.hasPlayerWon(PlayField.OTHER));
	}
	
	@Test
	public void shouldNotFindVerticalWin() {
		PlayField field = PlayField.emptyField();
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(4, PlayField.ME);
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(4, PlayField.OTHER);
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(4, PlayField.ME);
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(4, PlayField.ME);
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
	}
	
	@Test
	public void shouldFindHorizontalWin() {
		PlayField field = PlayField.emptyField();
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(4, PlayField.ME);
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(2, PlayField.ME);
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(3, PlayField.ME);
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(5, PlayField.ME);
		Assert.assertTrue(field.hasPlayerWon(PlayField.ME));
	}
	
	@Test
	public void shouldNotFindHorizontalWin() {
		PlayField field = PlayField.emptyField();
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(4, PlayField.ME);
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(2, PlayField.ME);
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(3, PlayField.OTHER);
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(5, PlayField.ME);
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
	}
	
	@Test
	public void shouldFindDiagonalFromLeftToToRight() {
		PlayField field = PlayField.emptyField();
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(1, OTHER);
		field.addDisc(1, ME);
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(2, OTHER);
		field.addDisc(2, OTHER);
		field.addDisc(2, ME);
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(3, OTHER);
		field.addDisc(3, OTHER);
		field.addDisc(3, OTHER);
		field.addDisc(3, ME);
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(4, OTHER);
		field.addDisc(4, OTHER);
		field.addDisc(4, OTHER);
		field.addDisc(4, OTHER);
		field.addDisc(4, ME);
		Assert.assertTrue(field.hasPlayerWon(PlayField.ME));
	}
	
	@Test
	public void shouldFindDiagonalFromBottomToToRight() {
		PlayField field = PlayField.emptyField();
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(3, OTHER);
		field.addDisc(3, ME);
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(4, OTHER);
		field.addDisc(4, OTHER);
		field.addDisc(4, ME);
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(5, OTHER);
		field.addDisc(5, OTHER);
		field.addDisc(5, OTHER);
		field.addDisc(5, ME);
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(6, OTHER);
		field.addDisc(6, OTHER);
		field.addDisc(6, OTHER);
		field.addDisc(6, OTHER);
		field.addDisc(6, ME);
		Assert.assertTrue(field.hasPlayerWon(PlayField.ME));
	}
	
	@Test
	public void shouldFindFeatures() {
		PlayField field = PlayField.emptyField();
		Assert.assertArrayEquals(new int[] {0, 0}, field.countThrees());
		field.addDisc(0, ME);
		field.addDisc(1, ME);
		field.addDisc(2, ME);
		Assert.assertArrayEquals(new int[] {1, 0}, field.countThrees());
		field = PlayField.emptyField();
		field.addDisc(2, ME);
		field.addDisc(3, ME);
		field.addDisc(4, ME);
		Assert.assertArrayEquals(new int[] {2, 0}, field.countThrees());
		field = PlayField.emptyField();
		field.addDisc(4, ME);
		field.addDisc(5, ME);
		field.addDisc(6, ME);
		Assert.assertArrayEquals(new int[] {1, 0}, field.countThrees());
		field = PlayField.emptyField();
		field.addDisc(0, OTHER);
		field.addDisc(1, OTHER);
		field.addDisc(2, OTHER);
		Assert.assertArrayEquals(new int[] {0, 1}, field.countThrees());
		field = PlayField.emptyField();
		field.addDisc(2, OTHER);
		field.addDisc(3, OTHER);
		field.addDisc(4, OTHER);
		Assert.assertArrayEquals(new int[] {0, 2}, field.countThrees());
	}
	
	@Test
	public void shouldNotBeFull () {
		PlayField field = PlayField.emptyField();
		for (int y=0; y<PlayField.HEIGHT; y++) {
			field.addDisc(0, ME);
		}
		Assert.assertFalse(field.isFull());
	}
	
	@Test
	public void shouldBeFull () {
		PlayField field = PlayField.emptyField();
		for (int x=0; x<PlayField.WIDTH; x++) {
			for (int y=0; y<PlayField.HEIGHT; y++) {
				field.addDisc(x, ME);
			}
		}
		Assert.assertTrue(field.isFull());
	}

	
	
	private Field setupTestField () {
		Field field = new Field(7,6);
		field.addDisc(4, 1);
		field.addDisc(4, 2);
		field.addDisc(2, 1);
		return field;
	}
}
