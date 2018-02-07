package wildfour;

import java.util.Optional;

import riddlesbot.BotParser;
import bot.Field;

/**
 * Wildfour main class.
 * 
 */
public class Wildfour {	

	private final MapMoveFinder mapFinder = new MapMoveFinder(TheMap.MAP);
	private final MaxMinMoveFinder moveFinder = new MaxMinMoveFinder(13);
	
	private PlayField field;
	private int myId;
	private int round;
	private int time;

	public void setField(Field f) {
		field = PlayField.fromBotField(f, myId);
	}
	
	public void setField(riddlesbot.Field riddlesfield) {
		field = PlayField.fromBotField(riddlesfield);
	}

	public void setBotId (int id) {
		myId = id;
	}
	
	public void setRound (int n) {
		round = n;
	}
	
	public void setTime (int t) {
		time = t;
	}

	/**
	 * Makes a turn. Edit this method to make your bot smarter.
	 *
	 * @return The column where the turn was made.
	 */
	public int makeTurn() {
		Optional<Integer> precomputed = mapFinder.findMove(field);
		if (precomputed.isPresent()) {
			System.err.println("Round " + round + ": using precomputed move for " + MapMoveFinder.encodeField(field));
			return precomputed.get();
		}
		moveFinder.updateMaxDepth(round, time);
		return moveFinder.findBestMove(field).move;
	}

	public static void main(String[] args) throws Exception {
		BotParser parser = new BotParser(new Wildfour());
		parser.run();
	}

}
