import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScoreFileSystem {
    private HashMap<String, Integer> scoresMap = new HashMap<>();
    private final String FILE_NAME = "data/scores.txt";

    public ScoreFileSystem() {
        try {
            File myObj = new File(FILE_NAME);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        loadScores();
    }

    private void loadScores() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String username = parts[0];
                    int highestScore = Integer.parseInt(parts[1]);
                    scoresMap.put(username, highestScore);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveScores() {
        if (scoresMap.size() >= 10) {
            ArrayList<NameScoreDateTime> highestScores = getHighScoreTable();
            NameScoreDateTime key = highestScores.get(highestScores.size() - 1);
            scoresMap.remove(key.getDateTime() + key.getName());
        }
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            for (Map.Entry<String, Integer> entry : scoresMap.entrySet()) {
                String username = entry.getKey();
                int highestScore = entry.getValue();
                writer.write(username + "," + highestScore + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getUserScore(String username) {
        int max = 0;
        for (Map.Entry<String, Integer> entry : scoresMap.entrySet()) {
            String keyUsername = entry.getKey().substring(19);
            Integer value = entry.getValue();
            if (username.equals(keyUsername)) {
                if (value > max) {
                    max = value;
                }
            }
        }
        return max;
    }

    public void saveUserScore() {
        int newScore = LoginPage.player.getScore();
        scoresMap.put(dateAndTime() + LoginPage.player.getUsername(), newScore);
        saveScores();
    }

    public ArrayList<NameScoreDateTime> getHighScoreTable() {
        ArrayList<NameScoreDateTime> highScoreTable = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : scoresMap.entrySet()) {
            String dateTime = entry.getKey().substring(0, 19);
            String name = entry.getKey().substring(19);
            Integer score = entry.getValue();
            NameScoreDateTime nameScoreDate = new NameScoreDateTime(name, score, dateTime);
            highScoreTable.add(nameScoreDate);
        }

        highScoreTable.sort(new Comparator<NameScoreDateTime>() {
            @Override
            public int compare(NameScoreDateTime ns1, NameScoreDateTime ns2) {
                return Integer.compare(ns2.getScore(), ns1.getScore());
            }
        });

        return highScoreTable;
    }

    public static String dateAndTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return now.format(formatter);
    }
}

