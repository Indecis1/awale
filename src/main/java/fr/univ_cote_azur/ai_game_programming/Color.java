package fr.univ_cote_azur.ai_game_programming;

import java.util.Random;

public enum Color {
    R, B, TR, TB, UNDEFINED;

    public static int to_index(Color color) {
        if (color.equals(R)) return 0;
        else if (color.equals(B)) return 1;
        else return 2;
    }

    public static Color to_Color(int n) {
        if (n == 0)
            return R;
        if (n == 1)
            return B;
        if (n == 2)
            return TR;
        if (n == 3)
            return TB;
        else
            return UNDEFINED;
    }

    public static Color getRandomColor() {
        Color[] colors = {R, B, TR, TB};
        return colors[new Random().nextInt(colors.length)];
    }

}
