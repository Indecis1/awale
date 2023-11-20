package fr.univ_cote_azur.ai_game_programming.Player;

import fr.univ_cote_azur.ai_game_programming.Color;
import fr.univ_cote_azur.ai_game_programming.arraysOperations;

/**
 * The `Player` abstract class represents a player in the game.
 */
public abstract class Player {

    /**
     * Constructs a new `Player` instance.
     */
    public Player() {

    }

    /**
     * Captures seeds from the board starting from the given last index.
     *
     * @param board     The game board represented as a 2D array.
     * @param lastIndex The last index where the capturing starts.
     * @return The number of captured seeds.
     */
    protected int capturing(int[][] board, int lastIndex) {
        int captured_seeds = 0;
        int index;
        int score;
        for (int i = lastIndex; true; i--) {
            index = (i + 16) % 16;
            score = arraysOperations.count_seeds_at_index(board, index);
            if (!(score == 2 || score == 3)) break;
            captured_seeds += score;
            arraysOperations.emptyBoard_at_index(board, index);
        }
        return captured_seeds;
    }

    /**
     * Sows seeds on the board in the red player's style.
     *
     * @param board            The game board represented as a 2D array.
     * @param index_first_hole The index of the first hole where sowing starts.
     * @param color            The color of the seeds to sow (R or TR).
     * @param seeds            The number of seeds to sow.
     * @return The index of the last sown hole.
     */
    protected int sowingRed(int[][] board, int index_first_hole, Color color, int seeds) {
        int color_int = Color.to_index(color);
        int index = 0;
        for (int i = index_first_hole + 1; seeds > 0; i++) {
            index = i % 16;
            if (index != index_first_hole) {
                board[color_int][index]++;
                seeds--;
            }
        }
        return index;
    }

    /**
     * Sows seeds on the board in the blue player's style.
     *
     * @param board            The game board represented as a 2D array.
     * @param index_first_hole The index of the first hole where sowing starts.
     * @param color            The color of the seeds to sow (B or TB).
     * @param seeds            The number of seeds to sow.
     * @return The index of the last sown hole.
     */
    protected int sowingBlue(int[][] board, int index_first_hole, Color color, int seeds) {
        int color_int = Color.to_index(color);
        int index = 0;
        for (int i = index_first_hole + 1; seeds > 0; i += 2) {
            index = i % 16;
            if (index != index_first_hole) {
                board[color_int][index]++;
                seeds--;
            }
        }
        return index;
    }

    /**
     * Gets the current score of the player.
     *
     * @return The player's score.
     */
    public abstract int getScore();

    /**
     * Makes a move on the game board.
     *
     * @param board The game board represented as a 2D array.
     */
    public abstract void play(int[][] board);

    /**
     * Sows seeds on the board.
     *
     * @param board The game board represented as a 2D array.
     * @param index The index where sowing starts.
     * @param color The color of the seeds to sow.
     * @return The index of the last sown hole.
     */
    abstract int sowing(int[][] board, int index, Color color);

    /**
     * Checks if the other player is starving (has no legal moves).
     *
     * @param board The game board represented as a 2D array.
     * @return `true` if the other player is starving, otherwise `false`.
     */
    public abstract boolean otherPlayerIsStarving(int[][] board);

    /**
     * Prints the player's score.
     */
    public abstract void printScore();
}

