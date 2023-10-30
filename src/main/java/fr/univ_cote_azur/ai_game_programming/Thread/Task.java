package fr.univ_cote_azur.ai_game_programming.Thread;

import fr.univ_cote_azur.ai_game_programming.Player.Simulate_Player;
import fr.univ_cote_azur.ai_game_programming.arraysOperations;

import java.util.ArrayList;

public class Task implements Runnable {
    private final int[][] board;
    private final int turn;
    boolean isMax;
    int depth;
    int[] move;
    private int local_evaluation;

    public Task(int[][] board, int turn, int eval_Parent, boolean isMax, int depth, int[] move) {
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

//        Simulate_Player player = new Simulate_Player(turn);
//        player.simulate_play(board, move);
        boolean isMax = this.isMax;
        int depth = this.depth;
        int turn = this.turn;
        int local_evals = local_evaluation;
        final ArrayList<int[][]> parent_boards = new ArrayList<>();
        parent_boards.add(board);
        local_evaluation = minMax(parent_boards, (turn + 1) % 2, move, local_evals, !isMax, depth);
        parent_boards.remove(parent_boards.size() - 1);
    }

    public synchronized int minMax(ArrayList<int[][]> parent_boards, int turn, int[] parent_move, int parent_eval, boolean isMax, int depth) {

        int[][] local_board = new int[3][16];
        arraysOperations.deepCopy(parent_boards.get(parent_boards.size() - 1), local_board);
        int local_eval = parent_eval;
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

        parent_boards.add(local_board);
        ArrayList<int[]> legitMoves = arraysOperations.setLegitMoves(parent_boards.get(parent_boards.size() - 1), turn);
        if (legitMoves.isEmpty() && isMax) {
            return -100;
        } else if (legitMoves.isEmpty()) {
            return 100;
        }

        int bestEval;
        if (isMax) bestEval = -100;
        else bestEval = 100;


        for (int[] move : legitMoves) {

            int score = minMax(parent_boards, (turn + 1) % 2, move, local_eval, !isMax, depth - 1);

            if (move == legitMoves.get(0)) {
                bestEval = score;
                continue;
            }

            bestEval = eval(isMax, bestEval, score);

            if ((isMax && bestEval > parent_eval) || (!isMax && bestEval < parent_eval)) {
                break;
            }
        }
        parent_boards.remove(parent_boards.size() - 1);
        return bestEval;
    }

    private synchronized int eval(boolean isMax, int local_parent_eval, int score) {
        if ((isMax && score > local_parent_eval) || (!isMax && score < local_parent_eval)) return score;
        return local_parent_eval;
    }

    public int getEval() {
        return local_evaluation;
    }
}