import javax.sound.sampled.*;
import java.io.IOException;
import java.io.File;

public class SoundManager {
    private Clip backgroundClip;
    public String BACKGROUND = "resources/background.wav";
    public String SHUFFLING = "resources/shuffling.wav";
    public String MATCH = "resources/match.wav";
    public String WRONG_CHOICE = "resources/wrong_choice.wav";
    public String LEVEL_PASS = "resources/level_pass.wav";
    public String LEVEL_LOSE = "resources/level_lose.wav";
    public String NEW_MAX_SCORE = "resources/new_max_score.wav";

    public void match() {
        playSound(MATCH);
    }

    public void shuffling() {
        playSound(SHUFFLING);
    }

    public void wrongChoice() {
        playSound(WRONG_CHOICE);
    }

    public void levelPass() {
        playSound(LEVEL_PASS);
    }

    public void levelLose() {
        playSound(LEVEL_LOSE);
    }

    public void newMaxScore() {
        playSound(NEW_MAX_SCORE);
    }

    private void playSound(String soundFileName) {
        try {
            File soundFile = new File(soundFileName);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void playBackgroundMusic() {
        try {
            if (backgroundClip != null && backgroundClip.isRunning()) {
                backgroundClip.stop();  // Stop the current playing music if it's playing
            }
            File musicFile = new File(BACKGROUND);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(musicFile);
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audioIn);
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);  // Loop indefinitely
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
        }
    }
}
