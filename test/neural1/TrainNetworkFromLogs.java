package neural1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import wildfour.PlayField;

public class TrainNetworkFromLogs {
	
	private static final int NUM_INPUT = 8;
	private static final int NUM_HIDDEN = 4;
	private static final double LEARN_RATE = 0.1;
	private static final double MOMENTUM = 0.1;
	
	private static final int NUM_ROUNDS = 10000;
	
	private static final int MAX_MOVES_LEFT = 1;
	private static final double DECAY = 0.9;
	
	private static final File LOG_FILE = new File("/home/adrian/aigames/wildfour/logs10000-r3.txt");

	
 public static void main(String args[]) throws IOException  {
	 Network network =  new Network(NUM_INPUT, NUM_HIDDEN, 1, LEARN_RATE, MOMENTUM);
	 //Network network = Learn1000.getNetwork(0.7, 0.9);
	 LogLine logline = null;
	 int bestRound = 0;
	 double bestError = 10000;
	 int round = 0;
	 while (true) {
		 round++;
		 int nInputs = 0;
		 try (BufferedReader br = new BufferedReader(new FileReader(LOG_FILE))) {
			 String line;
			 while ((line = br.readLine()) != null) {
			      if (line.isEmpty() || line.startsWith("#")) {
			    	  continue;
			      }
			      logline = LogLine.parseLine(line);
			      if (logline.getMovesToEnd() > MAX_MOVES_LEFT) {
			      //if (logline.getMovesToEnd() <= MAX_MOVES_LEFT && logline.getMovesToEnd() > 0) {
				      learnForPlayer(PlayField.ME, logline, network);
				      learnForPlayer(PlayField.OTHER, logline, network);
				      nInputs += 2;
			      }
			 }
		 }
		 double error = network.getError(nInputs);
		 System.out.println("Round " + round + ", mean_error=" + error + " (" + nInputs + " samples)");
		 if (error < bestError) {
			 bestRound = round;
			 bestError = error;
		 }
		 if (round - bestRound > 19 || round > NUM_ROUNDS) {
			 break;
		 }
	 }
	 PlayField field = PlayField.fromBotField(logline.getField(), PlayField.ME);
	 double e1 =network.computeOutputs(field.encodeFieldAsNetworkInput())[0];
	 field = PlayField.fromBotField(logline.getField(), PlayField.OTHER);
	 double e2 = network.computeOutputs(field.encodeFieldAsNetworkInput())[0];
	 System.out.println("Result: " + e1 + " - " + e2 + "  -> " + e1/e2);
	 
   network.writeToClass("Features01b");
  }

 
 private static void learnForPlayer (char player, LogLine line, Network network) {
	 PlayField field = PlayField.fromBotField(line.getField(), player);
     double expected = 0.5*Math.pow(DECAY, line.getMovesToEnd());
     expected = line.getWinner() == player ? 0.5+expected : 0.5-expected;
     double[] encoded;
     if (NUM_INPUT == 8) {
    	 encoded = field.encodeFeaturesAsNetworkInput();
     } else if (NUM_INPUT == 84) {
    	 encoded = field.encodeFieldAsNetworkInput();
    	 
     } else {
    	 throw new IllegalStateException("don't know how to encode input length " + NUM_INPUT);
     }
     network.computeOutputs(field.encodeFieldAsNetworkInput());
     network.calcError(new double[] {expected});
     network.learn();
 }
 
}
