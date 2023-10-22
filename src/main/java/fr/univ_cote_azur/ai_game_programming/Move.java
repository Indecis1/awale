package fr.univ_cote_azur.ai_game_programming;

/**
 * A class which represents a move. It has three private attributes : {@link Hole}[] idHole, {@link Color} color,
 * {@link Integer} scoreEvaluation.
 * This class was created only to represent a move in the {@link minMax} algorithm. There was a problem in the memory
 * references during the recursion, we partially solved the probleme with this class and some deep copy.
 */
public class Move {

    private int idHole;
    private Color color;
    private int scoreEvaluation;

    private Hole[] holes;

    /**
     * First constructor of the class. This constructor is mainly used in {@link Game} to create a new Move when necessary.
     *
     * @param idHole set the private attribute {@code this.idHole} to this {@link Integer} value.
     * @param color  set the private attribute {@code this.color} to this {{@link Color}} color.
     */
    public Move(int idHole, Color color) {
        this.idHole = idHole;
        this.color = color;
        scoreEvaluation = 0;
    }

    /**
     * Second constructor of the class. This constructor is mainly used in {@link minMax} to create a new move with
     * predefined holes in order to simulate the plays on this holes, and not the originals.
     *
     * @param idHole set the private attribute {@code this.idHole} to this {@link Integer} value.
     * @param color  set the private attribute {@code this.color} to this {{@link Color}} color.
     * @param holes  set the private attribute {@code this.holes} to this {{@link Hole}}[] array.
     */
    public Move(int idHole, Color color, Hole[] holes) {
        this.idHole = idHole;
        this.color = color;
        this.holes = holes;
        scoreEvaluation = 0;
    }

    /**
     * Return the id of the hole from where the move start.
     *
     * @return the id of the first hole.
     */
    public int getIdHole() {
        return idHole;
    }

    /**
     * Return the color of the hole from where the move start.
     *
     * @return the {@link Color} of the first hole.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Return the evaluated score (during the min-max algorithm) after this move.
     *
     * @return the evaluated score ({@link Integer}).
     */
    public int getScoreEvaluation() {
        return scoreEvaluation;
    }

    /**
     * Set the evaluated score the its new value.
     *
     * @param scoreEvaluation new value of the evaluated score.
     */
    public void setScoreEvaluation(int scoreEvaluation) {
        this.scoreEvaluation = scoreEvaluation;
    }

    /**
     * Modify the evaluation score by adding it a new value. Used to set the new  evaluation score at n-depth based on the
     * value of the previous move's score in the min-max algorithm.
     * Note : This methode is called if we are in a depth where {@code isMax} is true.
     *
     * @param valueMove the evaluation of the previous move ({@link Integer}).
     */
    public void addEvaluationScore(int valueMove) {
        this.scoreEvaluation += valueMove;
    }

    /**
     * Modify the evaluation score by subtracting it a new value. Used to set the new  evaluation score at n-depth based on the
     * value of the previous move's score in the min-max algorithm.
     * Note : This methode is called if we are in a depth where {@code isMax} is false.
     *
     * @param valueMove the evaluation of the previous move ({@link Integer}).
     */
    public void minusEvaluationScore(int valueMove) {
        this.scoreEvaluation -= valueMove;
    }
}
