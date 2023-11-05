package fr.univ_cote_azur.ai_game_programming;

import fr.univ_cote_azur.ai_game_programming.Player.*;

import java.util.Scanner;

public class Game {
    private final int[][] board;
    private final Player[] playerOrder;
    private Opponent op;
    private Player ia;


    public Game() {
        board = new int[3][16];
        for (int j = 0; j < 16; j++) {
            board[0][j] = 2;
            board[1][j] = 2;
            board[2][j] = 1;
        }

        playerOrder = new Player[2];
        whoStart();


    }

    private void whoStart() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Do IA_eval2 start ? [Y/N]");
        if (sc.nextLine().equals("Y")) {
            ia = new IA_eval2(0);
            playerOrder[0] = ia;
            op = new Opponent(1);
            playerOrder[1] = op;
        } else {
            op = new Opponent(0);
            playerOrder[0] = op;
            ia = new IA_eval2(1);
            playerOrder[1] = ia;
        }
    }

    public void start() {
        boolean endGame = false;
        int turn = 0;
        arraysOperations.print_Board(board);
        while (!endGame) {
            if (playerOrder[turn] instanceof Opponent) op.play(board);
            else ia.play(board);
            arraysOperations.print_Board(board, playerOrder);
            printEndGamesCondition();
            endGame = playerHasMoreThen40seeds() || playersHave40seeds() || notEnoughSeeds();
            turn = (turn + 1) % 2;
        }
        printWinner();
    }

    private void printEndGamesCondition() {
        System.out.print("One player has more then 40 seeds :" + playerHasMoreThen40seeds() +
                ". Both player have 40 seeds :" + playersHave40seeds() +
                ". It remains less then 10 seeds :" + notEnoughSeeds());
        System.out.println("\n" + "-".repeat(90));
    }

    private void printWinner() {
        if (playerOrder[0] instanceof IA_eval2 && playerOrder[0].getScore() > playerOrder[1].getScore())
            System.out.println("IA_eval2 is the winner.");
        else if (playerOrder[0].getScore() == playerOrder[1].getScore()) System.out.println("It is a draw.");
        else if (playerOrder[0] instanceof Opponent && playerOrder[0].getScore() > playerOrder[1].getScore())
            System.out.println("Opponent is the winner.");
        else if (playerOrder[0] instanceof IA_eval2 && playerOrder[0].getScore() < playerOrder[1].getScore())
            System.out.println("Opponent is the winner.");
        else if (playerOrder[0] instanceof Opponent && playerOrder[0].getScore() < playerOrder[1].getScore())
            System.out.println("IA_eval2 is the winner.");
    }

    private boolean playerHasMoreThen40seeds() {
        return playerOrder[0].getScore() > 40 || playerOrder[1].getScore() > 40;
    }

    private boolean playersHave40seeds() {
        return playerOrder[0].getScore() == 40 && playerOrder[1].getScore() == 40;
    }

    private boolean notEnoughSeeds() {
        return arraysOperations.count_seeds(board) < 10;
    }
}