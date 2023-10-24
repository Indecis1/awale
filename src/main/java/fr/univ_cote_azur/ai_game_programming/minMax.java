package fr.univ_cote_azur.ai_game_programming;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class is the heart of our AI. This is our algorithm to determine the best move to play at the x-moment of
 * the game.
 */
public class minMax {

    private static Stack<Holes> original_holes;
    private static int alpha = -100;
    private static int beta = 100;

    /**
     * The recursive of our min-max algorithm starts here.
     *
     * @param player the player for whom the best next move is computed.
     * @return the best {@link Move} to play for {@param player} at x-moment of the game.
     */
    public static Move decision(Player player) {
        long start = System.nanoTime();
        boolean isMax = true;
        original_holes = new Stack<>();
        Move bestMove = new Move(0, null);
        int maxDepth = maxDepth(player.numberLegitMoves());
        System.out.println("Legit moves :" + player.numberLegitMoves() + ". Depth :" + maxDepth + "." );
        bestMove = decisionMinMax(player, bestMove, isMax, maxDepth);
        System.out.println("***Duree :" + ((System.nanoTime() - start) / Math.pow(10, 9)) + "s. Evaluation :" + bestMove.getScoreEvaluation() + ". Legit moves :" + player.numberLegitMoves() + "***" );
        return bestMove;
    }

    private static int maxDepth(int sizeLegitMove){
        if (sizeLegitMove > 24)
            return 3;
        else if (sizeLegitMove > 10)
            return 4;
        else
            return 5;
    }

    /**
     * This is our modified min-max algorithm.
     * Each object of the Movement class has a Move.scoreEvaluation attribute. This attribute calculates the
     * potential gain or loss of score.
     * In simple terms: if the movement brings x points and we are in "MAX" mode, then we add x to our score evaluation associated with this movement.
     * If the movement brings x points and we are in "MIN" mode, then we subtract x from our score evaluation associated with this movement.
     * <p>
     * Then, similar to a classic min-max algorithm, we look at all the child movements of the parent movement and select the one
     * with the maximum (or minimum) evaluation if isMax is true (or false).
     * <p>
     * In the end, we take the maximum estimated value. That means we have a higer change to score seeds if we take this path.
     *
     * @param player     The {{@link Player}} who will play the next movement.
     * @param parentMove In the min-max tree, each node represents a {@link Move}. This is the parent node from where
     *                   we explore the children (other legits moov from this one). Its estimated value is equal to zero.
     * @param isMax      A {@link Boolean} parameter to determine if we consider the maximum or minimum evaluated values in our algorithm.
     * @param maxDepth   An {@link Integer} to determine the maximum depth until which our algorithm computes the important values.
     * @return The min-max estimated value as an {{@link Integer}} among the values of the child movements.
     */
    private static Move decisionMinMax(Player player, Move parentMove, boolean isMax, int maxDepth) {

        original_holes.push(new Holes(player.getHoles().clone()));

        // Get all the legit move
        ArrayList<Move> legitMove = new ArrayList<>();
        createListeOfLegitMove(player, legitMove, original_holes.peek().holes);
        if (legitMove.isEmpty()) return parentMove;

        Move bestMove = legitMove.get(0);

        final int initial_score = parentMove.getScoreEvaluation();

        for (Move move : legitMove) {

            //if (alpha_beta(isMax, parentMove.getScoreEvaluation(), bestMove.getScoreEvaluation())) break;

            Hole[] depth_holes = original_holes.peek().holes;

            // Resetting the holes and the score.
            player.setHoles(depth_holes.clone());
            player.resetScore(initial_score);
            move.setScoreEvaluation(initial_score);


            // Simulate the legit move and adjust the estimated score after the move
            player.simulate_NextPlay(move);
            int after_score = player.getScore();

            adjust_evaluationScore(move, after_score - initial_score, isMax);


            if (maxDepth - 1 >= 0){
                // Create the opponent player
                Player opponent = setOpponent(player);
                opponent.setHoles(player.getHoles());
                Move bestChild = decisionMinMax(opponent, move, !isMax, maxDepth - 1);
                move.setScoreEvaluation(bestChild.getScoreEvaluation());
            }

            if (move == legitMove.get(0)) {
                bestMove = move;
                parentMove.setScoreEvaluation(bestMove.getScoreEvaluation());
            } else if (isMinMax(move.getScoreEvaluation(), parentMove.getScoreEvaluation(), isMax)) {
                bestMove = move;
                parentMove.setScoreEvaluation(bestMove.getScoreEvaluation());
            }





        }

        original_holes.pop();
        player.resetScore(initial_score);


        // Si par hasard, il nous retourne un mouvement interdit. On recupere un moov au hasard.
        if(!legitMove.contains(bestMove)){
            System.out.print("***au hasard...***");
            Collections.shuffle(legitMove);

            // Récupérez le premier élément (élément aléatoire)
            bestMove = legitMove.get(0);
        }

        return bestMove;
    }

    /**
     * Alpha beta algorithm. Explication :
     * isMax == true (resp. false) for the parent :
     * When a node is evaluated at x and his parent is evaluated at y, if x < y (resp. x > y) then there is no need to compute the score
     * of the other move because we will only choose moves with a smaller (resp. greater) score (isMax == false (resp. true) for the child)...
     * and this one is smaller (resp. greater) than the parent's evaluated score.
     * Therefore, we can "cut" this part of the tree and save time and ressources.
     *
     * @param isMax       boolean value to determine in which state of min-max we are.
     * @param scoreParent parent's move's evaluated score
     * @param scoreMove   actual move's evaluated score
     * @return true if we can do an alpha beta cut; false otherwise.
     */
    private static boolean alpha_beta(boolean isMax, int evaluation) {

        return true;
    }

    private static boolean isMinMax(int scoreMove, int scoreParent, boolean isMax) {
        if (isMax) return scoreMove > scoreParent;
        else return scoreMove < scoreParent;
    }

    private static void adjust_evaluationScore(Move move, int score, boolean isMax) {
        if (isMax) move.addEvaluationScore(score);
        else move.minusEvaluationScore(score);
    }

    private static Player setOpponent(Player p) {
        Player opponent;
        if (p instanceof PlayerOne) opponent = new PlayerTwo(16);
        else opponent = new PlayerOne(16);
        return opponent;
    }

    private static void createListeOfLegitMove(Player p, ArrayList<Move> legitMove, Hole[] holes) {
        if (p instanceof PlayerOne) legitPlays(0, legitMove, holes);
        else legitPlays(1, legitMove, holes);
    }

    private static void legitPlays(int startIndex, ArrayList<Move> legitMove, Hole[] holes) {
        for (int i = startIndex; i < holes.length; i += 2) {
            if (holes[i].hasColorSeeds(Color.R)) legitMove.add(new Move(i + 1, Color.R));
            if (holes[i].hasColorSeeds(Color.B)) legitMove.add(new Move(i + 1, Color.B));
            if (holes[i].hasColorSeeds(Color.TB)) {
                legitMove.add(new Move(i + 1, Color.TR));
                legitMove.add(new Move(i + 1, Color.TB));
            }
        }
    }

    private record Holes(Hole[] holes) {

    }

}
