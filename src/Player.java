public class Player {
    private final String username;
    private int score;
    private int userHighestScore = 0;

    public Player(String username, int highestScore) {
        this.username = username;
        this.userHighestScore = highestScore;
        this.score = 0;
    }

    public int getUserHighestScore() {
        return userHighestScore;
    }

    public void setUserHighestScore(int userHighestScore) {
        this.userHighestScore = userHighestScore;
    }

    public int getScore() {
        return score;
    }

    public void changeScore(int changeValue) {
        setScore(getScore() + changeValue);
    }

    public void setScore(int score) {
        if (score > this.userHighestScore) {
            setUserHighestScore(score);
        }
        this.score = score;
    }

    public String getUsername() {
        return username;
    }
}
