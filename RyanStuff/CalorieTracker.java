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

    private CalorieReport calorieReport;
    private JLabel totalCaloriesLabel;
    private JLabel remainingCaloriesLabel;
    private JPanel reportPanel;
    //private static JLabel dailyGoalLabel;
    private SleepReport sleepReport;
    private JLabel totalSleepLabel;
    private JPanel sleepReportPanel;
    private WeightReport weightReport;
    private JLabel weightLabel;
    private JPanel weightReportPanel;

    public CalorieTracker(App frame){
        frame.setTitle("Statistics Tracker");
        setLayout(new GridBagLayout());
        setSize(500, 500);

        int dailyCalorieGoal = getCalorieGoalForUser(frame.getUser());
        calorieReport = new CalorieReport(dailyCalorieGoal); //change with goals
        sleepReport = new SleepReport();
        weightReport = new WeightReport();

        createGUI();
    }

    public void createGUI() {
        GridBagConstraints c = new GridBagConstraints();

        //Set up Title
        JLabel calorieTitle = new JLabel("Calorie Tracker", JLabel.CENTER);
        calorieTitle.setFont(new Font("Arial", Font.BOLD, 20));
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.insets = new Insets(10, 10, 10, 10);
        add(calorieTitle, c);

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
        c.gridy = 2;
        c.gridwidth = 2;
        c.insets = new Insets(5, 10, 5, 10);
        add(calorieInput, c);

        //call function to make calorie report display
        createCalorieReport();

        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        c.insets = new Insets(10, 10, 10, 10);
        c.anchor  = GridBagConstraints.CENTER;
        add(reportPanel, c);

        //add sleep panel
        JLabel sleepTitle = new JLabel("Sleep Tracker", JLabel.CENTER);
        sleepTitle.setFont(new Font("Arial", Font.BOLD, 20));
        c.gridy = 4;
        add(sleepTitle, c);

        JPanel sleepInput = new JPanel();
        sleepInput.setLayout(new FlowLayout());
        JLabel sleepLabel = new JLabel("Enter Sleep Time (Hours, Minutes): ");
        JTextField hoursField = new JTextField(10);
        JTextField minutesField = new JTextField(10);
        JButton sleepButton = new JButton("Submit");

        sleepButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    int hours = Integer.parseInt(hoursField.getText());
                    int minutes = Integer.parseInt(minutesField.getText());

                    if(hours < 0 || minutes < 0 || minutes >= 60){
                        JOptionPane.showMessageDialog(CalorieTracker.this, "Please enter valid hours and minutes (minutes < 60)");
                    } else{
                        sleepReport.addSleep(hours, minutes);
                        updateSleepReport();
                    }
                } catch(NumberFormatException ex){
                    JOptionPane.showMessageDialog(CalorieTracker.this, "Enter valid numbers for sleep.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        sleepInput.add(sleepLabel);
        sleepInput.add(hoursField);
        sleepInput.add(new JLabel("h"));
        sleepInput.add(minutesField);
        sleepInput.add(new JLabel("m"));
        sleepInput.add(sleepButton);

        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 2;
        c.insets = new Insets(10, 10, 10, 10);
        add(sleepInput, c);

        createSleepReport();

        c.gridx = 0;
        c.gridy = 6;
        add(sleepReportPanel, c);

        //weight tracking
        JLabel weightTitle = new JLabel("Weight Tracker", JLabel.CENTER);
        weightTitle.setFont(new Font("Arial", Font.BOLD, 20));
        c.gridy = 7;
        add(weightTitle, c);

        JPanel weightInput = new JPanel();
        weightInput.setLayout(new FlowLayout());
        JLabel weightPrompt = new JLabel("Enter Weight (lbs): ");
        JTextField weightField = new JTextField(10);
        JButton weightButton = new JButton("Submit");

        weightButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    double weight = Double.parseDouble(weightField.getText());
                    if(weight <= 0){
                        JOptionPane.showMessageDialog(CalorieTracker.this, "Please enter a positive number!");
                    } else{
                        weightReport.setWeight(weight);
                        updateWeightReport();
                    }
                } catch(NumberFormatException ex){
                    JOptionPane.showMessageDialog(CalorieTracker.this, "Please enter a valid number!",  "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        weightInput.add(weightPrompt);
        weightInput.add(weightField);
        weightInput.add(weightButton);

        c.gridx = 0;
        c.gridy = 8;
        c.gridwidth = 2;
        c.insets = new Insets(10, 10, 10, 10);
        add(weightInput, c);

        createWeightReport();

        c.gridx = 0;
        c.gridy = 9;
        add(weightReportPanel, c);

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

    public void updateSleepReport() {
        int hours = sleepReport.getHours();
        int minutes = sleepReport.getMinutes();
        totalSleepLabel.setText("Total Sleep Time: " + hours + "h " + minutes + "m");
        sleepReportPanel.setVisible(true);
    }

    public void updateWeightReport() {
        weightLabel.setText("Current Weight: " + weightReport.getWeight() + " lbs");
        weightReportPanel.setVisible(true);
    }

    //function to create the calorie report display
    public void createCalorieReport() {
        reportPanel = new JPanel();
        reportPanel.setLayout(new BoxLayout(reportPanel, BoxLayout.Y_AXIS));

        JLabel reportHeader = new JLabel("Calorie Report");
        reportHeader.setFont(new Font("Arial", Font.BOLD, 16));

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

    private void createSleepReport() {
        sleepReportPanel = new JPanel();
        sleepReportPanel.setLayout(new BoxLayout(sleepReportPanel, BoxLayout.Y_AXIS));

        JLabel sleepHeader = new JLabel("Sleep Report");
        sleepHeader.setFont(new Font("Arial", Font.BOLD, 16));

        totalSleepLabel = new JLabel("Total Sleep Time: 0h 0m");

        sleepReportPanel.add(sleepHeader);
        sleepReportPanel.add(totalSleepLabel);

        sleepReportPanel.setVisible(false);
    }

    private void createWeightReport() {
        weightReportPanel = new JPanel();
        weightReportPanel.setLayout(new BoxLayout(weightReportPanel, BoxLayout.Y_AXIS));

        JLabel weightHeader = new JLabel("Weight Report");
        weightHeader.setFont(new Font("Arial", Font.BOLD, 16));

        weightLabel = new JLabel("Current Weight: N/A");

        weightReportPanel.add(weightHeader);
        weightReportPanel.add(weightLabel);

        weightReportPanel.setVisible(false);
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
