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

package bot;
import java.util.Random;

/**
 * BotStarter class
 * 
 * Magic happens here. You should edit this file, or more specifically
 * the makeTurn() method to make your bot do more than random moves.
 * 
 * @author Jim van Eeden <jim@starapple.nl>, Joost de Meij <joost@starapple.nl>
 */

public class BotStarter {	
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
    	 System.out.println(myId + " playing ");
    	 while (true) {
    		 int move = new Random().nextInt(7); 
    		 if (field.isValidMove(move)) {
    			 System.out.println(myId + " playing " + move);
    			 return move;
    		 }
    	 } 
     }
     
 	public static void main(String[] args) {
 		System.out.println("wildfour started...");
 		BotParser parser = new BotParser(new BotStarter());
 		parser.run();
 	}
 	
 }
