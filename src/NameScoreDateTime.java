public class NameScoreDateTime {
    private final String name;
    private final int score;
    private final String dateTime;

    public NameScoreDateTime(String name, int score, String dateTime) {
        this.name = name;
        this.score = score;
        this.dateTime = dateTime;
    }
    public String getName() {
        return name;
    }
    public int getScore() {
        return score;
    }
    public String getDateTime() {
        return dateTime;
    }
    public String getDate() {
        return dateTime.substring(0, 5);
    }

    @Override
    public String toString() {
        String format = "%-20s %-3d %5s";
        return String.format(format, getName(), getScore(), getDate());
    }
}
