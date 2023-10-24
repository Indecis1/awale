package fr.univ_cote_azur.ai_game_programming.Player;

import fr.univ_cote_azur.ai_game_programming.Color;
import fr.univ_cote_azur.ai_game_programming.Main;

public abstract class Player {

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

    protected int sowingRed(int[][] board, int index_first_hole, Color color, int seeds){
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

    protected int sowingBlue(int[][] board, int index_first_hole, Color color, int seeds){
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

    public abstract int getScore();
    abstract int sowing(int[][] board, int index, Color color);
    public abstract boolean otherPlayerIsStarving(int[][] board);

}
