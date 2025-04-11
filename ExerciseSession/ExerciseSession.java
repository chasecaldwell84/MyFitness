package MyFitness.ExerciseSession;

import MyFitness.ExerciseSession.Workout.*;
import MyFitness.NavBar;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class ExerciseSession extends JPanel {
    String date;
    Set<Workout> workouts;

    public ExerciseSession(JFrame frame, JPanel journal, NavBar navBar) {
        this.date = null;
        this.workouts = new HashSet<>();

        JPanel session = this;

        setSize(frame.getWidth(), frame.getHeight());
        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        Dimension buttonsSize = new Dimension(120, 30);

        //Set up Title
        JLabel title = new JLabel("Exercise Session", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        add(title, c);

//        // Input fields (centered)
//        JLabel dateLabel = new JLabel("Date: ");
//        JTextField dateField = new JTextField(14);
//        c.gridy = 1;
//        c.gridwidth = 1;
//        c.anchor = GridBagConstraints.LINE_END;
//        add(dateLabel, c);
//        c.gridx = 1;
//        c.anchor = GridBagConstraints.LINE_START;
//        add(dateField, c);

        JPanel namePanel = new JPanel(new FlowLayout());
        namePanel.add(new JLabel("Enter Workout Name: "));
        JTextField nameField = new JTextField(14);
        namePanel.add(nameField);
        namePanel.setVisible(false);
        c.gridy = 2;
        c.gridwidth = 1;
        add(namePanel, c);

        JLabel instructionLabel = new JLabel("Select Workout Type: ");
        instructionLabel.setFont(new Font("Arial", Font.BOLD, 20));
        instructionLabel.setVisible(false);
        c.gridy = 3;
        add(instructionLabel, c);

        JPanel workoutButtons = new JPanel(new FlowLayout());

        JButton liftButton = new JButton("Weight Lifting");
        liftButton.setPreferredSize(buttonsSize);
        liftButton.addActionListener(e -> {
            namePanel.setVisible(false);
            instructionLabel.setVisible(false);
            workoutButtons.setVisible(false);

            frame.getContentPane().removeAll();

            Workout currWorkout = null;
            String newWorkoutName = nameField.getText().trim();
            boolean isNewWorkout = true;

            for (Workout workout : workouts) {
                if(workout.getWorkoutName() != null && workout.getWorkoutName().equals(newWorkoutName)) {
                    isNewWorkout = false;
                    currWorkout = workout;
                    break;
                }
            }

            if(isNewWorkout){
                currWorkout = new LiftWorkout(frame, session, newWorkoutName);
                workouts.add(currWorkout);
            }

            frame.getContentPane().add(currWorkout);
            frame.revalidate();
            frame.repaint();
        });
        workoutButtons.add(liftButton);

        JButton cardioButton = new JButton("Cardio");
        cardioButton.setPreferredSize(buttonsSize);
        cardioButton.addActionListener(e -> {
            namePanel.setVisible(false);
            instructionLabel.setVisible(false);
            workoutButtons.setVisible(false);

            frame.getContentPane().removeAll();

            Workout currWorkout = null;
            String newWorkoutName = nameField.getText().trim();
            boolean isNewWorkout = true;

            for (Workout workout : workouts) {
                if(workout.getWorkoutName() != null && workout.getWorkoutName().equals(newWorkoutName)) {
                    isNewWorkout = false;
                    currWorkout = workout;
                    break;
                }
            }

            if(isNewWorkout){
                currWorkout = new CardioWorkout(frame, session, newWorkoutName);
                workouts.add(currWorkout);
            }

            frame.getContentPane().add(currWorkout);
            frame.revalidate();
            frame.repaint();
        });
        workoutButtons.add(cardioButton);

        c.gridy = 4;
        workoutButtons.setVisible(false);
        add(workoutButtons, c);

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());

        JButton addWorkout = new JButton("Add Workout");
        addWorkout.setPreferredSize(buttonsSize);
        addWorkout.addActionListener(e -> {
            namePanel.setVisible(true);
            instructionLabel.setVisible(true);
            workoutButtons.setVisible(true);
        });
        buttons.add(addWorkout);

        JButton saveSession = new JButton("Save Session");
        saveSession.setPreferredSize(buttonsSize);
        saveSession.addActionListener(e -> {

        });
        buttons.add(saveSession);

        c.gridy = 1;

        add(buttons, c);

        // Back Button to return to the journal
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.getContentPane().add(navBar);
            frame.add(journal);
            frame.revalidate();
            frame.repaint();
        });

        JPanel exitButtonPanel = new JPanel();
        exitButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        exitButtonPanel.add(backButton);


        c.gridy = 5;
        c.anchor = GridBagConstraints.LAST_LINE_END;
        c.weightx = 1;
        c.weighty = 1;
        add(exitButtonPanel, c);

        setVisible(true);
    }

    public void setDate(String date){
        this.date = date;
    }
}
