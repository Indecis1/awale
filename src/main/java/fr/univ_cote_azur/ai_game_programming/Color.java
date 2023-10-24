package fr.univ_cote_azur.ai_game_programming;

import java.util.Random;

public enum Color {
    R, B, TR, TB, UNDEFINED;

    public static int to_int(Color color) {
        if (color == R) return 0;
        else if (color == B) return 1;
        else return 2;
    }
    public static Color getRandomColor() {
        Color[] colors = {R, B, TR, TB};
        return colors[new Random().nextInt(colors.length)];
    }
}
