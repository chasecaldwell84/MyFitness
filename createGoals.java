package MyFitness;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class createGoals {

    private static Goal currentGoal;
    private static JLabel goalStatusLabel;
    private static JFrame frame;

    public static void main(String[] args) {
        createGUI();
    }
    public static void createGUI() {
        frame = new JFrame("Goal Creator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        //Set up title
        JLabel title = new JLabel("Goal Creator", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.insets = new Insets(10, 10, 10, 10);
        frame.add(title, c);

        //Create panel for goal setting
        JPanel goalPanel = new JPanel();
        goalPanel.setLayout(new FlowLayout());

        JLabel goalLengthLabel = new JLabel("Select Goal Length (Day, Week, Month): ");
        String[] goalLengths = { "Day", "Week", "Month" };
        JComboBox<String> goalLengthComboBox = new JComboBox<>(goalLengths);

        JLabel goalTypeLabel = new JLabel("Select Goal Type (Calories, Weight, Sleep): ");
        String[] goalTypes = { "Calories", "Weight", "Sleep" };
        JComboBox<String> goalTypeComboBox = new JComboBox<>(goalTypes);

        JLabel goalValueLabel = new JLabel("Enter Goal Value: ");
        JTextField goalValueTextField = new JTextField(10);

        JButton setGoalButton = new JButton("Set Goal");
        setGoalButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    String selectedGoalLength = (String) goalLengthComboBox.getSelectedItem();
                    String selectedGoalType = (String) goalTypeComboBox.getSelectedItem();
                    int goalValue = Integer.parseInt(goalValueTextField.getText());

                    if(goalValue <= 0){
                        JOptionPane.showMessageDialog(frame, "Goal Value must be greater than zero");
                    } else{
                        currentGoal = new Goal(selectedGoalLength, selectedGoalType, goalValue);
                        System.out.println(currentGoal);
                        updateGoalStatus(frame);
                    }
                } catch (NumberFormatException ex){
                    JOptionPane.showMessageDialog(frame, "Please enter a valid number for the goal value.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        goalPanel.add(goalLengthLabel);
        goalPanel.add(goalLengthComboBox);
        goalPanel.add(goalTypeLabel);
        goalPanel.add(goalTypeComboBox);
        goalPanel.add(goalValueLabel);
        goalPanel.add(goalValueTextField);
        goalPanel.add(setGoalButton);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        frame.add(goalPanel, c);

        //create goal status
        goalStatusLabel = new JLabel("No Goal Set Yet");
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.insets = new Insets(10, 10, 10, 10);
        frame.add(goalStatusLabel, c);

        //create exit button
        JButton exitButton = new JButton("Exit Goal Creator");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                System.exit(0);
            }
        });

        JPanel exitPanel = new JPanel();
        exitPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        exitPanel.add(exitButton);

        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        //c.anchor = GridBagConstraints.SOUTHWEST;
        //c.weightx = 1;
        //c.weighty = 1;
        frame.add(exitPanel, c);

        frame.setSize(500, 500);
        frame.setVisible(true);
    }

    private static void updateGoalStatus(JFrame frame) {
        if(currentGoal != null){
            goalStatusLabel.setText("Current Goal: " + currentGoal);
        }
        frame.revalidate();
        frame.repaint();
    }
}
