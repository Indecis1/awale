package fr.univ_cote_azur.ai_game_programming;

import java.util.Random;

/**
 * The `Color` enum represents different colors used in the AI Game Programming application.
 * It includes enum constants for Red, Blue, Transparent Red, Transparent Blue, and Undefined colors.
 */
public enum Color {
    R,        // Red
    B,        // Blue
    TR,       // Transparent Red
    TB,       // Transparent Blue
    UNDEFINED;  // Undefined or unknown color

    /**
     * Converts a Color enum value to an integer index.
     *
     * @param color The Color enum value to convert.
     * @return An integer index corresponding to the Color enum value.
     */
    public static int to_index(Color color) {
        if (color == R) return 0;
        else if (color == B) return 1;
        else return 2;
    }

    /**
     * Converts an integer index to a Color enum value.
     *
     * @param n The integer index to convert.
     * @return The Color enum value corresponding to the integer index.
     */
    public static Color to_Color(int n) {
        if (n == 0) return R;
        if (n == 1) return B;
        if (n == 2) return TR;
        if (n == 3) return TB;
        else return UNDEFINED;
    }

    /**
     * Generates and returns a random Color enum value.
     *
     * @return A randomly selected Color enum value from Red, Blue, Transparent Red, or Transparent Blue.
     */
    public static Color getRandomColor() {
        Color[] colors = {R, B, TR, TB};

        // Generate a random index and return the corresponding color
        return colors[new Random().nextInt(colors.length)];
    }
}