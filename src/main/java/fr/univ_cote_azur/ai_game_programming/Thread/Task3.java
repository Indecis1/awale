package fr.univ_cote_azur.ai_game_programming.Thread;

import fr.univ_cote_azur.ai_game_programming.Player.Simulate_Player;
import fr.univ_cote_azur.ai_game_programming.arraysOperations;

public class Task3 implements Runnable {
    private final int[][] board;
    private final int turn;
    boolean isMax;
    int depth;
    int[] move;
    private double local_evaluation;

    public Task3(int[][] board, int turn, double eval_Parent, boolean isMax, int depth, int[] move) {
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


    public synchronized double minMax(int[][] parent_board, int turn, int[] parent_move, double parent_eval, boolean isMax, int depth) {
        int[][] local_board = new int[3][16];
        arraysOperations.deepCopy(parent_board, local_board);
        double local_eval = parent_eval;
        // Simulate the parent play here.
        Simulate_Player player = new Simulate_Player((turn + 1) % 2);
        player.setScore(0);
        player.simulate_play(local_board, parent_move);
        int captured_seeds = player.getScore();
        // Si on est max, le parent etait en min. Donc ce move nous fait perdre des points.
        if (isMax) local_eval -= captured_seeds;
        else local_eval += captured_seeds;


        // TODO : Trop de temps... Trouver comment optimiser ce code.
        boolean hasMoreThen_41_seeds = local_eval > 41;
        int[][] legitMoves = arraysOperations.setLegitMoves(local_board, turn);
        boolean isStarving = legitMoves == null;

        boolean end_game_One = isStarving || hasMoreThen_41_seeds;
        boolean end_game_Two = arraysOperations.count_seeds(local_board) < 10;
        boolean end_minMax = depth - 1 == -1;

        if (end_game_One || end_minMax || end_game_Two) {
            if (end_game_One) {
                if (isMax) {
                    return -100;
                }
                return 100;
            }
            if (end_minMax) {
                if (!isMax) {
                    double count_criticHoles = arraysOperations.count_criticHoles2(local_board, (turn + 1) % 2);
                    // 1/17 = 0.0625
                    if (count_criticHoles > 0) local_eval -= (0.0625 * count_criticHoles);
                }
                return local_eval;
            } else {
                return local_eval;
            }
        }

        double bestEval;
        if (isMax) bestEval = -100;
        else bestEval = 100;


        for (int[] move : legitMoves) {

            double score = minMax(local_board, (turn + 1) % 2, move, local_eval, !isMax, depth - 1);

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

    protected synchronized double eval(boolean isMax, double local_parent_eval, double score) {
        if (isMax && score > local_parent_eval) return score;
        if (!isMax && score < local_parent_eval) return score;
        return local_parent_eval;
    }

    public double getEval() {
        if (move[1] == 0) return local_evaluation - 0.01;
        return local_evaluation;
    }

}