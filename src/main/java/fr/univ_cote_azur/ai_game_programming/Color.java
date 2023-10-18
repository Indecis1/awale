package fr.univ_cote_azur.ai_game_programming;

import java.util.ArrayList;
import java.util.List;

/**
 * An enum class which represents the different colors possible for a seed: R, B, TR, TB, and UNDEFINED if
 * there is an error during the data entry.
 */
public enum Color {
    R, B, TR, TB, UNDEFINED;

    /**
     * Class used by the {@link Player} to get the values of the different possible colors.
     * <p>
     * Note: This method will be useless after the determination of the minMax algorithm.
     *
     * @return a {@link Color} array of the allowed colors for a seed
     */
    public static Color[] getValues() {
        List<Color> colorList = new ArrayList<>();

        for (Color color : Color.values()) {
            if (color != Color.UNDEFINED) {
                colorList.add(color);
            }
        }

        return colorList.toArray(new Color[0]);
    }
}