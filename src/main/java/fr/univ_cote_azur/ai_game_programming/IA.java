package fr.univ_cote_azur.ai_game_programming;


import java.util.ArrayList;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class IA {

    private final boolean isMaxPlayer;
    private int[] oldPlayerSeeds;

    /**
     *
     * @param player the 0-based player number (0 for the first and 1 for the second)
     */
    public IA(int player){
        isMaxPlayer = player == 0;
        oldPlayerSeeds = new int[] {0, 0};
    }

    public Move play(Board board){
        oldPlayerSeeds = board.getPlayerSeeds().clone();
        ArrayList<MinimaxResult> result = minimax(board, -1, null, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, isMaxPlayer);
        System.out.println(result);
        MinimaxResult lastResult = result.get(result.size() - 1);
        System.out.println("Evaluation Metric: " + lastResult.score);
        if (lastResult.score == Integer.MAX_VALUE)
            System.out.println("Victoire dans " + (result.size() - 2) + " tours");
        else if (lastResult.score == -Integer.MAX_VALUE) {
            System.out.println("Défaite dans " + (result.size() - 2) + " tours");
        }
//        else if (lastResult.score == 0) {
//            System.out.println("Nul dans " + (result.size() - 2) + " tours");
//        }
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
        int[] updatePlayerSeed = board.getPlayerSeeds();
        int[] seedCaptured = {updatePlayerSeed[0] - oldPlayerSeeds[0], updatePlayerSeed[1] - oldPlayerSeeds[1]};
        //return seedCaptured[0] - seedCaptured[1];
        return updatePlayerSeed[0] - updatePlayerSeed[1];
        //return updatePlayerSeed[player] - updatePlayerSeed[nextPlayers[player]];
        //return (2 * seedCaptured[0] + vulnerableCellMetric(board, true)) - (2 * seedCaptured[1] + vulnerableCellMetric(board, false));
    }

    private ArrayList<MinimaxResult> minimax(Board board, int cell, Color color, int depth, int alpha, int beta, boolean maxPlayer){
        ArrayList<MinimaxResult> result;
        int score;
        int gameState = -1;
        Board newBoard = board.clone();
        if (cell > -1)
            gameState = newBoard.play(cell, getPlayer(!maxPlayer), color);
        if (gameState != -1 && gameState != -2){
            result = new ArrayList<>();
            if ((gameState + 1) == 1){
                result.add(new MinimaxResult(Integer.MAX_VALUE, cell, color));
            }
            else if ((gameState + 1) == 2){
                result.add(new MinimaxResult(-Integer.MAX_VALUE, cell, color));
            }
            else
            {
                result.add(new MinimaxResult(0, cell, color));
            }

            return result;
        }
        if (depth == 7){
            result = new ArrayList<>();
            result.add(new MinimaxResult(heuristic(board, maxPlayer), cell, color));
            return result;
        }
        boolean couldPlay;
        ArrayList<MinimaxResult> bestMove = new ArrayList<>();
        ArrayList<MinimaxResult> bestResult = new ArrayList<>();
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
                    result = minimax(newBoard, i, c,depth + 1, alpha, beta, false);
                    score = result.get(result.size() - 1).score();
//                    if (alpha >= beta){
//                        result = new ArrayList<>();
//                        result.add(new MinimaxResult(alpha, cell, color));
//                    }
//                    if (score >= beta)
//                        break;
                    alpha = max(alpha, score);
                    if (alpha >= beta){
                        bestMove.addAll(result);
                        bestMove.add(new MinimaxResult(alpha, cell, color));
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
            bestMove.add(new MinimaxResult(bestScore, bestMoveScoreCell, bestMoveScoreColor));
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
                    result = minimax(newBoard, i, c,depth + 1, alpha, beta, true);
                    score = result.get(result.size() - 1).score();
//                    if (alpha >= beta){
//                        result = new ArrayList<>();
//                        result.add(new MinimaxResult(alpha, cell, color));
//                    }

//                    if (alpha >= score)
//                        break;
                    beta = min(beta, score);
                    if (alpha >= beta){
                        bestMove.addAll(result);
                        bestMove.add(new MinimaxResult(beta, cell, color));
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
            bestMove.add(new MinimaxResult(minScore, bestMoveScoreCell, bestMoveScoreColor));
        }
        return bestMove;
    }

    private int getPlayer(boolean maxPlayer){
        if (maxPlayer)
            return 0;
        else
            return 1;
    }

    public record MinimaxResult(int score, int cell, Color color) {
        @Override
        public String toString() {
            return "MinimaxResult{score = " + score + " , cell = " + cell + ", color = "+ color.toString()+"}";
        }
    }

    public record Move(int cell, Color color){}
}