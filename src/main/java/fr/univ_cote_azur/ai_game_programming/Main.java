package fr.univ_cote_azur.ai_game_programming;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }

    public static int count_seeds(int[][] board){
        int count_seeds_val = 0;
        for (int j = 0; j < board.length; j++) {
            count_seeds_val += board[0][j] + board[1][j] + board[2][j];
        }
        return count_seeds_val;
    }


}