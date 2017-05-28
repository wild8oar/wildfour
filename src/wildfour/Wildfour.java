package wildfour;

import java.util.Optional;

import bot.BotParser;
import bot.Field;

/**
 * Wildfour main class.
 * 
 */
public class Wildfour {	

	private final MaxMinMoveFinder moveFinder;

	Field field;
	int myId;
	int round;
	int time;

	public Wildfour (Evaluator evaluator) {
		this.moveFinder = new MaxMinMoveFinder(11);
	}

	public void setField(Field f) {
		field = f;
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
		PlayField field = PlayField.fromBotField(this.field, myId);
		Optional<Integer> precomputed = MappedEvaluator.findMove(field);
		if (precomputed.isPresent()) {
			System.err.println("Round " + round + ": using precomputed move");
			return precomputed.get();
		}
		moveFinder.updateMaxDepth(round, time);
		return moveFinder.findBestMove(field).move;
	}

	public static void main(String[] args) throws Exception {
//		Network network;
//		if (args.length > 0) {
//			System.out.println("Loading " + args[0]);
//			Class<?> nwclass = Class.forName("neural1.networks." + args[0]);
//			Method m = nwclass.getDeclaredMethod("getNetwork", Double.TYPE, Double.TYPE);
//			network = (Network) m.invoke(null, 0, 0);
//		} else {
//			network = Learn01.getNetwork(0, 0);
//		}
		//BotParser parser = new BotParser(new Wildfour(new RandomizedNetworkEvaluator(network)));
		BotParser parser = new BotParser(new Wildfour(new EqualEvaluator()));
		parser.run();
	}

}
