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
     * Performs the next play for the player.
     * If the color is null, we set a random possible play. Useful to continue the game if our AI couldn't find a solution...
     * Then, we verify that the move is possible is a possible color.
     * After that, we can sow and capture.
     * Note: when we sow, there's a second check to see if the play is legit.
     *
     * @param move The {@link Move} which is going to be played by {@code this}.
     * @throws IllegalArgumentException if the param is not a valid play.
     */
    @Override
    public void nextPlay(Move move) {
        int id_firstHole;
        Color seedColor;
        if (move.getColor() == null) {
            Object[] dataPlay = getPossiblePlay();
            id_firstHole = (Integer) dataPlay[0];
            seedColor = (Color) dataPlay[1];
        } else {
            id_firstHole = move.getIdHole();
            seedColor = move.getColor();
        }
        try {
            validateInput(id_firstHole, seedColor);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            exit(0);
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

    /**
     * Simulate the next play for the player. This method is used in {@link minMax}  to not perform a move and only simulate it.
     *
     * @param move The {@link Move} which is going to be played by {@code this}.
     * @throws IllegalArgumentException if the {@param move} is not a valid play.
     */
    @Override
    public void simulate_NextPlay(Move move) {
        int id_firstHole;
        Color seedColor;
        if (move.getColor() == null) {
            Object[] dataPlay = getPossiblePlay();
            id_firstHole = (Integer) dataPlay[0];
            seedColor = (Color) dataPlay[1];
        } else {
            id_firstHole = move.getIdHole();
            seedColor = move.getColor();
        }
        try {
            validateInput(id_firstHole, seedColor);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            exit(0);
        }

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
            throw new IllegalArgumentException("Hole " + hole.getId() + " doesn't have seeds of this color :" + color + ".\n");
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
        for (int i = id_firstHole; number_of_seeds > 0; i+=2) {
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
     * Sets the state holes as they are after the play of the opponents.
     *
     * @param holes the array of holes to set
     */
    @Override
    public void setHoles(Hole[] holes) {
        int i = 0;
        for (Hole hole : holes) {
            this.holes[i++] = new Hole(hole.getId(), new int[]{hole.getColorSeeds(Color.R), hole.getColorSeeds(Color.B), hole.getColorSeeds(Color.TR)});
        }
    }


    private void capturing(int lastHoleId) {
        for (int i = lastHoleId - 1; true; i--) {
            int index = (i + NUMBER_OF_HOLES) % NUMBER_OF_HOLES;
            int score = holes[index].sumSeeds();
            if (score == 2 || score == 3) {
                this.score += score;
                holes[index].emptiesHole();
            } else break;
        }
        if (opponentIsStarving()) score += sumSeeds();
    }

    /**
     * Verify if the opponent is starving.
     *
     * @return true if the opponent is starving; false else.
     */
    public boolean opponentIsStarving() {
        for (int i = 0; i < NUMBER_OF_HOLES; i++) {
            if (!holes[i].isEven()) {
                if (!holes[i].isEmpty()) return false;
            }
        }
        return true;
    }

    /**
     * Method used to reset the score to its origin value after an iteration of the min-max algorithm
     *
     * @param resetScore the value of the score {@link Integer}
     */
    @Override
    public void resetScore(int resetScore) {
        score = resetScore;
    }

    private int sumSeeds() {
        int score = 0;
        for (int i = 0; i < NUMBER_OF_HOLES; i++) {
            score += holes[i].sumSeeds();
        }
        return score;
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
}
