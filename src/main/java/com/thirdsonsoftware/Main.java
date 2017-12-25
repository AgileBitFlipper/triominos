/*
 * Copyright (c) 2017 Third Son Software, LLC
 * (http://thirdsonsoftware.com/) and others. All rights reserved.
 * Created by Andrew B. Montcrieff on 11/30/17 5:08 PM.
 *
 * This file is part of Project triominos.
 *
 * triominos can not be copied and/or distributed without the express
 * permission of Andrew B. Montcrieff or Third Son Software, LLC.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Last modified: 11/30/17 4:57 PM
 */

package com.thirdsonsoftware;

import java.io.*;
import org.apache.commons.cli.*;

class Main {

    static private int DEFAULT_GAME_COUNT = 1;
    static private int DEFAULT_NUMBER_OF_PLAYERS = 2 ;

    static private int gameCount = DEFAULT_GAME_COUNT ;
    static private int numberOfPlayers = DEFAULT_NUMBER_OF_PLAYERS ;

    static private boolean analyzeResults = false ;

    static Game game = null ;

    public static boolean isAnalyzeResults() {
        return analyzeResults;
    }

    public static void setAnalyzeResults(boolean analyzeResults) {
        Main.analyzeResults = analyzeResults;
    }

    public static Game getGame() {
        return game;
    }

    public static void setGame(Game game) {
        Main.game = game;
    }

    public static int getGameCount() {
        return gameCount;
    }

    public static void setGameCount(int gameCount) {
        Main.gameCount = gameCount;
    }

    public static int getNumberOfPlayers() {
        return numberOfPlayers ;
    }

    public static void setNumberOfPlayers(int numPlayers) {
        Main.numberOfPlayers = numPlayers ;
    }

    private static void processCommandLine( String[] args ) {

        Options options = new Options();

        options.addOption("d",false,"Debug Logging");
        options.addOption("g",true,"Number Of Games To Play");
        options.addOption("p",true,"Number of Players in the Game");
        options.addOption("a",false,"Analyze the events recorded");

        CommandLineParser parser = new DefaultParser();
        try {

            CommandLine cmd = parser.parse( options, args);

            // Determine if we are in debug mode
            if (cmd.hasOption("d")) {
                Log.Info( "   Enabling debug logging.");
                Log.debugMode = true ;
            }

            // Setting the number of games to play
            if (cmd.hasOption("g")) {
                String strGameCount = cmd.getOptionValue("g");
                setGameCount(Integer.parseInt(strGameCount));
                Log.Info( String.format("   Performing %d games.",getGameCount()));
            }

            // Setting the number of players in the game
            if (cmd.hasOption("p")) {
                String strNumberOfPlayers = cmd.getOptionValue("p");
                setNumberOfPlayers(Integer.parseInt(strNumberOfPlayers));
                Log.Info( "   Setting the number of players to " + getNumberOfPlayers());
            }

            if (cmd.hasOption("a")) {
                Main.setAnalyzeResults(true);
                Log.Info("\nAnalyzing results...");
            }

        } catch ( ParseException pe ) {
            Log.Error("  Parse exception: " + pe.getMessage());
        }
    }

    public static void main(String[] args) {

        Main.processCommandLine(args);

        // Are we being asked to analyze the results, let's get to it.
        if ( isAnalyzeResults() ) {

            // Let's dump all the Event data to a text file
            EventManager.getInstance().dumpAllEventData();

            // Let's convert any object files not converted to JSON
            EventManager.getInstance().convertToJSON();

            // Let's kick off the analysis phase
            AnalyzeResults analyzeResults = new AnalyzeResults() ;

        } else {

            // Let's loop through the number of games we were asked to play
            for (int nGame = 1; nGame <= getGameCount(); nGame++) {

                // Clear out any events before we get started with the next game
                EventManager.getInstance().clearEvents();

                Event.logEvent(EventType.START_A_GAME, nGame);

                setGame(new Game(getNumberOfPlayers()));

                getGame().play();

                Log.Info(getGame().toString());

                Event.logEvent(EventType.END_A_GAME, nGame);

                String eventLog = EventManager.getInstance().logEvents();
                Log.Info(String.format(" Event log for Game %d is %s.", nGame, eventLog));
            }

            EventManager.getInstance().dumpAllEventData();
        }
    }

    // Todo: We should be able to save and retrieve game state...finish this later.

    /**
     * Save the game to a file
     */
    static protected void saveGame() {
        try {
            Log.Info("Saving the current game state...") ;
            FileOutputStream fos = new FileOutputStream("triominos.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(game);
            oos.close();
        } catch (FileNotFoundException e) {
            Log.Error("  File Not Found Exception in saveGame(): " + e.getMessage());
        } catch (IOException e) {
            Log.Error("  IO Exception in saveGame(): " + e.getMessage());
        }
    }

    /**
     * Load a game from a file
     * @return (Game) the game after loading from a saved state
     */
    static protected Game loadGame() {
        Game loadedGame = null;
        try {
            Log.Info("Loading game from saved state...");
            FileInputStream fis = new FileInputStream("triominos.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            loadedGame = (Game) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            Log.Error("  File Not Found Exception in saveGame(): =" + e.getMessage());
        } catch (IOException e) {
            Log.Error("  IO Exception in saveGame(): " + e.getMessage());
        } catch (ClassNotFoundException e) {
            Log.Error("  Class Not Found Exception in saveGame(): " + e.getMessage());
        }
        return loadedGame;
    }
}
