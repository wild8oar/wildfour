package wildfour;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import maps.KnownWins;
import wildfour.MoveFinder.BestMove;

public class ComputeKnownWins {
	
	private static final Map<String, Integer> KNOWN_WINS = KnownWins.MAP;
	private static final Set<String> WINS = new HashSet<>();
	private static final MaxMinMoveFinder optimizer = new MaxMinMoveFinder(42);

	public static void main(String[] args) throws IOException {
		WINS.addAll(Files.readAllLines(new File("known_wins.txt").toPath(), Charset.defaultCharset()));
		System.out.println("Analyzing " + WINS.size() + " known wins");
		System.out.println("Map already contains " + KNOWN_WINS.keySet().size() + " entries");
		int nComp = 0;
		int nFound = 0;
		long last = System.currentTimeMillis();
		for (String win: WINS) {
			String norm = MapMoveFinder.normalize(win);
			if (!KNOWN_WINS.containsKey(norm)) {
				BestMove best = optimizer.findBestMove(MapMoveFinder.decodeField(norm));
				nComp++;
				if (best.score < 9900) {
					System.out.println("WARNING: no win found, increase search depth");
					continue;
				}
				nFound++;
				KNOWN_WINS.put(norm, best.move);
				if (System.currentTimeMillis()-last > TimeUnit.MINUTES.toMillis(10)) {
					MapWriter.writeMap("KnownWins", KNOWN_WINS);
					last = System.currentTimeMillis();
				}
			}
		}
		System.out.println("Computed " + nComp + " moves, found " + nFound + " wins.");
		MapWriter.writeMap("KnownWins", KNOWN_WINS);
	}

}
