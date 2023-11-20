package fr.univ_cote_azur.ai_game_programming.Player;

import fr.univ_cote_azur.ai_game_programming.Color;
import fr.univ_cote_azur.ai_game_programming.arraysOperations;

import java.util.Scanner;

/**
 * The `Opponent` class represents an opponent player controlled by user input.
 */
public class Opponent extends Player {

    protected final int turn;
    protected int score;

    /**
     * Constructs a new `Opponent` instance.
     *
     * @param turn The turn of the opponent player (0 or 1).
     */
    public Opponent(int turn) {
        this.turn = turn;
        this.score = 0;
    }

    @Override
    public void play(int[][] board) {
        String asked_play = askPlay();
        int index_first_hole = setHoleIndex(asked_play);
        Color color = setSeedColor(asked_play);
        while (!legitPlay(board, index_first_hole, color)) {
            asked_play = askPlay();
            index_first_hole = setHoleIndex(asked_play);
            color = setSeedColor(asked_play);
        }

        System.out.println("Opponent play is :" + (index_first_hole + 1) + color);

        int last_index = sowing(board, index_first_hole, color);
        int seed_captured = capturing(board, last_index);
        add_to_score(seed_captured);

        if (otherPlayerIsStarving(board)) {
            System.out.println("IA IS STARVING");
            seed_captured = arraysOperations.count_seeds(board);
            add_to_score(seed_captured);
            arraysOperations.emptyBoard(board);
        }
    }

    /**
     * Asks the user for the opponent's play input.
     *
     * @return The input string representing the opponent's play.
     */
    String askPlay() {
        System.out.print("Opponent play :");
        return new Scanner(System.in).nextLine();
    }

    private boolean legitPlay(int[][] board, int index_first_hole, Color color) {
        boolean possibleNumber = 0 <= index_first_hole && index_first_hole <= 15;
        boolean possibleColor = color == Color.R || color == Color.B || color == Color.TR || color == Color.TB;
        if (!possibleNumber || !possibleColor) {
            System.out.println("Impossible play from Opponent... Impossible color or Number :" + (index_first_hole + 1) + color + ".");
            return false;
        } else if (index_first_hole % 2 != turn) {
            System.out.println("Impossible play from Opponent... Number parity is incorrect.");
            return false;
        } else if (!arraysOperations.has_seed_of_Color(board, index_first_hole, color)) {
            System.out.println("Impossible play from Opponent... " + (index_first_hole + 1) + " hole has no " + color + " seeds.");
            return false;
        }
        return true;
    }

    /**
     * Converts the color string from the input into a `Color` enum value.
     *
     * @param asked_play The input string representing the opponent's play.
     * @return The corresponding `Color` enum value.
     */
    Color setSeedColor(String asked_play) {
        String seedColor = "";
        for (int i = 0; i < asked_play.length(); i++) {
            char c = asked_play.charAt(i);
            if (!Character.isDigit(c)) seedColor += c;
        }
        return switch (seedColor) {
            case "R" -> Color.R;
            case "B" -> Color.B;
            case "TR" -> Color.TR;
            case "TB" -> Color.TB;
            default -> Color.UNDEFINED;
        };
    }

    /**
     * Extracts the hole index from the input string.
     *
     * @param asked_play The input string representing the opponent's play.
     * @return The hole index.
     */
    int setHoleIndex(String asked_play) {
        int holeNumberId = 0;
        for (int i = 0; i < asked_play.length(); i++) {
            char c = asked_play.charAt(i);
            if (Character.isDigit(c)) {
                holeNumberId = holeNumberId * 10 + Character.getNumericValue(c);
            } else {
                break;
            }
        }
        return holeNumberId - 1;
    }

    @Override
    int sowing(int[][] board, int index_first_hole, Color color) {
        int seeds = arraysOperations.get_seedColor(board, index_first_hole, color);
        arraysOperations.emptySeedColor_at_index(board, index_first_hole, color);
        if (color == Color.R || color == Color.TR) return sowingRed(board, index_first_hole, color, seeds);
        else return sowingBlue(board, index_first_hole, color, seeds);
    }

    @Override
    public boolean otherPlayerIsStarving(int[][] board) {
        int start;
        if (turn == 0) start = 1;
        else start = 0;
        for (int i = start; i < 16; i += 2) {
            if (arraysOperations.has_seed_of_Color(board, i, Color.R) || arraysOperations.has_seed_of_Color(board, i, Color.B) || arraysOperations.has_seed_of_Color(board, i, Color.TR))
                return false;
        }
        return true;
    }

    @Override
    public void printScore() {
        System.out.println("Opponent score :" + getScore());
    }

    void add_to_score(int seedCaptured) {
        score += seedCaptured;
    }

    @Override
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}

