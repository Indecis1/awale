package fr.univ_cote_azur.ai_game_programming.Player;

import fr.univ_cote_azur.ai_game_programming.BoardOperations;
import fr.univ_cote_azur.ai_game_programming.Color;

import java.util.ArrayList;
import java.util.Stack;

public class IA extends Player {

    private final int turn;
    private final int maxDepth;
    private int score;
    private final Stack<int[][]> parent_boards;
    private Move bestMove;

    public IA(int turn) {
        this.turn = turn;
        this.score = 0;
        this.parent_boards = new Stack<>();
        this.bestMove = new Move(0, null);
        this.maxDepth = 1;
    }

    @Override
    public void play(int[][] board) {
        int eval = minMax(board, 0, turn, true, maxDepth);
        int index_first_hole = bestMove.indexPlay;
        Color color = bestMove.color;

        System.out.println("AI play is : **" + (index_first_hole + 1) + color + "**. Evaluation for a " + maxDepth + " depth is :" + eval);

        int last_index = sowing(board, index_first_hole, color);
        int seed_captured = capturing(board, last_index);
        add_to_score(seed_captured);

        if (otherPlayerIsStarving(board)) {
            seed_captured = BoardOperations.count_seeds(board);
            add_to_score(seed_captured);
            BoardOperations.emptyBoard(board);
        }
    }

    // TODO : Implementer algorithm Min-Max
    private int minMax(int[][] board, int eval, int turn, boolean isMax, int depth) {
        int local_eval = eval;
        parent_boards.push(board);


        Simulate_Player player = new Simulate_Player(turn);

        if (depth == -1) {
            parent_boards.pop();
            return local_eval;
        }

        ArrayList<Move> legitMoves = BoardOperations.setLegitMoves(board, turn);
        if (legitMoves.isEmpty()) {
            return local_eval;
        }
        if(depth == maxDepth)
            bestMove = new Move(legitMoves.get(0).indexPlay, legitMoves.get(0).color);

        for (Move move :
                legitMoves) {
            int[][] local_board = new int[3][16];
            BoardOperations.deepCopy(parent_boards.peek(), local_board);
//            System.out.print("Depth : " + depth + ". Move : " + (move.indexPlay+1) + move.color + ". -- ");
//            for (int j = 0; j < 16; j++) {
//                System.out.print((j + 1) + "(" + board[0][j] + "R," + board[1][j] + "B," + board[2][j] + "T)   ");
//            }
//            System.out.println();

            player.simulate_play(local_board, move);
            int captured_seeds = player.getScore();

            if (isMax)
                local_eval += captured_seeds;
            else
                local_eval -= captured_seeds;

            int score = minMax(local_board, local_eval, (turn + 1) % 2, !isMax, depth - 1);
            if (local_eval != setNewEval(eval, score, isMax)) {
                bestMove = new Move(move.indexPlay, move.color);
                local_eval = setNewEval(eval, score, isMax);
            }

            //TODO : implement alpha beta here.

        }
        parent_boards.pop();
        return local_eval;
    }

    private int setNewEval(int eval, int score, boolean isMax) {
        if (isMax)
            return Math.max(eval, score);
        return Math.min(eval, score);
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    int sowing(int[][] board, int index_first_hole, Color color) {
        int seeds = BoardOperations.get_seedColor(board, index_first_hole, color);
        BoardOperations.emptySeedColor_at_index(board, index_first_hole, color);
        if (color == Color.R || color == Color.TR) return sowingRed(board, index_first_hole, color, seeds);
        else return sowingBlue(board, index_first_hole, color, seeds);
    }

    @Override
    public boolean otherPlayerIsStarving(int[][] board) {
        int start;
        if (turn == 0) start = 1;
        else start = 0;
        for (int i = start; i < 16; i += 2) {
            if (!BoardOperations.has_seed_of_Color(board, i, Color.R) && !BoardOperations.has_seed_of_Color(board, i, Color.B) && BoardOperations.has_seed_of_Color(board, i, Color.TR))
                return true;
        }
        return false;
    }

    @Override
    public void printScore() {
        System.out.println("IA score :" + getScore());
    }

    private void add_to_score(int seedCaptured) {
        score += seedCaptured;
    }

    public record Move(int indexPlay, Color color) {
    }

}
