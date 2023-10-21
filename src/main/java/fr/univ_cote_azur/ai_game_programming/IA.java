package fr.univ_cote_azur.ai_game_programming;

import fr.univ_cote_azur.ai_game_programming.Board;
import fr.univ_cote_azur.ai_game_programming.Color;

import java.util.ArrayList;

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
        ArrayList<MinimaxResult> result = minimax(board, -1, null, 0, isMaxPlayer);
        MinimaxResult lastResult =  result.get(result.size() - 1);
        return new Move(lastResult.cell, lastResult.color);
    }

    private ArrayList<MinimaxResult> minimax(Board board, int cell, Color color, int depth, boolean maxPlayer){
        ArrayList<MinimaxResult> result;
        if (depth == 5){
            result = new ArrayList<>();
            result.add(new MinimaxResult(board.getPlayerSeeds()[0], -1, null));
            return result;
        }
        boolean couldPlay;

        Board newBoard = board.clone();
        ArrayList<MinimaxResult> bestMove = new ArrayList<>();
        ArrayList<MinimaxResult> bestResult = new ArrayList<>();
        Color bestMoveScoreColor = null;
        int bestMoveScoreCell = 0;
        int score = -1;
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
                    result = minimax(newBoard, i, c,depth + 1, false);
                    score = result.get(0).score();
                    if (result.get(0).score() > bestScore){
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
                    result = minimax(newBoard, i, c,depth + 1, true);
                    score = result.get(0).score();
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

    public record MinimaxResult(int score, int cell, Color color) {}

    public record Move(int cell, Color color){}
}
