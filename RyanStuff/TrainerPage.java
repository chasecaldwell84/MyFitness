package MyFitness.RyanStuff;

import MyFitness.App;
import MyFitness.Database;
import MyFitness.ExerciseSession.ExerciseSession;
import MyFitness.NavBar;
import MyFitness.User.Trainer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class TrainerPage extends JPanel {

    private Database db = Database.getInstance();

    public TrainerPage(App frame, NavBar navBar, Trainer trainer) {
        frame.setTitle("Create Plan");
        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);

        JLabel title = new JLabel("Create Plan", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        c.gridx = 0;
        c.gridy = 0;
        add(title, c);

        JLabel planNameLabel = new JLabel("Plan Name:");
        c.gridx = 0;
        c.gridy = 1;
        add(planNameLabel, c);

        JTextField planNameField = new JTextField(20);
        c.gridx = 1;
        c.gridy = 1;
        add(planNameField, c);

        JLabel exerciseLabel = new JLabel("Exercise:");
        c.gridx = 0;
        c.gridy = 2;
        add(exerciseLabel, c);

        JTextField exerciseField = new JTextField(20);
        c.gridx = 1;
        c.gridy = 2;
        add(exerciseField, c);

        JLabel repsLabel = new JLabel("Repetitions:");
        c.gridx = 0;
        c.gridy = 3;
        add(repsLabel, c);

        JTextField repsField = new JTextField(20);
        c.gridx = 1;
        c.gridy = 3;
        add(repsField, c);

        JLabel durationLabel = new JLabel("Duration (minutes):");
        c.gridx = 0;
        c.gridy = 4;
        add(durationLabel, c);

        JTextField durationField = new JTextField(20);
        c.gridx = 1;
        c.gridy = 4;
        add(durationField, c);

        JButton createPlanButton = new JButton("Create Plan");
        createPlanButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String planName = planNameField.getText();
                String exercise = exerciseField.getText();
                String repetitions = repsField.getText();
                String duration = durationField.getText();

                if(planName.isEmpty() || exercise.isEmpty() || repetitions.isEmpty() || duration.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill all the required fields!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try{
                    int durationInMinutes = Integer.parseInt(duration);
                    db.saveExercisePlan(planName, exercise, repetitions, Integer.parseInt(duration), trainer.getUserName());
                    JOptionPane.showMessageDialog(frame, "Plan created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                    planNameField.setText("");
                    exerciseField.setText("");
                    repsField.setText("");
                    durationField.setText("");
                } catch(NumberFormatException ex){
                    JOptionPane.showMessageDialog(frame, "Please enter a valid number for duration", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex){
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error saving plan.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        c.gridx = 1;
        c.gridy = 5;
        add(createPlanButton, c);
    }
}

/*
package MyFitness;

public class ExerciseJournal extends JPanel {

    public ExerciseJournal(App frame, NavBar navBar) {

        // add exercise session button
        JButton addSession = new JButton("Add Exercise Session");
        addSession.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll();
                frame.add(navBar);
                frame.add(new ExerciseSession(frame, journal, navBar));
                frame.revalidate();
                frame.repaint();
            }
        });

        c.gridy = 1;
        add(addSession, c);

        JLabel note = new JLabel("This is where Exercise Journal\n stats will be displayed.");
        note.setFont(new Font("Arial", Font.BOLD, 20));
        c.gridy = 2;
        c.insets = new Insets(200, 5, 200, 5);
        add(note, c);

        // TODO: Modify functionality of exit button.
        //NOTE do not need exit button
        //Create Exit button that shuts program down
        /*JButton exitButton = new JButton("Exit Exercise Journal");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                System.exit(0);
            }
        });

        JPanel exitButtonPanel = new JPanel();
        exitButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        exitButtonPanel.add(exitButton);


        c.gridy = 3;
        c.anchor = GridBagConstraints.LAST_LINE_END;
        c.insets = new Insets(5, 5, 5, 5);
        c.weightx = 1;
        c.weighty = 1;
        add(exitButtonPanel, c);

        setVisible(true);
    }
}
*/