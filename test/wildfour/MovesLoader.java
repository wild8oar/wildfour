package wildfour;

import static wildfour.PlayField.EMPTY;
import static wildfour.PlayField.ME;
import static wildfour.PlayField.OTHER;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class MovesLoader {
	
	public static List<Move> loadMoves (Map<String, Integer> map) {
		long start = System.currentTimeMillis();
		List<Move> moves = new ArrayList<>();
		for (String enc: map.keySet()) {
			String norm = MapMoveFinder.normalize(enc);
			int move = norm.equals(enc) ? map.get(enc) : 6-map.get(enc);
			moves.add(new Move(norm, move));
		}
		Collections.sort(moves);
		findOrder(moves);
		long time = (System.currentTimeMillis()-start)/1000;
		System.out.println("Loaded " + moves.size() + " moves from map in " + time + "s.");
		return moves;
	}

	private static void findOrder (List<Move> moves) {
		for (int a=0; a<moves.size()-1; a++) {
			Move prev = moves.get(a);
			int prevRd = prev.getRound();
			PlayField field = prev.getField().copy(); // important
			field.addDisc(prev.getMove(), ME);
			char[] fa = field.getField();
			char[] faMirr = mirror(field).getField();
			for (int b=a+1; b<moves.size(); b++) {
				Move curr = moves.get(b);
				if (curr.getRound()-prevRd != 2) {
					continue;
				}
				char[] fb = curr.getField().getField();
				if (follows(fa, fb) || follows(faMirr, fb)) {
					curr.addPrevious(prev);
					prev.setHasNext();
				}
			}
		}
	}
	
	private static PlayField mirror (PlayField field) {
		String enc = MapMoveFinder.encodeField(field);
		String mirr = MapMoveFinder.mirror(enc);
		return MapMoveFinder.decodeField(mirr);
	}
	
	private static boolean follows (char[] prev, char[] curr) {
		for (int i=0; i< prev.length; i++) {
			if (prev[i] != EMPTY && curr[i] != prev[i]) {
				return false;
			}
		}
		return true;
	}
	
	@Test
	public void shouldOrder () {
		Move prev = new Move("00000000000000", 3);
		PlayField currF = prev.getField().copy();
		currF.addDisc(3, ME);
		currF.addDisc(3, OTHER);
		Map<String, Integer> map = new HashMap<>();
		map.put(prev.getEncoded(), 3);
		map.put(MapMoveFinder.encodeField(currF), 2);
		List<Move> moves = loadMoves(map);
		Assert.assertTrue(moves.get(0).hasNext());
		Assert.assertTrue(moves.get(1).getPrevious().size() == 1);
	}
	
	@Test
	public void shouldMirrored () {
		Move prev = new Move("00000000000000", 3);
		PlayField currF = prev.getField().copy();
		currF.addDisc(4, ME);
		currF.addDisc(4, OTHER);
		Map<String, Integer> map = new HashMap<>();
		map.put(prev.getEncoded(), 2);
		map.put(MapMoveFinder.encodeField(currF), 2);
		List<Move> moves = loadMoves(map);
		Assert.assertTrue(moves.get(0).hasNext());
		Assert.assertTrue(moves.get(1).getPrevious().size() == 1);
	}
	
	@Test
	public void shouldNotOrder1 () {
		Move prev = new Move("00000000000000", 3);
		PlayField currF = prev.getField().copy();
		currF.addDisc(3, ME);
		currF.addDisc(3, OTHER);
		Map<String, Integer> map = new HashMap<>();
		map.put(prev.getEncoded(), 2);
		map.put(MapMoveFinder.encodeField(currF), 2);
		List<Move> moves = loadMoves(map);
		Assert.assertFalse(moves.get(0).hasNext());
		Assert.assertTrue(moves.get(1).getPrevious().isEmpty());
	}
	
	@Test
	public void shouldNotOrder () {
		Move prev = new Move("00000BF@0@0H00", 2);
		prev.getField().print();
		PlayField currF = prev.getField().copy();
		currF.addDisc(4, OTHER);
		currF.addDisc(4, ME);
		currF.print();
		Map<String, Integer> map = new HashMap<>();
		map.put(prev.getEncoded(), 2);
		map.put(MapMoveFinder.encodeField(currF), 2);
		List<Move> moves = loadMoves(map);
		Assert.assertFalse(moves.get(0).hasNext());
		Assert.assertTrue(moves.get(1).getPrevious().isEmpty());
	}
}
