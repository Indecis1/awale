package fr.univ_cote_azur.ai_game_programming;

import java.util.ArrayList;
import java.util.Stack;

/**
 * This class is the heart of our AI. This is our algorithm to determine the best move to play at the x-moment of
 * the game.
 */
public class minMax {

    private static Stack<Holes> original_holes;

    /**
     * The recursive of our min-max algorithm starts here.
     * @param player the player for whom the best next move is computed.
     * @return the best {@link Move} to play for {@param player} at x-moment of the game.
     */
    public static Move decision(Player player) {
        boolean isMax = true;
        int maxDepth = 2;
        original_holes = new Stack<>();
        Move bestMove = new Move(0, null);
        return decisionMinMax(player, bestMove, isMax, maxDepth);
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


        final int initial_score = parentMove.getScoreEvaluation();

        for (Move move : legitMove) {

            Hole[] depth_holes = original_holes.peek().holes;

            // Resetting the holes and the score.
            player.setHoles(depth_holes.clone());
            player.resetScore(initial_score);


            // Simulate the legit move and adjust the estimated score after the move
            player.simulate_NextPlay(move);
            int after_score = player.getScore();

            adjust_evaluationScore(move, after_score - initial_score, isMax);


            // Create the opponent player
            Player opponent = setOpponent(player);
            opponent.setHoles(player.getHoles());

            if (maxDepth - 1 > 0)
                move.setScoreEvaluation(decisionMinMax(opponent, move, !isMax, maxDepth - 1).getScoreEvaluation());

        }

        original_holes.pop();
        player.resetScore(initial_score);
        if (legitMove.isEmpty()) return parentMove;
        return selectMinMax(legitMove, isMax);
    }

    private static void adjust_evaluationScore(Move move, int score, boolean isMax) {
        if (isMax) move.addEvaluationScore(score);
        else move.minusEvaluationScore(score);
    }

    private static Move selectMinMax(ArrayList<Move> legitMove, boolean isMax) {
        if (isMax) {
            return findMax(legitMove);
        } else {
            return findMin(legitMove);
        }
    }

    private static Move findMax(ArrayList<Move> legitMove) {
        Move max = legitMove.get(0);

        for (int i = 1; i < legitMove.size(); i++) {
            Move currentMove = legitMove.get(i);

            if (currentMove.getScoreEvaluation() > max.getScoreEvaluation()) {
                max = currentMove;
            }
        }

        return max;
    }

    private static Move findMin(ArrayList<Move> legitMove) {
        Move min = legitMove.get(0);

        for (int i = 1; i < legitMove.size(); i++) {
            Move currentMove = legitMove.get(i);

            if (currentMove.getScoreEvaluation() < min.getScoreEvaluation()) {
                min = currentMove;
            }
        }

        return min;
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
