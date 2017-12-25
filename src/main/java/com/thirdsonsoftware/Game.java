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
 * Last modified: 11/30/17 4:59 PM
 */

package com.thirdsonsoftware;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

@SuppressWarnings("SpellCheckingInspection")
class Game implements Serializable {

    private static final int DEFAULT_NUMBER_OF_PLAYERS = 2 ;
    private static final int UP_TO_FOUR_NUMBER_OF_PLAYERS = 4 ;

    private static final int DEFAULT_NUM_ROUNDS = 5 ;

    private int numPlayers; // how many players are playing this game?

    // Each game can have a number of players
    private ArrayList<Player> players;

    // Each game is composed of a set of Rounds
    private ArrayList<Round> rounds;

    /**
     * Construct the default game
     */
    Game(int numPlayers) {

        Log.Info("Let's play triominos!");

        Event.logEvent(EventType.SETUP_PLAYERS);

        // Set the number of players for this game
        setNumPlayers(numPlayers);

        Log.Info(String.format(" Setting up for %d players.", numPlayers));

        // Allocate the number of players specified
        setPlayers(new ArrayList<Player>(numPlayers));

        // Assign player names
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player(String.format("Player %c", 'A' + i )));
        }

        // Let's setup the rounds and get started
        setRounds(new ArrayList<Round>(DEFAULT_NUM_ROUNDS));
    }

    /**
     * Do you want to play a game?  Let's play triominos!!
     */
    public void play() {

        int index = 0 ;
        Player playerWonGame ;

        // We need to kick off the rounds.
        do {
            Round round = new Round(++index, players);
            round.getBoard().setUseColor(true);
            rounds.add(round);
            Event.logEvent(EventType.START_A_ROUND,round);
            playerWonGame = round.playRound();
            Event.logEvent(EventType.END_A_ROUND,round);
        } while ( playerWonGame == null ) ;

        Log.Info(String.format("  Game completed in %d rounds.  Player '%s' won the game.", index, playerWonGame.getName()));
    }

    /**
     * The number of players needs to be setup so we can deterine
     *   the number of pieces per player, and how each draw is
     *   handled.
     * @param numPlayers - the number of players in the game
     */
    public void setNumPlayers(int numPlayers) {

        // Set the number of players first
        if ( ( numPlayers >= 2) && ( numPlayers <= 4 ) ) {
            this.numPlayers = numPlayers ;
        } else {
            this.numPlayers = DEFAULT_NUMBER_OF_PLAYERS ;
        }
    }

    /**
     * How many players are in this game?
     * @return (int) number of players in the game
     */
    public int getNumPlayers() {
        return this.numPlayers;
    }

    /**
     * The list of players in the game.
     * @return players (ArrayList<Player>) - List of players in the game
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Returns back a single player from the list of players in the game
     * @param index - the index of which player is requested
     * @return (Player) the instance of the player that is specified.
     */
    public Player getPlayer(int index) {
        if ( ( index < players.size() ) && ( index >= 0 ) )
            return players.get(index) ;
        else
            return null;
    }

    /**
     * Establishes the list of players in the Game.
     * @param players (ArrayList<Player>) - Array list of players in the game.
     */
    protected void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    /**
     * Provides a text view of the current game, including the Tile pool,
     *   the player list, and the current board.
     * @return - String containing a snapshot view of the current Game.
     */
    public String toString() {
        return ("\n\nGame Results:\n" +
                displayRounds());
    }

    /**
     * Games are played in Rounds until a player scores above 400 points.
     * @return (ArrayList<Round>) - the list of Rounds played
     */
    public ArrayList<Round> getRounds() {
        return rounds;
    }

    /**
     * Set the list of rounds
     * @param rounds the list of rounds for the Game
     */
    public void setRounds(ArrayList<Round> rounds) {
        this.rounds = rounds;
    }

    /**
     * We need a way to display the final stats based on the Rounds played
     * @return (String) a string representing the Rounds played for this Game
     */
    private String displayRounds() {
        StringBuilder strRounds = new StringBuilder(500);
        for ( Round r:rounds ) {
            strRounds.append(r.toString()) ;
        }
        return ( "  Rounds:\n" + strRounds ) ;
    }
}
