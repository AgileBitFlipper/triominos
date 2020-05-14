# Triominos - A Study in Game Development and Automated Gameplay

*Note: _Triominos is a game marketed and sold by Pressman Toy Corporation, and in no way does this project intend to take away any and all rights that Pressman have. This is solely for the use of a study in how AI and Machine Intelligence can be useful in the automation of gameplay. You can find the Triominos board game online and in your local stores where games are sold. In addition, Triominos can be found as an App in the Apple and Google App Store._*

## Object of the game

Be the first player to score 400 points. Each game consists of several rounds. **Tiles**: 56 individual playing tiles, called the pool, each with a unique sequence of corner values. **Number of Players**: Two (2) to four (4) players Tray: Each player has a tray that is filled with tiles during gameplay. Each player starts off with either seven (7) or nine (9) tiles, depending on the number of players. For two (2) players, each player draws from the pool nine (9) tiles and places them in their tray. Do not show your tiles to your opponents. For three to four (3-4) players, each player draws seven (7) tiles from the pool and places them in their tray.

## Starting Player

The starting player is chosen based on the tiles in their tray. The player with the highest value triplet tile starts. Playing a triplet gets you the value of the corners added together plus a **10 point bonus**. (For example, playing tile '4-4-4' is worth 12 + 10 or 22 points). The player with the '0-0-0' tile earns a **30 point bonus** instead of the 10 point bonus when it is played as the first tile. If the player with '0-0-0' also has the highest triplet, they can choose which of these tiles to play first, but will only receive the 30 point bonus for the 0-triplet. If no player has a triplet, the highest value tile starts. The value of a tile is the sum of the corners. (For example, the '3-4-5' tile is 12 points.)

## Gameplay

Each player takes a turn trying to match a tile face (a side with two corner values) from their tray to an open face on a tile on the board. If a match if found, the player's score is increased by the sum of the three corners of the tile played. If a matching tile can not be played from their tray, the player must draw a new tile from the pool of remaining tiles. Each pick **deducts 5 points** from their score. This continues for that player until a tile is pulled from the pool that can be played. When played, the player receives the normal value of the tile. If the pool is empty, deduct 10 points from the player and gameplay continues to the next player.

## End of Play

When a player has no more tiles in their tray, the round is concluded and the player receives a **25 point bonus** plus the value of all the tiles in the other players' trays. No deductions are made for any of the other players. If there are no more moves for any player on the board, the round is concluded and no bonus points are awarded. The player with the least number of tiles in their tray wins. Again, all points from the other hands are added to their score. Each player deducts the value of all their tiles remaining in their trays from their own scores.

## Winner

First player to pass 400 wins. If more than one player finishes past 400 points, the winner of the round wins the game.

*High-value plays*: If a player completes a hexagon, the player earns the value of the tile plus a **50 point bonus**. If a player completes a bridge, the player earns the value of the tile plus a **40 point bonus**. The same value plus 40 point bonus is subsequently used if a player adds to the bridge matching two sides.

## Example Gameplay

The displayed board now appears in color (not able to show it here). From the example below, a single round, you can get a feel for how the gameplay progresses. 

The board itself is blue, with the scales purple. Player 'A' is red, while Player 'B' is green. All player's pieces retain their color when played on the board, showing a clear indication of which player played each piece.

Start the game using the command line:

```bash
java -jar target/triominos.jar
```

Note that each run of the game is unique. The chances of you seeing the same results are not impossible, just improbable. The output for the gameplay should look something like the following:

