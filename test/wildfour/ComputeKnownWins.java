package wildfour;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import maps.KnownWins;
import wildfour.MoveFinder.BestMove;

public class ComputeKnownWins {
	
	private static final Map<String, Integer> KNOWN_WINS = KnownWins.MAP;
	private static final Set<String> WINS = new HashSet<>();
	private static final MaxMinMoveFinder optimizer = new MaxMinMoveFinder(20);

	public static void main(String[] args) throws IOException {
		WINS.addAll(Files.readAllLines(new File("known_wins.txt").toPath(), Charset.defaultCharset()));
		int nComp = 0;
		int nFound = 0;
		for (String win: WINS) {
			if (!KNOWN_WINS.containsKey(win)) {
				BestMove best = optimizer.findBestMove(MapMoveFinder.decodeField(win));
				nComp++;
				if (best.score < 9900) {
					System.out.println("WARNING: no win found, incease search depth");
					continue;
				}
				nFound++;
				KNOWN_WINS.put(win, best.move);
			}
		}
		System.out.println("Computed " + nComp + " moves, found " + nFound + " wins.");
		MapWriter.writeMap("KnownWins", KNOWN_WINS);
	}

}
