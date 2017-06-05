package wildfour;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import maps.KnownWins;
import maps.MapR16D18;
import wildfour.MoveFinder.BestMove;

public class OptimizeMap {
	
	private static final Map<String, Integer> KNOWN_WINS = KnownWins.MAP;
	private static final Map<String, Integer> MAP = MapR16D18.MAP;
	private static final String OUTPUT_MAP = "MapR16D18X";
	private static final int LOSS_DEPTH = 18;
	
	private static final Set<String> losses = new HashSet<>();

	public static void optimizeWins (List<Move> moves) throws FileNotFoundException {
		int nChg = 0;
		for (Move move: moves) {
			if (!move.hasNext() && KNOWN_WINS.containsKey(move.getEncoded())) {
				int win = KNOWN_WINS.get(move.getEncoded());
				if (win != move.getMove()) {
					removeMove(move);
					MAP.put(move.getEncoded(), win);
					nChg++;
				}
			}
		}
		System.out.println("Changed " + nChg + " moves to wins.");
		if (nChg > 0) {
			MapWriter.writeMap(OUTPUT_MAP, MAP);
		}
	}
	
	private static void removeMove (Move move) {
		MAP.remove(move.getEncoded());
		MAP.remove(MapMoveFinder.mirror(move.getEncoded()));
	}
	
	public static void optimizeLosses (List<Move> moves) throws IOException {
		losses.addAll(Files.readAllLines(new File("known_losses.txt").toPath(), Charset.defaultCharset()));
		int n = 0;
		for (Move move: moves) {
			if (!move.hasNext() && losses.contains(move.getEncoded())) {
				MaxMinMoveFinder finder = new MaxMinMoveFinder(LOSS_DEPTH);
				BestMove best = finder.findBestMove(move.getField());
				if (best.score > -9900) {
					System.out.println("Loss detection level not enough, cannot remove certain loss");
					continue;
				}
				removeLoss(move, LOSS_DEPTH, "");
				n++;
			}
		}
		System.out.println("Removed " + n + " positions that lead to loss");
		if (n > 0) {
			MapWriter.writeMap(OUTPUT_MAP, MAP);
		}
		System.out.println();
	}
	
	private static void removeLoss (Move move, int lossDepth, String indent) {
		System.out.println(indent + "Removing round " + move.getRound() + " position because it leads to loss");
		removeMove(move);
		System.out.println(indent + "Analyzing " + move.getPrevious().size() + " previous positions...");
		MaxMinMoveFinder finder = new MaxMinMoveFinder(lossDepth + 2);
		for (Move prev: move.getPrevious()) {
			BestMove best = finder.findBestMove(prev.getField());
			if (best.score < -9900) {
				removeLoss(prev, lossDepth+2, indent + "  ");
			} else if (best.move == prev.getMove()) {
				System.out.println("Current:" + prev.getEncoded() + " or " + MapMoveFinder.mirror(prev.getEncoded()));
				move.getField().print();
				System.out.println("Previous:" + prev.getEncoded() 
				+ " or " + MapMoveFinder.mirror(prev.getEncoded()) + ": " + prev.getMove());
				prev.getField().print();
				throw new IllegalStateException("Previous move must also be loss or change!");
			} else {
				System.out.println(indent + "Changing previous move");
				removeMove(prev);
				MAP.put(prev.getEncoded(), best.move);
			}
		}
	}

	public static void main (String[] args) throws IOException {
		List<Move> moves = MovesLoader.loadMoves(MAP);
		optimizeWins(moves);
		optimizeLosses(moves);
	}
}
