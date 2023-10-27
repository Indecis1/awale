package fr.univ_cote_azur.ai_game_programming.Player;

import fr.univ_cote_azur.ai_game_programming.Color;
import fr.univ_cote_azur.ai_game_programming.arraysOperations;

import java.util.LinkedList;
import java.util.Stack;

public class IA extends Player {

    private final int turn;
    private final Stack<int[][]> parent_boards;
    private int score;
    private int maxDepth;
    private final int[] bestMove;

    public IA(int turn) {
        this.turn = turn;
        this.score = 0;
        this.parent_boards = new Stack<>();
        this.bestMove = new int[2];
        this.maxDepth = 0;
    }

    @Override
    public void play(int[][] board) {
        this.maxDepth = setMaxDepth(board);
        int eval_Global = getScore();
        boolean isMax = true;
        long time_start = System.nanoTime();
        eval_Global = minMax(board, turn, eval_Global, isMax, maxDepth);
        long time_end = System.nanoTime();
        if( (time_end - time_start)/Math.pow(10, 9) < 0.009){
            maxDepth+=2;
            time_start = System.nanoTime();
            eval_Global = minMax(board, turn, eval_Global, isMax, maxDepth);
            time_end = System.nanoTime();
        }else if( (time_end - time_start)/Math.pow(10, 9) < 0.09){
            maxDepth++;
            time_start = System.nanoTime();
            eval_Global = minMax(board, turn, eval_Global, isMax, maxDepth);
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

    private int setMaxDepth(int[][] board) {
        int count_legitMoves = arraysOperations.count_LegitMoves(board, turn);
        int count_seeds = arraysOperations.count_seeds(board);
        if (count_seeds > 60     && count_legitMoves > 25) {
            return 4;
        } else if (count_seeds > 35 && count_legitMoves < 20) {
            return 6;
        } else if (count_seeds > 25 && count_legitMoves < 20) {
            return 7;
        } else if (count_seeds > 15 && count_legitMoves < 20) {
            return 9;
        } else if (count_seeds > 10 && count_legitMoves < 15) {
            return 10;
        } else if (count_legitMoves < 10) {
            return 11;
        } else return 5;
    }

    //TODO : Multi-threading si possible pour le fond 1
    private int minMax(int[][] board, int turn, int eval_Parent, boolean isMax, int depth) {
        int save_eval = eval_Parent;
        int parent_eval = eval_Parent;

        int local_eval;
        if (isMax) local_eval = -100;
        else local_eval = 100;

        LinkedList<int[]> legitMoves = arraysOperations.setLegitMoves(board, turn);
        if (legitMoves.isEmpty() && isMax) {
            return -100;
        } else if (legitMoves.isEmpty()) {
            return 100;
        }
        if (depth == maxDepth) {
            arraysOperations.deepCopy(legitMoves.getFirst(), bestMove);
        }


        parent_boards.add(board);
        Simulate_Player player = new Simulate_Player(turn);

        for (int[] move : legitMoves) {
            if (isMax && local_eval > parent_eval) parent_eval = local_eval;
            else if (!isMax && local_eval < parent_eval) {
                parent_eval = local_eval;
            }
            int[][] local_board = new int[3][16];
            arraysOperations.deepCopy(parent_boards.get(parent_boards.size() - 1), local_board);

            // reset eval pour nouveau min_max
            eval_Parent = save_eval;

            player.simulate_play(local_board, move);
            int captured_seeds = player.getScore();

            // TODO : ajouter / supprimer le nombre de move legit par l'adversaire pour affiner notre fonction
            if (isMax) eval_Parent += captured_seeds;
            else eval_Parent -= captured_seeds;

            int score;
            if (depth - 1 == -1) score = eval_Parent;
            else score = minMax(local_board, (turn + 1) % 2,eval_Parent, !isMax, depth - 1);

            local_eval = eval(isMax, local_eval, score, move, depth);

            if ((isMax && local_eval > parent_eval) || (!isMax && local_eval < parent_eval)) {
                parent_boards.pop();
                return parent_eval;
            }
        }

        parent_boards.pop();
        return local_eval;
    }

    private int eval(boolean isMax, int local_eval, int score, int[] move, int depth) {
        if (isMax) {
            if (score > local_eval) {
                local_eval = score;
                if (depth == maxDepth) arraysOperations.deepCopy(move, bestMove);
            }
        } else {
            if (score < local_eval) {
                local_eval = score;
            }
        }
        return local_eval;
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
