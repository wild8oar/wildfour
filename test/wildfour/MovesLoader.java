package wildfour;

import static wildfour.PlayField.EMPTY;
import static wildfour.PlayField.ME;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
			Move ma = moves.get(a);
			int roundA = ma.getRound();
			ma.getField().addDisc(ma.getMove(), ME);
			char[] fa = ma.getField().getField();
			char[] faMirr = ma.mirrored().getField().getField();
			ma.getField().removeDisc(ma.getMove());
			for (int b=a+1; b<moves.size(); b++) {
				Move mb = moves.get(b);
				if (mb.getRound()-roundA != 2) {
					continue;
				}
				char[] fb = mb.getField().getField();
				if (follows(fa, fb) || follows(faMirr, fb)) {
					mb.addPrevious(ma);
					ma.setHasNext();
				}
			}
		}
	}
	
	private static boolean follows (char[] fa,char[] fb) {
		for (int i=0; i< fa.length; i++) {
			if (fa[i] != EMPTY && fb[i] != fa[i]) {
				return false;
			}
		}
		return true;
	}
}
