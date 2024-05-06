import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class HighScoresScreen extends JFrame {
    private static final long serialVersionUID = -998294357010273359L;

	public HighScoresScreen() {
        setTitle("High Scores");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Set up the background panel
        ImageIcon background = new ImageIcon("assets/background.jpg");
        BackgroundPanel backgroundPanel = new BackgroundPanel(background.getImage());
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS)); // Use BoxLayout for vertical alignment

        // Title label
        backgroundPanel.add(Box.createVerticalStrut(20));
        JLabel titleLabel = new JLabel("High Scores", JLabel.CENTER);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        backgroundPanel.add(titleLabel);
        backgroundPanel.add(Box.createVerticalStrut(20));

        // Table header label
        JLabel tableHeaderLabel = new JLabel("Rank | Name | Score | DD-MM", JLabel.CENTER);
        tableHeaderLabel.setFont(new Font("Monospaced", Font.PLAIN, 24));
        tableHeaderLabel.setForeground(Color.WHITE);
        tableHeaderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tableHeaderLabel.setBackground(new Color(255, 255, 255, 70));
        backgroundPanel.add(tableHeaderLabel);

        // Add high score labels
        ArrayList<NameScoreDateTime> highScoreTableArray = LoginPage.scoreFileSystem.getHighScoreTable();
        String format = " %-2d - %s ";
        for (int i = 1; i <= 10; i++) {
            String scoreText;
            if (i <= highScoreTableArray.size()) {
                scoreText = String.format(format, i, highScoreTableArray.get(i - 1));
            } else {
                scoreText = "";
            }
            JLabel highScoreLabel = new JLabel(scoreText, JLabel.CENTER);
            highScoreLabel.setFont(new Font("Monospaced", Font.BOLD, 24));
            highScoreLabel.setForeground(Color.WHITE);
            highScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Align center for BoxLayout
            highScoreLabel.setOpaque(true);
            highScoreLabel.setBackground(getBackgroundColorForRank(i));
            backgroundPanel.add(highScoreLabel);
        }

        // Adding the panel to the frame
        setContentPane(backgroundPanel);
        setVisible(true);
    }

    private Color getBackgroundColorForRank(int rank) {
        switch (rank) {
            case 1:  // Gold
                return new Color(255, 215, 0, 255);
            case 2:  // Silver
                return new Color(192, 192, 192, 255);
            case 3:  // Bronze
                return new Color(205, 127, 50, 255);
            default:  // White for other ranks
                return new Color(255, 255, 255, 70);
        }
    }
}