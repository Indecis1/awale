package fr.univ_cote_azur.ai_game_programming;

import java.util.Scanner;

/**
 * Public class {@link Game} which simulate a game of awale.
 */
public class Game {

    // The number of Holes in the game
    private final int NUMBER_OF_HOLES = 16;
    // Tables which contains the number of seeds in each hole
    private int[] redSeeds;
    private int[] blueSeeds;
    private int[] tranparentSeeds;
    // Two players' scores
    private int scoreP1;
    private int scoreP2;

    /**
     * Constructor of the class. It initialies its attributes and the initial state of the game's board.
     */
    public Game() {
        redSeeds = new int[NUMBER_OF_HOLES];
        blueSeeds = new int[NUMBER_OF_HOLES];
        tranparentSeeds = new int[NUMBER_OF_HOLES];
        initiateTables();
        scoreP1 = 0;
        scoreP2 = 0;
    }

    private void initiateTables() {
        for (int i = 0; i < NUMBER_OF_HOLES; i++) {
            redSeeds[i] = 2;
            blueSeeds[i] = 2;
            tranparentSeeds[i] = 1;
        }
    }

    /**
     * Main method of the class. This method allows to start a simulation of the game in {@link Main}.{@code main}
     */
    public void start_game() {
        Scanner sc = new Scanner(System.in);
        boolean endGame = false;
        boolean isP1_turn = false;
        String playerPlays = "";

        printStateOfHoles();
        // Loop of the game
        while (!endGame) {
            isP1_turn = !isP1_turn;
            askPlayerNextPlay(isP1_turn);
            playerPlays = sc.nextLine();
            String seedColor = getSeedColor(playerPlays);
            int holeNumberIndex = getHoleNumber(playerPlays);
            try {
                validateInput(isP1_turn, holeNumberIndex, seedColor);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                return;
            }


            System.out.println("seed color :" + seedColor + "\nhole number :" + holeNumberIndex);
            int lastHoleIndex;
            try {
                lastHoleIndex = sowing(isP1_turn, holeNumberIndex, seedColor);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                return;
            }
            capturing(isP1_turn, lastHoleIndex);
            printStateOfHoles();
            endGame = one_Player_Have_More_Then_Forty_Seeds() || isaDraw() || notEnoughSeeds();
        }
        printResult();
    }

    private void askPlayerNextPlay(boolean isP1_turns) {
        if (isP1_turns) System.out.print("Player 1's play is : ");
        else System.out.print("Player 2's play is : ");
    }

    private void validateInput(Boolean isP1_turns, int holeNumberIndex, String seedColor) {
        boolean possibleNumber = 0 <= holeNumberIndex && holeNumberIndex < NUMBER_OF_HOLES;
        boolean possibleColor = seedColor.equals("R") || seedColor.equals("B") || seedColor.equals("TR") || seedColor.equals("TB");
        if (!possibleNumber || !possibleColor)
            throw new IllegalArgumentException("Impossible play from the last player... Impossible color or Number.");
        else if (isP1_turns && (holeNumberIndex + 1) % 2 == 0)
            throw new IllegalArgumentException("Player 1 can't play this move. He has the odd holes.");
        else if (!isP1_turns && (holeNumberIndex + 1) % 2 == 1) {
            throw new IllegalArgumentException("Player 2 can't play this move. He has the even holes.");
        }
    }

    private String getSeedColor(String J1_plays) {
        String seedColor = "";
        for (int i = 0; i < J1_plays.length(); i++) {
            char c = J1_plays.charAt(i);
            if (!Character.isDigit(c)) seedColor += c;
        }
        return seedColor;
    }

    private int getHoleNumber(String J1_plays) {
        int holeNumberIndex = 0;
        for (int i = 0; i < J1_plays.length(); i++) {
            char c = J1_plays.charAt(i);
            if (Character.isDigit(c)) {
                holeNumberIndex = holeNumberIndex * 10 + Character.getNumericValue(c);
            } else {
                break;
            }
        }
        return holeNumberIndex - 1;
    }

    /**
     * The methode sowing simulates the sowing of sades starting from the {@code holeNumberIndex} hole.
     * Depending on the play chose by the player, we will choose the right method to snow the seed without
     * violate the game's rules.
     *
     * @param isP1_turn       a {@link Boolean} to determine if it is player 1 turn or not.
     * @param holeNumberIndex an {@link Integer} representing the hole from wihch we take the seed and from where
     *                        we start the snowing
     * @param seedColor       a {@link String} that represents the seed's color chose by the player.
     * @return an {@link Integer} which corresponds to the last sowed hole.
     */
    private int sowing(boolean isP1_turn, int holeNumberIndex, String seedColor) {
        if (noSeedOfThisColor(holeNumberIndex, seedColor))
            throw new IllegalArgumentException("There is no " + seedColor + " seed in hole " + (holeNumberIndex + 1));
        int lastHoleIndex;
        if (seedColor.equals("R")) lastHoleIndex = sowingRedSeeds(holeNumberIndex);
        else if (seedColor.equals("TR")) lastHoleIndex = sowingTransparentRedSeeds(holeNumberIndex);
        else if (isP1_turn && seedColor.equals("TB")) lastHoleIndex = sowingTransparentBlueSeeds(1, holeNumberIndex);
        else if (!isP1_turn && seedColor.equals("TB")) lastHoleIndex = sowingTransparentBlueSeeds(0, holeNumberIndex);
        else if (isP1_turn) lastHoleIndex = sowingBlueSeeds(1, holeNumberIndex);
        else lastHoleIndex = sowingBlueSeeds(0, holeNumberIndex);
        return lastHoleIndex;
    }

