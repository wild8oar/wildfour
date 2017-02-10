package wildfour;

import java.lang.reflect.Method;

import bot.BotParser;
import bot.Field;
import neural1.Network;
import neural1.networks.Learn01;

/**
 * Wildfour main class.
 * 
 */
public class Wildfour {	

	private final MoveFinder moveFinder;

	Field field;
	int myId;

	public Wildfour (Evaluator evaluator) {
		this.moveFinder = new MaxMinMoveFinder(evaluator, 5);
	}

	public void setField(Field f) {
		field = f;
	}

	public void setBotId (int id) {
		myId = id;
	}

	/**
	 * Makes a turn. Edit this method to make your bot smarter.
	 *
	 * @return The column where the turn was made.
	 */
	public int makeTurn() {
		PlayField field = PlayField.fromBotField(this.field, myId);
		return moveFinder.findBestMove(field, 0).move;
	}

	public static void main(String[] args) throws Exception {
		Network network;
		if (args.length > 0) {
			System.out.println("Loading " + args[0]);
			Class<?> nwclass = Class.forName("neural1.networks." + args[0]);
			Method m = nwclass.getDeclaredMethod("getNetwork", Double.TYPE, Double.TYPE);
			network = (Network) m.invoke(null, 0, 0);
		} else {
			network = Learn01.getNetwork(0, 0);
		}
		BotParser parser = new BotParser(new Wildfour(new RandomizedNetworkEvaluator(network)));
		parser.run();
	}

}
