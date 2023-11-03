package fr.univ_cote_azur.ai_game_programming.Thread;

import fr.univ_cote_azur.ai_game_programming.Player.Simulate_Player;
import fr.univ_cote_azur.ai_game_programming.arraysOperations;

public class Task implements Runnable {
    private final int[][] board;
    private final int turn;
    boolean isMax;
    int depth;
    int[] move;
    private float local_evaluation;

    public Task(int[][] board, int turn, float eval_Parent, boolean isMax, int depth, int[] move) {
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


    public synchronized float minMax(int[][] parent_board, int turn, int[] parent_move, float parent_eval, boolean isMax, int depth) {

        int[][] local_board = new int[3][16];
        arraysOperations.deepCopy(parent_board, local_board);
        float local_eval = parent_eval;
        // Simulate the parent play here.
        Simulate_Player player = new Simulate_Player((turn + 1) % 2);
        player.setScore(0);
        player.simulate_play(local_board, parent_move);
        int captured_seeds = player.getScore();
        // Si on est max, le parent etait en min. Donc ce move nous fait perdre des points.
        if (isMax) local_eval -= captured_seeds;
        else local_eval += captured_seeds;

        if (depth - 1 == -1 || arraysOperations.count_seeds(local_board) < 10 || Math.abs(local_eval) > 41) {
            // A supprimer si on se fait exploser
            if (!isMax) {
                int count_criticHoles = arraysOperations.count_criticHoles(local_board, (turn + 1) % 2);
                // 1/16 = 0.0625
                if (count_criticHoles > 0) local_eval -= (float) (0.0620 * count_criticHoles);
            }
            return local_eval;
        }

        int[][] legitMoves = arraysOperations.setLegitMoves(local_board, turn);
        if (legitMoves == null && isMax) {
            return -100;
        } else if (legitMoves == null) {
            return 100;
        }

        float bestEval;
        if (isMax) bestEval = -100;
        else bestEval = 100;


        for (int[] move : legitMoves) {

            float score = minMax(local_board, (turn + 1) % 2, move, local_eval, !isMax, depth - 1);

            if (move == legitMoves[0]) {
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

    protected synchronized float eval(boolean isMax, float local_parent_eval, float score) {
        if (isMax && score > local_parent_eval) return score;
        if (!isMax && score < local_parent_eval) return score;
        return local_parent_eval;
    }

    public float getEval() {
        return local_evaluation;
    }
}