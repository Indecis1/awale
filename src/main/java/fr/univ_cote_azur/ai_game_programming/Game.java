package fr.univ_cote_azur.ai_game_programming;

import java.util.Scanner;

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
            validateInput(isP1_turn, playerPlays);

            String seedColor = getSeedColor(playerPlays);
            int holeNumberIndex = getHoleNumber(playerPlays);

            int lastHoleIndex = sowing(isP1_turn, holeNumberIndex, seedColor);
            capturing(isP1_turn, lastHoleIndex);
            printStateOfHoles();
            endGame = one_Player_Have_More_Then_Forty_Seeds() || isaDraw() || notEnoughSeeds();
        }
        printResult();
    }

    private void validateInput(Boolean isP1_turns, @org.jetbrains.annotations.NotNull String input) {
        if (!input.matches("^1[0-6]?T?[R|B]$"))
            throw new IllegalArgumentException("Impossible play from the last player... He lost.\n " + "Wrong color or Number.");
        if (isP1_turns && input.matches("^\\d*[02468]([A-B]|[a-b])*"))
            throw new IllegalArgumentException("Player 1 can't play this moov. He has the even holes.");
        else if (!isP1_turns && !input.matches("^\\d*[02468]([A-B]|[a-b])*")) {
            throw new IllegalArgumentException("Player 2 can't play this moov. He has the odd holes.");
        }
    }

    private void askPlayerNextPlay(boolean isP1_turns) {
        if (isP1_turns) System.out.print("Player 1's play is :");
        else System.out.print("Player 2's play is :");
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
            if (Character.isDigit(c)) holeNumberIndex += c;
            else break;
        }
        return --holeNumberIndex;
    }

    /**
     * The methode sowing simulates the sowing of sades starting from the {@code holeNumberIndex} hole.
     * Depending on the play chose by the player, we will choose the right method to snow the seed without
     * violate the game's rules.
     * @param isP1_turn a {@link Boolean} to determine if it is player 1 turn or not.
     * @param holeNumberIndex an {@link Integer} representing the hole from wihch we take the seed and from where
     *                        we start the snowing
     * @param seedColor a {@link String} that represents the seed's color chose by the player.
     * @return an {@link Integer} which corresponds to the last sowed hole.
     */
    private int sowing(boolean isP1_turn, int holeNumberIndex, String seedColor) {
        int lastHoleIndex = holeNumberIndex;
        if (seedColor.equalsIgnoreCase("R")) lastHoleIndex = sowingRedSeeds(holeNumberIndex);
        else if (seedColor.equalsIgnoreCase("TR")) lastHoleIndex = sowingTransparentRedSeeds(holeNumberIndex);
        else if (isP1_turn && seedColor.equalsIgnoreCase("TB"))
            lastHoleIndex = sowingTransparentBlueSeeds(1, holeNumberIndex);
        else if (!isP1_turn && seedColor.equalsIgnoreCase("TB"))
            lastHoleIndex = sowingTransparentBlueSeeds(0, holeNumberIndex);
        else if (isP1_turn) sowingBlueSeeds(1, holeNumberIndex);
        else lastHoleIndex = sowingBlueSeeds(0, holeNumberIndex);
        return lastHoleIndex;
    }

    private int sowingRedSeeds(int holeNumberIndex) {
        int numberOfRedSeed = redSeeds[holeNumberIndex];
        redSeeds[holeNumberIndex] = 0;
        int index = holeNumberIndex;
        for (int i = holeNumberIndex + 1; numberOfRedSeed > 0; i++) {
            index = i % NUMBER_OF_HOLES;
            if (index == holeNumberIndex) continue;
            redSeeds[index]++;
            numberOfRedSeed--;
        }
        return index;
    }

    private int sowingTransparentRedSeeds(int holeNumberIndex) {
        int numberOfTransparentSeed = tranparentSeeds[holeNumberIndex];
        tranparentSeeds[holeNumberIndex] = 0;
        int index = holeNumberIndex;
        for (int i = holeNumberIndex + 1; numberOfTransparentSeed > 0; i++) {
            index = i % NUMBER_OF_HOLES;
            if (index == holeNumberIndex) continue;
            tranparentSeeds[index]++;
            numberOfTransparentSeed--;
        }
        return index;
    }

    private int sowingTransparentBlueSeeds(int polarityOfholes, int holeNumberIndex) {
        int numberOfBlueSeed = blueSeeds[holeNumberIndex];
        blueSeeds[holeNumberIndex] = 0;
        int index = holeNumberIndex;
        for (int i = holeNumberIndex + 1; numberOfBlueSeed > 0; i++) {
            index = i % NUMBER_OF_HOLES;
            if (index % 2 != polarityOfholes) continue;
            blueSeeds[index]++;
            numberOfBlueSeed--;
        }
        return index;
    }

    private int sowingBlueSeeds(int polarityOfholes, int holeNumberIndex) {
        int numberOfBlueSeed = blueSeeds[holeNumberIndex];
        blueSeeds[holeNumberIndex] = 0;
        int index = holeNumberIndex;
        for (int i = holeNumberIndex + 1; numberOfBlueSeed > 0; i++) {
            index = i % NUMBER_OF_HOLES;
            if (index % 2 != polarityOfholes) continue;
            blueSeeds[index]++;
            numberOfBlueSeed--;
        }
        return index;
    }

    /**
     * This methode simulate the capture of the seed after the snowing. Thus, it updates the scores' value.
     * @param isP1_turns a {@link Boolean} to determine if it is player 1 turn or not.
     * @param lastHoleIndex an {@link Integer} to determine the last sowed hole's index.
     */
    private void capturing(boolean isP1_turns, int lastHoleIndex) {
        for (int i = lastHoleIndex; true; i--) {
            int score = holesSeeds(i%NUMBER_OF_HOLES);
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

    private void printStateOfHoles(){
        for (int i = 0; i < NUMBER_OF_HOLES/2; i++) {
            System.out.print((i+1) + " ( "+ redSeeds[i] +"R, " + blueSeeds[i] +"B, " + tranparentSeeds[i] +"T )");
        }
        for (int i = NUMBER_OF_HOLES-1; i > NUMBER_OF_HOLES/2  ; i++) {
            System.out.print((i+1) + " ( "+ redSeeds[i] +"R, " + blueSeeds[i] +"B, " + tranparentSeeds[i] +"T )");
        }
    }

    private boolean one_Player_Have_More_Then_Forty_Seeds(){
        return scoreP1 > 40 || scoreP2 > 40;
    }

    private boolean isaDraw(){
        return scoreP1==40 && scoreP2 ==40;
    }
    private boolean notEnoughSeeds(){
        return remainingSeeds() < 10;
    }

    private int remainingSeeds(){
        int seedRemained = 0;
        for (int i = 0; i < NUMBER_OF_HOLES; i++) {
            seedRemained += redSeeds[i] + blueSeeds[i] + tranparentSeeds[i];
        }
        return seedRemained;
    }

    private void printResult(){
        if (isaDraw())
            System.out.println("It's a draw (score : " + scoreP1 + " ). Congratulations to both players.");
        else if (scoreP1 > scoreP2)
            System.out.println("Player 1 has won (score : " + scoreP1 + " ). Congratulations to both players.");
        else
            System.out.println("Player 2 has won (score : " + scoreP2 + " ). Congratulations to both players.");
    }
}
