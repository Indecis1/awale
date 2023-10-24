package fr.univ_cote_azur.ai_game_programming.Player;

import fr.univ_cote_azur.ai_game_programming.Color;
import fr.univ_cote_azur.ai_game_programming.Main;

import java.util.Scanner;

public class Opponent extends Player {

    private final int turn;
    private int score;

    public Opponent(int turn) {
        this.turn = turn;
        this.score=0;
    }

    public void play(int[][] board) {
        String asked_play = askPlay();
        int index_first_hole = setHoleIndex(asked_play);
        Color color = setSeedColor(asked_play);
        try {
            legitPlay(board, index_first_hole, color);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        int last_index = sowing(board, index_first_hole, color);
        int seed_captured = capturing(board, last_index);
        add_to_score(seed_captured);

        if(otherPlayerIsStarving(board)){
            seed_captured = Main.count_seeds(board);
            add_to_score(seed_captured);
            Main.emptyBoard(board);
        }

    }

    private String askPlay() {
        System.out.print("Opponent play :");
        return new Scanner(System.in).nextLine();
    }

    private void legitPlay(int[][] board, int index_first_hole, Color color) {
        boolean possibleNumber = 0 <= index_first_hole && index_first_hole <= 15;
        boolean possibleColor = color == Color.R || color == Color.B || color == Color.TR || color == Color.TB;
        if (!possibleNumber || !possibleColor)
            throw new IllegalArgumentException("Impossible play from Opponent... Impossible color or Number :" + (index_first_hole + 1) + color + ".");
        else if (index_first_hole % 2 != turn)
            throw new IllegalArgumentException("Impossible play from Opponent... Number parity is incorrect :" + (index_first_hole % 2) + ".");
        else if (Main.has_seedColor(board, index_first_hole, color))
            throw new IllegalArgumentException("Impossible play from Opponent... " + (index_first_hole + 1) + " hole has no " + color + "seeds.");
    }

    private Color setSeedColor(String asked_play) {
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

    private int setHoleIndex(String asked_play) {
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
        int seeds = Main.get_seedColor(board, index_first_hole, color);
        Main.emptySeedColor_at_index(board, index_first_hole, color);
        if(color == Color.R || color == Color.TR)
            return sowingRed(board, index_first_hole, color, seeds);
        else
            return sowingBlue(board, index_first_hole, color, seeds);
    }

    private int sowingRed(int[][] board, int index_first_hole, Color color, int seeds){
        int color_int = Color.to_int(color);
        int index = 0;
        for (int i = index_first_hole+2; seeds > 0 ; i++) {
            index = i%16;
            if(index == index_first_hole)
                continue;
            board[color_int][i]++;
            seeds--;
        }
        return index;
    }

    private int sowingBlue(int[][] board, int index_first_hole, Color color, int seeds){
        int color_int = Color.to_int(color);
        int index = 0;
        for (int i = index_first_hole+2; seeds > 0 ; i+= 2) {
            index = i%16;
            if(index == index_first_hole)
                continue;
            board[color_int][i]++;
            seeds--;
        }
        return index;
    }

    @Override
    public boolean otherPlayerIsStarving(int[][] board) {
        for (int i = (turn+1)%2; i < 16; i+=2) {
            if(!Main.has_seedColor(board, i, Color.R) && !Main.has_seedColor(board, i, Color.R) && Main.has_seedColor(board, i, Color.TR))
                return true;
        }
        return false;
    }
    private void add_to_score(int seedCaptured){
        score += seedCaptured;
    }

    @Override
    public int getScore() {
        return score;
    }
}
