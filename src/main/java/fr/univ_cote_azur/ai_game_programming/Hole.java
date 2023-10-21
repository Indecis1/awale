package fr.univ_cote_azur.ai_game_programming;

/**
 * Class to represent the different holes.
 */
public class Hole {

    private int id;
    private int redSeeds;
    private int blueSeeds;
    private int transparentSeeds;

    /**
     * Constructs a Hole object with the specified ID.
     *
     * @param id The ID of the hole.
     */
    public Hole(int id) {
        this.id = id;
        redSeeds = 2;
        blueSeeds = 2;
        transparentSeeds = 1;
    }

    public Hole(int id, int[] numberSeeds) {
        this.id = id;
        redSeeds = numberSeeds[0];
        blueSeeds = numberSeeds[1];
        transparentSeeds = numberSeeds[2];
    }

    /**
     * Returns the number of seeds of the specified color in the hole.
     *
     * @param color The color of the seeds (Color.R for red, Color.B for blue, Color.T for transparent).
     * @return The number of seeds of the specified color in the hole.
     */
    public int getColorSeeds(Color color) {
        if (color == Color.R) return redSeeds;
        else if (color == Color.B) return blueSeeds;
        else return transparentSeeds;
    }

    /**
     * Increments the number of seeds of the specified color in the hole.
     *
     * @param color The color of the seeds (Color.R for red, Color.B for blue, Color.T for transparent).
     */
    public void incrementsColorSeeds(Color color) {
        if (color == Color.R) redSeeds++;
        else if (color == Color.B) blueSeeds++;
        else transparentSeeds++;
    }

    /**
     * Empties the hole by setting the number of seeds of all colors to zero.
     */
    public void emptiesHole() {
        redSeeds = 0;
        blueSeeds = 0;
        transparentSeeds = 0;
    }

    /**
     * Empties the hole of seeds of the specified color.
     *
     * @param color The color of the seeds to empty (Color.R for red, Color.B for blue, Color.T for transparent).
     */
    public void emptiesColorHole(Color color) {
        if (color == Color.R) redSeeds = 0;
        else if (color == Color.B) blueSeeds = 0;
        else transparentSeeds = 0;
    }

    /**
     * Returns the total number of seeds in the hole.
     *
     * @return The sum of seeds in the hole.
     */
    public int sumSeeds() {
        return redSeeds + blueSeeds + transparentSeeds;
    }

    /**
     * Checks if the hole contains seeds of the specified color.
     *
     * @param color The color to check for seeds (Color.R for red, Color.B for blue, Color.T for transparent).
     * @return true if the hole contains seeds of the specified color, false otherwise.
     */
    public boolean hasColorSeeds(Color color) {
        if (color == Color.R) return redSeeds > 0;
        else if (color == Color.B) return blueSeeds > 0;
        else return transparentSeeds > 0;
    }

    /**
     * Checks if the hole has an even ID.
     *
     * @return true if the hole has an even ID, false otherwise.
     */
    public boolean isEven() {
        return id % 2 == 0;
    }

    /**
     * Prints the hole's ID and the number of seeds for each color in the hole.
     */
    public void printHole() {
        System.out.print(id + "(" + redSeeds + "R, " + blueSeeds + "B, " + transparentSeeds + "T)");
    }

    /**
     * Returns the ID of the hole.
     *
     * @return The ID of the hole.
     */
    public int getId() {
        return id;
    }

    /**
     * Checks if the hole is empty (contains no seeds).
     *
     * @return true if the hole is empty, false otherwise.
     */
    public boolean isEmpty() {
        return redSeeds + blueSeeds + transparentSeeds == 0;
    }
}