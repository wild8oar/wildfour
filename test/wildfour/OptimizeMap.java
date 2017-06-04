package wildfour;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import maps.KnownWins;
import maps.MapR16D18;

public class OptimizeMap {
	
	private static final Map<String, Integer> KNOWN_WINS = KnownWins.MAP;
	private static final Map<String, Integer> MAP = MapR16D18.MAP;
	private static final String OUTPUT_MAP = "MapR16D18X";

	public static void optimizeWins (List<Move> moves) throws FileNotFoundException {
		int nChg = 0;
		int nFlip = 0;
		for (Move move: moves) {
			if (!move.hasNext() && KNOWN_WINS.containsKey(move.getEncoded())) {
				int win = KNOWN_WINS.get(move.getEncoded());
				if (win != move.getMove()) {
//					System.out.println("Changed move from " + move.getMove()
//					+ " to " + win + " (round " + move.getRound() + ", flipped=" + move.isFlipped() + ")");
//					if (!move.isFlipped()) {
//						move.getField().print();
//						System.out.println("Old move: " + move.getMove());
//						System.out.println("Win move: " + win);
//					}
					MAP.put(move.getEncoded(), win);
					nChg++;
					if (move.isFlipped()) {
						nFlip++;
					}
				}
			}
		}
		System.out.println("Changed " + nChg + " moves to wins (" + (nChg-nFlip) + " not flipped moves).");
		if (nChg > 0) {
			MapWriter.writeMap(OUTPUT_MAP, MAP);
		}
	}
	
	public static void main (String[] args) throws FileNotFoundException {
		List<Move> moves = MovesLoader.loadMoves(MAP);
		optimizeWins(moves);
	}
}
