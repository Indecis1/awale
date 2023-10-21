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
     *
     * @param move {@link Move} choosed by the player.
     */
    public void nextPlay(Move move);

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

}
