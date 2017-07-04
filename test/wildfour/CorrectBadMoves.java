package wildfour;
import static wildfour.PlayField.OTHER;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import maps.MapR19D24T;
import wildfour.MoveFinder.BestMove;

public class CorrectBadMoves {
	
	private static final Map<String, Integer> MAP = MapR19D24T.MAP;
	private static final String OUTPUT_MAP = "MapR19D24T";
	private static final MaxMinMoveFinder FINDER = new MaxMinMoveFinder(26);
	private static final MaxMinMoveFinder OPTIMIZER = new MaxMinMoveFinder(28);
	
	private static final Set<String> WINS = new HashSet<>();
	private static final Set<String> LOSSES = new HashSet<>();

	public static void correctBadMoves (List<Move> moves) throws FileNotFoundException {
		int nChg = 0;
		int nComp = 0;
		for (Move move: moves) {
			if (move.hasNext() || WINS.contains(move.getEncoded()) || LOSSES.contains(move.getEncoded())) {
				continue;
			}
			nComp++;
			PlayField myField = move.getField();
			PlayField otherField = myField.getInverted();
			otherField.addDisc(move.getMove(), OTHER);
			BestMove opponentMove = FINDER.findBestMove(otherField);
			if (opponentMove.score > 9900) {
				System.out.println("Moving " + move.getMove() + " on a round " + move.getRound() + " position leads to loss");
				BestMove instead = OPTIMIZER.findBestMove(myField);
				System.out.println("Optimized move: " + instead.move + " (score " + instead.score + ")");
				if (instead.score >= 0 && instead.move != move.getMove()) {
					System.out.println("Changing move");
					removeMove(move);
					MAP.put(move.getEncoded(), instead.move);
					nChg++;
				}
			}
			if (nComp % 10 == 0) {
				System.out.println("Changed " + nChg + " of " + nComp + " moves");
			}
		}
		if (nChg > 0) {
			MapWriter.writeMap(OUTPUT_MAP, MAP);
		}
	}
	
	private static void removeMove (Move move) {
		MAP.remove(move.getEncoded());
		MAP.remove(MapMoveFinder.mirror(move.getEncoded()));
	}
	

	public static void main (String[] args) throws IOException {
		WINS.addAll(Files.readAllLines(new File("known_wins.txt").toPath(), Charset.defaultCharset()));
		System.out.println("Known wins loaded: " + WINS.size());
		LOSSES.addAll(Files.readAllLines(new File("known_losses.txt").toPath(), Charset.defaultCharset()));
		System.out.println("Known losses loaded: " + LOSSES.size());
		List<Move> moves = MovesLoader.loadMoves(MAP);
		correctBadMoves(moves);
	}
}
