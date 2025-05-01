//Calorie Tracker Use Case
//Author: Ryan Meador

package MyFitness.RyanStuff;

import MyFitness.App;
import MyFitness.Database;
import MyFitness.User.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CalorieTracker extends JPanel{

    private static CalorieReport calorieReport;
    private static JLabel totalCaloriesLabel;
    private static JLabel remainingCaloriesLabel;
    private static JPanel reportPanel;
    //private static JLabel dailyGoalLabel;

    public CalorieTracker(App frame){
        frame.setTitle("Calorie Tracker");
        setLayout(new GridBagLayout());
        setSize(500, 500);

        int dailyCalorieGoal = getCalorieGoalForUser(frame.getUser());
        calorieReport = new CalorieReport(dailyCalorieGoal); //change with goals
        createGUI();
    }

    public void createGUI() {
        GridBagConstraints c = new GridBagConstraints();

        //Set up Title
        JLabel title = new JLabel("Calorie Tracker", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.insets = new Insets(10, 10, 10, 10);
        add(title, c);

        //create place to enter calories
        JPanel calorieInput = new JPanel();
        calorieInput.setLayout(new FlowLayout());

        JLabel calorieLabel = new JLabel("Enter Calories Consumed: ");
        JTextField caloriesField = new JTextField(10);
        JButton calorieButton = new JButton("Submit");

        calorieButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    int inputCals = Integer.parseInt(caloriesField.getText());
                    if(inputCals <= 0){
                        JOptionPane.showMessageDialog(CalorieTracker.this, "Please enter a positive number!");
                    } else{
                        calorieReport.addCalories(inputCals);
                        updateCalorieReport();
                    }
                } catch (NumberFormatException ex){
                    JOptionPane.showMessageDialog(CalorieTracker.this, "Please enter a valid number!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        calorieInput.add(calorieLabel);
        calorieInput.add(caloriesField);
        calorieInput.add(calorieButton);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.insets = new Insets(5, 10, 5, 10);
        add(calorieInput, c);

        //Create Exit Calorie Tracker button that shuts program down
        //NOTE do not need Exit button
        /*JButton exitButton = new JButton("Exit Calorie Tracker");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        JPanel exitButtonPanel = new JPanel();
        exitButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        exitButtonPanel.add(exitButton);

        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.SOUTHWEST;
        c.weightx = 1;
        c.weighty = 1;
        add(exitButtonPanel, c);*/

        //call function to make calorie report display
        createCalorieReport();

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.insets = new Insets(10, 10, 10, 10);
        c.anchor  = GridBagConstraints.CENTER;
        add(reportPanel, c);

        //make frame size and visible
        revalidate();
        repaint();
    }

    //function to update the calorie report
    private void updateCalorieReport() {
        totalCaloriesLabel.setText("Total Calories: " + calorieReport.getTotalCalories());
        remainingCaloriesLabel.setText("Calories Remaining: " + (Math.max(calorieReport.getRemainingCalories(), 0)));
        reportPanel.setVisible(true);
    }

    //function to create the calorie report display
    public void createCalorieReport() {
        reportPanel = new JPanel();
        reportPanel.setLayout(new BoxLayout(reportPanel, BoxLayout.Y_AXIS));

        JLabel reportHeader = new JLabel("Calorie Report");
        reportHeader.setFont(new Font("Arial", Font.BOLD, 20));

        totalCaloriesLabel = new JLabel("Total Calories: ");
        remainingCaloriesLabel = new JLabel("Calories Remaining: " + calorieReport.getDailyGoal());

        reportPanel.add(reportHeader);
        reportPanel.add(totalCaloriesLabel);
        reportPanel.add(remainingCaloriesLabel);

        reportPanel.setVisible(false);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        add(reportPanel, c);
    }

    private int getCalorieGoalForUser(User user){
        List<Goal> goals = Database.getInstance().findGoalsByUser(user);
        for(Goal goal : goals){
            if(goal.getGoalType().equalsIgnoreCase("Calories")){
                return goal.getGoalValue();
            }
        }
        return 2000;
    }

}
