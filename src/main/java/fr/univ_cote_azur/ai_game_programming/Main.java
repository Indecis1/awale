package fr.univ_cote_azur.ai_game_programming;

/**
 * The main class of the AI Game Programming application.
 * This class serves as the entry point for the program.
 */
public class Main {
    /**
     * The main method of the program.
     * This method is called when the program is executed.
     *
     * @param args The command-line arguments passed to the program.
     */
    public static void main(String[] args) {
        // Create an instance of the 'Game' class
        Game game = new Game();

        // Call the 'start' method on the 'game' object to begin the game
        game.start();
    }
}
