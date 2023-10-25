package fr.univ_cote_azur.ai_game_programming.Player;

import fr.univ_cote_azur.ai_game_programming.BoardOperations;
import fr.univ_cote_azur.ai_game_programming.Color;

import java.util.ArrayList;
import java.util.Stack;

public class IA extends Player {

    private final int turn;
    private final int maxDepth;
    private final Stack<int[][]> parent_boards;
    private int score;
    private Move bestMove;
    private int eval_Global;

    public IA(int turn) {
        this.turn = turn;
        this.score = 0;
        this.parent_boards = new Stack<>();
        this.bestMove = new Move(0, null);
        this.maxDepth = 1;
        this.eval_Global = 0;
    }

    @Override
    public void play(int[][] board) {
        this.eval_Global = 0;
        boolean isMax = true;
        int eval = minMax(board, turn, isMax, maxDepth);
        int index_first_hole = bestMove.indexPlay;
        Color color = bestMove.color;

        System.out.println("AI play is : **" + (index_first_hole + 1) + color + "**. Evaluation for a " + maxDepth + " depth is :" + eval);

        int last_index = sowing(board, index_first_hole, color);
        int seed_captured = capturing(board, last_index);
        add_to_score(seed_captured);

        if (otherPlayerIsStarving(board)) {
            seed_captured = BoardOperations.count_seeds(board);
            add_to_score(seed_captured);
            BoardOperations.emptyBoard(board);
        }
    }

    // TODO : Implementer algorithm Min-Max
    private int minMax(int[][] board, int turn, boolean isMax, int depth) {
        if (depth == -1) {
            return eval_Global;
        }

        int save_eval = eval_Global;

        int local_eval;
        if (isMax)
            local_eval = -100;
        else
            local_eval = 100;

        ArrayList<Move> legitMoves = BoardOperations.setLegitMoves(board, turn);
        if (legitMoves.isEmpty() && isMax) {
            parent_boards.pop();
            return -100;
        } else if (legitMoves.isEmpty()) {
            parent_boards.pop();
            return 100;
        }
        if (depth == maxDepth) bestMove = new Move(legitMoves.get(0).indexPlay, legitMoves.get(0).color);


        parent_boards.push(board);
        Simulate_Player player = new Simulate_Player(turn);


        for (Move move : legitMoves) {
            int[][] local_board = new int[3][16];
            BoardOperations.deepCopy(parent_boards.peek(), local_board);
//            System.out.print(" Depth :" + depth +" Move : " + (move.indexPlay + 1) + move.color + ". -- ");
//            for (int j = 0; j < 16; j++) {
//                System.out.print((j + 1) + "(" + local_board[0][j] + "R," + local_board[1][j] + "B," + local_board[2][j] + "T)   ");
//            }
//            System.out.println();

            eval_Global = save_eval;

            player.simulate_play(local_board, move);
            int captured_seeds = player.getScore();

            if (isMax) eval_Global += captured_seeds;
            else eval_Global -= captured_seeds;

            int score = minMax(local_board, (turn + 1) % 2, !isMax, depth - 1);

            if (isMax && score > local_eval) {
                local_eval = score;
                if (depth == maxDepth) {
                    bestMove = new Move(move.indexPlay, move.color);
                }
            } else if (!isMax && score < local_eval) {
                local_eval = score;
            }


            //TODO : implement alpha beta here.

        }
        parent_boards.pop();
        return local_eval;
    }

    @Override
    public int getScore() {
        return score;
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
        System.out.println("IA score :" + getScore());
    }

    private void add_to_score(int seedCaptured) {
        score += seedCaptured;
    }

    public record Move(int indexPlay, Color color) {
    }

}
