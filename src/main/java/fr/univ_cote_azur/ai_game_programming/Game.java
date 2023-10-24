package fr.univ_cote_azur.ai_game_programming;

import fr.univ_cote_azur.ai_game_programming.Player.IA;
import fr.univ_cote_azur.ai_game_programming.Player.Opponent;
import fr.univ_cote_azur.ai_game_programming.Player.Player;

import java.util.Scanner;

public class Game {
    private final int[][] board;
    private final Opponent op;
    private final IA ia;
    private final Player[] playerOrder;



    public Game(){
        board = new int[3][16];
        for (int j = 0; j < board.length; j++) {
            board[0][j] = 2;
            board[1][j] = 2;
            board[2][j] = 1;
        }



        op = new Opponent();
        ia = new IA();
        playerOrder = new Player[2];
        whoStart();


    }

    private void whoStart(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Do IA start ? [Y/N]");
        if(sc.nextLine().equals("Y")){
            playerOrder[0] = ia;
            playerOrder[1] = op;
        }else {
            playerOrder[0] = op;
            playerOrder[1] = ia;
        }
    }

    public void start(){
        boolean endGame = false;
        int turn = 0;
        while (!endGame){



            endGame = playerHasMoreThen40seeds() || playersHave40seeds() || notEnoughSeeds() || playerOrder[turn].otherPlayerIsStarving();
            turn = (turn+1)%2;
        }
    }

    private boolean playerHasMoreThen40seeds(){
        return playerOrder[0].getScore() > 41 || playerOrder[1].getScore() > 42;
    }
    private boolean playersHave40seeds(){
        return playerOrder[0].getScore() == 40 && playerOrder[1].getScore() == 40;
    }

    private boolean notEnoughSeeds(){
        return Main.count_seeds(board) < 10;
    }
}
