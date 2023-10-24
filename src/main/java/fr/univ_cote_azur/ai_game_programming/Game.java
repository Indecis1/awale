package fr.univ_cote_azur.ai_game_programming;

import fr.univ_cote_azur.ai_game_programming.Player.IA;
import fr.univ_cote_azur.ai_game_programming.Player.Opponent;
import fr.univ_cote_azur.ai_game_programming.Player.Player;

import java.util.Scanner;

public class Game {
    private final int[][] board;
    private Opponent op;
    private IA ia;
    private final Player[] playerOrder;



    public Game(){
        board = new int[3][16];
        for (int j = 0; j < board.length; j++) {
            board[0][j] = 2;
            board[1][j] = 2;
            board[2][j] = 1;
        }


        playerOrder = new Player[2];
        whoStart();


    }

    private void whoStart(){
        Scanner sc = new Scanner(System.in);
        ia = new IA();
        System.out.print("Do IA start ? [Y/N]");
        if(sc.nextLine().equals("Y")){
            playerOrder[0] = ia;
            op = new Opponent(1);
            playerOrder[1] = op;
        }else {
            op = new Opponent(0);
            playerOrder[0] = op;
            playerOrder[1] = ia;
        }
    }

    public void start(){
        boolean endGame = false;
        int turn = 0;
        Main.print_Board(board);
        while (!endGame){
            if(playerOrder[turn] instanceof Opponent)
                op.play(board);
            else
                ia.play();

            Main.print_Board(board);
            endGame = playerHasMoreThen40seeds() || playersHave40seeds() || notEnoughSeeds() || playerOrder[turn].otherPlayerIsStarving(board);
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
