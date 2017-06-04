package wildfour;

import static wildfour.PlayField.EMPTY;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MovesLoader {
	
	public static List<Move> loadMoves (Map<String, Integer> map) {
		List<Move> moves = new ArrayList<>();
		for (String enc: map.keySet()) {
			String norm = MapMoveFinder.normalize(enc);
			int move = norm.equals(enc) ? map.get(enc) : 6-map.get(enc);
			moves.add(new Move(norm, move));
		}
		Collections.sort(moves);
		findOrder(moves);
		System.out.println("Loaded " + moves.size() + " moves from map.");
		return moves;
	}

	private static void findOrder (List<Move> moves) {
		for (int a=0; a<moves.size()-1; a++) {
			Move ma = moves.get(a);
			for (int b=a+1; b<moves.size(); b++) {
				Move mb = moves.get(b);
				if (follows(ma, mb)) {
					mb.addPrevious(ma);
					ma.setHasNext();
				}
			}
		}
	}
	
	private static boolean follows (Move ma, Move mb) {
		if (mb.getRound() - ma.getRound() != 2) {
			return false;
		}
		char[] fa = ma.getField().getField();
		char[] fb = mb.getField().getField();
		for (int i=0; i< fa.length; i++) {
			if (fa[i] != EMPTY && fb[i] != fa[i]) {
				return false;
			}
		}
		return true;
	}
}
