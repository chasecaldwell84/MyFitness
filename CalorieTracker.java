//Calorie Tracker Use Case
//Author: Ryan Meador

package MyFitness;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalorieTracker {

    private static int caloriesConsumed = 0;
    //dailyGoal will be set somewhere else (probably Goal Setter)
    private static int dailyGoal = 2000;
    private static int totalCaloriesConsumed = 0;

    private static JLabel totalCaloriesLabel;
    private static JLabel remainingCaloriesLabel;
    //private static JLabel dailyGoalLabel;

    public static void main(String[] args) {
        createGUI();
    }

    public static void createGUI() {
        JFrame frame = new JFrame("Calorie Tracker");

        frame.setLayout(new BorderLayout());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Set up Title
        JLabel title = new JLabel("Calorie Tracker", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(title, BorderLayout.NORTH);

        //create place to enter calories
        JPanel calorieInput = new JPanel();
        calorieInput.setLayout(new FlowLayout());

        JLabel calorieLabel = new JLabel("Enter Calories Consumed: ");
        JTextField caloriesField = new JTextField(10);
        JButton calorieButton = new JButton("Submit");
        calorieButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    caloriesConsumed = Integer.parseInt(caloriesField.getText());
                    System.out.println("Calories Consumed: " + caloriesConsumed);
                    updateCalorieReport();
                } catch (NumberFormatException ex){
                    JOptionPane.showMessageDialog(null, "Please enter a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        calorieInput.add(calorieLabel);
        calorieInput.add(caloriesField);
        calorieInput.add(calorieButton);
        frame.add(calorieInput, BorderLayout.NORTH);

        //call function to make calorie report display
        createCalorieReport(frame);

        //Create Exit Calorie Tracker button that shuts program down
        JButton exitButton = new JButton("Exit Calorie Tracker");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                System.exit(0);
            }
        });
        JPanel exitButtonPanel = new JPanel();
        exitButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        exitButtonPanel.add(exitButton);
        frame.add(exitButtonPanel, BorderLayout.SOUTH);


        //make frame size and visible
        frame.setSize(500, 500);
        frame.setVisible(true);
    }

    //function to update the calorie report
    private static void updateCalorieReport(){
        totalCaloriesLabel.setText("Total Calories: " + caloriesConsumed);

        int caloriesRemaining = dailyGoal - caloriesConsumed;
        remainingCaloriesLabel.setText("Calories Remaining: " + caloriesRemaining);
    }

    //function to create the calorie report display
    public static void createCalorieReport(JFrame frame) {
        JPanel reportPanel = new JPanel();
        reportPanel.setLayout(new BoxLayout(reportPanel, BoxLayout.Y_AXIS));

        totalCaloriesLabel = new JLabel("Total Calories: ");
        remainingCaloriesLabel = new JLabel("Calories Remaining: ");

        reportPanel.add(totalCaloriesLabel);
        reportPanel.add(remainingCaloriesLabel);

        frame.add(reportPanel, BorderLayout.CENTER);
    }

}
