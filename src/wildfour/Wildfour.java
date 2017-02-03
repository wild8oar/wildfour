package wildfour;
// // Copyright 2015 theaigames.com (developers@theaigames.com)

//    Licensed under the Apache License, Version 2.0 (the "License");
//    you may not use this file except in compliance with the License.
//    You may obtain a copy of the License at

//        http://www.apache.org/licenses/LICENSE-2.0

//    Unless required by applicable law or agreed to in writing, software
//    distributed under the License is distributed on an "AS IS" BASIS,
//    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//    See the License for the specific language governing permissions and
//    limitations under the License.
//	
//    For the full copyright and license information, please view the LICENSE
//    file that was distributed with this source code.


import java.util.Random;

import bot.BotParser;
import bot.Field;

/**
 * Wildfour main class.
 * 
 */
public class Wildfour {	
     Field field;
     int myId;
     
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
    			 double score = field.computeScore ();
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
     
 	public static void main(String[] args) {
 		BotParser parser = new BotParser(new Wildfour());
 		parser.run();
 	}
 	
 }
