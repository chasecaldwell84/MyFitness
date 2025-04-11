package MyFitness;

import MyFitness.ExerciseSession.ExerciseSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


/* TODO: Refactor code completely. Don't use static. Extend JPanel.
*   Configure so that the user can input a lift or cardio workout. Configure so
*   that input is more strict and won't be as prone to user error.  */

/* TODO UPDATE: Mostly done with this phase of refactoring. No longer using
*   static. Now extending JPanel. Is now configured to enter a lift or cardio
*   workout. Still need to make input more strict and less prone to user error.
*   While the initial version of ExerciseJournal could write to a csv, this
*   version does not yet write to any file or database. Will be implementing
*   later. Once functionality is finished, make the UI look better. */

public class ExerciseJournal extends JPanel {

    /*public static void main(String[] args){
        JFrame frame = new JFrame("Exercise Journal");
        ExerciseJournal exerciseJournal = new ExerciseJournal(frame);
        frame.add(exerciseJournal);
        frame.setVisible(true);
    }*/

    public ExerciseJournal(App frame, NavBar navBar) {
        frame.setTitle("Exercise Journal");
        setLayout(new GridBagLayout());


        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);

        //Set up Title
        JLabel title = new JLabel("Exercise Journal", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        c.gridx = 0;
        c.gridy = 0;
        add(title, c);

        JPanel journal = this;

        // add exercise session button
        JButton addSession = new JButton("Add Exercise Session");
        addSession.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll();
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
        add(exitButtonPanel, c);*/

        setVisible(true);
    }
}


//    NOTE: old code below. . .

//    private static void saveSessionToCSV(ExerciseSession session) {
//        // Write session and workouts to CSV file
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter("exercise_sessions.csv", true))) {
//            writer.write(session.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(null, "Error saving to file.", "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    private void openExerciseSessionDialog( JFrame frame ) {
//        JDialog sessionDialog = new JDialog(frame, "New Exercise Session", true); // Modal dialog
//        sessionDialog.setSize(300, 150);
//        sessionDialog.setLayout(new GridBagLayout());
//        GridBagConstraints c = new GridBagConstraints();
//
//        ExerciseSession session = new ExerciseSession(frame, null);
//
//        // Set general padding and spacing
//        c.insets = new Insets(10, 10, 10, 10);
//
//        // Input fields (centered)
//        JLabel dateLabel = new JLabel("Date: ");
//        JTextField dateField = new JTextField(7);
//
//        c.gridx = 0;
//        c.gridy = 0;
//        c.anchor = GridBagConstraints.LINE_END;
//        sessionDialog.add(dateLabel, c);
//
//        c.gridx = 1;
//        c.gridy = 0;
//        c.anchor = GridBagConstraints.LINE_START;
//        sessionDialog.add(dateField, c);
//
//        // Add Workout Dialog button
//        JButton addWorkout = new JButton("Add Workout");
//        addWorkout.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                openWorkoutDialog(frame, session);
//            }
//        });
//
//        c.gridx = 0;
//        c.gridy = 1;
//        c.anchor = GridBagConstraints.LINE_END;
//        sessionDialog.add(addWorkout, c);
//
//        // Submit button (bottom left)
//        JButton submitButton = new JButton("Save Session");
//        submitButton.addActionListener(e -> {
//            String date = dateField.getText();
//            session.setDate(date);
//            saveSessionToCSV(session);
//            JOptionPane.showMessageDialog(sessionDialog, "Date: " + date,
//                    "Session Saved", JOptionPane.INFORMATION_MESSAGE);
//            sessionDialog.dispose(); // Close the dialog
//        });
//
//        c.gridx = 1;
//        c.gridy = 1;
//        c.anchor = GridBagConstraints.LINE_START;
//        sessionDialog.add(submitButton, c);
//
//        sessionDialog.setLocationRelativeTo(this); // Centers the dialog
//        sessionDialog.setVisible(true);
//    }
//
//    private void openWorkoutDialog( JFrame frame,  ExerciseSession session) {
//        JDialog workoutDialog = new JDialog(frame, "New Workout", true);
//        workoutDialog.setSize(250, 150);
//        workoutDialog.setLayout(new GridBagLayout());
//        GridBagConstraints c = new GridBagConstraints();
//
//        JLabel workoutLabel = new JLabel("Workout: ");
//        JTextField workoutField = new JTextField(10);
//
//        c.gridx = 0;
//        c.gridy = 0;
//        c.anchor = GridBagConstraints.LINE_END;
//        workoutDialog.add(workoutLabel, c);
//        c.gridx = 1;
//        c.anchor = GridBagConstraints.LINE_START;
//        workoutDialog.add(workoutField, c);
//
//        JLabel weightLabel = new JLabel("Working Weight: ");
//        JTextField weightField = new JTextField(10);
//        c.gridx = 0;
//        c.gridy = 1;
//        c.anchor = GridBagConstraints.LINE_END;
//        workoutDialog.add(weightLabel, c);
//        c.gridx = 1;
//        c.anchor = GridBagConstraints.LINE_START;
//        workoutDialog.add(weightField, c);
//
//        JLabel repsLabel = new JLabel("Repetitions: ");
//        JTextField repsField = new JTextField(10);
//        c.gridx = 0;
//        c.gridy = 2;
//        c.anchor = GridBagConstraints.LINE_END;
//        workoutDialog.add(repsLabel, c);
//        c.gridx = 1;
//        c.anchor = GridBagConstraints.LINE_START;
//        workoutDialog.add(repsField, c);
//
//        // Buttons panel
//        JPanel buttonPanel = new JPanel();
//        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));  // Align buttons to the right
//
//        // Add Button
//        JButton addButton = new JButton("Add");
//        addButton.addActionListener(e -> {
//            String workout = workoutField.getText();
//            String weight = weightField.getText();
//            String reps = repsField.getText();
//
//            // Validate input (optional)
//            if (!workout.isEmpty() && !weight.isEmpty() && !reps.isEmpty()) {
////                session.addWorkout(new Workout(workout, Double.parseDouble(weight), Integer.parseInt(reps)));
//                JOptionPane.showMessageDialog(workoutDialog,
//                        "Workout: " + workout + "\nWeight: " + weight + "\nReps: " + reps,
//                        "Workout Saved", JOptionPane.INFORMATION_MESSAGE);
//                workoutDialog.dispose(); // Close dialog after saving
//            } else {
//                JOptionPane.showMessageDialog(workoutDialog, "Please fill in all fields.",
//                        "Missing Information", JOptionPane.ERROR_MESSAGE);
//            }
//        });
//        buttonPanel.add(addButton);
//
//        // Cancel Button
//        JButton cancelButton = new JButton("Cancel");
//        cancelButton.addActionListener(e -> workoutDialog.dispose());  // Close dialog without saving
//        buttonPanel.add(cancelButton);
//
//        // Add button panel to the dialog
//        c.gridx = 0;
//        c.gridy = 3;
//        c.gridwidth = 2;
//        c.anchor = GridBagConstraints.LINE_END;
//        workoutDialog.add(buttonPanel, c);
//
//        workoutDialog.setLocationRelativeTo(this);
//        workoutDialog.setVisible(true);
//    }