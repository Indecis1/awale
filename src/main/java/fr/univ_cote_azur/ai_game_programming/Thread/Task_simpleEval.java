package fr.univ_cote_azur.ai_game_programming.Thread;

import fr.univ_cote_azur.ai_game_programming.Player.Simulate_Player;
import fr.univ_cote_azur.ai_game_programming.arraysOperations;

public class Task_simpleEval extends Task {
    public Task_simpleEval(int[][] board, int turn, double eval_Parent, boolean isMax, int depth, int[] move) {
        super(board, turn, eval_Parent, isMax, depth, move);
    }

    @Override
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

        if (depth - 1 == -1) {
            return local_eval;
        }

        int[][] legitMoves = arraysOperations.setLegitMoves(local_board, turn);
        if (legitMoves == null && isMax) {
            return -100;
        } else if (legitMoves == null) {
            return 100;
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
}
