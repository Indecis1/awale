package fr.univ_cote_azur.ai_game_programming.Thread;

import fr.univ_cote_azur.ai_game_programming.Player.Simulate_Player;
import fr.univ_cote_azur.ai_game_programming.arraysOperations;

import java.util.ArrayList;

public class Task implements Runnable {
    private final int[][] board;
    private final int turn;
    boolean isMax;
    int depth;
    int[] move;
    private int local_evaluation;

    public Task(int[][] board, int turn, int eval_Parent, boolean isMax, int depth, int[] move) {
        this.board = new int[3][16];
        arraysOperations.deepCopy(board, this.board);
        this.turn = turn;
        this.local_evaluation = eval_Parent;
        this.isMax = isMax;
        this.depth = depth;
        this.move = move;
    }

    @Override
    public synchronized void run() {
        boolean isMax = this.isMax;
        int depth = this.depth;
        int turn = this.turn;
        local_evaluation = minMax(board, (turn + 1) % 2, move, local_evaluation, !isMax, depth);
    }




    public synchronized int minMax(int[][] parent_board, int turn, int[] parent_move, int parent_eval, boolean isMax, int depth) {

        int[][] local_board = new int[3][16];
        arraysOperations.deepCopy(parent_board, local_board);
        int local_eval = parent_eval;
        // Simulate the parent play here.
        Simulate_Player player = new Simulate_Player((turn + 1) % 2);
        player.setScore(0);
        player.simulate_play(local_board, parent_move);
        int captured_seeds = player.getScore();
        // Si on est max, le parent etait en min. Donc ce move nous fait perdre des points.
        if (isMax) local_eval -= captured_seeds;
        else local_eval += captured_seeds;

        if (depth - 1 == -1) {
            return local_eval;
        }

        ArrayList<int[]> legitMoves = arraysOperations.setLegitMoves(local_board, turn);
        if (legitMoves.isEmpty() && isMax) {
            return -100;
        } else if (legitMoves.isEmpty()) {
            return 100;
        }

        int bestEval;
        if (isMax) bestEval = -100;
        else bestEval = 100;


        for (int[] move : legitMoves) {

            int score = minMax(local_board, (turn + 1) % 2, move, local_eval, !isMax, depth - 1);

            if (move == legitMoves.get(0)) {
                bestEval = score;
                continue;
            }

            bestEval = eval(isMax, bestEval, score);

            if ((isMax && bestEval > parent_eval) || (!isMax && bestEval < parent_eval)) {
                break;
            }
        }
        return bestEval;
    }

    private synchronized int eval(boolean isMax, int local_parent_eval, int score) {
        if (isMax && score > local_parent_eval) return score;
        if (!isMax && score < local_parent_eval) return score;
        return local_parent_eval;
    }

    public int getEval() {
        return local_evaluation;
    }
}