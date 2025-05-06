package MyFitness.LoginSignUp;

import MyFitness.Database;
import MyFitness.User.GeneralUser;
import MyFitness.User.Trainer;

import javax.swing.*;
import java.io.*;
import java.nio.file.*;
import java.util.List;

public class SignUp extends JDialog {
    private Database db = Database.getInstance();
    private JTextField usernameField;
    private JPasswordField passwordField, passwordDoubleField;
    private JComboBox<String> roleSelector;
    private JButton submitButton;

    public SignUp() {
        setTitle("Sign-Up");
        setSize(350, 300);
        setLayout(null);
        setModal(true);
        setLocationRelativeTo(null);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(20, 20, 80, 25);
        add(usernameLabel);
        usernameField = new JTextField();
        usernameField.setBounds(120, 20, 180, 25);
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(20, 60, 80, 25);
        add(passwordLabel);
        passwordField = new JPasswordField();
        passwordField.setBounds(120, 60, 180, 25);
        add(passwordField);

        JLabel passwordDoubleLabel = new JLabel("Confirm Password:");
        passwordDoubleLabel.setBounds(20, 100, 120, 25);
        add(passwordDoubleLabel);
        passwordDoubleField = new JPasswordField();
        passwordDoubleField.setBounds(140, 100, 160, 25);
        add(passwordDoubleField);

        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setBounds(20, 140, 80, 25);
        add(roleLabel);
        roleSelector = new JComboBox<>(new String[]{"user", "trainer"});
        roleSelector.setBounds(120, 140, 180, 25);
        add(roleSelector);

        submitButton = new JButton("Sign Up");
        submitButton.setBounds(120, 190, 100, 30);
        submitButton.addActionListener(e -> handleSignUp());
        add(submitButton);
    }

    private void handleSignUp() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String passwordDouble = new String(passwordDoubleField.getPassword());
        String role = (String) roleSelector.getSelectedItem();

        if (username.isEmpty() || password.isEmpty() || passwordDouble.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid username or password.");
            return;
        }
        if (!password.equals(passwordDouble)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.");
            return;
        }
        if (db.findByUsername(username) != null) {
            JOptionPane.showMessageDialog(this, "Username already exists. Please choose another.");
            return;
        }

        // Save to Derby database
        if ("User".equals(role)) {
            db.saveUser(new GeneralUser(username, password));
        } else {
            db.saveUser(new Trainer(username, password));
        }

        JOptionPane.showMessageDialog(this, "Sign-up successful!");
        dispose();
    }


    private boolean isUsernameExists(String username) {
        try {
            List<String> lines;
            String operatingSystem = System.getProperty("os.name");
            if (operatingSystem.startsWith("Windows")) {
                lines = Files.readAllLines(Paths.get("./src/main/java/MyFitness/resources/UserAuth.csv"));
            } else {
                lines = Files.readAllLines(Paths.get("resources/UserAuth.csv"));
            }
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void saveUser(String username, String password, String type) {
        BufferedWriter writer = null;
        try {
            String operatingSystem = System.getProperty("os.name");
            Path filePath;

            if (operatingSystem.startsWith("Windows")) {
                filePath = Paths.get("./src/main/java/MyFitness/resources/UserAuth.csv");
            } else {
                filePath = Paths.get("resources/UserAuth.csv");
            }

            Files.createDirectories(filePath.getParent());

            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }

            writer = Files.newBufferedWriter(filePath, StandardOpenOption.APPEND);
            writer.write(username + "," + password + "," + type);
            writer.newLine();

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving user.");
        } finally {
            try {
                if (writer != null) writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
