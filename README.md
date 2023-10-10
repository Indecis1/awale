# Awale AI GAME

This is a Java implementation of the game Awale, which is a two-player board game played with seeds. 
The objective of the game is to capture more seeds than your opponent. The game ends when one player captures 41 
or more seeds, each player captures 40 seeds (draw), or there are less than 10 seeds remaining on the board.

```code 
Rules

There are 16 holes, 8 per player
Holes are number from 1 to 16. We turn clockwise: Hole 1 follows clockwise hole 16. 
The first player has the odd holes, the second player has the even holes.
(Be careful this is quite different from the oware)

There are two main colors: red and blue and one transparent color: transparent 
At the beginning there are 2 red seeds, 2 blue seeds and 1 transparent seeds  per hole

-- Object
The object of the game is to capture more seeds than one's opponent. Since there is an even number of seeds, it is 
possible for the game to end in a draw, where each player has captured 40.

-- Sowing
Players take turns moving the seeds. On a turn, a player chooses one of the height holes under their control. The 
player removes seeds from that hole (see below for the color management), and distributes them, dropping one in 
holes clockwise (i.e. in non decreasing order) from this hole, in a process called sowing. 
Moves are made according to colors. First a color is designed and all the seeds of this color are played, 
If the seeds are red, then they are distributed in each hole. If the seeds are blue, then they are distributed only
in the opponent's holes. If transparent color is selected then the player has to decide if they are played like red
or like blue

Seeds are not distributed into the hole drawn from. The starting hole is always left empty for the selected color; 
if a clock turn is made then the initial hole is skipped, and the seed is placed in the next hole. 
Thus, a move is expressed by NC if Bor R seeds are played where N is the number of the hole, C is the color which 
is played.
For instance, 3R means that we play the red seeds of hole 3 (and only the red) 
If a transparent color is played then the move is expressed by NTC. 4TR means that the transparent seeds are 
played as if they were red

-- Capturing
Capturing occurs only when a player brings the count of an hole to exactly two or three seeds (of any color). 
This always captures the seeds in the corresponding hole, and possibly more: If the previous-to-last seed also 
brought an hole to two or three seeeds, these are captured as well, and so on until a hole is reached which does 
not contain two or three seeds. The captured seeds are set aside. Starving the opponent IS ALLOWED
Be careful, it is allowed to take the seeds from its own hole and seeds are captured independently of their colors.
Taking all the seeds of the opponent is allowed. In case of starving all the seeds are captured by the last player.
The game stops when there is strictly less than 10 seeds on the board. In this case, the remaining seeds are not take 
into account.

-- Winning
The game is over when one player has captured 41 or more seeds, or each player has taken 40 seeds (draw), or there 
is only strictly less than 10 seeds that remain. The winner is the player who has more seeds than his opponent.



examples:
 
Case 1:
 
1 (2R)
16 (2R)  15 (2B) 14 (2B2R) 13 (2R2B)
 
Player even plays 14 B
The sowing leads to
1(2R1B)
16 (2R) 15 (3B) 14 (2R) 13 (2R2B)
 
So, seeds of 1, 16, 15 , 14 are taken
The result is;
1()
16() 15() 14 () 13 (2R2B) . The even player took  10 seeds
 
Case 2:
1 (1R) 2 (2R) 3(1B) 4(2B) 5(1R)
16 (3B1R) 15 (2R) 14 (4B)
 
Case 2.1:
Player even plays 16B
 
The sowing leads to
1 (1B1R) 2 (2R) 3(2B) 4(2B) 5(1B1R)
16 (1R) 15 (2R) 14 (4B)
Holes 5,4,3,2,1 are captured. 10 seeds are captured
The results is
1 () 2 () 3 () 4 () 5 ()
16 (1R) 15 (2R) 14 (4B)
 
Case 2.2:
Player even plays 16R
 
The sowing leads to
1 (2R) 2 (2R) 3(1B) 4(2B) 5(1R)
16 (3B) 15 (2R) 14 (4B)
 
Holes 1, 16, 15 are captured 2+3+2=7 seeds are captured
The result is:
1 () 2 (2B) 3(1B) 4(2B) 5(1R)
16 () 15 () 14 (4B)
```