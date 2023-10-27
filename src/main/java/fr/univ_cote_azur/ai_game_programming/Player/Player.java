package fr.univ_cote_azur.ai_game_programming.Player;

import fr.univ_cote_azur.ai_game_programming.arraysOperations;
import fr.univ_cote_azur.ai_game_programming.Color;

public abstract class Player {

    public Player() {

    }

    protected int capturing(int[][] board, int lastIndex) {
        int captured_seeds = 0;
        for (int i = lastIndex; true; i--) {
            int index = (i + 16) % 16;
            int score = arraysOperations.count_seeds_at_index(board, index);
            if (!(score == 2 || score == 3)) break;
            captured_seeds += score;
            arraysOperations.emptyBoard_at_index(board, index);
        }
        return captured_seeds;
    }

    protected int sowingRed(int[][] board, int index_first_hole, Color color, int seeds) {
        int color_int = Color.to_index(color);
        int index = 0;
        for (int i = index_first_hole + 1; seeds > 0; i++) {
            index = i % 16;
            if (index == index_first_hole) continue;
            board[color_int][index]++;
            seeds--;
        }
        return index;
    }

    protected int sowingBlue(int[][] board, int index_first_hole, Color color, int seeds) {
        int color_int = Color.to_index(color);
        int index = 0;
        for (int i = index_first_hole + 1; seeds > 0; i += 2) {
            index = i % 16;
            if (index == index_first_hole) continue;
            board[color_int][index]++;
            seeds--;
        }
        return index;
    }

    public abstract int getScore();

    abstract void play(int[][] board);

    abstract int sowing(int[][] board, int index, Color color);

    public abstract boolean otherPlayerIsStarving(int[][] board);

    public abstract void printScore();

}
