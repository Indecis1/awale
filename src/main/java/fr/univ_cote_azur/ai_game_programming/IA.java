package fr.univ_cote_azur.ai_game_programming;


import java.util.ArrayList;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class IA {

    private final boolean isMaxPlayer;

    /**
     *
     * @param player the 0-based player number (0 for the first and 1 for the second)
     */
    public IA(int player){
        isMaxPlayer = player == 0;
    }

    public Move play(Board board){
        ArrayList<MinimaxResult> result = minimax(board, -1, null, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, isMaxPlayer);
        MinimaxResult lastResult =  result.get(result.size() - 1);
        return new Move(lastResult.cell, lastResult.color);
    }

    private int vulnerableCellMetric(Board board, boolean isMaxPlayer){
        int vulnerableCell = 0;
        int[] redSeedBoard = board.getRedSeedBoard(); ;
        int[] blueSeedBoard = board.getBlueSeedBoard();
        int[] transparentSeedBoard = board.getTransparentSeedBoard() ;
        for (int i = getPlayer(isMaxPlayer); i<16; i=i+2){
            if(redSeedBoard[i] == 1 || redSeedBoard[i] == 2 || blueSeedBoard[i] == 1 || blueSeedBoard[i] == 2 || transparentSeedBoard[i] == 1 || transparentSeedBoard[i] == 2)
                vulnerableCell += 1;
        }
        return vulnerableCell;
    }

    private int heuristic(Board board, boolean isMaxPlayer){
        int[] nextPlayers = {1, 0};
        int player = getPlayer(isMaxPlayer);
        return (2 * board.getPlayerSeeds()[player] + vulnerableCellMetric(board, isMaxPlayer)) - (2 * board.getPlayerSeeds()[nextPlayers[player]] + vulnerableCellMetric(board, !isMaxPlayer));
    }

    private ArrayList<MinimaxResult> minimax(Board board, int cell, Color color, int depth, int alpha, int beta, boolean maxPlayer){
        ArrayList<MinimaxResult> result;
        int score;
        if (depth == 7){
            result = new ArrayList<>();
            result.add(new MinimaxResult(heuristic(board, maxPlayer), -1, null));
            return result;
        }
        boolean couldPlay;

        Board newBoard = board.clone();
        ArrayList<MinimaxResult> bestMove = new ArrayList<>();
        ArrayList<MinimaxResult> bestResult = new ArrayList<>();
        Color bestMoveScoreColor = null;
        int bestMoveScoreCell = 0;
        // maxPlayer is player 1
        if (maxPlayer){
            if (cell > -1)
                newBoard.play(cell, 0, color);
            int bestScore = - Integer.MAX_VALUE;
            for(int i = 0; i < 16; i+=2){
                for(Color c: Color.values()){
                    switch (c) {
                        case RED -> couldPlay = newBoard.getRedSeedBoard()[i] > 0;
                        case BLUE -> couldPlay = newBoard.getBlueSeedBoard()[i] > 0;
                        default -> couldPlay = newBoard.getTransparentSeedBoard()[i] > 0;
                    }
                    if (!couldPlay)
                        continue;
                    result = minimax(newBoard, i, c,depth + 1, alpha, beta, false);
                    score = result.get(result.size() - 1).score();
                    if (score >= beta)
                        break;
                    alpha = max(alpha, score);
                    if (result.get(result.size() - 1).score() > bestScore){
                        bestScore = score;
                        bestMoveScoreColor = c;
                        bestMoveScoreCell = i;
                        bestResult = result;
                    }
                }
            }
            bestMove.addAll(bestResult);
            bestMove.add(new MinimaxResult(bestScore, bestMoveScoreCell, bestMoveScoreColor));
        }
        else{
            if (cell > -1)
                newBoard.play(cell, 1, color);
            int minScore = Integer.MAX_VALUE;
            for(int i = 1; i < 16; i+=2){
                for(Color c: Color.values()){
                    switch (c) {
                        case RED -> couldPlay = newBoard.getRedSeedBoard()[i] > 0;
                        case BLUE -> couldPlay = newBoard.getBlueSeedBoard()[i] > 0;
                        default -> couldPlay = newBoard.getTransparentSeedBoard()[i] > 0;
                    }
                    if (!couldPlay)
                        continue;
                    result = minimax(newBoard, i, c,depth + 1, alpha, beta, true);
                    score = result.get(result.size() - 1).score();
                    if (score <= alpha)
                        break;
                    beta = min(beta, score);
                    if (score < minScore){
                        minScore = score;
                        bestMoveScoreColor = c;
                        bestMoveScoreCell = i;
                        bestResult = result;
                    }
                }
            }
            bestMove.addAll(bestResult);
            bestMove.add(new MinimaxResult(minScore, bestMoveScoreCell, bestMoveScoreColor));
        }
        return bestMove;
    }

    public int getPlayer(boolean maxPlayer){
        if (maxPlayer)
            return 0;
        else
            return 1;
    }

    public record MinimaxResult(int score, int cell, Color color) {}

    public record Move(int cell, Color color){}
}
