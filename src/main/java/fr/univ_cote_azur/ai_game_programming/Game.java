package fr.univ_cote_azur.ai_game_programming;

import java.util.Scanner;

/**
 * Public class {@link Game} which simulate a game of awale.
 */
public class Game {

    private final int NUMBER_OF_HOLES = 16;
    private Hole[] holes;
    private Scanner sc;
    private boolean AIplaysAsPlayerOne;
    private Player ia;
    private Player opponent;

    /**
     * Constructor of the class.
     */
    public Game() {
        sc = new Scanner(System.in);
        holes = new Hole[NUMBER_OF_HOLES];
        initiateHoles();
        initiatePlayer();
        System.out.println("-".repeat(15) + "START GAME" + "-".repeat(15));
    }

    private void initiatePlayer() {
        whoStarts();
        if (AIplaysAsPlayerOne) {
            ia = new PlayerOne(NUMBER_OF_HOLES);
            opponent = new PlayerTwo(NUMBER_OF_HOLES);
        } else {
            opponent = new PlayerOne(NUMBER_OF_HOLES);
            ia = new PlayerTwo(NUMBER_OF_HOLES);
        }
    }

    private void initiateHoles() {
        for (int i = 0; i < NUMBER_OF_HOLES; i++) {
            holes[i] = new Hole(i + 1);
        }
    }

    private void whoStarts() {
        System.out.print("Is our AI player One ? [Y/N]");
        AIplaysAsPlayerOne = sc.nextLine().equals("Y");
    }

    /**
     * Heart of the game. This method allow to start a game.
     * It determines which player's going to be the AI and start the right module.
     * Then, it prints the result of the game.
     */
    public void startGame() {
        printBoard();
        if (AIplaysAsPlayerOne) case_AI_isPlayerOne();
        else case_AI_isPlayerTwo();

        if (ia.getScore() > opponent.getScore()) System.out.print("Amine's IA won ; score : " + ia.getScore());
        else if (ia.getScore() < opponent.getScore()) System.out.print("Opponent won ; score : " + opponent.getScore());
        else System.out.print("It's a draw ; scores : " + ia.getScore());
    }

    private void case_AI_isPlayerOne() {
        boolean endGame = false;
        while (!endGame) {

            /**     Player 1 -- AI     **/
            int score_actual = ia.getScore();
            ia.setHoles(holes.clone());
            Move bestMove = minMax.decision(ia);
            System.out.println("Move min max : " + bestMove.getIdHole() + bestMove.getColor());
            ia.resetScore(score_actual);
            ia.setHoles(holes.clone());
            ia.nextPlay(bestMove);
            holes = ia.getHoles().clone();
            printBoard();

            if (ia.opponentIsStarving()) return;

            /**     Player 2 -- opponent **/
            opponent.setHoles(holes);
            askPlayerNextPlay(AIplaysAsPlayerOne);
            String playerPlays = sc.nextLine();
            Color seedColor = getSeedColor(playerPlays);
            int holeNumberId = getHoleId(playerPlays);
            opponent.nextPlay(new Move(holeNumberId, seedColor));
            holes = opponent.getHoles();
            printBoard();

            /** check the end of the game **/
            endGame = notEnoughSeeds() || isaDraw() || one_Player_Have_More_Then_Forty_Seeds() || opponent.opponentIsStarving();
        }
    }

    private void case_AI_isPlayerTwo() {
        boolean endGame = false;
        while (!endGame) {
            /**     Player 1 -- opponent   **/
            opponent.setHoles(holes);
            askPlayerNextPlay(AIplaysAsPlayerOne);
            String playerPlays = sc.nextLine();
            Color seedColor = getSeedColor(playerPlays);
            int holeNumberId = getHoleId(playerPlays);
            opponent.nextPlay(new Move(holeNumberId, seedColor));
            holes = opponent.getHoles();
            printBoard();

            if (opponent.opponentIsStarving()) return;

            /**     Player 2 -- AI     **/
            int score_actual = ia.getScore();
            ia.setHoles(holes.clone());
            Move bestMove = minMax.decision(ia);
            System.out.println("Moov min max : " + bestMove.getIdHole() + bestMove.getColor());
            ia.resetScore(score_actual);
            ia.setHoles(holes.clone());
            ia.nextPlay(bestMove);
            holes = ia.getHoles().clone();
            printBoard();

            /** check the end of the game **/
            endGame = notEnoughSeeds() || isaDraw() || one_Player_Have_More_Then_Forty_Seeds() || ia.opponentIsStarving();
        }
    }

    private void askPlayerNextPlay(boolean AIplaysAsPlayerOne) {
        if (AIplaysAsPlayerOne) System.out.print("Player 2's play is : ");
        else System.out.print("Player 1's play is : ");
    }

    private Color getSeedColor(String J1_plays) {
        String seedColor = "";
        for (int i = 0; i < J1_plays.length(); i++) {
            char c = J1_plays.charAt(i);
            if (!Character.isDigit(c)) seedColor += c;
        }
        return switch (seedColor) {
            case "R" -> Color.R;
            case "B" -> Color.B;
            case "TR" -> Color.TR;
            case "TB" -> Color.TB;
            default -> Color.UNDEFINED;
        };
    }

    private int getHoleId(String J1_plays) {
        int holeNumberId = 0;
        for (int i = 0; i < J1_plays.length(); i++) {
            char c = J1_plays.charAt(i);
            if (Character.isDigit(c)) {
                holeNumberId = holeNumberId * 10 + Character.getNumericValue(c);
            } else {
                break;
            }
        }
        return holeNumberId;
    }

    private void printBoard() {
        for (int i = 0; i < NUMBER_OF_HOLES / 2; i++) {
            holes[i].printHole();
            System.out.print(" -- ");
        }
        System.out.println();
        for (int i = NUMBER_OF_HOLES - 1; i >= NUMBER_OF_HOLES / 2; i--) {
            holes[i].printHole();
            System.out.print(" -- ");
        }
        System.out.println();
        if (ia instanceof PlayerOne)
            System.out.println("Score ia : " + ia.getScore() + "\nScore opponent : " + opponent.getScore());
        else System.out.println("Score opponent : " + opponent.getScore() + "\nScore ia : " + ia.getScore());
        System.out.println("-".repeat(90));
    }

    private boolean one_Player_Have_More_Then_Forty_Seeds() {
        return ia.getScore() > 40 || opponent.getScore() > 40;
    }

    private boolean isaDraw() {
        return ia.getScore() == 40 && opponent.getScore() == 40;
    }

    private boolean notEnoughSeeds() {
        return remainingSeeds() < 10;
    }

    private int remainingSeeds() {
        int seedRemained = 0;
        for (int i = 0; i < NUMBER_OF_HOLES; i++) {
            seedRemained += holes[i].sumSeeds();
        }
        return seedRemained;
    }

    private void caseAIvsAI() {
        boolean endGame = false;
        while (!endGame) {
            /**     Player 1 -- AI     **/
            ia.setHoles(holes);
            Move bestMove = minMax.decision(ia);
            ia.nextPlay(bestMove);
            holes = ia.getHoles();
            printBoard();


            /**     Player 2 -- AI   **/
            opponent.setHoles(holes);
            opponent.nextPlay(new Move(0, null));
            holes = opponent.getHoles();
            printBoard();

            /** check the end of the game **/
            endGame = notEnoughSeeds() || isaDraw() || one_Player_Have_More_Then_Forty_Seeds();
        }
    }

}