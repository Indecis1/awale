package fr.univ_cote_azur.ai_game_programming.Player;

import fr.univ_cote_azur.ai_game_programming.Color;
import fr.univ_cote_azur.ai_game_programming.arraysOperations;

import java.util.Random;

public class Random_IA extends Player {

    private final int turn;
    private int score;

    public Random_IA(int turn) {
        this.turn = turn;
        this.score = 0;
    }

    public void play(int[][] board) {
        int index_first_hole = randomIndex(board);
        Color color = randomColor(board, index_first_hole);

        System.out.println("Random IA play is :" + (index_first_hole + 1) + color);

        int last_index = sowing(board, index_first_hole, color);
        int seed_captured = capturing(board, last_index);
        add_to_score(seed_captured);

        if (otherPlayerIsStarving(board)) {
            seed_captured = arraysOperations.count_seeds(board);
            add_to_score(seed_captured);
            arraysOperations.emptyBoard(board);
        }
    }

    private int randomIndex(int[][] board) {
        int random_index = new Random().nextInt(16);
        while (random_index % 2 != turn || arraysOperations.count_seeds_at_index(board, random_index) == 0)
            random_index = new Random().nextInt(16);
        return random_index;
    }

    private Color randomColor(int[][] board, int index_first_hole) {
        Color random_color = Color.getRandomColor();
        while (!arraysOperations.has_seed_of_Color(board, index_first_hole, random_color))
            random_color = Color.getRandomColor();
        return random_color;
    }

    @Override
    public int getScore() {
        return score;
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
        System.out.println("Random_IA score :" + getScore());
    }

    private void add_to_score(int seedCaptured) {
        score += seedCaptured;
    }
}