```text
Round 2:
Players (2):
Name: Player A
    Starts: no
    Score: 437
    Hand (8):
         ^       ^       ^       ^       ^       ^       ^       ^
        /1\     /0\     /1\     /1\     /0\     /3\     /3\     /1\
       / A \   / A \   / A \   / A \   / A \   / A \   / A \   / A \
      /5   2\ /4   4\ /3   1\ /4   2\ /3   3\ /4   4\ /5   4\ /3   3\
      -- 30-- -- 19-- -- 24-- -- 29-- -- 16-- -- 50-- -- 51-- -- 31--
  Name: Player B
    Starts: yes
    Score: 402
    Hand (10):
         ^       ^       ^       ^       ^       ^       ^       ^       ^       ^
        /2\     /2\     /0\     /0\     /1\     /1\     /2\     /0\     /2\     /1\
       / B \   / B \   / B \   / B \   / B \   / B \   / B \   / B \   / B \   / B \
      /2   2\ /5   2\ /2   1\ /4   2\ /2   1\ /5   4\ /2   2\ /4   0\ /5   3\ /4   4\
      -- 37-- -- 40-- --  8-- -- 14-- -- 23-- -- 35-- -- 37-- --  5-- -- 43-- -- 34--
 Tile Pool (31):
     ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^
    /1\     /0\     /0\     /0\     /1\     /3\     /1\     /1\     /2\     /2\     /1\     /0\     /2\     /1\     /1\     /3\     /2\     /3\     /5\     /1\     /0\     /2\     /3\     /2\     /0\     /1\     /2\     /0\     /4\     /5\     /2\
   /   \   /   \   /   \   /   \   /   \   /   \   /   \   /   \   /   \   /   \   /   \   /   \   /   \   /   \   /   \   /   \   /   \   /   \   /   \   /   \   /   \   /   \   /   \   /   \   /   \   /   \   /   \   /   \   /   \   /   \   /   \
  /2   2\ /5   2\ /3   2\ /1   1\ /4   3\ /5   5\ /4   1\ /3   3\ /5   2\ /5   3\ /5   3\ /2   0\ /5   4\ /5   2\ /1   1\ /5   4\ /5   5\ /5   3\ /5   5\ /5   1\ /5   3\ /2   2\ /4   3\ /4   2\ /5   4\ /3   1\ /3   3\ /4   1\ /4   4\ /4   5\ /4   4\
  -- 27-- -- 15-- -- 13-- --  7-- -- 32-- -- 52-- -- 25-- -- 31-- -- 40-- -- 43-- -- 33-- --  3-- -- 45-- -- 30-- -- 22-- -- 51-- -- 46-- -- 49-- -- 56-- -- 26-- -- 18-- -- 37-- -- 48-- -- 39-- -- 20-- -- 24-- -- 41-- -- 10-- -- 53-- -- 55-- -- 44--
 Played Tiles (27):
     ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^
    /3\     /3\     /0\     /0\     /0\     /1\     /0\     /1\     /1\     /0\     /0\     /1\     /1\     /1\     /0\     /3\     /0\     /3\     /0\     /0\     /0\     /4\     /1\     /1\     /1\     /2\     /2\
   / A \   / B \   / A \   / B \   / A \   / B \   / A \   / B \   / A \   / B \   / A \   / B \   / A \   / B \   / A \   / B \   / A \   / B \   / A \   / B \   / A \   / B \   / A \   / B \   / A \   / B \   / A \
  /3 P 3\ /3 P 3\ /3 P 3\ /3 P 1\ /4 P 3\ /3 P 2\ /4 P 4\ /4 P 4\ /2 P 1\ /4 P 0\ /1 P 0\ /1 P 1\ /3 P 1\ /5 P 3\ /5 P 1\ /5 P 5\ /5 P 5\ /4 P 4\ /5 P 0\ /3 P 0\ /3 P 1\ /5 P 4\ /5 P 5\ /5 P 4\ /4 P 2\ /4 P 3\ /3 P 2\
  -- 47-- -- 47-- -- 16-- --  9-- -- 17-- -- 28-- -- 19-- -- 34-- -- 23-- --  5-- --  2-- -- 22-- -- 24-- -- 33-- -- 11-- -- 52-- -- 21-- -- 50-- --  6-- --  4-- --  9-- -- 54-- -- 36-- -- 35-- -- 29-- -- 42-- -- 38--
Board:
  Played Piece Count:27
  Boundaries:(55,60,52,62)
---------------------------------------------------------------------------------------------------------
          52   53   54   55   56   57   58   59   60   61   62
---------------------------------------------------------------------------------------------------------
|    |+++++=+++++++++=+++++++++=+++++++++^ -- 54-- ^+++++++++=+++++
|    |++++===+++++++===+++++++===+++++++/4\\4 P 5//5\+++++++===++++
| 55 |+++=====+++++=====+++++=====+++++/ B \\ B // B \+++++=====+++
|    |++=======+++=======+++=======+++/3 P 4\\4//4 P 1\+++=======++
|    |+=========+=========+=========+ -- 50-- v -- 35-- +=========+
|    |=+++++++++=+++++++++^ -- 47-- ^ -- 17-- ^ -- 34-- ^+++++++++=
|    |==+++++++===+++++++/3\\3 P 3//3\\3 P 4//4\\4 P 1//1\+++++++==
| 56 |===+++++=====+++++/ B \\ A // A \\ A // A \\ B // A \+++++===
|    |====+++=======+++/3 P 3\\3//3 P 0\\0//0 P 4\\4//4 P 2\+++====
|    |=====+=========+ -- 47-- v -- 16-- v -- 19-- v -- 29-- +=====
|    |+++++=+++++++++=+++++++++^ --  9-- ^ --  5-- = -- 42-- ^+++++
|    |++++===+++++++===+++++++/3\\3 P 0//0\\0 P 4/===\4 P 2//2\++++
| 57 |+++=====+++++=====+++++/ B \\ B // A \\ B /=====\ B // A \+++
|    |++=======+++=======+++/2 P 1\\1//1 P 0\\0/=======\3//3 P 2\++
|    |+=========+=========+ -- 28-- v --  2-- v=========v -- 38-- +
|    |= --  9-- ^+++++++++= -- 23-- ^+++++++++=+++++++++=+++++++++=
|    |==\1 P 3//3\+++++++===\2 P 1//1\+++++++===+++++++===+++++++==
| 58 |===\ A // B \+++++=====\ A // B \+++++=====+++++=====+++++===
|    |====\0//0 P 0\+++=======\1//1 P 1\+++=======+++=======+++====
|    |=====v --  4-- +=========v -- 22-- +=========+=========+=====
|    |+++++= --  6-- ^ -- 11-- ^ -- 24-- =+++++++++=+++++++++=+++++
|    |++++===\0 P 0//0\\0 P 1//1\\1 P 1/===+++++++===+++++++===++++
| 59 |+++=====\ A // A \\ A // B \\ A /=====+++++=====+++++=====+++
|    |++=======\5//5 P 5\\5//5 P 3\\3/=======+++=======+++=======++
|    |+=========v -- 21-- v -- 33-- v=========+=========+=========+
|    |=+++++++++=+++++++++^ -- 52-- =+++++++++=+++++++++=+++++++++=
|    |==+++++++===+++++++/5\\5 P 3/===+++++++===+++++++===+++++++==
| 60 |===+++++=====+++++/ A \\ B /=====+++++=====+++++=====+++++===
|    |====+++=======+++/1 P 5\\5/=======+++=======+++=======+++====
|    |=====+=========+ -- 36-- v=========+=========+=========+=====
 Pieces on Board with Empty Faces (21):
     ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^
    /3\     /3\     /1\     /1\     /0\     /0\     /1\     /1\     /0\     /3\     /0\     /3\     /0\     /0\     /0\     /4\     /1\     /1\     /1\     /2\     /2\
   / A \   / B \   / B \   / A \   / B \   / A \   / B \   / A \   / A \   / B \   / A \   / B \   / A \   / B \   / A \   / B \   / A \   / B \   / A \   / B \   / A \
  /3 P 3\ /3 P 3\ /3 P 2\ /2 P 1\ /4 P 0\ /1 P 0\ /1 P 1\ /3 P 1\ /5 P 1\ /5 P 5\ /5 P 5\ /4 P 4\ /5 P 0\ /3 P 0\ /3 P 1\ /5 P 4\ /5 P 5\ /5 P 4\ /4 P 2\ /4 P 3\ /3 P 2\
  -- 47-- -- 47-- -- 28-- -- 23-- --  5-- --  2-- -- 22-- -- 24-- -- 11-- -- 52-- -- 21-- -- 50-- --  6-- --  4-- --  9-- -- 54-- -- 36-- -- 35-- -- 29-- -- 42-- -- 38--
  
```

