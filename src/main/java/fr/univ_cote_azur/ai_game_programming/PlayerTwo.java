package fr.univ_cote_azur.ai_game_programming;

import java.util.Random;

import static java.lang.System.exit;

/**
 * Represents the second player of the game.
 */
public class PlayerTwo implements Player {

    private final int NUMBER_OF_HOLES;
    private int score;
    private Hole[] holes;

    /**
     * Constructor of the class.
     *
     * @param NUMBER_OF_HOLES number of the holes in the game {@link Integer}.
     */
    public PlayerTwo(int NUMBER_OF_HOLES) {
        this.NUMBER_OF_HOLES = NUMBER_OF_HOLES;
        score = 0;
        this.holes = new Hole[NUMBER_OF_HOLES];
    }

    /**
     * Retrieves the score of the player.
     *
     * @return the player's score
     */
    @Override
    public int getScore() {
        return score;
    }

    /**
     * Performs the next play for the player.
     *
     * @param id_firstHole the ID of the first hole to play from
     * @param seedColor    the color of the seeds to play
     */
    @Override
    public void nextPlay(int id_firstHole, Color seedColor) {
        if (seedColor == null) {
            Object[] dataPlay = getPossiblePlay();
            id_firstHole = (Integer) dataPlay[0];
            seedColor = (Color) dataPlay[1];
        }
        try {
            validateInput(id_firstHole, seedColor);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            exit(0);
            ;
        }
        System.out.println("Player Two play : " + id_firstHole + seedColor);

        int lastHoleId;
        try {
            lastHoleId = sowing(id_firstHole, seedColor);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }
        capturing(lastHoleId);
    }

    private void validateInput(int holeNumberId, Color seedColor) {
        boolean possibleNumber = 1 <= holeNumberId && holeNumberId <= NUMBER_OF_HOLES;
        boolean possibleColor = seedColor == Color.R || seedColor == Color.B || seedColor == Color.TR || seedColor == Color.TB;
        if (!possibleNumber || !possibleColor)
            throw new IllegalArgumentException("Impossible play from the last player... Impossible color or Number : " + holeNumberId + seedColor + ".");
    }

    private Object[] getPossiblePlay() {
        int idHole = getRandomNumber();
        while (!holes[idHole - 1].isEven() || holes[idHole - 1].isEmpty()) {
            idHole = getRandomNumber();
        }
        Color color = getRandomColor();
        while (!holes[idHole - 1].hasColorSeeds(color)) {
            color = getRandomColor();
        }
        return new Object[]{idHole, color};
    }

    private Color getRandomColor() {
        Color[] colorArray = Color.getValues();
        Random random = new Random();
        int randomIndex = random.nextInt(colorArray.length);
        return colorArray[randomIndex];
    }

    private int getRandomNumber() {
        return new Random().nextInt(16) + 1;
    }

    /**
     * Performs the sowing action for the player.
     *
     * @param id_firstHole the ID of the first hole to sow from
     * @param color        the color of the seeds to sow
     * @return the ID of the last sowed hole
     */
    public int sowing(int id_firstHole, Color color) {
        try {
            isLegitPlay(id_firstHole, color);
        } catch (IllegalArgumentException e) {
            System.out.print(e.getMessage());
            exit(0);
            ;
        }
        int number_of_seeds = holes[id_firstHole - 1].getColorSeeds(color);
        holes[id_firstHole - 1].emptiesColorHole(color);
        int lastSowedHole;
        if (color == Color.R || color == Color.TR) lastSowedHole = sowingRed(id_firstHole, color, number_of_seeds);
        else lastSowedHole = sowingBlue(id_firstHole, color, number_of_seeds);
        return ++lastSowedHole;
    }

    private void isLegitPlay(int id_firstHole, Color color) {
        Hole hole = holes[id_firstHole - 1];
        if (!hole.isEven()) throw new IllegalArgumentException("Player Two can't sow an odd hole.\n");
        if (!hole.hasColorSeeds(color))
            throw new IllegalArgumentException("Hole " + hole.getId() + "doesn't have seeds of this color :" + color + ".\n");
    }

    private int sowingRed(int id_firstHole, Color color, int number_of_seeds) {
        int index = 0;
        for (int i = id_firstHole; number_of_seeds > 0; i++) {
            index = i % holes.length;
            if (index == id_firstHole - 1) continue;
            holes[index].incrementsColorSeeds(color);
            number_of_seeds--;
        }
        return index;
    }

    private int sowingBlue(int id_firstHole, Color color, int number_of_seeds) {
        int index = 0;
        for (int i = id_firstHole; number_of_seeds > 0; i++) {
            index = i % holes.length;
            if (index == id_firstHole - 1 || index % 2 == 1) continue;
            holes[index].incrementsColorSeeds(color);
            number_of_seeds--;
        }
        return index;
    }

    /**
     * Retrieves the holes of the player after the change he has applied.
     *
     * @return an array of holes
     */
    @Override
    public Hole[] getHoles() {
        return holes;
    }

    /**
     * Sets the state holes as they are after the play of the opponents..
     *
     * @param holes the array of holes to set
     */
    @Override
    public void setHoles(Hole[] holes) {
        this.holes = holes;
    }


    private void capturing(int lastHoleId) {
        if (opponentIsStarving()) {
            score += sumSeeds();
            return;
        }
        for (int i = lastHoleId - 1; true; i--) {
            int index = (i + holes.length) % holes.length;
            int score = holes[index].sumSeeds();
            if (score == 2 || score == 3) {
                this.score += score;
                holes[index].emptiesHole();
            } else return;
        }
    }

    private boolean opponentIsStarving() {
        for (int i = 0; i < NUMBER_OF_HOLES; i++) {
            if (holes[i].isEven()) {
                if (!holes[i].isEmpty()) return false;
            }
        }
        return true;
    }

    private int sumSeeds() {
        int score = 0;
        for (int i = 0; i < NUMBER_OF_HOLES; i++) {
            score += holes[i].sumSeeds();
        }
        return score;
    }


}
