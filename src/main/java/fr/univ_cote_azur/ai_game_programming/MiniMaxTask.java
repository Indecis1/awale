package fr.univ_cote_azur.ai_game_programming;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class MiniMaxTask implements Callable<IA.MinimaxResult> {

    private final Board board;
    private final boolean isMaxPlayer;
    private final int cell;
    private final Color color;
    private final int maxDepth;
    private final int alpha;
    private final int beta;
    private final int[] oldPlayerSeeds;


    public MiniMaxTask(Board board, boolean isMaxPlayer, int cell, Color color, int maxDepth, int alpha, int beta) {
        this.board = board;
        this.isMaxPlayer = isMaxPlayer;
        this.cell = cell;
        this.color = color;
        this.maxDepth = maxDepth;
        this.alpha = alpha;
        this.beta = beta;
        oldPlayerSeeds = board.getPlayerSeeds().clone();
    }

    @Override
    public IA.MinimaxResult call() {
        ArrayList<IA.MinimaxResult> result = minimax(board, cell, color, 0, maxDepth, alpha, beta, isMaxPlayer);
        //        return new IA.Move(lastResult.cell, lastResult.color);
        return result.get(result.size() - 1);
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
        int[] updatePlayerSeed = board.getPlayerSeeds();
        int[] seedCaptured = {updatePlayerSeed[0] - oldPlayerSeeds[0], updatePlayerSeed[1] - oldPlayerSeeds[1]};
        //return seedCaptured[0] - seedCaptured[1];
        return updatePlayerSeed[0] - updatePlayerSeed[1];
        //return updatePlayerSeed[player] - updatePlayerSeed[nextPlayers[player]];
        //return (2 * seedCaptured[0] + vulnerableCellMetric(board, true)) - (2 * seedCaptured[1] + vulnerableCellMetric(board, false));
    }

    private ArrayList<IA.MinimaxResult> minimax(Board board, int cell, Color color, int depth, int maxDepth, int alpha, int beta, boolean maxPlayer){
        ArrayList<IA.MinimaxResult> result;
        int score;
        int gameState = -1;
        Board newBoard = board.clone();
        if (cell > -1)
            gameState = newBoard.play(cell, getPlayer(!maxPlayer), color);
        if (gameState != -1 && gameState != -2){
            result = new ArrayList<>();
            if ((gameState + 1) == 1){
                result.add(new IA.MinimaxResult(Integer.MAX_VALUE, cell, color));
            }
            else if ((gameState + 1) == 2){
                result.add(new IA.MinimaxResult(-Integer.MAX_VALUE, cell, color));
            }
            else
            {
                result.add(new IA.MinimaxResult(0, cell, color));
            }

            return result;
        }
        if (depth == maxDepth){
            result = new ArrayList<>();
            result.add(new IA.MinimaxResult(heuristic(board, maxPlayer), cell, color));
            return result;
        }
        boolean couldPlay;
        ArrayList<IA.MinimaxResult> bestMove = new ArrayList<>();
        ArrayList<IA.MinimaxResult> bestResult = new ArrayList<>();
        Color bestMoveScoreColor = null;
        int bestMoveScoreCell = 0;
        // maxPlayer is player 1
        if (maxPlayer){
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
                    result = minimax(newBoard, i, c,depth + 1, maxDepth, alpha, beta, false);
                    score = result.get(result.size() - 1).score();
                    alpha = max(alpha, score);
                    if (alpha >= beta){
                        bestMove.addAll(result);
                        bestMove.add(new IA.MinimaxResult(alpha, cell, color));
                        return bestMove;
                    }
                    if (score > bestScore){
                        bestScore = score;
                        bestMoveScoreColor = c;
                        bestMoveScoreCell = i;
                        bestResult = result;
                    }
                }
            }
            bestMove.addAll(bestResult);
            bestMove.add(new IA.MinimaxResult(bestScore, bestMoveScoreCell, bestMoveScoreColor));
        }
        else{
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
                    result = minimax(newBoard, i, c,depth + 1, maxDepth, alpha, beta, true);
                    score = result.get(result.size() - 1).score();
                    beta = min(beta, score);
                    if (alpha >= beta){
                        bestMove.addAll(result);
                        bestMove.add(new IA.MinimaxResult(beta, cell, color));
                        return bestMove;
                    }
                    if (score < minScore){
                        minScore = score;
                        bestMoveScoreColor = c;
                        bestMoveScoreCell = i;
                        bestResult = result;
                    }
                }
            }
            bestMove.addAll(bestResult);
            bestMove.add(new IA.MinimaxResult(minScore, bestMoveScoreCell, bestMoveScoreColor));
        }
        return bestMove;
    }

    private int getPlayer(boolean maxPlayer){
        if (maxPlayer)
            return 0;
        else
            return 1;
    }
}
