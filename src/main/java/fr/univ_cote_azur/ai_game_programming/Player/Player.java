package fr.univ_cote_azur.ai_game_programming.Player;

import fr.univ_cote_azur.ai_game_programming.Color;
import fr.univ_cote_azur.ai_game_programming.Main;

public abstract class Player {
    private int score;

    public Player(){

    }

    protected int capturing(int[][] board, int lastIndex){
        int captured_seeds = 0;
        int index = lastIndex;
        while (Main.count_seeds_at_index(board, lastIndex) == 2 || Main.count_seeds_at_index(board, lastIndex) == 3){
            captured_seeds += Main.count_seeds_at_index(board, lastIndex);
            Main.emptyBoard_at_index(board, index);
            index = (index-1)%16;
        }
        return captured_seeds;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    abstract void sowing(int[][] board, int index, Color color);
    public abstract boolean otherPlayerIsStarving();

}