    private boolean noSeedOfThisColor(int holeNumberIndex, String seedColor) {
        if (seedColor.equals("R")) {
            return redSeeds[holeNumberIndex] == 0;
        } else if (seedColor.equals("B")) {
            return blueSeeds[holeNumberIndex] == 0;
        } else {
            return tranparentSeeds[holeNumberIndex] == 0;
        }
    }

    private int sowingRedSeeds(int holeNumberIndex) {
        int numberOfRedSeeds = redSeeds[holeNumberIndex];
        redSeeds[holeNumberIndex] = 0;
        int index = holeNumberIndex;
        for (int i = holeNumberIndex + 1; numberOfRedSeeds > 0; i++) {
            index = i % NUMBER_OF_HOLES;
            if (index == holeNumberIndex) continue;
            redSeeds[index]++;
            numberOfRedSeeds--;
        }
        return index;
    }

    private int sowingTransparentRedSeeds(int holeNumberIndex) {
        int numberOfTransparentSeeds = tranparentSeeds[holeNumberIndex];
        tranparentSeeds[holeNumberIndex] = 0;
        int index = holeNumberIndex;
        for (int i = holeNumberIndex + 1; numberOfTransparentSeeds > 0; i++) {
            index = i % NUMBER_OF_HOLES;
            if (index == holeNumberIndex) continue;
            tranparentSeeds[index]++;
            numberOfTransparentSeeds--;
        }
        return index;
    }

    private int sowingTransparentBlueSeeds(int polarityOfholes, int holeNumberIndex) {
        int numberOfTransparentSeeds = tranparentSeeds[holeNumberIndex];
        tranparentSeeds[holeNumberIndex] = 0;
        int index = holeNumberIndex;
        for (int i = holeNumberIndex + 1; numberOfTransparentSeeds > 0; i++) {
            index = i % NUMBER_OF_HOLES;
            if (index == holeNumberIndex || (index+1)%2 != polarityOfholes) continue;
            tranparentSeeds[index]++;
            numberOfTransparentSeeds--;
        }
        return index;
    }

    private int sowingBlueSeeds(int polarityOfholes, int holeNumberIndex) {
        int numberOfBlueSeeds = blueSeeds[holeNumberIndex];
        blueSeeds[holeNumberIndex] = 0;
        int index = holeNumberIndex;
        for (int i = holeNumberIndex + 1; numberOfBlueSeeds > 0; i++) {
            index = i % NUMBER_OF_HOLES;
            if (index == holeNumberIndex || (index+1)%2 != polarityOfholes) continue;
            blueSeeds[index]++;
            numberOfBlueSeeds--;
        }
        return index;
    }

    /**
     * This methode simulate the capture of the seed after the snowing. Thus, it updates the scores' value.
     *
     * @param isP1_turns    a {@link Boolean} to determine if it is player 1 turn or not.
     * @param lastHoleIndex an {@link Integer} to determine the last sowed hole's index.
     */
    private void capturing(boolean isP1_turns, int lastHoleIndex) {
        for (int i = lastHoleIndex; true; i--) {
            int score = holesSeeds(i % NUMBER_OF_HOLES);
            if (score == 0) break;
            else if (isP1_turns) scoreP1 += score;
            else scoreP2 += score;
        }
    }

    private int holesSeeds(int index) {
        if ((redSeeds[index] + blueSeeds[index] + tranparentSeeds[index] == 2)) return 2;
        else if (redSeeds[index] + blueSeeds[index] + tranparentSeeds[index] == 3) return 3;
        else return 0;
    }

    private void printStateOfHoles() {
        for (int i = 0; i < NUMBER_OF_HOLES / 2; i++) {
            System.out.print((i + 1) + " ( " + redSeeds[i] + "R, " + blueSeeds[i] + "B, " + tranparentSeeds[i] + "T )  ");
        }
        System.out.println();
        for (int i = NUMBER_OF_HOLES - 1; i >= NUMBER_OF_HOLES / 2; i--) {
            System.out.print((i + 1) + " ( " + redSeeds[i] + "R, " + blueSeeds[i] + "B, " + tranparentSeeds[i] + "T )  ");
        }
        System.out.println();
    }

    private boolean one_Player_Have_More_Then_Forty_Seeds() {
        return scoreP1 > 40 || scoreP2 > 40;
    }

    private boolean isaDraw() {
        return scoreP1 == 40 && scoreP2 == 40;
    }

    private boolean notEnoughSeeds() {
        return remainingSeeds() < 10;
    }

    private int remainingSeeds() {
        int seedRemained = 0;
        for (int i = 0; i < NUMBER_OF_HOLES; i++) {
            seedRemained += redSeeds[i] + blueSeeds[i] + tranparentSeeds[i];
        }
        return seedRemained;
    }

    private void printResult() {
        if (isaDraw()) System.out.println("It's a draw (score : " + scoreP1 + " ). Congratulations to both players.");
        else if (scoreP1 > scoreP2)
            System.out.println("Player 1 has won (score : " + scoreP1 + " ). Congratulations to both players.");
        else System.out.println("Player 2 has won (score : " + scoreP2 + " ). Congratulations to both players.");
    }
}
