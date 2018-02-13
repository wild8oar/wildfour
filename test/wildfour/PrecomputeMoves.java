package wildfour;
import static wildfour.MapMoveFinder.encodeField;
import static wildfour.PlayField.ME;
import static wildfour.PlayField.OTHER;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import maps.MapDXP2;

public class PrecomputeMoves {
	
	private static final int MAX_ROUNDS = 19;
	private static final int MAX_DEPTH = 24;
	private static final MaxMinMoveFinder finder = new MaxMinMoveFinder(MAX_DEPTH);
	private static final Map<String, Integer> inmap = MapDXP2.MAP; // new HashMap<>();
	private static final String LETTER = "EP2";
	
	private static final MapMoveFinder mapFinder = new MapMoveFinder(inmap);
	private static final Map<String, Integer> newMap = new HashMap<>();

	private static final Set<String> SEEN = new HashSet<>();
	
	private static int nComputed = 0;
	private static int nStored = 0;
	private static int nKept = 0;
	private static int nQuick = 0;
	private static String current = "started";
	
	private static int[] nEval = new int[MAX_ROUNDS+1];
	private static long[] timeSum = new long[MAX_ROUNDS+1];
	
	private static void precomputeForPlayer1 (PlayField field, int round) {
		if (alreadyEvaluated(field)) {
			return;
		}
		int move = findBestMove(field, round);
		field.addDisc(move, ME);
		round++;
		if (round >= MAX_ROUNDS || field.hasPlayerWon(ME) || field.isFull()) {
			field.removeDisc(move);
			return;
		}
		for (int i=0; i<PlayField.WIDTH; i++) {
			if (field.addDisc(i, OTHER)) {
				if (round == 2) {
					current = "Player 1, move " + i;
				}
				if (field.hasPlayerWon(OTHER)) {
					throw new IllegalStateException("Other player must not win!");
				}
				if (!field.isFull()) {
					precomputeForPlayer1(field, round+1);
				}
				field.removeDisc(i);
			}			
		}
		field.removeDisc(move);
	}
	
	private static void precomputeForPlayer2 (PlayField field, int round) {
		if (round > MAX_ROUNDS) {
			return;
		}
		for (int i=0; i<PlayField.WIDTH; i++) {
			if (field.addDisc(i, OTHER)) {
				current = "Player 2, move " + i;
				precomputeForPlayer1(field, round+1);
				field.removeDisc(i);
			}			
		}
	}
	
	private static boolean alreadyEvaluated (PlayField field) {
		String norm = MapMoveFinder.encodeNormalized(field);
		return !SEEN.add(norm);
	}
	
	private static int findBestMove (PlayField field, int round) {
		nComputed++;
		Optional<Integer> existing = mapFinder.findMove(field);
		if (existing.isPresent()) {
			String key = encodeField(field);
			if (!newMap.containsKey(key)) {
				newMap.put(key, existing.get());
				nKept++;
			}
			return existing.get();
		}
		nEval[round]++;
		long start = System.currentTimeMillis();
		int move = finder.findBestMove(field).move;
		long time = System.currentTimeMillis()-start;
		timeSum[round] += time;
		if (move == -1) {
			throw new IllegalStateException("Illegal move: -1");
		}
		String key = encodeField(field);
		if (time < 500) {
			nQuick++;
			return move;
		}
		if (time > 10000) {
			System.out.println("Evaluating round " + round + " move took " + (time/1000) + "s");
		}
		newMap.put(key, move);
		nStored++;
		if (nStored % 10 == 0) {
			System.out.println(nComputed + " / " + nStored + " (current: " + current + ")");
		}
		return move;
	}
	
	private static void saveMap() throws FileNotFoundException {
		if (newMap.isEmpty()) {
			return;
		}
		MapWriter.writeMap(String.format("MapR%02dD%02d%s", MAX_ROUNDS, MAX_DEPTH, LETTER), newMap);
	}

	public static void main (String[] args) throws Exception {
		nComputed = nStored = 0;
		long start = System.currentTimeMillis();
		PlayField field = PlayField.emptyField();
		//precomputeForPlayer1(field, 1);
		//System.out.println("Finished player 1 in:   " + (System.currentTimeMillis() - start)/1000 + "s");
		precomputeForPlayer2(field, 1);
		saveMap();
		System.out.println("Moves computed:           " + nComputed);
		System.out.println("New moves stored:         " + nStored);
		System.out.println("Moves kept from input:    " + nKept);
		System.out.println("Quick finds not stored:   " + nQuick);
		System.out.println("Completed in:             " + (System.currentTimeMillis() - start)/1000 + "s");
		System.out.println("Timing stats:");
		for (int i=1; i<=MAX_ROUNDS; i++) {
			if (nEval[i] > 0) {
				System.out.println(String.format("Round %2d: %6d evals, avg %5dms", i, nEval[i], timeSum[i]/nEval[i]));
			}
		}
	}



}
