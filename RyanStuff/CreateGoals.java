package MyFitness.RyanStuff;

import MyFitness.App;
import MyFitness.Database;
import MyFitness.User.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CreateGoals extends JPanel {
    private Database db = Database.getInstance();
    private static Goal currentGoal;
    private static JLabel goalStatusLabel;
    private JTextArea allGoalsTextArea;
    private User currentUser;
    public CreateGoals(App frame) {
        frame.setTitle("Goal Creator");
        setLayout(new GridBagLayout());
        setSize(500, 500);
        currentUser = frame.getUser();
        createGUI();
    }

    public void createGUI() {
        GridBagConstraints c = new GridBagConstraints();

        //Set up title
        JLabel title = new JLabel("Goal Creator", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.insets = new Insets(10, 10, 10, 10);
        add(title, c);

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
                        JOptionPane.showMessageDialog(CreateGoals.this, "Goal Value must be greater than zero");
                    } else{
                        currentGoal = new Goal(selectedGoalLength, selectedGoalType, goalValue, currentUser);
                        /*NOTE: could update database here but i need to know the user so i can store it under so either the
                        *  CreateGoals page could know about the user or the user could have a list of goals that it stores
                        * */
                        db.saveGoal(currentUser, currentGoal);
                        updateGoalStatus();
                    }

                } catch (NumberFormatException ex){
                    JOptionPane.showMessageDialog(CreateGoals.this, "Please enter a valid number for the goal value.",
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
        add(goalPanel, c);

        //create goal status
        goalStatusLabel = new JLabel("No Goal Set Yet");
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.insets = new Insets(10, 10, 10, 10);
        add(goalStatusLabel, c);

        JButton showAllGoalsButton = new JButton("Show All Goals");
        showAllGoalsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayAllGoals();
            }
        });
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        add(showAllGoalsButton, c);

        allGoalsTextArea = new JTextArea(10, 30);
        allGoalsTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(allGoalsTextArea);

        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        add(scrollPane, c);

        //create exit button

        //NOTE do not need exitButton
        /*JButton exitButton = new JButton("Exit Goal Creator");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
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
        add(exitPanel, c);*/

        revalidate();
        repaint();
    }

    private void displayAllGoals() {
        List<Goal> goals = db.findGoalsByUser(currentUser);
        if(goals.isEmpty()){
            allGoalsTextArea.setText("No Goal Set Yet");
        } else{
            StringBuilder sb = new StringBuilder();
            for(Goal goal : goals){
                sb.append(goal).append("\n");
            }
            allGoalsTextArea.setText(sb.toString());
        }
    }

    private void updateGoalStatus() {
        if(currentGoal != null){
            goalStatusLabel.setText("Current Goal: " + currentGoal);
        }
    }
}
