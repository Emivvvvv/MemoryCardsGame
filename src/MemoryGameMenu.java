import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MemoryGameMenu extends JFrame {
    private static final long serialVersionUID = 2824834227961195428L;

	public MemoryGameMenu() {
        setTitle("Memory Cards Game");
        setSize(800, 600);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load the background image
        ImageIcon background = new ImageIcon("assets/background.jpg");
        BackgroundPanel backgroundPanel = new BackgroundPanel(background.getImage());

        // Center-align all components
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS));
        
        // Set up the menu bar
        JMenuBar menuBar = new JMenuBar();
        
        JMenu highScoresMenu = new JMenu("High Scores");
        JMenuItem highScoresItem = new JMenuItem("High Scores");
        highScoresMenu.add(highScoresItem);

        JMenu aboutMenu = new JMenu("About");
        JMenuItem aboutDeveloperItem = new JMenuItem("About the Developer");
        JMenuItem aboutGameItem = new JMenuItem("About the Game");
        aboutMenu.add(aboutDeveloperItem);
        aboutMenu.add(aboutGameItem);

        menuBar.add(highScoresMenu);
        menuBar.add(aboutMenu);

        // Add action listeners for menu items
        highScoresItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new HighScoresScreen();
            }
        });

        aboutDeveloperItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "Emirhan Tala, \n20210702012",
                        "About the Developer", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        aboutGameItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null
                		, "This game was developed as a CSE 212 term project.\n"
                		+ "The goal is to match pairs of cards by flipping them over.\n"
                		+ "There are total of 3 levels.\n"
                		+ "Have fun!",
                        "About the Game", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        setJMenuBar(menuBar);

        // Welcome Label
        JLabel welcomeLabel = new JLabel("Welcome, " + LoginPage.player.getUsername() + "!");
        welcomeLabel.setFont(new Font("Monospaced", Font.PLAIN, 18));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setForeground(Color.LIGHT_GRAY);

        // High Score Label
        JLabel highScoreLabel = new JLabel("Your current highest score is: " + LoginPage.player.getUserHighestScore());
        highScoreLabel.setFont(new Font("Monospaced", Font.PLAIN, 12));
        highScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        highScoreLabel.setForeground(Color.LIGHT_GRAY);

        // Title Label
        JLabel titleLabel = new JLabel("Memory Card Game");
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 48));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(Color.WHITE);

        // Standard size for buttons
        Dimension buttonSize = new Dimension(200, 40);

        // Buttons
        JButton startButton = new JButton("Start Game");
        JButton selectLevelButton = new JButton("Select Level");
        JButton instructionsButton = new JButton("Instructions");
        JButton exitButton = new JButton("Exit");

        startButton.setPreferredSize(buttonSize);
        selectLevelButton.setPreferredSize(buttonSize);
        instructionsButton.setPreferredSize(buttonSize);
        exitButton.setPreferredSize(buttonSize);

        startButton.setMaximumSize(buttonSize);
        selectLevelButton.setMaximumSize(buttonSize);
        instructionsButton.setMaximumSize(buttonSize);
        exitButton.setMaximumSize(buttonSize);

        // Center-align buttons
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectLevelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructionsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add action listeners
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new MemoryGameLevelScreen(1, MemoryGameMenu.this);
                dispose();
            }
        });

        selectLevelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showSelectLevelDialog();
            }
        });


        instructionsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null
                		, "Instructions:\n"
                		+ "There are 3 levels in game. It gets gradually harder!\n"
                		+ "Match all pairs of cars to win!");
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Add components with spacing
        backgroundPanel.add(Box.createVerticalGlue());
        backgroundPanel.add(welcomeLabel);
        backgroundPanel.add(highScoreLabel);
        backgroundPanel.add(titleLabel);
        backgroundPanel.add(Box.createVerticalStrut(20));
        backgroundPanel.add(startButton);
        backgroundPanel.add(Box.createVerticalStrut(10));
        backgroundPanel.add(selectLevelButton);
        backgroundPanel.add(Box.createVerticalStrut(10));
        backgroundPanel.add(instructionsButton);
        backgroundPanel.add(Box.createVerticalStrut(10));
        backgroundPanel.add(exitButton);
        backgroundPanel.add(Box.createVerticalGlue());

        setContentPane(backgroundPanel);
        pack();
        setVisible(true);
    }

    public MemoryGameMenu(JFrame previousWindow) {
        this();
        previousWindow.dispose(); // Close the previous window
    }

    private void showSelectLevelDialog() {
        JDialog dialog = new JDialog(this, "Select Level", true);
        dialog.setLayout(new FlowLayout());

        JLabel label = new JLabel("Select a level to play:");
        JButton level1Button = new JButton("Level 1 - Easy");
        JButton level2Button = new JButton("Level 2 - Medium");
        JButton level3Button = new JButton("Level 3 - Hard");

        level1Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new MemoryGameLevelScreen(1, MemoryGameMenu.this);
                dialog.dispose();
                dispose();
            }
        });

        level2Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new MemoryGameLevelScreen(2, MemoryGameMenu.this);
                dialog.dispose();
                dispose();
            }
        });

        level3Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new MemoryGameLevelScreen(3, MemoryGameMenu.this);
                dialog.dispose();
                dispose();
            }
        });

        dialog.add(label);
        dialog.add(level1Button);
        dialog.add(level2Button);
        dialog.add(level3Button);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}

class BackgroundPanel extends JPanel {
    private static final long serialVersionUID = -1119860028052009357L;
	private final Image backgroundImage;

    public BackgroundPanel(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}