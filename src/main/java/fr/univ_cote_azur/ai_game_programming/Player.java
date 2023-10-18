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
     * @param id_firstHole the ID of the first hole to play from
     * @param seedColor    the color of the seeds to play
     */
    public void nextPlay(int id_firstHole, Color seedColor);

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

}
