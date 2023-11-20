package fr.univ_cote_azur.ai_game_programming.Thread;

import fr.univ_cote_azur.ai_game_programming.Player.Simulate_Player;
import fr.univ_cote_azur.ai_game_programming.arraysOperations;

/**
 * The `Task3` class represents a task to be executed concurrently in a separate thread.
 * It is used for performing an extended minimax search with additional evaluation conditions
 * in the AI Game Programming application.
 */
public class Task3 implements Runnable {
    private final int[][] board;
    private final int turn;
    boolean isMax;
    int depth;
    int[] move;
    private double local_evaluation;

    /**
     * Constructs a new `Task3` instance.
     *
     * @param board       The game board represented as a 2D array.
     * @param turn        The current turn in the game.
     * @param eval_Parent The evaluation score of the parent node.
     * @param isMax       Indicates whether the task is for maximizing or minimizing the evaluation.
     * @param depth       The depth of the extended minimax search tree.
     * @param move        The move to be considered at this node.
     */
    public Task3(int[][] board, int turn, double eval_Parent, boolean isMax, int depth, int[] move) {
        this.board = new int[3][16];
        arraysOperations.deepCopy(board, this.board);
        this.turn = turn;
        this.local_evaluation = eval_Parent;
        this.isMax = isMax;
        this.depth = depth;
        this.move = move;
    }

    /**
     * Executes the task, performing an extended minimax search with additional evaluation conditions.
     */
    @Override
    public synchronized void run() {
        boolean isMax = this.isMax;
        int depth = this.depth;
        int turn = this.turn;
        local_evaluation = minMax(board, (turn + 1) % 2, move, local_evaluation, !isMax, depth);
    }

    /**
     * Performs the extended minimax search algorithm with additional evaluation conditions
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
            if (!isMax) {
                double count_criticHoles = arraysOperations.count_criticHoles2(local_board, (turn + 1) % 2);
                // 1/17 = 0.0625
                if (count_criticHoles > 0) {
                    local_eval -= (0.0625 * count_criticHoles);
                }
            }
            return local_eval;
        }

        if (arraysOperations.count_seeds(local_board) < 10) {
            if (local_eval > 0) {
                return 100;
            }
            return -100;
        }

        if (Math.abs(local_eval) > 40) {
            if (local_eval > 40) {
                return 100;
            }
            return -100;
        }

        if (player.otherPlayerIsStarving(local_board)) {
            if (isMax) {
                return -100;
            }
            return 100;
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

            // TODO: evaluation error...

            if ((isMax && bestEval > parent_eval) || (!isMax && bestEval < parent_eval)) {
                break;
            }
        }
        return bestEval;
    }

    /**
     * Evaluates and returns the best score based on the current node type (max or min).
     *
     * @param isMax             Indicates whether the current node is maximizing or minimizing.
     * @param local_parent_eval The evaluation score at the parent node.
     * @param score             The evaluation score of the current node.
     * @return The best evaluation score considering the node type (max or min).
     */
    protected synchronized double eval(boolean isMax, double local_parent_eval, double score) {
        if (isMax && score > local_parent_eval) {
            return score;
        }
        if (!isMax && score < local_parent_eval) {
            return score;
        }
        return local_parent_eval;
    }

    /**
     * Gets the evaluation score of the current task.
     *
     * @return The evaluation score of the current task.
     */
    public double getEval() {
        if (move[1] == 0) {
            return local_evaluation - 0.01;
        }
        return local_evaluation;
    }
}