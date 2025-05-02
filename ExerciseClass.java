package MyFitness;

import MyFitness.RyanStuff.ExercisePlan;

import javax.swing.*;
import java.awt.*;

public class ExerciseClass extends ExercisePlan {
    private JTextField userName;
    private JComboBox<String> classComboBox;
    private JButton addButton;

    public ExerciseClass() {
        /*setTitle("Add User to Workout Class");*/
        setSize(1200, 800);
        /*setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);*/
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Add User to Class", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(titleLabel, gbc);

        JLabel userIdLabel = new JLabel("User Name:");
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(userIdLabel, gbc);

        userName = new JTextField();
        gbc.gridx = 1;
        add(userName, gbc);

        JLabel classLabel = new JLabel("My Workout Classes:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(classLabel, gbc);

        classComboBox = new JComboBox<>(new String[] {
                "Class 1",
                "Class 2",
                "Class 3",
        });
        gbc.gridx = 1;
        add(classComboBox, gbc);

        addButton = new JButton("Add User to Class");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(addButton, gbc);

        addButton.addActionListener(e -> addUserToClass());
    }

    private void addUserToClass() {
        String userId = userName.getText();
        String selectedClass = (String) classComboBox.getSelectedItem();

        if (userId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid User Name.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Simulate backend logic
        JOptionPane.showMessageDialog(this,
                "User '" + userId + "' successfully added to '" + selectedClass + "'.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

        userName.setText("");
    }

    /*public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ExerciseClass().setVisible(true);
        });
    }*/
}

