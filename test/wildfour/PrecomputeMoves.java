package wildfour;
import static wildfour.MapMoveFinder.encodeField;
import static wildfour.PlayField.ME;
import static wildfour.PlayField.OTHER;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import maps.MapR19D24Y;

public class PrecomputeMoves {
	
	private static final int MAX_ROUNDS = 18;
	private static final int MAX_DEPTH = 18;
		
	private static final MaxMinMoveFinder finder = new MaxMinMoveFinder(MAX_DEPTH);
	private static final Map<String, Integer> inmap = MapR19D24Y.MAP;
	private static final MapMoveFinder mapFinder = new MapMoveFinder(inmap);
	private static final Map<String, Integer> newMap = new HashMap<>();
	private static final MapMoveFinder newFinder = new MapMoveFinder(newMap);
	private static final Map<String, Integer> quickMap = new HashMap<>();
	private static final MapMoveFinder quickFinder = new MapMoveFinder(quickMap);
	
	private static int nComputed = 0;
	private static int nStored = 0;
	private static int nKept = 0;
	private static int nQuick = 0;
	
	private static int[] nEval = new int[MAX_ROUNDS+1];
	private static long[] timeSum = new long[MAX_ROUNDS+1];
	
	private static void precomputeForPlayer1 (PlayField field, int round) {
		int move = findBestMove(field, round);
		field.addDisc(move, ME);
		round++;
		if (round >= MAX_ROUNDS || field.hasPlayerWon(ME) || field.isFull()) {
			field.removeDisc(move);
			return;
		}
		for (int i=0; i<PlayField.WIDTH; i++) {
			if (field.addDisc(i, OTHER)) {
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
		if (round >= MAX_ROUNDS) {
			return;
		}
		for (int i=0; i<PlayField.WIDTH; i++) {
			if (field.addDisc(i, OTHER)) {
				precomputeForPlayer1(field, round+1);
				field.removeDisc(i);
			}			
		}
	}
	
	private static int findBestMove (PlayField field, int round) {
		nComputed++;
		Optional<Integer> stored = newFinder.findMove(field);
		if (stored.isPresent()) {
			return stored.get();
		}
		Optional<Integer> quick = quickFinder.findMove(field);
		if (quick.isPresent()) {
			return quick.get();
		}
		Optional<Integer> existing = mapFinder.findMove(field);
		if (existing.isPresent()) {
			String key = encodeField(field);
			if (!newMap.containsKey(key)) {
				newMap.put(encodeField(field), existing.get());
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
			quickMap.put(key, move);
			return move;
		}
		if (time > 10000) {
			System.out.println("Evaluating round " + round + " move took " + (time/1000) + "s");
		}
		newMap.put(key, move);
		nStored++;
		if (nStored % 10 == 0) {
			System.out.println(nComputed + " / " + nStored);
		}
		return move;
	}
	
	private static void saveMap() throws FileNotFoundException {
		if (newMap.isEmpty()) {
			return;
		}
		MapWriter.writeMap(String.format("MapR%02dD%02dY", MAX_ROUNDS, MAX_DEPTH), newMap);
	}

	public static void main (String[] args) throws Exception {
		nComputed = nStored = 0;
		long start = System.currentTimeMillis();
		PlayField field = PlayField.emptyField();
		precomputeForPlayer1(field, 1);
		System.out.println("Finished player 1 in:   " + (System.currentTimeMillis() - start)/1000 + "s");
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
