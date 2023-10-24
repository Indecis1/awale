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

    public static void emptyBoard(int[][] board) {
        for (int j = 0; j < board.length; j++) {
            board[0][j] = 0;
            board[1][j] = 0;
            board[2][j] = 0;
        }
    }

    public static void emptyBoard_at_index(int[][] board, int index) {
        board[0][index] = 0;
        board[1][index] = 0;
        board[2][index] = 0;
    }

    public static void emptySeedColor_at_index(int[][] board, int index, Color color) {
        int line;
        if(color == Color.R)
            line = 0;
        else if (color == Color.B)
            line = 1;
        else
            line = 2;
        board[line][index] = 0;
    }

    public static void print_Board(int[][] board) {
        for (int j = 0; j < 8; j++) {
            System.out.print((j+1) +"(R" + board[0][j] + ",B" + board[1][j]+ ",T" + board[2][j] +") -- ");
        }
        System.out.println();
        for (int j = 15; j >= 8; j--) {
            System.out.print((j+1) +"(R" + board[0][j] + ",B" + board[1][j]+ ",T" + board[2][j] +") -- ");
        }
        System.out.println("-".repeat(90));
    }

    public static boolean has_seedColor(int[][] board, int index, Color color){
        int line;
        if(color == Color.R)
            line = 0;
        else if (color == Color.B)
            line = 1;
        else
            line = 2;
        return board[line][index] > 0;

    }
    public static int get_seedColor(int[][] board, int index, Color color){
        int line;
        if(color == Color.R)
            line = 0;
        else if (color == Color.B)
            line = 1;
        else
            line = 2;
        return board[line][index];
    }

}