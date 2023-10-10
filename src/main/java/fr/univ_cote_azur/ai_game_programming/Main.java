package fr.univ_cote_azur.ai_game_programming;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
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
            //Color color = Color.valueOf(matcher.group(2).toUpperCase());
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