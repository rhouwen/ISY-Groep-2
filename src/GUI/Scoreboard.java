package GUI;

public class Scoreboard {
    private int playerScore;
    private int aiScore;

    public Scoreboard() {
        playerScore = 0;
        aiScore = 0;
    }

    public void incrementPlayerScore() {
        playerScore++;
    }

    public void incrementAIScore() {
        aiScore++;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public int getAIScore() {
        return aiScore;
    }

    @Override
    public String toString() {
        return "Score - Speler: " + playerScore + " | AI: " + aiScore;
    }
}
