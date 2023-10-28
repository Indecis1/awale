package fr.univ_cote_azur.ai_game_programming.Thread;

import fr.univ_cote_azur.ai_game_programming.Player.IA;
import fr.univ_cote_azur.ai_game_programming.Player.Simulate_Player;
import fr.univ_cote_azur.ai_game_programming.arraysOperations;

import java.util.LinkedList;

public class Task implements Runnable {
    private int numberThread;
    private final int[][] board;
    private final int turn;
    private int eval_Parent;
    boolean isMax;
    int depth;
    int[] move;

    public Task(int numberThread, int[][] board, int turn, int eval_Parent, boolean isMax, int depth, int[] move) {
        this.numberThread = numberThread;
        this.board = new int[3][16];
        arraysOperations.deepCopy(board, this.board);
        this.turn = turn;
        this.eval_Parent = eval_Parent;
        this.isMax = isMax;
        this.depth = depth;
        this.move = move;
    }

    @Override
    public void run() {
        System.out.println("Started thread " + numberThread + "....");
        final LinkedList<int[][]> parent_boards = new LinkedList<>();
        Simulate_Player player = new Simulate_Player(turn);
        player.simulate_play(board, move);
        parent_boards.addFirst(board);
        int local_evals = eval_Parent;
        boolean isMax = this.isMax;
        int depth = this.depth;
        int turn = this.turn;
        local_evals = minMax(parent_boards, (turn+1)%2, local_evals, !isMax, depth-1);
        parent_boards.removeFirst();
        eval_Parent = local_evals;
        System.out.println("Finished thread " + numberThread + "....");
    }

    public int minMax(LinkedList<int[][]> parent_boards, int turn, int eval_Parent, boolean isMax, int depth) {
        int save_eval = eval_Parent;
        int parent_eval = eval_Parent;

        int local_eval;
        if (isMax) local_eval = -100;
        else local_eval = 100;

        LinkedList<int[]> legitMoves = arraysOperations.setLegitMoves(parent_boards.get(parent_boards.size() - 1), turn);
        if (legitMoves.isEmpty() && isMax) {
            return -100;
        } else if (legitMoves.isEmpty()) {
            return 100;
        }

        for (int[] move : legitMoves) {
            if (isMax && local_eval > parent_eval) parent_eval = local_eval;
            else if (!isMax && local_eval < parent_eval) {
                parent_eval = local_eval;
            }
            int[][] local_board = new int[3][16];
            arraysOperations.deepCopy(parent_boards.get(parent_boards.size() - 1), local_board);

            // reset eval pour nouveau min_max
            eval_Parent = save_eval;

            Simulate_Player player = new Simulate_Player(turn);
            player.simulate_play(local_board, move);
            int captured_seeds = player.getScore();

            // TODO : ajouter / supprimer le nombre de move legit par l'adversaire pour affiner notre fonction
            if (isMax) eval_Parent += captured_seeds;
            else eval_Parent -= captured_seeds;

            int score;
            if (depth - 1 == -1) score = eval_Parent;
            else {
                parent_boards.addFirst(local_board);
                score = minMax(parent_boards, (turn + 1) % 2, eval_Parent, !isMax, depth - 1);
            }

            local_eval = eval(isMax, local_eval, score, move, depth);

            if ((isMax && local_eval > parent_eval) || (!isMax && local_eval < parent_eval)) {
                if (depth - 1 != -1)parent_boards.removeFirst();
                return parent_eval;
            }
        }

        if (depth - 1 == -1)parent_boards.removeFirst();
        return local_eval;
    }

    private int eval(boolean isMax, int local_eval, int score, int[] move, int depth) {
        if (isMax) {
            if (score > local_eval) {
                local_eval = score;
            }
        } else {
            if (score < local_eval) {
                local_eval = score;
            }
        }
        return local_eval;
    }

    public int getEval() {
        return eval_Parent;
    }
}