## Building Triominos

Triominos is following the DevSecOps Software Factory model for development. As such, there are some basic expectations of the build environment. First, the _pom.xml_ defines a repository for it's cache of build artifacts. This is so all artifacts will be availble for release, deliver and deployment. Comment this entire section out to use your local M2 cache. Modify the URL to point to your Artifact Repository, port and common group.

```xml
  <repositories>
    <repository>
      <id>maven-group</id>
      <url>http://localhost:8085/repository/maven-group/</url>
    </repository>
  </repositories>
```

The included _build.sh_ file can be used to run the full development lifecycle from clean to package. All artifacts needed for escrow are generated and displayed for review including, but not limited to

* dependency-analyze.log - Dependency analysis report
* dependency-resolve.log - Dependency resolution report
* dependency-tree.log - Dependency tree view
* dependency-updates-report.html - View a full report on any dependency updates that need to occur
* checkstyle.html - Checkstyle warnings and errors report
* API documents - Generated JavaDoc site
* jacoco-ut/index.html - Code Coverage report

Play the game using the command:

```bash
java -jar target/triominos.jar
```

Command line options include:

* 'd' - to enable debug logging _default:disabled_
* 'g #' - to set the number of games to play _default:1_
* 'p #' - to set the number of players _default:2_
* 'a' - to analyze all of the game event data so far _default:disabled_

Where '#' is a numerica value. For example, to play a four (4) player, single (1) game the command line would be:

```bash
java -jar target/triominos.jar -p 4 -g 1
```

To subsequently analyze all game play collected to date, the command line would be:

```bash
java -jar target/triominos.jar -a
```
