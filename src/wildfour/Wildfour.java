package wildfour;
// // Copyright 2015 theaigames.com (developers@theaigames.com)

import java.lang.reflect.Method;

import bot.BotParser;
import bot.Field;
import neural1.Network;
import neural1.networks.Learn1000;

/**
 * Wildfour main class.
 * 
 */
public class Wildfour {	
	
	private final Evaluator evaluator;
	
     Field field;
     int myId;
     
     public Wildfour (Evaluator evaluator) {
		this.evaluator = evaluator;
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
    	 int bestMove = -1;
    	 double bestScore = -100000.0;
    	 for (int move=0; move<PlayField.WIDTH; move++) {
    		 if (field.addDisc(move, PlayField.ME)) {
    			 double score = evaluator.evaluate(field);
    			 if (score > bestScore) {
    				 bestMove = move;
    				 bestScore = score;
    			 }
    			 field.removeDisc(move);
    		 }
    	 }
    	 if (bestMove == -1) {
    		 throw new IllegalStateException("No move found!");
    	 }
    	return bestMove;
     }
     
 	public static void main(String[] args) throws Exception {
 		Network network;
 		if (args.length == 1) {
 			System.out.println("Loading " + args[0]);
 			Class<?> nwclass = Class.forName("neural1.networks." + args[0]);
 			Method m = nwclass.getDeclaredMethod("getNetwork", Double.TYPE, Double.TYPE);
 			network = (Network) m.invoke(null, 0, 0);
 		} else {
 			network = Learn1000.getNetwork(0, 0);
 		}
 		BotParser parser = new BotParser(new Wildfour(new NetworkEvaluator(network)));
 		parser.run();
 	}
 	
 }
