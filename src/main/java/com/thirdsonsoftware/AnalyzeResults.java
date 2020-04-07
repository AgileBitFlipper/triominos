package com.thirdsonsoftware;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * This is the class designed to brute force analyze the raw data for each game.
 * Futures: This action should be replaced by the use of the ELK stack. Logstash
 * would read the JSON file data and send it to Elasticsearch. Then Kibana would
 * digest that data and present it in a format that the user desires.
 */
public class AnalyzeResults {

    int numberOfEventFiles;
    int tilesPlayedInRound;
    int tilesPlayedInGame;
    int numberOfRoundsPlayed;
    int numberOfGamesPlayed;

    HashMap<Player, Integer> playersThatWonAGame = new HashMap<Player, Integer>();
    HashMap<Player, Integer> playersThatWonARound = new HashMap<Player, Integer>();
    HashMap<Tile, Integer> tilesPlayed = new HashMap<Tile, Integer>();
    HashMap<Tile, Integer> startingTilesPlayed = new HashMap<Tile, Integer>();

    List<Integer> listTilesPlayedInRound = new ArrayList<Integer>();
    List<Integer> listTilesPlayedInGame = new ArrayList<Integer>();

    /**
     * Provide a way to analyze the results.
     */
    public AnalyzeResults() {

        // Let's go analyze the data..
        analyze();

        Log.Info(String.format("\nAnalysis of Events in %d Event files complete:\n",
            numberOfEventFiles));

        Log.Info(numberOfGames());
        Log.Info(numberOfRounds());
        Log.Info(averageNumberOfTilesInARound());
        Log.Info(averageNumberOfTilesInAGame());
        Log.Info(gamesWonByAPlayer());
        Log.Info(roundsWonByAPlayer());
        Log.Info(tilesPlayed());
        Log.Info(startingTilePlayed());
    }

    private String numberOfGames() {
        return String.format("Number of games played: %d\n", numberOfGamesPlayed);
    }

    private String numberOfRounds() {
        return String.format("Number of rounds played: %d\n", numberOfRoundsPlayed);
    }

    private String averageNumberOfTilesInARound() {
        int sum = 0;
        Iterator<Integer> iterator = listTilesPlayedInRound.iterator();
        while (iterator.hasNext()) {
            sum += iterator.next().intValue();
        }
        int denominator = listTilesPlayedInRound.size();
        int average = (denominator == 0) ? 0 : (sum / denominator);
        return String.format("Average number of Tiles played in a Round: %d\n", average);
    }

    private String averageNumberOfTilesInAGame() {
        int sum = 0;
        Iterator<Integer> iterator = listTilesPlayedInGame.iterator();
        while (iterator.hasNext()) {
            sum += iterator.next().intValue();
        }
        int denominator = listTilesPlayedInGame.size();
        int average = (denominator == 0) ? 0 : (sum / denominator);
        return String.format("Average number of Tiles played in a Game: %d\n", average);
    }

    /**
     * Provide a method to analyze the results.
     * How many times has each Player won a game? How many times has each Player won
     * a round? How many times has a Tile been played? How many times has a Tile
     * been used to start a Round? What is the average number of tiles played in a
     * Round? What is the highest number of tiles played in a Round?
     */
    public void analyze() {

        // Event files
        List<String> eventFiles = EventManager.getInstance().getAllEventDataFiles();
        numberOfEventFiles = eventFiles.size();
        for (String eventFile : eventFiles) {

            // Start with no tiles played/counted
            tilesPlayedInGame = 0;
            tilesPlayedInRound = 0;

            // Events in file
            List<Event> events = EventManager.getInstance().getAllEventsForDataFile(eventFile);
            for (Event event : events) {

                switch (event.type) {

                    case WIN_A_GAME: {
                        Player player = event.player;
                        if (playersThatWonAGame.containsKey(player)) {
                            playersThatWonAGame.put(player, playersThatWonAGame.get(player) + 1);
                        } else {
                            playersThatWonAGame.put(event.player, 1);
                        }
                        listTilesPlayedInGame.add(tilesPlayedInGame);
                    }
                    break;

                    case WIN_A_ROUND_BY_EMPTY_TRAY:
                    case WIN_A_ROUND_BY_FEWEST_TILES: {
                        Player player = event.player;
                        if (playersThatWonARound.containsKey(player)) {
                            playersThatWonARound.put(player, playersThatWonARound.get(player) + 1);
                        } else {
                            playersThatWonARound.put(player, 1);
                        }
                        listTilesPlayedInRound.add(tilesPlayedInRound);
                        tilesPlayedInRound = 0;
                    }
                        break;

                    case PLACE_A_TILE: {
                        tilesPlayedInRound++;
                        tilesPlayedInGame++;

                        Tile tile = event.tile;
                        if (tilesPlayed.containsKey(tile)) {
                            tilesPlayed.put(tile, tilesPlayed.get(tile) + 1);
                        } else {
                            tilesPlayed.put(tile, 1);
                        }
                    }
                    break;

                    case TRIPLE_ZERO_BONUS:
                    case TRIPLE_PLAY_BONUS:
                    case HIGHEST_TILE_START: {
                        Tile tile = event.tile;
                        if (startingTilesPlayed.containsKey(tile)) {
                            startingTilesPlayed.put(tile, startingTilesPlayed.get(tile) + 1);
                        } else {
                            startingTilesPlayed.put(tile, 1);
                        }
                    }
                        break;

                    case START_A_GAME: {
                        numberOfGamesPlayed++;
                    }
                    break;

                    case START_A_ROUND: {
                        numberOfRoundsPlayed++;
                    }
                    break;

                    case CREATE_A_BRIDGE:
                    default: {
                        /**
                         * Unexpected event type received.
                         */
                    }
                        break;
                }
            }
        }
    }

