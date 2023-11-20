package fr.univ_cote_azur.ai_game_programming.Thread;

import fr.univ_cote_azur.ai_game_programming.Player.Simulate_Player;
import fr.univ_cote_azur.ai_game_programming.arraysOperations;

/**
 * The `Task_simpleEval` class represents a task to be executed concurrently in a separate thread.
 * It is used for performing a simplified minimax search with basic evaluation conditions
 * in the AI Game Programming application.
 */
public class Task_simpleEval extends Task {
    /**
     * Constructs a new `Task_simpleEval` instance.
     *
     * @param board       The game board represented as a 2D array.
     * @param turn        The current turn in the game.
     * @param eval_Parent The evaluation score of the parent node.
     * @param isMax       Indicates whether the task is for maximizing or minimizing the evaluation.
     * @param depth       The depth of the simplified minimax search tree.
     * @param move        The move to be considered at this node.
     */
    public Task_simpleEval(int[][] board, int turn, double eval_Parent, boolean isMax, int depth, int[] move) {
        super(board, turn, eval_Parent, isMax, depth, move);
    }

    /**
     * Performs the simplified minimax search algorithm with basic evaluation conditions
     * to determine the best move and its evaluation score.
     *
     * @param parent_board The game board at the parent node.
     * @param turn         The current turn in the game.
     * @param parent_move  The move made at the parent node.
     * @param parent_eval  The evaluation score of the parent node.
     * @param isMax        Indicates whether the current node is maximizing or minimizing.
     * @param depth        The remaining depth in the search tree.
     * @return The evaluation score of the best move.
     */
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

        // Update the local evaluation score based on the captured seeds and node type.
        if (isMax) {
            local_eval -= captured_seeds;
        } else {
            local_eval += captured_seeds;
        }

        // Check termination conditions for the search.
        if (depth - 1 == -1) {
            return local_eval;
        }

        // Get the legitimate moves for the current player.
        int[][] legitMoves = arraysOperations.setLegitMoves(local_board, turn);

        if (legitMoves == null && isMax) {
            return -100;
        } else if (legitMoves == null) {
            return 100;
        }

        double bestEval;
        if (isMax) {
            bestEval = -100;
        } else {
            bestEval = 100;
        }

        // Iterate through legitimate moves and recursively evaluate them.
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
