package fr.univ_cote_azur.ai_game_programming.Player;

import fr.univ_cote_azur.ai_game_programming.BoardOperations;
import fr.univ_cote_azur.ai_game_programming.Color;

import static java.lang.System.exit;

public class Simulate_Player extends Opponent {
    public Simulate_Player(int turn) {
        super(turn);
    }

    public void simulate_play(int[][] board, IA.Move move) {
        int index_first_hole = move.indexPlay();
        Color color = move.color();
        try {
            legitPlay(board, index_first_hole, color);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            exit(0);
        }

        int last_index = sowing(board, index_first_hole, color);
        int seed_captured = capturing(board, last_index);
        add_to_score(seed_captured);

        if (otherPlayerIsStarving(board)) {
            seed_captured = BoardOperations.count_seeds(board);
            add_to_score(seed_captured);
            BoardOperations.emptyBoard(board);
        }
    }

}
