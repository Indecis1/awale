// Import statements to include necessary classes from other packages
package fr.univ_cote_azur.ai_game_programming;

import fr.univ_cote_azur.ai_game_programming.Player.IA_eval3;
import fr.univ_cote_azur.ai_game_programming.Player.Opponent;
import fr.univ_cote_azur.ai_game_programming.Player.Player;

import java.util.Scanner;

/**
 * The `Game` class represents the core logic of the AI Game Programming application.
 * It manages the game board, players, and the game loop.
 */
public class Game {
    private final int[][] board;  // 2D array representing the game board
    private final Player[] playerOrder;  // Array to store player order
    private Opponent op;  // Opponent player
    private IA_eval3 ia;  // AI player

    /**
     * Constructor for the `Game` class.
     * Initializes the game board with default values and determines which player starts the game.
     */
    public Game() {
        // Initialize the game board with default values
        board = new int[3][16];
        for (int j = 0; j < 16; j++) {
            board[0][j] = 2;
            board[1][j] = 2;
            board[2][j] = 1;
        }

        playerOrder = new Player[2];
        whoStart();  // Determine which player starts the game
    }

    /**
     * Determines the starting player based on user input.
     */
    private void whoStart() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Do IA_eval3 start? [Y/N]");
        if (sc.nextLine().equals("Y")) {
            ia = new IA_eval3(0);
            playerOrder[0] = ia;
            op = new Opponent(1);
            playerOrder[1] = op;
        } else {
            op = new Opponent(0);
            playerOrder[0] = op;
            ia = new IA_eval3(1);
            playerOrder[1] = ia;
        }
    }

    /**
     * Starts the game by entering the main game loop.
     */
    public void start() {
        boolean endGame = false;
        int turn = 0;
        arraysOperations.print_Board(board);

        // Main game loop
        while (!endGame) {
            // Depending on the player's turn, either AI or Opponent plays
            if (playerOrder[turn] instanceof Opponent)
                op.play(board);
            else
                ia.play(board);

            // Print the updated game board and check end game conditions
            arraysOperations.print_Board(board, playerOrder);
            printEndGamesCondition();
            endGame = playerHasMoreThen40seeds() || playersHave40seeds() || notEnoughSeeds();
            turn = (turn + 1) % 2;  // Switch turn between players
        }

        // Print the winner of the game
        printWinner();
    }

    /**
     * Prints the end game conditions.
     */
    private void printEndGamesCondition() {
        System.out.print("One player has more than 40 seeds: " + playerHasMoreThen40seeds() +
                ". Both players have 40 seeds: " + playersHave40seeds() +
                ". It remains less than 10 seeds: " + notEnoughSeeds());
        System.out.println("\n" + "-".repeat(90));
    }

    /**
     * Prints the winner of the game based on the player's scores.
     */
    private void printWinner() {
        if (playerOrder[0] instanceof IA_eval3 && playerOrder[0].getScore() > playerOrder[1].getScore())
            System.out.println("IA_eval3 is the winner.");
        else if (playerOrder[0].getScore() == playerOrder[1].getScore())
            System.out.println("It is a draw.");
        else if (playerOrder[0] instanceof Opponent && playerOrder[0].getScore() > playerOrder[1].getScore())
            System.out.println("Opponent is the winner.");
        else if (playerOrder[0] instanceof IA_eval3 && playerOrder[0].getScore() < playerOrder[1].getScore())
            System.out.println("Opponent is the winner.");
        else if (playerOrder[0] instanceof Opponent && playerOrder[0].getScore() < playerOrder[1].getScore())
            System.out.println("IA_eval3 is the winner.");
    }

    /**
     * Checks if any player has more than 40 seeds.
     *
     * @return `true` if any player has more than 40 seeds, otherwise `false`.
     */
    private boolean playerHasMoreThen40seeds() {
        return playerOrder[0].getScore() > 40 || playerOrder[1].getScore() > 40;
    }

    /**
     * Checks if both players have 40 seeds each.
     *
     * @return `true` if both players have 40 seeds each, otherwise `false`.
     */
    private boolean playersHave40seeds() {
        return playerOrder[0].getScore() == 40 && playerOrder[1].getScore() == 40;
    }

    /**
     * Checks if there are less than 10 seeds remaining on the board.
     *
     * @return `true` if there are less than 10 seeds remaining on the board, otherwise `false`.
     */
    private boolean notEnoughSeeds() {
        return arraysOperations.count_seeds(board) < 10;
    }
}