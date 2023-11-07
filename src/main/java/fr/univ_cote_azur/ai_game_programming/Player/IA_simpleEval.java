package fr.univ_cote_azur.ai_game_programming.Player;

import fr.univ_cote_azur.ai_game_programming.Thread.Task_simpleEval;
import fr.univ_cote_azur.ai_game_programming.arraysOperations;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.lang.System.exit;

public class IA_simpleEval extends IA {
    public IA_simpleEval(int turn) {
        super(turn);
    }

    @Override
    protected double min_max_parent(int[][] board, int turn, double eval_Parent, boolean isMax, int depth) {
        int[][] legitMoves = arraysOperations.setLegitMoves(board, turn);

        assert legitMoves != null;
        Task_simpleEval[] Task_simpleEvals = new Task_simpleEval[legitMoves.length];


        ExecutorService executorService = Executors.newFixedThreadPool(legitMoves.length);
        for (int i = 0; i < legitMoves.length; i++) {
            Task_simpleEvals[i] = new Task_simpleEval(board, turn, eval_Parent, isMax, depth, legitMoves[i]);
            executorService.submit(Task_simpleEvals[i]);
        }

        executorService.shutdown();

        try {
            // Attendre la fin de l'exécution de tous les threads
            boolean terminated = executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

            if (!terminated) {
                System.out.println("The waiting time has elapsed, not all threads have finished.");
            }
        } catch (InterruptedException e) {
            System.out.println("Interruption occurred while waiting for the threads to finish.");
            exit(0);
        }

        double bestScore = Task_simpleEvals[0].getEval();
        arraysOperations.deepCopy(legitMoves[0], bestMove);
        for (int i = 1; i < Task_simpleEvals.length; i++) {
            if (bestScore < Task_simpleEvals[i].getEval()) {
                bestScore = Task_simpleEvals[i].getEval();
                arraysOperations.deepCopy(legitMoves[i], bestMove);
            }
        }

        return bestScore;
    }

    @Override
    public void printScore() {
        System.out.println("Basic IA score :" + getScore());
    }
}
