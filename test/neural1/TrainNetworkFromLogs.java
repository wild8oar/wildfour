package neural1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import wildfour.PlayField;

public class TrainNetworkFromLogs {
	
	private static final int NUM_INPUT = 84;
	private static final int NUM_HIDDEN = 21;
	private static final double LEARN_RATE = 0.3;
	private static final double MOMENTUM = 0.3;
	
	private static final int NUM_ROUNDS = 1000;
	
	private static final int MAX_MOVES_LEFT = 3;
	private static final double DECAY = 0.5;
	
	private static final File LOG_FILE = new File("/home/adrian/aigames/wildfour/logs10000-r2.txt");

	
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
			      if (logline.getMovesToEnd() <= MAX_MOVES_LEFT) {
				      learnForPlayer(1, logline, network);
				      learnForPlayer(2, logline, network);
				      nInputs += 2;
			      }
			 }
		 }
		 double error = network.getError(nInputs);
		 System.out.println("Round " + round + ", mean_error=" + error);
		 if (error < bestError) {
			 bestRound = round;
			 bestError = error;
		 }
		 if (round - bestRound > 19 || round > 10000) {
			 break;
		 }
	 }
	 PlayField field = PlayField.fromBotField(logline.getField(), 1);
	 double e1 =network.computeOutputs(field.encodeAsNetworkInput())[0];
	 field = PlayField.fromBotField(logline.getField(), 2);
	 double e2 = network.computeOutputs(field.encodeAsNetworkInput())[0];
	 System.out.println("Result: " + e1 + " - " + e2 + "  -> " + e1/e2);
	 
   network.writeToClass("Learn02c");
  }

 
 private static void learnForPlayer (int player, LogLine line, Network network) {
	 PlayField field = PlayField.fromBotField(line.getField(), player);
     double expected = 0.5*Math.pow(DECAY, line.getMovesToEnd());
     expected = line.getWinner() == player ? 0.5+expected : 0.5-expected;
     network.computeOutputs(field.encodeAsNetworkInput());
     network.calcError(new double[] {expected});
     network.learn();
 }
 
}
