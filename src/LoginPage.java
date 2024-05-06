import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class LoginPage extends JFrame {
    private static final long serialVersionUID = 6630766142782440931L;
    
	public static Player player;
    public static ScoreFileSystem scoreFileSystem;
    public static SoundManager soundManager;

    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final Map<String, String> userData = new HashMap<>();
    private static final String USERS_FILE = "data/users.txt";

    public LoginPage() {
        setTitle("Memory Cards Game - Login");
        setSize(400, 150);
        setMinimumSize(new Dimension(400, 150));
        setMaximumSize(new Dimension(400, 150));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        loadUserData();

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleRegister();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username or password can't be empty!", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (userData.containsKey(username) && userData.get(username).equals(password)) {
            JOptionPane.showMessageDialog(this, "Login successful!");

            scoreFileSystem = new ScoreFileSystem();

            login(username);

            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRegister() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username or password can't be empty!", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (userData.containsKey(username)) {
            JOptionPane.showMessageDialog(this, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (username.length() > 20) {
            JOptionPane.showMessageDialog(this, "Username can't have more than 20 characters!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            userData.put(username, password);
            saveUserData();
            JOptionPane.showMessageDialog(this, "Registration successful!");

            scoreFileSystem = new ScoreFileSystem();

            login(username);

            dispose();
        }
    }

    private void loadUserData() {
        try {
            File file = new File(USERS_FILE);
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(":");
                        if (parts.length == 2) {
                            userData.put(parts[0], parts[1]);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void login(String username) {
        soundManager = new SoundManager();

        int userHighestScore = scoreFileSystem.getUserScore(username);
        LoginPage.player = new Player(username, userHighestScore);

        soundManager.playBackgroundMusic();

        new MemoryGameMenu(this);
    }

    private void saveUserData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (Map.Entry<String, String> entry : userData.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
