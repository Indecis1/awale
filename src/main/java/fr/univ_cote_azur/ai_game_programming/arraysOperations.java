package fr.univ_cote_azur.ai_game_programming;

import fr.univ_cote_azur.ai_game_programming.Player.Player;

public class arraysOperations {


    public static int count_seeds(int[][] board) {
        int count_seeds_val = 0;
        for (int j = 0; j < 16; j++) {
            count_seeds_val += board[0][j] + board[1][j] + board[2][j];
        }
        return count_seeds_val;
    }

    public static int count_seeds_at_index(int[][] board, int index) {
        return board[0][index] + board[1][index] + board[2][index];
    }

    public static void emptyBoard(int[][] board) {
        for (int j = 0; j < 16; j++) {
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
        if (color == Color.R) line = 0;
        else if (color == Color.B) line = 1;
        else line = 2;
        board[line][index] = 0;
    }

    public static void print_Board(int[][] board) {
        print_board(board);
        System.out.println("\n" + "-".repeat(90));
    }

    public static void print_Board(int[][] board, Player[] players) {
        print_board(board);
        System.out.println("\n\n");
        players[0].printScore();
        players[1].printScore();
        System.out.println("\n" + "-".repeat(90));
    }

    private static void print_board(int[][] board) {
        System.out.println();
        for (int j = 0; j < 8; j++) {
            System.out.print((j + 1) + "(" + board[0][j] + "R," + board[1][j] + "B," + board[2][j] + "T)   ");
        }
        System.out.println();
        for (int j = 15; j >= 8; j--) {
            System.out.print((j + 1) + "(" + board[0][j] + "R," + board[1][j] + "B," + board[2][j] + "T)   ");
        }
    }

    public static boolean has_seed_of_Color(int[][] board, int index, Color color) {
        int line;
        if (color == Color.R) line = 0;
        else if (color == Color.B) line = 1;
        else line = 2;
        return board[line][index] > 0;
    }

    public static int get_seedColor(int[][] board, int index, Color color) {
        int line;
        if (color == Color.R) line = 0;
        else if (color == Color.B) line = 1;
        else line = 2;
        return board[line][index];
    }

    public static int[][] setLegitMoves(int[][] board, int index_start) {
        int[][] legitMoves = new int[32][2]; // Pré-allouer la taille maximale possible
        int count = 0;

        for (int j = index_start; j < 16; j += 2) {
            for (int i = 0; i <= 2; i++) {
                if (board[i][j] > 0) {
                    legitMoves[count][0] = j;
                    legitMoves[count][1] = i;
                    count++;
                    if (i == 2) {
                        legitMoves[count][0] = j;
                        legitMoves[count][1] = 3;
                        count++;
                    }
                }
            }
        }

        if (count == 0) {
            return null;
        }

        // Réduire la taille du tableau à la taille réelle des mouvements légitimes
        int[][] result = new int[count][2];
        System.arraycopy(legitMoves, 0, result, 0, count);

        return result;
    }

    public static int count_criticHoles(int[][] board, int index_start) {
        int count = 0;

        for (int i = index_start; i < 16; i += 2) {
            int count_seeds_at_index = count_seeds_at_index(board, i);
            if (count_seeds_at_index < 3) count++;
            if (count_seeds_at_index == 0) count += 2;
        }
        return count;
    }

    public static double count_criticHoles2(int[][] board, int index_start) {
        double count = 0;

        for (int i = index_start; i < 16; i += 2) {
            int count_seeds_at_index = count_seeds_at_index(board, i);
            if (count_seeds_at_index < 3) count++;
            if (count_seeds_at_index == 0) count += 0.05;
        }
        return count;
    }

    public static double count_criticHoles3(int[][] board, int index_start) {
        double count = 0;

        for (int i = index_start; i < 16; i += 2) {
            int count_seeds_at_index = count_seeds_at_index(board, i);
            if (count_seeds_at_index == 2 || count_seeds_at_index == 1) count++;
            if (count_seeds_at_index == 0) count += 0.05;
        }
        return count;
    }

    public static void deepCopy(int[][] source_board, int[][] new_board) {
        for (int i = 0; i < 3; i++) {
            System.arraycopy(source_board[i], 0, new_board[i], 0, 16);
        }
    }

    public static void deepCopy(int[] source_board, int[] new_board) {
        System.arraycopy(source_board, 0, new_board, 0, 2);
    }

}
