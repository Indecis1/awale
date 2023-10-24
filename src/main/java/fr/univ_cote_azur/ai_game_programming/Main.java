package fr.univ_cote_azur.ai_game_programming;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }

    public static int count_seeds(int[][] board) {
        int count_seeds_val = 0;
        for (int j = 0; j < board.length; j++) {
            count_seeds_val += board[0][j] + board[1][j] + board[2][j];
        }
        return count_seeds_val;
    }

    public static int count_seeds_at_index(int[][] board, int index) {
        return board[0][index] + board[1][index] + board[2][index];
    }

    public static void emptyBoard_at_index(int[][] board, int index) {
        board[0][index] = 0;
        board[1][index] = 0;
        board[2][index] = 0;
    }

    public static void print_Board(int[][] board, int index) {
        board[0][index] = 0;
        board[1][index] = 0;
        board[2][index] = 0;
    }


}