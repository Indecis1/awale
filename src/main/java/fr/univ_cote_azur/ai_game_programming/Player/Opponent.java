package fr.univ_cote_azur.ai_game_programming.Player;

import fr.univ_cote_azur.ai_game_programming.BoardOperations;
import fr.univ_cote_azur.ai_game_programming.Color;

import java.util.Scanner;

import static java.lang.System.exit;

public class Opponent extends Player {

    protected final int turn;
    protected int score;

    public Opponent(int turn) {
        this.turn = turn;
        this.score = 0;
    }

    @Override
    public void play(int[][] board) {
        String asked_play = askPlay();
        int index_first_hole = setHoleIndex(asked_play);
        Color color = setSeedColor(asked_play);
        try {
            legitPlay(board, index_first_hole, color);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            exit(0);
        }

        System.out.println("Opponent play is :" + (index_first_hole + 1) + color);

        int last_index = sowing(board, index_first_hole, color);
        int seed_captured = capturing(board, last_index);
        add_to_score(seed_captured);

        if (otherPlayerIsStarving(board)) {
            System.out.println("IA IS STARVING");
            seed_captured = BoardOperations.count_seeds(board);
            add_to_score(seed_captured);
            BoardOperations.emptyBoard(board);
        }
    }

    String askPlay() {
        System.out.print("Opponent play :");
        return new Scanner(System.in).nextLine();
    }

    void legitPlay(int[][] board, int index_first_hole, Color color) {
        boolean possibleNumber = 0 <= index_first_hole && index_first_hole <= 15;
        boolean possibleColor = color == Color.R || color == Color.B || color == Color.TR || color == Color.TB;
        if (!possibleNumber || !possibleColor)
            throw new IllegalArgumentException("Impossible play from Opponent... Impossible color or Number :" + (index_first_hole + 1) + color + ".");
        else if (index_first_hole % 2 != turn)
            throw new IllegalArgumentException("Impossible play from Opponent... Number parity is incorrect.");
        else if (!BoardOperations.has_seed_of_Color(board, index_first_hole, color))
            throw new IllegalArgumentException("Impossible play from Opponent... " + (index_first_hole + 1) + " hole has no " + color + " seeds.");
    }

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
        int seeds = BoardOperations.get_seedColor(board, index_first_hole, color);
        BoardOperations.emptySeedColor_at_index(board, index_first_hole, color);
        if (color == Color.R || color == Color.TR) return sowingRed(board, index_first_hole, color, seeds);
        else return sowingBlue(board, index_first_hole, color, seeds);
    }


    @Override
    public boolean otherPlayerIsStarving(int[][] board) {
        int start;
        if (turn == 0) start = 1;
        else start = 0;
        for (int i = start; i < 16; i += 2) {
            if (BoardOperations.has_seed_of_Color(board, i, Color.R) || BoardOperations.has_seed_of_Color(board, i, Color.B) || BoardOperations.has_seed_of_Color(board, i, Color.TR))
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
}
