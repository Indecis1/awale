package fr.univ_cote_azur.ai_game_programming;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Game mode: \n1-IA Vs Bot \n2-Human Vs IA\n3- IA vs IA");
        String input = scanner.nextLine();
        input = input.strip();
        while (!input.equals("1") && !input.equals("2") && !input.equals("3")) {
            System.out.println("Please enter a correct number: \n1-IA Vs IA \n2-Human Vs IA\n3- IA vs IA");
            input = scanner.nextLine();
            input = input.strip();
        }
        try{
            if (input.equals("2")) {
                Game.gameLoopHumanVsHuman();
            }
            if (input.equals("1")) {
                System.out.println("Is this IA the player 1 or 2? \n1- Player 1\n2- Player 2\n");
                input = scanner.nextLine();
                input = input.strip();
                while (!input.equals("1") && !input.equals("2")) {
                    System.out.println("Please enter a correct number: \n1- Player 1\n2- Player 2\n");
                    input = scanner.nextLine();
                    input = input.strip();
                }
                int playNum = Integer.parseInt(input);
                Game.gameLoopIaVsBot(playNum - 1);
            }
            if (input.equals("3")){
                System.out.println("Is this IA1 the player 1 or 2? \n1- Player 1\n2- Player 2\n");
                input = scanner.nextLine();
                input = input.strip();
                while (!input.equals("1") && !input.equals("2")) {
                    System.out.println("Please enter a correct number: \n1- Player 1\n2- Player 2\n");
                    input = scanner.nextLine();
                    input = input.strip();
                }
                int playNum = Integer.parseInt(input);
                Game.gameLoopIaVsIa(playNum - 1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}