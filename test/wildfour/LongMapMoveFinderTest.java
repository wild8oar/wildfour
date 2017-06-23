package wildfour;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;


public class LongMapMoveFinderTest {
	
	LongMapMoveFinder finder = new LongMapMoveFinder(new HashMap<>());
	
	@Test
	public void shouldEncodeEmpty () {
		PlayField field = PlayField.emptyField();
		Assert.assertEquals(0, finder.encodeField(field));
	}
	
	@Test
	public void shouldEncodeSimple1 () {
		PlayField field = PlayField.emptyField();
		field.addDisc(0, PlayField.ME);
		Assert.assertEquals(1, finder.encodeField(field));
	}
	
	@Test
	public void shouldEncodeSimple2 () {
		PlayField field = PlayField.emptyField();
		field.addDisc(1, PlayField.ME);
		Assert.assertEquals(103, finder.encodeField(field));
	}
	
	@Test
	public void shouldEncodeOther1 () {
		PlayField field = PlayField.emptyField();
		field.addDisc(0, PlayField.OTHER);
		Assert.assertEquals(2, finder.encodeField(field));
	}
	
	@Test
	public void shouldEncodeOther2 () {
		PlayField field = PlayField.emptyField();
		field.addDisc(0, PlayField.OTHER);
		field.addDisc(0, PlayField.OTHER);
		Assert.assertEquals(6, finder.encodeField(field));
	}
	
	@Test
	public void shouldEncodeAndDecode () {
		String enc = "000909G@0B0B00";
		PlayField field = MapMoveFinder.decodeField(enc);
		
		long res1 = finder.encodeField(field);
		PlayField res2 = finder.decodeField(res1);
		Assert.assertEquals(res1, finder.encodeField(res2));
		Assert.assertEquals(enc, MapMoveFinder.encodeField(res2));
	}
	
	@Test
	public void shouldMirror () {
		String enc = "000909G@0B0B00";
		PlayField field = MapMoveFinder.decodeField(enc);
		
		long res1 = finder.encodeField(field);
		long res2 = finder.mirror(res1);
		long res3 = finder.mirror(res2);
		Assert.assertEquals(res1, res3);
	}
	
	@Test
	public void verify () {
		String enc = "000B000=00000B";
		PlayField field = MapMoveFinder.decodeField(enc);
		field.print();
		System.out.println("String:");
		MapMoveFinder.decodeField(MapMoveFinder.mirror(enc)).print();
		System.out.println("Long:");
		long aslong = LongMapMoveFinder.encodeField(field);
		LongMapMoveFinder.decodeField(LongMapMoveFinder.mirror(aslong)).print();
	}
	

}
