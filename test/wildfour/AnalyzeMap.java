package wildfour;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import maps.MapR19D24Y;
import wildfour.MoveFinder.BestMove;

public class AnalyzeMap {
	
	private static final File KNOWN_WINS = new File("known_wins.txt");
	private static final File KNOWN_LOSSES = new File("known_losses.txt");
	
	private static final Map<String, Integer> map = MapR19D24Y.MAP;
	private static final MaxMinMoveFinder analyzer = new MaxMinMoveFinder(42);
	
	private static final Set<String> wins = new HashSet<>();
	private static final Set<String> losses = new HashSet<>();
	
	private static void showSimpleStats(List<Move> moves) throws IOException {
		long start = System.currentTimeMillis();
		int maxRound = 0;
		for (Move move: moves) {
			if (move.getRound() > maxRound) {
				maxRound = move.getRound();
			}
		}
		int[] nums = new int[maxRound];
		int[] term = new int[maxRound];
		int[] win = new int[maxRound];
		int[] loss = new int[maxRound];
		int[] diff = new int[maxRound];
		int nTerm = 0;
		int nWin = 0;
		int nLoss = 0;
		int nDiff = 0;
		int n = 0;
		long last = System.currentTimeMillis();
		for (Move move: moves) {
			int r = move.getRound()-1;
			nums[r]++;
			n++;
			if (!move.hasNext()) {
				term[r]++;
				nTerm++;
				if (wins.contains(move.getEncoded())) {
					win[r]++;
					nWin++;
					continue;
				}
				if (losses.contains(move.getEncoded())) {
					loss[r]++;
					nLoss++;
					continue;
				}
				BestMove best = analyzer.findBestMove(move.getField());
				if (best.score > 9900) {
					win[r]++;
					nWin++;
					addWin(move.getEncoded());
				} else if (best.score < -9900) {
					loss[r]++;
					nLoss++;
					addLoss(move.getEncoded());
				}
				if (best.move != move.getMove()) {
					diff[r]++;
					nDiff++;
				}
			}
			if (System.currentTimeMillis() - last > 10000) {
				long rem = (System.currentTimeMillis() - start) / n * (moves.size() - n);
				System.out.println(n + "/" + moves.size() + " done, " + rem/1000 + "s remaining");
				last = System.currentTimeMillis();
			}
		}
		System.out.println("Round   Moves  Terminal      Wins    Losses Different");
		for (int r=0; r<maxRound; r++) {
			System.out.println(String.format("%2d      %5d     %5d     %5d     %5d     %5d",
					r+1, nums[r], term[r], win[r], loss[r], diff[r]));
		}
		System.out.println("------------------------------------------------------");
		System.out.println((String.format("Total   %5d     %5d     %5d     %5d     %5d",
				moves.size(), nTerm, nWin, nLoss, nDiff)));
		long time = System.currentTimeMillis() - start;
		System.out.println("Analysis time: " + time/1000 + "s");
	}
	
	private static void addWin (String encoded) throws IOException {
		wins.add(encoded);
		try (FileWriter w = new FileWriter(KNOWN_WINS, true)) {
			w.write(encoded + System.lineSeparator());
		}
		
	}
	
	private static void addLoss (String encoded) throws IOException {
		losses.add(encoded);
		try (FileWriter w = new FileWriter(KNOWN_LOSSES, true)) {
			w.write(encoded + System.lineSeparator());
		}
	}

	public static void main(String[] args) throws IOException {
		wins.addAll(Files.readAllLines(KNOWN_WINS.toPath(), Charset.defaultCharset()));
		System.out.println("Known wins loaded: " + wins.size());
		losses.addAll(Files.readAllLines(KNOWN_LOSSES.toPath(), Charset.defaultCharset()));
		System.out.println("Known losses loaded: " + losses.size());
		List<Move> moves = MovesLoader.loadMoves(map);
		showSimpleStats(moves);
	}


}
