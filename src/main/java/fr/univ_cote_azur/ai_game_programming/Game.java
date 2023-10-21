package fr.univ_cote_azur.ai_game_programming;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Game {

    /**
     *
     * @param playerNumIa the 0-based player number for IA (0 for the first and 1 for the second)
     */
    public static void gameLoopIaVsIa(int playerNumIa){
        int result;
        int cell;
        Color color;
        int[] players = {1, 0};
        int player = 0;
        String input;
        Board board = new Board();
        Pattern p = Pattern.compile("^([0-9]{1,2})(R|B|TB|TR)$", Pattern.CASE_INSENSITIVE);
        Scanner scanner = new Scanner(System.in);
        System.out.println(board);
        IA ia = new IA(playerNumIa);
        do {
            if(player == playerNumIa){
                IA.Move move = ia.play(board);
                cell = move.cell();
                color = move.color();
                input = (cell+1) + move.color().toString();
                System.out.println("IA Move: " + input);
            }
            else{
                System.out.println("Enter the cell position to start with the color of the seed to play e.g 2B: ");
                input = scanner.nextLine();
                input = input.strip();
                Matcher matcher = p.matcher(input);
                while(!matcher.matches()){
                    System.out.println("Enter a correct the cell position and the color of the seed e.g 2B: ");
                    input = scanner.nextLine();
                    input = input.strip();
                    matcher = p.matcher(input);
                }
                cell = Integer.parseInt(matcher.group(1)) - 1;
                color = Color.parse(matcher.group(2));
            }
            result = board.play(cell, player, color);
            if (result == -2) {
                System.out.print(input + " is a forbidden move, Current Player: " + (player+1) + "\n");
            }
            else{
                player = players[player];
            }
            System.out.println(board);
        }while (result < 0);
        if (result > 0){
            System.out.println("the Player " + result + " won the game");
        }else{
            System.out.println("It is a draw");
        }
    }

    public static void gameLoopHumanVsHuman(){
        int result;
        int[] players = {1, 0};
        int player = 0;
        Board board = new Board();
        Pattern p = Pattern.compile("^([0-9]{1,2})(R|B|TB|TR)$", Pattern.CASE_INSENSITIVE);
        Scanner scanner = new Scanner(System.in);
        System.out.println(board);
        do {
            System.out.println("Enter the cell position to start with the color of the seed to play e.g 2B: ");
            String userInput = scanner.nextLine();
            userInput = userInput.strip();
            Matcher matcher = p.matcher(userInput);
            while(!matcher.matches()){
                System.out.println("Enter a correct the cell position and the color of the seed e.g 2B: ");
                userInput = scanner.nextLine();
                userInput = userInput.strip();
                matcher = p.matcher(userInput);
            }
            int start = Integer.parseInt(matcher.group(1)) - 1;
            Color color = Color.parse(matcher.group(2));
            result = board.play(start, player, color);
            if (result == -2) {
                System.out.print(userInput + " is a forbidden move, Current Player: " + (player+1) + "\n");
            }
            else{
                player = players[player];
            }
            System.out.println(board);
        }while (result < 0);
        if (result > 0){
            System.out.println("the Player " + result + " won the game");
        }else{
            System.out.println("It is a draw");
        }
    }
}
