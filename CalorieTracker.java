//Calorie Tracker Use Case
//Author: Ryan Meador

package MyFitness;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalorieTracker {

    private static CalorieReport calorieReport;

    private static JLabel totalCaloriesLabel;
    private static JLabel remainingCaloriesLabel;
    private static JPanel reportPanel;
    private static JFrame frame;
    //private static JLabel dailyGoalLabel;

    public static void main(String[] args) {
        createGUI();
    }

    public static void createGUI() {
        JFrame frame = new JFrame("Calorie Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        calorieReport = new CalorieReport(2000);

        frame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        //Set up Title
        JLabel title = new JLabel("Calorie Tracker", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        frame.add(title, c);

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
                        JOptionPane.showMessageDialog(frame, "Please enter a positive number!");
                    } else{
                        calorieReport.addCalories(inputCals);
                        System.out.println("Calories Consumed: " + calorieReport.getTotalCalories());
                        updateCalorieReport(frame);
                    }
                } catch (NumberFormatException ex){
                    JOptionPane.showMessageDialog(null, "Please enter a valid number!",
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
        frame.add(calorieInput, c);

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

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.SOUTHWEST;
        c.weightx = 1;
        c.weighty = 1;
        frame.add(exitButtonPanel, c);

        //call function to make calorie report display
        createCalorieReport(frame);

        //make frame size and visible
        frame.setSize(500, 500);
        frame.setVisible(true);
    }

    //function to update the calorie report
    private static void updateCalorieReport(JFrame frame) {
        totalCaloriesLabel.setText("Total Calories: " + calorieReport.getTotalCalories());
        remainingCaloriesLabel.setText("Calories Remaining: " + (Math.max(calorieReport.getRemainingCalories(), 0)));

        reportPanel.setVisible(true);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        frame.add(reportPanel, c);

        frame.revalidate();
        frame.repaint();
    }

    //function to create the calorie report display
    public static void createCalorieReport(JFrame frame) {
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
        frame.add(reportPanel, c);
    }

}
