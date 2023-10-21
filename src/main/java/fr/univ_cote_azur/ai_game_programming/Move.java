package fr.univ_cote_azur.ai_game_programming;

public class Move {

    private int idHole;
    private Color color;
    private int scoreEvalutation;

    private Hole[] holes;

    public Move(int idHole, Color color) {
        this.idHole = idHole;
        this.color = color;
        scoreEvalutation = 0;
    }

    public Move(int idHole, Color color, Hole[] holes) {
        this.idHole = idHole;
        this.color = color;
        scoreEvalutation = 0;
        this.holes = holes;
    }

    public int getIdHole() {
        return idHole;
    }

    public Color getColor() {
        return color;
    }

    public int getScoreEvalutation() {
        return scoreEvalutation;
    }

    public void addEvaluationScore(int valueMoov) {
        this.scoreEvalutation += valueMoov;
    }

    public void minusEvaluationScore(int valueMoov) {
        this.scoreEvalutation -= valueMoov;
    }

    public void setScoreEvalutation(int scoreEvalutation) {
        this.scoreEvalutation = scoreEvalutation;
    }

    public Hole[] getHoles() {
        return holes;
    }
}
