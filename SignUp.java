package MyFitness;

import javax.swing.*;
//import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.List;

public class SignUp extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField, passwordDoubleField;
    private JButton submitButton;

    public SignUp() {
        setTitle("Sign-Up");
        setSize(350, 250);
        setLayout(null);
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

        submitButton = new JButton("Sign Up");
        submitButton.setBounds(120, 150, 100, 30);
        submitButton.addActionListener(e -> handleSignUp());
        add(submitButton);
    }

    private void handleSignUp() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String passwordDouble = new String(passwordDoubleField.getPassword());

        if (!password.equals(passwordDouble)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.");
            return;
        }

        if (isUsernameExists(username)) {
            JOptionPane.showMessageDialog(this, "Please change to another username.");
            return;
        }

        saveUser(username, password);
        JOptionPane.showMessageDialog(this, "Sign-up successful!");
        dispose();
    }

    private boolean isUsernameExists(String username) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("resources/UserAuth.csv"));
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

    private void saveUser(String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("resources/UserAuth.csv", true))) {
            writer.write(username + "," + password);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
