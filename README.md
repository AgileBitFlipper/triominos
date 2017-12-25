# Triominos - A Study in Game Development and Automated Gameplay

*Note: _Triominos is a game marketed and sold by Pressman Toy Corporation, and in no way does this project intend to take away any and all rights that Pressman have. This is solely for the use of a study in how AI and Machine Intelligence can be useful in the automation of gameplay. You can find the Triominos board game online and in your local stores where games are sold. In addition, Triominos can be found as an App in the Apple and Google App Store._*

# Object of the game
Be the first player to score 400 points. Each game consists of several rounds. **Tiles**: 56 individual playing tiles, called the pool, each with a unique sequence of corner values. **Number of Players**: Two (2) to four (4) players Tray: Each player has a tray that is filled with tiles during gameplay. Each player starts off with either seven (7) or nine (9) tiles, depending on the number of players. For two (2) players, each player draws from the pool nine (9) tiles and places them in their tray. Do not show your tiles to your opponents. For three to four (3-4) players, each player draws seven (7) tiles from the pool and places them in their tray.

# Starting Player
The starting player is chosen based on the tiles in their tray. The player with the highest value triplet tile starts. Playing a triplet gets you the value of the corners added together plus a **10 point bonus**. (For example, playing tile '4-4-4' is worth 16 + 10 or 26 points). The player with the '0-0-0' tile earns a **30 point bonus** instead of the 10 point bonus when it is played as the first tile. If the player with '0-0-0' also has the highest triplet, they can choose which of these tiles to play first, but will only receive the 30 point bonus for the 0-triplet. If no player has a triplet, the highest value tile starts. The value of a tile is the sum of the corners. (For example, the '3-4-5' tile is 12 points.)

# Gameplay
Each player takes a turn trying to match a tile face (a side with two corner values) from their tray to an open face on a tile on the board. If a match if found, the player's score is increased by the sum of the three corners of the tile played. If a matching tile can not be played from their tray, the player must draw a new tile from the pool of remaining tiles. Each pick **deducts 5 points** from their score. This continues for that player until a tile is pulled from the pool that can be played. When played, the player receives the normal value of the tile. If the pool is empty, deduct 10 points from the player and gameplay continues to the next player.

# End of Play
When a player has no more tiles in their tray, the round is concluded and the player receives a **25 point bonus** plus the value of all the tiles in the other players' trays. No deductions are made for any of the other players. If there are no more moves for any player on the board, the round is concluded and no bonus points are awarded. The player with the least number of tiles in their tray wins. Again, all points from the other hands are added to their score. Each player deducts the value of all their tiles remaining in their trays from their own scores.

# Winner
First player to pass 400 wins. If more than one player finishes past 400 points, the winner of the round wins the game.

*High-value plays*: If a player completes a hexagon, the player earns the value of the tile plus a **50 point bonus**. If a player completes a bridge, the player earns the value of the tile plus a **40 point bonus**. The same value plus 40 point bonus is subsequently used if a player adds to the bridge matching two sides. 
