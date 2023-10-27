package fr.univ_cote_azur.ai_game_programming.Player;

import fr.univ_cote_azur.ai_game_programming.arraysOperations;
import fr.univ_cote_azur.ai_game_programming.Color;

import java.util.LinkedList;
import java.util.Stack;

public class IA extends Player {

    private final int turn;
    private final Stack<int[][]> parent_boards;
    private int score;
    private int maxDepth;
    private int[] bestMove;
    private int eval_Global;

    public IA(int turn) {
        this.turn = turn;
        this.score = 0;
        this.parent_boards = new Stack<>();
        this.bestMove = new int[2];
        this.eval_Global = 0;
        this.maxDepth = 0;
    }

    @Override
    public void play(int[][] board) {
        this.maxDepth = setMaxDepth(board);
        this.eval_Global = getScore();
        boolean isMax = true;
        long time_start = System.nanoTime();
        int eval = minMax(board, turn, isMax, maxDepth);
        long time_end = System.nanoTime();
        int index_first_hole = bestMove[0];
        Color color = Color.to_Color(bestMove[1]);

        System.out.println("AI play is : **" + (index_first_hole + 1) + color + "**. Evaluation for a " + maxDepth + " depth is :" + eval + " in " + (time_end - time_start) / Math.pow(10, 9) + "s.");

        int last_index = sowing(board, index_first_hole, color);
        int seed_captured = capturing(board, last_index);
        add_to_score(seed_captured);

        if (otherPlayerIsStarving(board)) {
            seed_captured = arraysOperations.count_seeds(board);
            add_to_score(seed_captured);
            arraysOperations.emptyBoard(board);
        }
    }

    private int setMaxDepth(int[][] board) {
        if (arraysOperations.count_seeds(board) > 74) return 4;
        else if (arraysOperations.count_seeds(board) > 50) {
            return 5;
        } else return 4;
    }

    private int minMax(int[][] board, int turn, boolean isMax, int depth) {
        int save_eval = eval_Global;
        int parent_eval = eval_Global;

        int local_eval;
        if (isMax) local_eval = -100;
        else local_eval = 100;

        LinkedList<int[]> legitMoves = arraysOperations.setLegitMoves(board, turn);
        if (legitMoves.isEmpty() && isMax) {
            return -100;
        } else if (legitMoves.isEmpty()) {
            return 100;
        }
        if (depth == maxDepth) {
            arraysOperations.deepCopy(legitMoves.getFirst(), bestMove);
        }


        parent_boards.push(board);
        Simulate_Player player = new Simulate_Player(turn);

        for (int[] move : legitMoves) {
            if(isMax && local_eval > parent_eval)
                parent_eval = local_eval;
            else if (!isMax && local_eval < parent_eval) {
                parent_eval = local_eval;
            }
            int[][] local_board = new int[3][16];
            arraysOperations.deepCopy(parent_boards.peek(), local_board);

            eval_Global = save_eval;

            player.simulate_play(local_board, move);
            int captured_seeds = player.getScore();

            if (isMax) eval_Global += captured_seeds;
            else eval_Global -= captured_seeds;

            int score;
            if (depth - 1 == -1) score = eval_Global;
            else score = minMax(local_board, (turn + 1) % 2, !isMax, depth - 1);

            local_eval = eval(isMax, local_eval, score, move, depth);

            if ((isMax && local_eval > parent_eval) || (!isMax && local_eval < parent_eval)) {
                parent_boards.pop();
                return parent_eval;
            }
        }
        
        parent_boards.pop();
        return local_eval;
    }

    private int eval(boolean isMax, int local_eval, int score, int[] move, int depth) {
        if (isMax) {
            if (score > local_eval) {
                local_eval = score;
                if (depth == maxDepth) arraysOperations.deepCopy(move, bestMove);
            }
        } else {
            if (score < local_eval) {
                local_eval = score;
            }
        }
        return local_eval;
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
        System.out.println("IA score :" + getScore());
    }

    private void add_to_score(int seedCaptured) {
        score += seedCaptured;
    }

//    public record Move(int indexPlay, Color color) {
//    }

}