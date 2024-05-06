import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class MemoryGameLevelScreen extends JFrame {
    private static final long serialVersionUID = 8005734854621841589L;
    
	private static final int GRID_SIZE = 4;
    private static final int NUM_PAIRS = 8;
    private static final int[] MAX_TRIES = {18, 15, 12};
    private static final int[] DELAY_TIME = {1000, 700, 400};
    private static final int TOTAL_LEVELS = 3;
    private final int currentLevel;
    private final JFrame previousWindow;

    private final JLabel triesLabel;
    private final JButton[] cards;
    private final ImageIcon questionIcon;
    private final ImageIcon[] pairIcons;
    private boolean[] cardFlipped;
    private boolean[] cardMatched;

    private int triesLeft;
    private int firstSelected = -1;
    private int secondSelected = -1;
    private List<ImageIcon> cardIcons;

    public MemoryGameLevelScreen(int level, JFrame previousWindow) {
        this.currentLevel = level;
        this.previousWindow = previousWindow;
        setTitle("Memory Card Game");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        triesLeft = MAX_TRIES[currentLevel - 1];
        questionIcon = new ImageIcon("assets/lvl" + level + "/no_image.png");
        pairIcons = loadPairIcons(currentLevel);
        cardMatched = new boolean[GRID_SIZE * GRID_SIZE];
        Arrays.fill(cardMatched, Boolean.FALSE);

        cardIcons = new ArrayList<>();
        for (int i = 0; i < NUM_PAIRS; i++) {
            cardIcons.add(pairIcons[i]);
            cardIcons.add(pairIcons[i]);
        }
        shuffleCards();

        // Set up the menu bar
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(Box.createHorizontalGlue());

        JMenu gameMenu = new JMenu("Game");
        JMenuItem restartItem = new JMenuItem("Restart");
        JMenuItem highScoresItem = new JMenuItem("High Scores");
        gameMenu.add(restartItem);
        gameMenu.add(highScoresItem);

        JMenu aboutMenu = new JMenu("About");
        JMenuItem aboutDeveloperItem = new JMenuItem("About the Developer");
        JMenuItem aboutGameItem = new JMenuItem("About the Game");
        aboutMenu.add(aboutDeveloperItem);
        aboutMenu.add(aboutGameItem);

        JMenu exitMenu = new JMenu("Exit");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitMenu.add(exitItem);

        menuBar.add(gameMenu);
        menuBar.add(aboutMenu);
        menuBar.add(exitMenu);
        menuBar.add(Box.createHorizontalGlue());
        
        // Add action listeners for menu items
        restartItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MemoryGameLevelScreen(currentLevel, MemoryGameLevelScreen.this.previousWindow);
                dispose();
            }
        });

        highScoresItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new HighScoresScreen();
            }
        });

        aboutDeveloperItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MemoryGameLevelScreen.this,
                        "Emirhan Tala, \n20210702012",
                        "About the Developer", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        aboutGameItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MemoryGameLevelScreen.this
                		, "This game was developed as a CSE 212 term project.\n"
                		+ "The goal is to match pairs of cards by flipping them over.\n"
                		+ "There are total of 3 levels.\n"
                		+ "Have fun!",
                        "About the Game", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MemoryGameMenu(previousWindow);
                dispose();
            }
        });

        setJMenuBar(menuBar);

        // Top panel with level and tries
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topPanel.setBackground(getLevelColor());
        
        Font labelFont = new Font("Monospaced", Font.BOLD, 16);

        JLabel levelLabel = new JLabel("Level " + currentLevel);
        levelLabel.setForeground(Color.WHITE);
        levelLabel.setFont(labelFont);
        topPanel.add(levelLabel);
        
        topPanel.add(Box.createHorizontalStrut(30));
        
        triesLabel = new JLabel("Tries left: " + triesLeft);
        triesLabel.setForeground(Color.WHITE);
        triesLabel.setFont(labelFont);
        topPanel.add(triesLabel);
        add(topPanel, BorderLayout.NORTH);
        
        UIManager.put("OptionPane.background", getLevelColor());
        UIManager.put("Panel.background", getLevelColor());

        // Center grid for cards
        JPanel gridPanel = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE, 5, 5));
        cards = new JButton[GRID_SIZE * GRID_SIZE];
        cardFlipped = new boolean[GRID_SIZE * GRID_SIZE];

        for (int i = 0; i < cards.length; i++) {
            JButton cardButton = new JButton(questionIcon);
            cardButton.setActionCommand(String.valueOf(i));
            final int index = i;

            cardButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    handleCardFlip(index, cardIcons.get(index));
                }
            });

            cards[i] = cardButton;
            gridPanel.add(cardButton);
        }

        add(gridPanel, BorderLayout.CENTER);
        setMinimumSize(new Dimension(600, 600));
        setVisible(true);
        previousWindow.dispose(); // Dispose the previous window
        pack();
    }

    private void handleCardFlip(int index, ImageIcon cardIcon) {
        if (cardFlipped[index] || secondSelected != -1) {
            return; // Already flipped or two cards are already selected
        }

        // Flip the card
        cards[index].setIcon(cardIcon);
        cardFlipped[index] = true;

        if (firstSelected == -1) {
            firstSelected = index;
        } else {
            secondSelected = index;
            if (cards[firstSelected].getIcon().equals(cards[secondSelected].getIcon())) {
                LoginPage.soundManager.match();
            } else {
                LoginPage.soundManager.wrongChoice();
            }

            Timer timer = new Timer(DELAY_TIME[currentLevel - 1], new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    checkForMatch();
                }
            });
            timer.setRepeats(false);
            timer.start();
        }
    }

    private void checkForMatch() {
        if (firstSelected != -1 && secondSelected != -1) {
            if (!cards[firstSelected].getIcon().equals(cards[secondSelected].getIcon())) {
                // No match, flip back
                cards[firstSelected].setIcon(questionIcon);
                cards[secondSelected].setIcon(questionIcon);
                cardFlipped[firstSelected] = false;
                cardFlipped[secondSelected] = false;
                triesLeft--;
                triesLabel.setText("Tries left: " + triesLeft);

                if (currentLevel == 3) {
                    shuffleCards();
                    refreshDisplay();
                }

                switch (currentLevel) {
                    case 1:
                        LoginPage.player.changeScore(-1);
                        break;
                    case 2:
                        LoginPage.player.changeScore(-2);
                        break;
                    case 3:
                        LoginPage.player.changeScore(-3);
                        break;
                }

                if (triesLeft == 0) {
                    handleLoss();
                }
            } else {
                cardMatched[firstSelected] = true;
                cardMatched[secondSelected] = true;

                switch (currentLevel) {
                    case 1:
                        LoginPage.player.changeScore(5);
                        break;
                    case 2:
                        LoginPage.player.changeScore(4);
                        break;
                    case 3:
                        LoginPage.player.changeScore(3);
                        break;
                }


                if (checkAllMatched()) {
                    handleWin();
                }
            }
            firstSelected = -1;
            secondSelected = -1;
        }
    }

    private void shuffleCards() {
        final int shuffleCount = 30; // Number of visual shuffles
        final int shuffleDelay = 20; // Delay between shuffles in milliseconds

        LoginPage.soundManager.shuffling();

        // Start background thread to shuffle only unmatched cards
        Thread shuffleThread = new Thread(new Runnable() {
            public void run() {
                List<Integer> unmatchedIndices = new ArrayList<>();
                for (int i = 0; i < cardMatched.length; i++) {
                    if (!cardMatched[i]) {
                        unmatchedIndices.add(i);
                    }
                }

                Collections.shuffle(unmatchedIndices);
                List<ImageIcon> tempIcons = new ArrayList<>();
                for (int index : unmatchedIndices) {
                    tempIcons.add(cardIcons.get(index));
                }
                Collections.shuffle(tempIcons);

                for (int i = 0; i < unmatchedIndices.size(); i++) {
                    cardIcons.set(unmatchedIndices.get(i), tempIcons.get(i));
                }
            }
        });
        shuffleThread.start();

        // Shuffle animation
        ActionListener shuffleAction = new ActionListener() {
            private int count = 0;

            @Override
            public void actionPerformed(ActionEvent evt) {
                if (count < shuffleCount) {
                    int index1, index2;
                    do {
                        index1 = (int) (Math.random() * cardIcons.size());
                        index2 = (int) (Math.random() * cardIcons.size());
                    } while (cardMatched[index1] || cardMatched[index2]); // Ensure selected indices are not matched

                    Collections.swap(cardIcons, index1, index2);
                    cards[index1].setIcon(cardIcons.get(index1));
                    cards[index2].setIcon(cardIcons.get(index2));
                    count++;
                } else {
                    ((Timer) evt.getSource()).stop();
                    new Timer(DELAY_TIME[currentLevel - 1], new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            // After showing all cards briefly, turn down only unmatched cards
                            for (int i = 0; i < cards.length; i++) {
                                if (!cardMatched[i]) {
                                    cards[i].setIcon(questionIcon);
                                }
                            }
                            ((Timer) e.getSource()).stop();
                        }
                    }).start();
                }
            }
        };

        Timer shuffleTimer = new Timer(shuffleDelay, shuffleAction);
        shuffleTimer.start();

        try {
            shuffleThread.join(); // Ensure that the shuffle logic completes
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void refreshDisplay() {
        for (int i = 0; i < cards.length; i++) {
            if (!cardMatched[i]) {
                cards[i].setIcon(questionIcon);
                cards[i].setActionCommand(String.valueOf(i));
                cardFlipped[i] = false;
            }
        }
    }


    private boolean checkAllMatched() {
        for (boolean flipped : cardFlipped) {
            if (!flipped) {
                return false;
            }
        }
        return true;
    }

    private void handleWin() {
        LoginPage.soundManager.levelPass();
        int response;
        if (currentLevel < TOTAL_LEVELS) {
            response = JOptionPane.showConfirmDialog(this, "You won Level " + currentLevel + "! Your current score is:"  + LoginPage.player.getScore() +"\nProceed to Level " + (currentLevel + 1) + "?", "You Win!", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                new MemoryGameLevelScreen(currentLevel + 1, this);
                dispose();
            } else {
                new MemoryGameMenu(this);
                dispose();
            }
        } else {
            JOptionPane.showMessageDialog(this, "You won the game! Congratulations!\nYour final score is: " + LoginPage.player.getScore(), "Game Over", JOptionPane.INFORMATION_MESSAGE);
            LoginPage.scoreFileSystem.saveUserScore();
            new MemoryGameMenu(this);
            dispose();
        }
    }

    private void handleLoss() {
        LoginPage.soundManager.levelLose();
        JOptionPane.showMessageDialog(this, "You lost! Returning to Level 1.", "You Lose", JOptionPane.INFORMATION_MESSAGE);
        new MemoryGameLevelScreen(1, this);
        dispose();
    }

    private ImageIcon[] loadPairIcons(int level) {
        ImageIcon[] icons = new ImageIcon[NUM_PAIRS];
        for (int i = 0; i < NUM_PAIRS; i++) {
            icons[i] = new ImageIcon("assets/lvl" + level + "/" + i + ".png");
        }
        return icons;
    }
    private Color getLevelColor() {
    	switch (currentLevel) {
        case 1:
        	return new Color(70, 130, 180);
        case 2:
        	return new Color(150, 120, 180);
        case 3:
        	return new Color(220, 20, 60);
        default:
        	return new Color(255, 255, 255);
        }
    }
}