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
        boolean isP1_turn = true;
        String playerPlays = "";

        // Loop of the game
        while (!endGame) {
            askPlayerNextPlay(isP1_turn);
            playerPlays = sc.nextLine();
            validateInput(isP1_turn, playerPlays);

            String seedColor = getSeedColor(playerPlays);
            int holeNumberIndex = getHoleNumber(playerPlays);

            sowing(isP1_turn, holeNumberIndex, seedColor);
            // TODO : capturing function
        }
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

    private void sowing(boolean isP1_turn, int holeNumberIndex, String seedColor) {
        if (seedColor.equalsIgnoreCase("R")) sowingRedSeeds(holeNumberIndex);
        else if (seedColor.equalsIgnoreCase("TR")) sowingTransparentRedSeeds(holeNumberIndex);
        else if (isP1_turn && seedColor.equalsIgnoreCase("TB")) sowingTransparentBlueSeeds(1, holeNumberIndex);
        else if (!isP1_turn && seedColor.equalsIgnoreCase("TB")) sowingTransparentBlueSeeds(0, holeNumberIndex);
        else if (isP1_turn) sowingBlueSeeds(1, holeNumberIndex);
        else sowingBlueSeeds(0, holeNumberIndex);
    }

    private void sowingRedSeeds(int holeNumberIndex) {
        int numberOfRedSeed = redSeeds[holeNumberIndex];
        redSeeds[holeNumberIndex] = 0;
        for (int i = holeNumberIndex + 1; numberOfRedSeed > 0; i++) {
            int index = i % NUMBER_OF_HOLES;
            if (index == holeNumberIndex) continue;
            redSeeds[index]++;
            numberOfRedSeed--;
        }
    }

    private void sowingTransparentRedSeeds(int holeNumberIndex) {
        int numberOfTransparentSeed = tranparentSeeds[holeNumberIndex];
        tranparentSeeds[holeNumberIndex] = 0;
        for (int i = holeNumberIndex + 1; numberOfTransparentSeed > 0; i++) {
            int index = i % NUMBER_OF_HOLES;
            if (index == holeNumberIndex) continue;
            tranparentSeeds[index]++;
            numberOfTransparentSeed--;
        }
    }

    private void sowingTransparentBlueSeeds(int polarityOfholes, int holeNumberIndex) {
        int numberOfBlueSeed = blueSeeds[holeNumberIndex];
        blueSeeds[holeNumberIndex] = 0;
        for (int i = holeNumberIndex + 1; numberOfBlueSeed > 0; i++) {
            int index = i % NUMBER_OF_HOLES;
            if (index % 2 != polarityOfholes) continue;
            blueSeeds[index]++;
            numberOfBlueSeed--;
        }
    }

    private void sowingBlueSeeds(int polarityOfholes, int holeNumberIndex) {
        int numberOfBlueSeed = blueSeeds[holeNumberIndex];
        blueSeeds[holeNumberIndex] = 0;
        for (int i = holeNumberIndex + 1; numberOfBlueSeed > 0; i++) {
            int index = i % NUMBER_OF_HOLES;
            if (index % 2 != polarityOfholes) continue;
            blueSeeds[index]++;
            numberOfBlueSeed--;
        }
    }

}
