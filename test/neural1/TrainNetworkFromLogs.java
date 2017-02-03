package neural1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import wildfour.PlayField;

public class TrainNetworkFromLogs {
	
	private static final int NUM_INPUT = 84;
	private static final int NUM_HIDDEN = 21;
	
	private static final int NUM_ROUNDS = 10;
	
	private static final int MAX_MOVES_LEFT = 3;
	private static final double DECAY = 0.7;
	
	private static final File LOG_FILE = new File("/home/adrian/aigames/wildfour/log.txt");

	
 public static void main(String args[]) throws IOException  {
	 Network network =  new Network(NUM_INPUT, NUM_HIDDEN, 1, 0.7, 0.9);
	 
	 for (int round =0; round<NUM_ROUNDS; round++) {
		 int nInputs = 0;
		 try (BufferedReader br = new BufferedReader(new FileReader(LOG_FILE))) {
			 String line;
			 while ((line = br.readLine()) != null) {
			      if (line.isEmpty() || line.startsWith("#")) {
			    	  continue;
			      }
			      LogLine logline = LogLine.parseLine(line);
			      if (logline.getMovesToEnd() <= MAX_MOVES_LEFT) {
				      learnForPlayer(1, logline, network);
				      learnForPlayer(2, logline, network);
				      nInputs += 2;
			      }
			 }
		 }
		 System.out.println("Round " + round + ", mean_error=" + network.getError(nInputs));
	 }
	 
   network.writeToClass("Learn1000");
  }

 
 private static void learnForPlayer (int player, LogLine line, Network network) {
	 PlayField field = PlayField.fromBotField(line.getField(), player);
     double expected = Math.pow(DECAY, line.getMovesToEnd());
     expected *= line.getWinner() == player ? 1 : -1;
     network.computeOutputs(field.encodeAsNetworkInput());
     network.calcError(new double[] {expected});
     network.learn();
 }
 
}
