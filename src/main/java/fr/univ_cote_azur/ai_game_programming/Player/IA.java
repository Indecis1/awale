package fr.univ_cote_azur.ai_game_programming.Player;

import fr.univ_cote_azur.ai_game_programming.Color;
import fr.univ_cote_azur.ai_game_programming.Thread.Task;
import fr.univ_cote_azur.ai_game_programming.arraysOperations;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.lang.System.exit;

/**
 * The `IA` class represents an AI player in the game.
 */
public class IA extends Player {

    protected final int turn;
    protected final int[] bestMove;
    protected int score;
    protected int maxDepth;

    /**
     * Constructs a new `IA` instance with the specified turn.
     *
     * @param turn The turn of the AI player (0 or 1).
     */
    public IA(int turn) {
        this.turn = turn;
        this.score = 0;
        this.bestMove = new int[2];
        this.maxDepth = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void play(int[][] board) {
        this.maxDepth = 4;
        double eval_Global = score;
        boolean isMax = true;
        long time_start = System.nanoTime();
        eval_Global = min_max_parent(board, turn, eval_Global, isMax, maxDepth);
        long time_end = System.nanoTime();

        // If the move is too quick, modify its depth to make it more powerful.
        while ((time_end - time_start) / Math.pow(10, 9) < 0.35) {
            System.out.println("changing depth " + maxDepth + "..." + (time_end - time_start) / Math.pow(10, 9) + "s.");
            double calcTime = (time_end - time_start) / Math.pow(10, 9);
            if (calcTime < 0.009) {
                maxDepth += 2;
            } else if (calcTime > 0.1 && maxDepth == 4 && score < 8) break;
            else if (calcTime > 0.2 && maxDepth == 6) break;
            else maxDepth++;
            time_start = System.nanoTime();
            eval_Global = min_max_parent(board, turn, eval_Global, isMax, maxDepth);
            time_end = System.nanoTime();
        }

        int index_first_hole = bestMove[0];
        Color color = Color.to_Color(bestMove[1]);

        System.out.println("AI play is : **" + (index_first_hole + 1) + color + "**. Evaluation for a " + maxDepth + " depth is :" + eval_Global + " in " + (time_end - time_start) / Math.pow(10, 9) + "s.");

        int last_index = sowing(board, index_first_hole, color);
        int seed_captured = capturing(board, last_index);
        add_to_score(seed_captured);

        if (otherPlayerIsStarving(board)) {
            seed_captured = arraysOperations.count_seeds(board);
            add_to_score(seed_captured);
            arraysOperations.emptyBoard(board);
        }
    }

    /**
     * Executes the parent node of the minimax algorithm and returns the best move evaluation score.
     *
     * @param board       The game board represented as a 2D array.
     * @param turn        The current turn in the game.
     * @param eval_Parent The evaluation score of the parent node.
     * @param isMax       Indicates whether the current node is maximizing or minimizing.
     * @param depth       The depth of the search tree.
     * @return The evaluation score of the best move.
     */
    protected double min_max_parent(int[][] board, int turn, double eval_Parent, boolean isMax, int depth) {
        int[][] legitMoves = arraysOperations.setLegitMoves(board, turn);

        assert legitMoves != null;
        Task[] tasks = new Task[legitMoves.length];

        ExecutorService executorService = Executors.newFixedThreadPool(legitMoves.length);
        for (int i = 0; i < legitMoves.length; i++) {
            tasks[i] = new Task(board, turn, eval_Parent, isMax, depth, legitMoves[i]);
            executorService.submit(tasks[i]);
        }

        executorService.shutdown();

        try {
            // Wait for the completion of all threads.
            boolean terminated = executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

            if (!terminated) {
                System.out.println("The waiting time has elapsed, not all threads have finished.");
            }
        } catch (InterruptedException e) {
            System.out.println("Interruption occurred while waiting for the threads to finish.");
            exit(0);
        }

        double bestScore = tasks[0].getEval();
        arraysOperations.deepCopy(legitMoves[0], bestMove);
        for (int i = 1; i < tasks.length; i++) {
            if (bestScore < tasks[i].getEval()) {
                bestScore = tasks[i].getEval();
                arraysOperations.deepCopy(legitMoves[i], bestMove);
            }
        }

        return bestScore;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getScore() {
        return score;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    int sowing(int[][] board, int index_first_hole, Color color) {
        int seeds = arraysOperations.get_seedColor(board, index_first_hole, color);
        arraysOperations.emptySeedColor_at_index(board, index_first_hole, color);
        if (color == Color.R || color == Color.TR) return sowingRed(board, index_first_hole, color, seeds);
        else return sowingBlue(board, index_first_hole, color, seeds);
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void printScore() {
        System.out.println("Advanced IA score :" + getScore());
    }

    /**
     * Adds the given seed count to the current score.
     *
     * @param seedCaptured The number of seeds captured in a move.
     */
    protected void add_to_score(int seedCaptured) {
        score += seedCaptured;
    }
}

