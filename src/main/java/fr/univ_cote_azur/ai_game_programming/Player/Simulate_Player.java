package fr.univ_cote_azur.ai_game_programming.Player;

import fr.univ_cote_azur.ai_game_programming.Color;
import fr.univ_cote_azur.ai_game_programming.arraysOperations;

import static java.lang.System.exit;

/**
 * The `Simulate_Player` class represents a player used for simulating game moves.
 * It extends the `Opponent` class.
 */
public class Simulate_Player extends Opponent {

    /**
     * Constructs a new `Simulate_Player` instance.
     *
     * @param turn The turn of the player (0 or 1).
     */
    public Simulate_Player(int turn) {
        super(turn);
    }

    /**
     * Simulates a play by the player.
     *
     * @param board The game board represented as a 2D array.
     * @param move  The move to simulate, where `move[0]` is the hole index and `move[1]` is the seed color.
     */
    public void simulate_play(int[][] board, int[] move) {
        int index_first_hole = move[0];
        Color color = Color.to_Color(move[1]);
        try {
            legitPlay_sim(board, index_first_hole, color);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            exit(0);
        }

        int last_index = sowing(board, index_first_hole, color);
        int seed_captured = capturing(board, last_index);
        add_to_score(seed_captured);

        if (otherPlayerIsStarving(board)) {
            seed_captured = arraysOperations.count_seeds(board);
            add_to_score(seed_captured);
            arraysOperations.emptyBoard(board);
        }
    }

    private void legitPlay_sim(int[][] board, int index_first_hole, Color color) {
        boolean possibleNumber = 0 <= index_first_hole && index_first_hole <= 15;
        boolean possibleColor = color == Color.R || color == Color.B || color == Color.TR || color == Color.TB;
        if (!possibleNumber || !possibleColor)
            throw new IllegalArgumentException("Impossible play from Simulate_Player... Impossible color or Number :" + (index_first_hole + 1) + color + ".");
        else if (index_first_hole % 2 != turn)
            throw new IllegalArgumentException("Impossible play from Simulate_Player... Number parity is incorrect.");
        else if (!arraysOperations.has_seed_of_Color(board, index_first_hole, color))
            throw new IllegalArgumentException("Impossible play from Simulate_Player... " + (index_first_hole + 1) + " hole has no " + color + " seeds.");
    }
}