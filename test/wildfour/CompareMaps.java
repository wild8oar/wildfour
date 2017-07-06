package wildfour;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import maps.MapR19D24T;
import maps.MapR19D24Y;

public class CompareMaps {
	
	private static final Map<String, Integer> MAP1 = MapR19D24Y.MAP;
	private static final Map<String, Integer> MAP2 = MapR19D24T.MAP;
	
	private static int determineMaxRounds(List<Move> moves1, List<Move> moves2) {
		int m1 = moves1.stream().mapToInt(Move::getRound).max().getAsInt();
		int m2 = moves2.stream().mapToInt(Move::getRound).max().getAsInt();
		return Math.max(m1, m2);
	}
	
	public static void main(String[] args) {
		List<Move> moves1 = MovesLoader.loadMoves(MAP1);
		List<Move> moves2 = MovesLoader.loadMoves(MAP2);
		int maxRounds = determineMaxRounds(moves1, moves2);
		int[] only1 = new int[maxRounds];
		int[] only2 = new int[maxRounds];
		int[] both = new int[maxRounds];
		int[] different = new int[maxRounds];
		List<String> diffs = new ArrayList<>();
		for (Move m: moves1) {
			int r = m.getRound()-1;
			String enc = m.getEncoded();
			if (MAP2.containsKey(enc)) {
				both[r]++;
				if (MAP1.get(enc) != MAP2.get(enc)) {
					different[r]++;
					if (diffs.size() < 6) {
						diffs.add(enc);
					}
				}
			} else {
				only1[r]++;
			}
		}
		for (Move m: moves2) {
			int r = m.getRound()-1;
			String enc = m.getEncoded();
			if (!MAP1.containsKey(enc)) {
				only2[r]++;
			}
		}
		System.out.println("Round Only1   Only2     Both  Different");
		System.out.println("=======================================");
		for (int r=0; r<maxRounds; r++) {
			System.out.println(String.format("%2d   %5d   %5d    %5d   %5d", r+1, only1[r], only2[r], both[r], different[r]));
		}
		for (String enc: diffs) {
			System.out.println();
			MapMoveFinder.decodeField(enc).print();
			System.out.println("Move in map1: " + MAP1.get(enc));
			System.out.println("Move in map2: " + MAP2.get(enc));
		}
	}


}
