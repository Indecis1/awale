package fr.univ_cote_azur.ai_game_programming.util;

import fr.univ_cote_azur.ai_game_programming.Board;
import fr.univ_cote_azur.ai_game_programming.Color;
import fr.univ_cote_azur.ai_game_programming.IA;

import java.util.ArrayList;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class IA2 {

        private final boolean isMaxPlayer;
        private int[] oldPlayerSeeds;

        /**
         *
         * @param player the 0-based player number (0 for the first and 1 for the second)
         */
        public IA2(int player){
            isMaxPlayer = player == 0;
            oldPlayerSeeds = new int[] {0, 0};
        }

        public fr.univ_cote_azur.ai_game_programming.IA.Move play(Board board){
            oldPlayerSeeds = board.getPlayerSeeds().clone();
            ArrayList<fr.univ_cote_azur.ai_game_programming.IA.MinimaxResult> result = minimax(board, -1, null, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, isMaxPlayer);
            System.out.println(result);
            fr.univ_cote_azur.ai_game_programming.IA.MinimaxResult lastResult = result.get(result.size() - 1);
            System.out.println("Evaluation Metric: " + lastResult.score());
            if (lastResult.score() == Integer.MAX_VALUE)
                System.out.println("Victoire dans " + (result.size() - 2) + " tours");
            else if (lastResult.score() == Integer.MIN_VALUE) {
                System.out.println("Défaite dans " + (result.size() - 2) + " tours");
            }
//        else if (lastResult.score == 0) {
//            System.out.println("Null dans " + (result.size() - 2) + " tours");
//        }
            return new fr.univ_cote_azur.ai_game_programming.IA.Move(lastResult.cell(), lastResult.color());
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
            return updatePlayerSeed[player] - updatePlayerSeed[nextPlayers[player]];
            //return seedCaptured[player] - seedCaptured[nextPlayers[player]];
            //return (2 * seedCaptured[player] + vulnerableCellMetric(board, isMaxPlayer)) - (2 * seedCaptured[nextPlayers[player]] + vulnerableCellMetric(board, !isMaxPlayer));
        }

        private ArrayList<fr.univ_cote_azur.ai_game_programming.IA.MinimaxResult> minimax(Board board, int cell, Color color, int depth, int alpha, int beta, boolean maxPlayer){
            ArrayList<fr.univ_cote_azur.ai_game_programming.IA.MinimaxResult> result;
            int score;
            int gameState = -1;
            Board newBoard = board.clone();
            if (cell > -1)
                gameState = newBoard.play(cell, getPlayer(!maxPlayer), color);
            if (gameState != -1 && gameState != -2){
                result = new ArrayList<>();
                if ((gameState + 1) == getPlayer(maxPlayer) + 1)
                    result.add(new fr.univ_cote_azur.ai_game_programming.IA.MinimaxResult(Integer.MAX_VALUE, cell, color));
                else if ((gameState + 1) == (getPlayer(!maxPlayer) + 1))
                    result.add(new fr.univ_cote_azur.ai_game_programming.IA.MinimaxResult(Integer.MIN_VALUE, cell, color));
                else
                    result.add(new fr.univ_cote_azur.ai_game_programming.IA.MinimaxResult(0, cell, color));
                return result;
            }
            if (depth == 5){
                result = new ArrayList<>();
                result.add(new fr.univ_cote_azur.ai_game_programming.IA.MinimaxResult(heuristic(board, maxPlayer), cell, color));
                return result;
            }
            boolean couldPlay;
            ArrayList<fr.univ_cote_azur.ai_game_programming.IA.MinimaxResult> bestMove = new ArrayList<>();
            ArrayList<fr.univ_cote_azur.ai_game_programming.IA.MinimaxResult> bestResult = new ArrayList<>();
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
                        //if (score >= beta)
                        //    break;
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
                bestMove.add(new fr.univ_cote_azur.ai_game_programming.IA.MinimaxResult(bestScore, bestMoveScoreCell, bestMoveScoreColor));
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
                        //if (score <= alpha)
                        //    break;
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
                bestMove.add(new fr.univ_cote_azur.ai_game_programming.IA.MinimaxResult(minScore, bestMoveScoreCell, bestMoveScoreColor));
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
