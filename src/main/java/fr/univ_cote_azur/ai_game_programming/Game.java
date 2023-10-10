package fr.univ_cote_azur.ai_game_programming;

public class Game {

    // The number of Holes in the game
    private final int NUMBER_OF_HOLES = 16;
    // Tables which contains the number of seeds in each hole
    private int[] redSeeds;
    private int[] blueSeeds;
    private int[] tranparentSeeds;


    public Game(){
        redSeeds = new int[NUMBER_OF_HOLES];
        blueSeeds = new int[NUMBER_OF_HOLES];
        tranparentSeeds = new int[NUMBER_OF_HOLES];
        initiateTables();

    }

    private void initiateTables(){
        for (int i = 0; i < NUMBER_OF_HOLES; i++) {
            redSeeds[i] = 2;
            blueSeeds[i] = 2;
            tranparentSeeds[i] = 1;
        }
    }

    public start_game(){

    }





}
