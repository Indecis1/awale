package fr.univ_cote_azur.ai_game_programming;

/**
 * Entry of the game. The game is started in this class.
 */
public class Main {


    /**
     * Main class of the whole application. It initiates a {@link Game} object in order to start a game.
     *
     * @param args not useful in this application. It's here by convention.
     */
    public static void main(String[] args) {
        Game game = new Game();
        game.start_game();
    }


}