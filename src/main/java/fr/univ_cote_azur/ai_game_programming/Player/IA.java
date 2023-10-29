package fr.univ_cote_azur.ai_game_programming.Player;

import fr.univ_cote_azur.ai_game_programming.Color;
import fr.univ_cote_azur.ai_game_programming.Thread.Task;
import fr.univ_cote_azur.ai_game_programming.arraysOperations;

import java.util.ArrayList;
import java.util.LinkedList;

public class IA extends Player {

    private final int turn;
    private final int[] bestMove;
    private int score;
    private int maxDepth;

    public IA(int turn) {
        this.turn = turn;
        this.score = 0;
        this.bestMove = new int[2];
        this.maxDepth = 0;
    }

    @Override
    public void play(int[][] board) {
        this.maxDepth = setMaxDepth(board);
        int eval_Global = 0;
        boolean isMax = true;
        long time_start = System.nanoTime();
        eval_Global = min_max_parent(board, turn, eval_Global, isMax, maxDepth);
        long time_end = System.nanoTime();
//        if ((time_end - time_start) / Math.pow(10, 9) < 0.009) {
//            maxDepth += 2;
//            time_start = System.nanoTime();
//            eval_Global = minMax(board, turn, eval_Global, isMax, maxDepth);
//            time_end = System.nanoTime();
//        } else if ((time_end - time_start) / Math.pow(10, 9) < 0.09) {
//            maxDepth++;
//            time_start = System.nanoTime();
//            eval_Global = minMax(board, turn, eval_Global, isMax, maxDepth);
//            time_end = System.nanoTime();
//        }

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

    private int setMaxDepth(int[][] board) {
        int count_legitMoves = arraysOperations.count_LegitMoves(board, turn);
        int count_seeds = arraysOperations.count_seeds(board);
        if (count_seeds > 60 && count_legitMoves > 25) {
            return 4;
        } else if (count_seeds < 50 && count_legitMoves < 20) {
            return 6;
        } else if (count_seeds > 25 && count_legitMoves < 20) {
            return 7;
        } else if (count_seeds > 15 && count_legitMoves < 20) {
            return 9;
        } else if (count_seeds > 10 && count_legitMoves < 15) {
            return 10;
        } else if (count_seeds < 10) {
            return 11;
        } else return 5;
    }

    private int min_max_parent(int[][] board, int turn, int eval_Parent, boolean isMax, int depth) {
        ArrayList<Integer> scores = new ArrayList<>();
        ArrayList<int[]> legitMoves = arraysOperations.setLegitMoves(board, turn);

        Thread[] threads = new Thread[legitMoves.size()];
        Task[] tasks = new Task[legitMoves.size()];

        for (int i = 0; i < legitMoves.size(); i++) {
            tasks[i] = new Task(board, turn, eval_Parent, isMax, depth, legitMoves.get(i));
            threads[i] = new Thread(tasks[i]);
            threads[i].start();
        }
        for (int i = 0; i < legitMoves.size(); i++) {
            try {
                threads[i].join(); // Attend la fin de chaque thread
                scores.add(tasks[i].getEval()); // Supposons que chaque tâche a une méthode pour récupérer le résultat
                System.out.println("" + legitMoves.get(i)[0] + Color.to_Color(legitMoves.get(i)[1]) + " score : " + scores.get(i));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println();
        int bestScore = scores.get(0);
        arraysOperations.deepCopy(legitMoves.get(0), bestMove);
        for (int i = 1; i < scores.size(); i++) {
            if (bestScore < scores.get(i)) {
                bestScore = scores.get(i);
                arraysOperations.deepCopy(legitMoves.get(i), bestMove);
            }
        }
        System.out.println();

        return bestScore;
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

}
