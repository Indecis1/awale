package fr.univ_cote_azur.ai_game_programming;


import java.util.ArrayList;
import java.util.List;

public class Board implements Cloneable {

    private int[] playerSeeds;
    private int[] redSeedBoard;
    private int[] blueSeedBoard;
    private int[] transparentSeedBoard;
    private int turn;

    //private List<IA.Move> moveHistory;

    public Board(){
        playerSeeds = new int[2];
        redSeedBoard = new int[16];
        blueSeedBoard = new int[16];
        transparentSeedBoard = new int[16];
        for(int i = 0; i<16; i++){
            redSeedBoard[i] = 2;
            blueSeedBoard[i] = 2;
            transparentSeedBoard[i] = 1;
        }
        turn = 0;
        //moveHistory = new ArrayList<>();
    }

    /**
     *
     * @param start the 0-based index of the cell where will would take the seeds
     * @param player the 0-based player number (0 for the first and 1 for the second)
     * @param color the color of the seed to take
     * @return a bool representing if we can play this position or not
     */
    public boolean canPlay(int start, int player, Color color){
        boolean result = (start+1) % 2 == (player + 1) % 2;
        result &= canPlay(start, color);
        return result;
    }

    /**
     *
     * @param start the 0-based index of the cell where will would take the seeds
     * @param color the color of the seed to take
     * @return a bool representing if we can play this position or not
     */
    public boolean canPlay(int start, Color color){
        int seeds = 0;
        switch (color) {
            case RED -> seeds = redSeedBoard[start];
            case BLUE -> seeds =  blueSeedBoard[start];
            case TRANSPARENT_RED, TRANSPARENT_BLUE -> seeds = transparentSeedBoard[start];
        }
        return seeds > 0;
    }

    /**
     *
     * @param start the 0-based index of the cell where will would take the seeds
     * @param player the 0-based player number (0 for the first and 1 for the second)
     * @param color the color of the seed to take
     * @return a number representing the winning player, 0 if a draw, -2 if it is a forbidden move and -1 otherwise
     */
    public int play(int start, int player, Color color){
        if (start < 0 || start > 15)
            return -1;
        if(!canPlay(start, player, color))
            return -2;
        int lastHole = 0;
        switch (color) {
            case RED -> lastHole = playRed(start, redSeedBoard);
            case BLUE -> lastHole = playBlue(start, blueSeedBoard);
            case TRANSPARENT_RED -> lastHole = playRed(start, transparentSeedBoard);
            case TRANSPARENT_BLUE -> lastHole = playBlue(start, transparentSeedBoard);
        }
        //moveHistory.add(new IA.Move(start, color));
        playerSeeds[start % 2] += capture(lastHole);
        turn++;
        return checkVictory(player);
    }

    /**
     *
     * @param start the 0-based index of the cell where will would take the seeds
     * @param board the color of the seed to take
     * @return the last index of the board updated
     */
    private int playRed(int start, int[] board){
        int i;
        int seeds = board[start];
        board[start] = 0;
        int endCell = start+seeds + 1;
        if (seeds % 16 == 0)
            endCell += seeds / 16;
        int startIndex = start % 16;
        for (i = start + 1; i < endCell; i++){
            if ((i % 16) == startIndex)
                continue;
            board[i % 16] += 1;
        }
        return (i-1) % 16;
    }

    /**
     *
     * @param start the 0-based index of the cell where will would take the seeds
     * @param board the color of the seed to take
     * @return the last index of the board updated
     */
    private int playBlue(int start, int[] board){
        int i;
        int seeds = board[start];
        board[start] = 0;
        for (i = start + 1; i < start+1+seeds*2; i += 2){
            board[i % 16] += 1;
        }
        return (i-2) % 16;
    }

    /**
     *
     * @param start the 0-based index of the last hole where we placed a seed
     * @return the number of seeds captured by the player
     */
    private int capture(int start){
        int seeds;
        int capturedSeeds = 0;
        int index = start;
        do {
            seeds = redSeedBoard[index] + blueSeedBoard[index] + transparentSeedBoard[index];
            if (seeds == 2 || seeds == 3){
                capturedSeeds += seeds;
                redSeedBoard[index] = 0;
                blueSeedBoard[index] = 0;
                transparentSeedBoard[index] = 0;
            }
            index--;
            if(index == -1) index = 15;
        }while(seeds == 2 || seeds == 3);
        return capturedSeeds;
    }

    /**
     * @param player the 0-based player number (0 for the first and 1 for the second)
     * @return the player who win the game. If it is a draw return 0 otherwise return -1
     */
    private int checkVictory(int player){
        int[] nextPlayer = {1, 0};
        //TODO when do we have a draw
        if(playerSeeds[0] >= 40 && playerSeeds[1] >= 40)
            return 0;
        if(playerSeeds[0] >= 40)
            return 1;
        else if (playerSeeds[1] >= 40)
            return 2;
        return detectHungriness(nextPlayer[player]);
    }


    /**
     * @param player the 0-based player number (0 for the first and 1 for the second)
     * Detect if one of the player is hungry return -1 if none are hungry and the player number otherwise
     */
    private int detectHungriness(int player){
        int playerSeeds = 0;
        for (int i=player; i<16; i=i+2){
            playerSeeds += redSeedBoard[i] + blueSeedBoard[i] + transparentSeedBoard[i];
        }
        if(playerSeeds == 0) return (player + 1);
        return -1;
    }

    public int[] getPlayerSeeds() {
        return playerSeeds;
    }

    public void setPlayerSeeds(int[] playerSeeds) {
        this.playerSeeds = playerSeeds;
    }

    public int[] getRedSeedBoard() {
        return redSeedBoard;
    }

    public void setRedSeedBoard(int[] redSeedBoard) {
        this.redSeedBoard = redSeedBoard;
    }

    public int[] getBlueSeedBoard() {
        return blueSeedBoard;
    }

    public void setBlueSeedBoard(int[] blueSeedBoard) {
        this.blueSeedBoard = blueSeedBoard;
    }

    public int[] getTransparentSeedBoard() {
        return transparentSeedBoard;
    }

    public void setTransparentSeedBoard(int[] transparentSeedBoard) {
        this.transparentSeedBoard = transparentSeedBoard;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  1   \t  2   \t  3   \t  4   \t  5   \t  6   \t  7   \t  8   \n");
        for(int i=0; i<8; i++){
            sb.append(redSeedBoard[i]).append("R").append(blueSeedBoard[i]).append("B").append(transparentSeedBoard[i]).append("T\t");
        }
        sb.append("\n");
        for(int i=15; i>7; i--){
            sb.append(redSeedBoard[i]).append("R").append(blueSeedBoard[i]).append("B").append(transparentSeedBoard[i]).append("T\t");
        }
        sb.append("\n").append("  16  \t  15  \t  14  \t  13  \t  12  \t  11  \t  10  \t  9   \n");
        sb.append("Seeds Captured by Player 1: ").append(playerSeeds[0]).append("\n");
        sb.append("Seeds Captured by Player 2: ").append(playerSeeds[1]).append("\n");
        return sb.toString();
    }

    @Override
    public Board clone() {
        try {
            Board board = (Board) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            board.setPlayerSeeds(playerSeeds.clone());
            board.setBlueSeedBoard(blueSeedBoard.clone());
            board.setRedSeedBoard(redSeedBoard.clone());
            board.setTransparentSeedBoard(transparentSeedBoard.clone());
            board.setTurn(turn);
            //board.getMoveHistory().addAll(moveHistory);
            return board;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
