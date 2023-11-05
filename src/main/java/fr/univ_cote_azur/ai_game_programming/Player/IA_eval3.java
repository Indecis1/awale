package fr.univ_cote_azur.ai_game_programming.Player;

import fr.univ_cote_azur.ai_game_programming.Color;
import fr.univ_cote_azur.ai_game_programming.Thread.Task3;
import fr.univ_cote_azur.ai_game_programming.arraysOperations;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.lang.System.exit;

public class IA_eval3 extends IA {


    public IA_eval3(int turn) {
        super(turn);
    }


    public void play(int[][] board, int score_IA, int score_OP) {
        this.maxDepth = 3;
        double eval_Global = score;
        boolean isMax = true;
        long time_start = System.nanoTime();
        eval_Global = this.min_max_parent(board, turn, eval_Global, isMax, maxDepth, score_IA, score_OP);
        long time_end = System.nanoTime();

//        while ((time_end - time_start) / Math.pow(10, 9) < 0.35) {
//            System.out.println("changing depth " + maxDepth + "..." + (time_end - time_start) / Math.pow(10, 9) + "s.");
//            double calcTime = (time_end - time_start) / Math.pow(10, 9);
//            if(maxDepth > 50) break;
//            if (calcTime < 0.009)
//                maxDepth ++;
//            else if (calcTime > 0.1 && maxDepth == 3 && score < 8) break;
//            else if (calcTime > 0.1 && maxDepth == 5) break;
//            else if (calcTime > 0.08 && maxDepth == 6) break;
//            else maxDepth++;
//            time_start = System.nanoTime();
//            eval_Global = min_max_parent(board, turn, score, isMax, maxDepth, score_IA, score_OP);
//            time_end = System.nanoTime();
//        }


        int index_first_hole = bestMove[0];
        Color color = Color.to_Color(bestMove[1]);

        System.out.println("AI eval3 play is : **" + (index_first_hole + 1) + color + "**. Evaluation for a " + maxDepth + " depth is :" + eval_Global + " in " + (time_end - time_start) / Math.pow(10, 9) + "s.");

        int last_index = sowing(board, index_first_hole, color);
        int seed_captured = capturing(board, last_index);
        add_to_score(seed_captured);

        if (otherPlayerIsStarving(board)) {
            seed_captured = arraysOperations.count_seeds(board);
            add_to_score(seed_captured);
            arraysOperations.emptyBoard(board);
        }
    }


    protected double min_max_parent(int[][] board, int turn, double eval_Parent, boolean isMax, int depth, int score_IA, int score_OP) {
        int[][] legitMoves = arraysOperations.setLegitMoves(board, turn);
        assert legitMoves != null;
        Task3[] task3s = new Task3[legitMoves.length];


        ExecutorService executorService = Executors.newFixedThreadPool(legitMoves.length);
        for (int i = 0; i < legitMoves.length; i++) {
            task3s[i] = new Task3(board, turn, eval_Parent, isMax, depth, legitMoves[i], score_IA, score_OP);
            executorService.submit(task3s[i]);
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

        double bestScore = task3s[0].getEval();
        arraysOperations.deepCopy(legitMoves[0], bestMove);
        for (int i = 1; i < task3s.length; i++) {
            System.out.println("Move " + (legitMoves[i][0]+1) + legitMoves[i][1]  + "'s eval : " + task3s[i].getEval());
            if (bestScore < task3s[i].getEval()) {
                bestScore = task3s[i].getEval();
                arraysOperations.deepCopy(legitMoves[i], bestMove);
            }
        }

        return bestScore;
    }

    @Override
    public void printScore() {
        System.out.println("IA eval 3 score :" + getScore());
    }
}
