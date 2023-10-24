package fr.univ_cote_azur.ai_game_programming;

/**
 * An interface to create the differents players. We find here the main methods used by the game to run.
 */
public interface Player {

    /**
     * Retrieves the score of the player.
     *
     * @return the player's score
     */
    public int getScore();

    /**
     * Performs the next play for the player.
     * If the color is null, we set a random possible play. Useful to continue the game if our AI couldn't find a solution...
     * Then, we verify that the move is possible is a possible color.
     * After that, we can sow and capture.
     * Note: when we sow, there's a second check to see if the play is legit.
     *
     * @param move The {@link Move} which is going to be played by {@code this}.
     * @throws IllegalArgumentException if the param is not a valid play.
     */
    public void nextPlay(Move move);

    /**
     * Simulate the next play for the player.
     *
     * @param move {@link Move} choosed by the player.
     */
    public void simulate_NextPlay(Move move);

    /**
     * Retrieves the holes of the player after the change he has applied.
     *
     * @return an array of holes
     */
    public Hole[] getHoles();

    /**
     * Sets the state holes as they are after the play of the opponents..
     *
     * @param holes the array of holes to set
     */
    public void setHoles(Hole[] holes);

    /**
     * Determines whenever the opponent is starving or not
     *
     * @return true if the oppoenent in starving, otherwise returns false.
     */
    public boolean opponentIsStarving();

    /**
     * Method used to reset the score to it's origin value after an iteration of the min-max algorithm
     *
     * @param resetScore the value of the score {@link Integer}
     */
    public void resetScore(int resetScore);

    /**
     * Compute the number of seeds on the board.
     *
     * @return the number of seeds on the board.
     */
    public int sumSeeds();

    /**
     * Compute the number of legit moves.
     *
     * @return the number of legit moves.
     */
    public int numberLegitMoves();

}
