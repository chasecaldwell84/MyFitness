package MyFitness.RyanStuff;

import MyFitness.App;
import MyFitness.Database;
import MyFitness.NavBar;
import MyFitness.User.Trainer;

import javax.swing.*;
import java.awt.*;

public class ModifyPlanPanel extends JPanel {
    private Database db = Database.getInstance();

    public ModifyPlanPanel(App frame, NavBar navBar, Trainer trainer, String planName) {
        frame.setTitle("Modify Plan");
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);

        JLabel title = new JLabel("Modify Plan: " + planName, JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        add(title, c);

        JLabel exerciseLabel = new JLabel("Exercise: ");
        JTextField exerciseField = new JTextField(10);

        JLabel repsLabel = new JLabel("Repetitions: ");
        JTextField repsField = new JTextField(10);

        JLabel durationLabel = new JLabel("Duration: ");
        JTextField durationField = new JTextField(10);

        try{
            String[] data = db.getExercisePlan(planName, trainer.getUserName());
            exerciseField.setText(data[0]);
            repsField.setText(data[1]);
            durationField.setText(data[2]);
        } catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to find exercise plan", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        c.gridwidth = 1;

        c.gridy = 1;
        c.gridx = 0;
        add(exerciseLabel, c);
        c.gridx = 1;
        add(exerciseField, c);

        c.gridy = 2;
        c.gridx = 0;
        add(repsLabel, c);
        c.gridx = 1;
        add(repsField, c);

        c.gridy = 3;
        c.gridx = 0;
        add(durationLabel, c);
        c.gridx = 1;
        add(durationField, c);

        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(e -> {
            String exercise = exerciseField.getText();
            String repetitions = repsField.getText();
            String duration = durationField.getText();

            if(exercise.isEmpty() || repetitions.isEmpty() || duration.isEmpty()){
                JOptionPane.showMessageDialog(frame, "All fields must be filled", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try{
                int durationInMinutes = Integer.parseInt(duration);
                db.updateExercisePlan(planName, exercise, repetitions, durationInMinutes, trainer.getUserName());
                JOptionPane.showMessageDialog(frame, "Exercise plan successfully updated", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch(NumberFormatException ex){
                JOptionPane.showMessageDialog(frame, "Please enter a valid number for duration.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error updating plan.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        c.gridx = 1;
        c.gridy = 4;
        add(saveButton, c);
    }
}