    // a comparator using generic type
    class ValueComparator<K, V extends Comparable<V>> implements Comparator<K> {

        HashMap<K, V> map = new HashMap<K, V>();

        /**
         * How to compare two elements.
         */
        public ValueComparator(HashMap<K, V> map) {
            this.map.putAll(map);
        }

        @Override
        /**
         * Override the comparitor and provide a way to 
         *      compare two elements.
         */
        public int compare(K s1, K s2) {
            V value1 = map.get(s1);
            V value2 = map.get(s2);
            // The order of the elements, ascending or descending, is determined by
            // the sign given to the compare result. If '-', the largest value is
            // first. If '+', the smallest value is first. If '0', you can decide
            // how to order the elements.
            int order = -value1.compareTo(value2);
            if (order == 0) {
                order = -1;
            }
            return order;
        }
    }

    /**
     * Determines the number of games won by a Player.
     */
    public String gamesWonByAPlayer() {

        Comparator<Player> playerComparator = 
            new ValueComparator<Player, Integer>(playersThatWonAGame);
        TreeMap<Player, Integer> sortedPlayers = new TreeMap<Player, Integer>(playerComparator);
        sortedPlayers.putAll(playersThatWonAGame);

        StringBuilder strGamesWonByPlayer = 
            new StringBuilder(100).append("Games won by players:\n");

        Set set = sortedPlayers.entrySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Map.Entry me = (Map.Entry) it.next();
            Player player = (Player) me.getKey();
            strGamesWonByPlayer
                    .append(String.format("  Player '%s' has won %d times.\n", 
                        player.getName(), me.getValue()));
        }
        return strGamesWonByPlayer.toString();
    }

    /**
     * Determines the number of rounds won by a Player.
     */
    public String roundsWonByAPlayer() {

        Comparator<Player> playerComparator = 
            new ValueComparator<Player, Integer>(playersThatWonARound);
        TreeMap<Player, Integer> sortedPlayers = 
            new TreeMap<Player, Integer>(playerComparator);
        sortedPlayers.putAll(playersThatWonARound);

        StringBuilder strRoundsWonByPlayer = 
            new StringBuilder(100).append("Rounds won by players:\n");

        Set set = sortedPlayers.entrySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Map.Entry me = (Map.Entry) it.next();
            Player player = (Player) me.getKey();
            strRoundsWonByPlayer
                    .append(String.format(
                        "  Player '%s' has won %d times.\n", player.getName(), me.getValue()));
        }
        return strRoundsWonByPlayer.toString();
    }

    /**
     * Method called when tiles are played.
     */
    public String tilesPlayed() {
        Comparator<Tile> tileComparator = new ValueComparator<Tile, Integer>(tilesPlayed);
        TreeMap<Tile, Integer> sortedTiles = new TreeMap<Tile, Integer>(tileComparator);
        sortedTiles.putAll(tilesPlayed);

        StringBuilder strTilesPlayed = new StringBuilder(100).append("Tiles played:\n");

        Set set = sortedTiles.entrySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Map.Entry me = (Map.Entry) it.next();
            Tile tile = (Tile) me.getKey();
            strTilesPlayed.append(String.format(
                "  Tile '%s' (%2d) has been played %s times.\n", me.getKey(),
                    tile.getId(), me.getValue()));
        }
        return strTilesPlayed.toString();
    }

    /**
     * Method called when the starting tile is played.
     */
    public String startingTilePlayed() {
        Comparator<Tile> tileComparator = new ValueComparator<Tile, Integer>(startingTilesPlayed);
        TreeMap<Tile, Integer> sortedTiles = new TreeMap<Tile, Integer>(tileComparator);
        sortedTiles.putAll(startingTilesPlayed);

        StringBuilder strTilesPlayed = new StringBuilder(100).append("Tiles played at start:\n");

        Set set = sortedTiles.entrySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Map.Entry me = (Map.Entry) it.next();
            Tile tile = (Tile) me.getKey();
            strTilesPlayed.append(String.format(
                "  Tile '%s' (%2d) has been played as the starting tile %s times.\n",
                    me.getKey(), tile.getId(), me.getValue()));
        }
        return strTilesPlayed.toString();
    }
}
