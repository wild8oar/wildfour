package wildfour;

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
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(4, 1);
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(4, 1);
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(4, 1);
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(4, 1);
		Assert.assertTrue(field.hasPlayerWon(PlayField.ME));
	}
	
	@Test
	public void shouldNotFindVerticalWin() {
		PlayField field = PlayField.emptyField();
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(4, 1);
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(4, 2);
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(4, 1);
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(4, 1);
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
	}
	
	@Test
	public void shouldFindHorizontalWin() {
		PlayField field = PlayField.emptyField();
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(4, 1);
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(2, 1);
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(3, 1);
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(5, 1);
		Assert.assertTrue(field.hasPlayerWon(PlayField.ME));
	}
	
	@Test
	public void shouldNotFindHorizontalWin() {
		PlayField field = PlayField.emptyField();
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(4, 1);
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(2, 1);
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(3, 2);
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(5, 1);
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
	}
	
//	@Test
//	public void shouldEncodeFeatues() {
//		PlayField field = PlayField.fromBotField(setupTestField(), 1);
//		field.addDisc(3, 1);
//		field.addDisc(3, 2);
//		field.print();
//		double[] feat = field.encodeFeaturesAsNetworkInput();
//		Assert.assertArrayEquals(new double[] {0.0, 0.0, 0.0, 0.2, 0.0, 0.2, 0.0, 0.0}, feat, 0.0);
//		field.addDisc(1, 2);
//		field.print();
//		feat = field.encodeFeaturesAsNetworkInput();
//		Assert.assertArrayEquals(new double[] {0.0, 0.0, 0.2, 0.0, 0.0, 0.2, 0.0, 0.0}, feat, 0.0);
//		field.addDisc(2, 1);
//		field.print();
//		feat = field.encodeFeaturesAsNetworkInput();
//		System.out.println(Arrays.toString(feat));
//		Assert.assertArrayEquals(new double[] {0.4, 0.0, 0.2, 0.0, 0.2, 0.0, 0.0, 0.0}, feat, 0.0);
//	}
	
	@Test
	public void shouldFindDiagonalFromLeftToToRight() {
		PlayField field = PlayField.emptyField();
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(1, 2);
		field.addDisc(1, 1);
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(2, 2);
		field.addDisc(2, 2);
		field.addDisc(2, 1);
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(3, 2);
		field.addDisc(3, 2);
		field.addDisc(3, 2);
		field.addDisc(3, 1);
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(4, 2);
		field.addDisc(4, 2);
		field.addDisc(4, 2);
		field.addDisc(4, 2);
		field.addDisc(4, 1);
		Assert.assertTrue(field.hasPlayerWon(PlayField.ME));
	}
	
	@Test
	public void shouldFindDiagonalFromBottomToToRight() {
		PlayField field = PlayField.emptyField();
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(3, 2);
		field.addDisc(3, 1);
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(4, 2);
		field.addDisc(4, 2);
		field.addDisc(4, 1);
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(5, 2);
		field.addDisc(5, 2);
		field.addDisc(5, 2);
		field.addDisc(5, 1);
		Assert.assertFalse(field.hasPlayerWon(PlayField.ME));
		field.addDisc(6, 2);
		field.addDisc(6, 2);
		field.addDisc(6, 2);
		field.addDisc(6, 2);
		field.addDisc(6, 1);
		Assert.assertTrue(field.hasPlayerWon(PlayField.ME));
	}
	
//	@Test
//	public void shouldCollectAll() {
//		PlayField field = PlayField.emptyField();
//		for (int x=0; x<PlayField.WIDTH; x++) {
//			field.addDisc(x, 1);
//		}
//		for (int y=1; y<PlayField.HEIGHT; y++) {
//			field.addDisc(0, 1);
//			field.addDisc(6, 1);
//		}
//		for (int x=1; x<PlayField.WIDTH-1; x++) {
//			for (int y=1; y<PlayField.HEIGHT-1; y++) {
//				field.addDisc(x, 2);
//			}
//		}
//		for (int x=1; x<PlayField.WIDTH-1; x++) {
//			field.addDisc(x, 1);
//		}
//		field.print();
//		int[][] collected = field.collectAll();
//		for (int[] row: collected) {
//			Assert.assertEquals(1, row[0]);
//			for (int i=1; i<row.length-1; i++) {
//				Assert.assertNotEquals(0, row[i]);
//			}
//			Assert.assertEquals(1, row[row.length-1]);
//		}
//	}
	
	
	private Field setupTestField () {
		Field field = new Field(7,6);
		field.addDisc(4, 1);
		field.addDisc(4, 2);
		field.addDisc(2, 1);
		return field;
	}
}
