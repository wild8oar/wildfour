package wildfour;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javafx.util.Pair;
import maps.KnownWins;
import maps.MapR18D18X;
import wildfour.MoveFinder.BestMove;

public class OptimizeMap {
	
	private static final Map<String, Integer> KNOWN_WINS = KnownWins.MAP;
	private static final Map<String, Integer> MAP = MapR18D18X.MAP;
	private static final String OUTPUT_MAP = "MapR18D18X";
	private static final int LOSS_DEPTH = 20;
	private static final int MIN_ROUND = 13; // don't go earlier than that to recompute losses
	
	private static final Set<String> losses = new HashSet<>();
	private static final Map<String, Pair<Integer, BestMove>> cache = new HashMap<>();

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
		long last = System.currentTimeMillis();
		for (int i=0; i<moves.size(); i++) {
			Move move = moves.get(i);
			if (!move.hasNext() && losses.contains(move.getEncoded())) {
				BestMove best = findBestMove(move, LOSS_DEPTH);
				if (best.score > -9900) {
					System.out.println("Loss detection level not enough, cannot remove certain loss");
					continue;
				}
				removeLoss(move, LOSS_DEPTH, "");
				n++;
				if (System.currentTimeMillis()-last > TimeUnit.MINUTES.toMillis(10)) {
					System.out.println(String.format("Saving temp. map after %d removed losses. %d%% complete", n, (100*i/moves.size()) ));
					MapWriter.writeMap(OUTPUT_MAP, MAP);
					last = System.currentTimeMillis();
				}
			}
		}
		System.out.println("Removed " + n + " positions that lead to loss");
		if (n > 0) {
			MapWriter.writeMap(OUTPUT_MAP, MAP);
		}
		System.out.println();
	}
	
	private static BestMove findBestMove (Move move, int depth) {
		Pair<Integer, BestMove> cached = cache.get(move.getEncoded());
		if (cached != null && (cached.getKey() >= depth || cached.getValue().score > 9900 || cached.getValue().score < -9900)) {
			return cached.getValue();
		}
		MaxMinMoveFinder finder = new MaxMinMoveFinder(depth);
		BestMove best = finder.findBestMove(move.getField());
		cache.put(move.getEncoded(), new Pair<Integer, BestMove>(depth, best));
		return best;
	}
	
	private static void removeLoss (Move move, int lossDepth, String indent) {
		System.out.println(indent + "Removing round " + move.getRound() + " position because it leads to loss");
		removeMove(move);
		if (move.getRound() < MIN_ROUND) {
			System.out.println("Skipping further analysis because round<" + MIN_ROUND + ". Last removed positon: " + move.getEncoded());
			return;
		}
		System.out.println(indent + "Analyzing " + move.getPrevious().size() + " previous positions...");
		for (Move prev: move.getPrevious()) {
			BestMove best = findBestMove(prev, lossDepth + 2);
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
