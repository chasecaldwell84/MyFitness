package MyFitness;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/* TODO: Refactor code completely. Don't use static. Extend JFrame and JDialog.
*   Configure so that the user can input a lyft or cardio workout. Configure so
*   that input is more strict and won't be as prone to user error.  */

public class ExerciseJournal extends JFrame {

    public ExerciseJournal() {
        setTitle("Exercise Journal");
        setSize(350, 250);
        setLayout(new GridBagLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GridBagConstraints c = new GridBagConstraints();

        //Set up Title
        JLabel title = new JLabel("Exercise Journal", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        add(title, c);

        // add exercise session button
        JButton addSession = new JButton("Add Exercise Session");
        addSession.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openExerciseSessionDialog();
            }
        });

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.gridheight = 2;
        add(addSession, c);

        //Create Exit button that shuts program down
        JButton exitButton = new JButton("Exit Exercise Journal");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                System.exit(0);
            }
        });

        JPanel exitButtonPanel = new JPanel();
        exitButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        exitButtonPanel.add(exitButton);

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.LAST_LINE_END;
        c.weightx = 1;
        c.weighty = 1;
        add(exitButtonPanel, c);

        //make frame size and visible
        setSize(500, 500);
        setVisible(true);

    }


    public static void main(String[] args){
//        createGUI();

        ExerciseJournal exerciseJournal = new ExerciseJournal();

    }

    private static class Workout {
        String workoutName;
        double weight;
        int reps;

        public Workout(String workoutName, double weight, int reps) {
            this.workoutName = workoutName;
            this.weight = weight;
            this.reps = reps;
        }

        @Override
        public String toString() {
            return workoutName + "," + weight + "," + reps;
        }
    }

    private static class ExerciseSession {
        String date;
        List<Workout> workouts;

        public ExerciseSession(String date) {
            this.date = date;
            this.workouts = new ArrayList<>();
        }

        public void addWorkout(Workout workout) {
            workouts.add(workout);
        }

        public void setDate(String date){
            this.date = date;
        }

        @Override
        public String toString() {
            StringBuilder sessionData = new StringBuilder();
            sessionData.append(date).append("\n");
            for (Workout workout : workouts) {
                sessionData.append(workout.toString()).append("\n");
            }
            return sessionData.toString();
        }
    }

    private static void saveSessionToCSV(ExerciseSession session) {
        // Write session and workouts to CSV file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("exercise_sessions.csv", true))) {
            writer.write(session.toString());
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving to file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

//    public static void createGUI(){
//        JFrame frame = new JFrame();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        frame.setLayout(new GridBagLayout());
//        GridBagConstraints c = new GridBagConstraints();
//
//        //Set up Title
//        JLabel title = new JLabel("Exercise Journal", JLabel.CENTER);
//        title.setFont(new Font("Arial", Font.BOLD, 20));
//        c.gridx = 0;
//        c.gridy = 0;
//        c.gridwidth = 2;
//        frame.add(title, c);
//
//        // add exercise session button
//        JButton addSession = new JButton("Add Exercise Session");
//        addSession.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                openExerciseSessionDialog(frame);
//            }
//        });
//
//        c.gridx = 0;
//        c.gridy = 1;
//        c.gridwidth = 2;
//        c.gridheight = 2;
//        frame.add(addSession, c);
//
//        //Create Exit button that shuts program down
//        JButton exitButton = new JButton("Exit Exercise Journal");
//        exitButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                frame.dispose();
//                System.exit(0);
//            }
//        });
//
//        JPanel exitButtonPanel = new JPanel();
//        exitButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
//        exitButtonPanel.add(exitButton);
//
//        c.gridx = 0;
//        c.gridy = 2;
//        c.gridwidth = 1;
//        c.anchor = GridBagConstraints.LAST_LINE_END;
//        c.weightx = 1;
//        c.weighty = 1;
//        frame.add(exitButtonPanel, c);
//
//        //make frame size and visible
//        frame.setSize(500, 500);
//        frame.setVisible(true);
//    }

    private void openExerciseSessionDialog(/* JFrame frame */) {
        JDialog sessionDialog = new JDialog(this, "New Exercise Session", true); // Modal dialog
        sessionDialog.setSize(300, 150);
        sessionDialog.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        ExerciseSession session = new ExerciseSession(null);

        // Set general padding and spacing
        c.insets = new Insets(10, 10, 10, 10);

        // Input fields (centered)
        JLabel dateLabel = new JLabel("Date: ");
        JTextField dateField = new JTextField(7);

        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_END;
        sessionDialog.add(dateLabel, c);

        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        sessionDialog.add(dateField, c);

        // Add Workout Dialog button
        JButton addWorkout = new JButton("Add Workout");
        addWorkout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openWorkoutDialog(session);
            }
        });

        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.LINE_END;
        sessionDialog.add(addWorkout, c);

        // Submit button (bottom left)
        JButton submitButton = new JButton("Save Session");
        submitButton.addActionListener(e -> {
            String date = dateField.getText();
            session.setDate(date);
            saveSessionToCSV(session);
            JOptionPane.showMessageDialog(sessionDialog, "Date: " + date,
                    "Session Saved", JOptionPane.INFORMATION_MESSAGE);
            sessionDialog.dispose(); // Close the dialog
        });

        c.gridx = 1;
        c.gridy = 1;
        c.anchor = GridBagConstraints.LINE_START;
        sessionDialog.add(submitButton, c);

        sessionDialog.setLocationRelativeTo(this); // Centers the dialog
        sessionDialog.setVisible(true);
    }

    private void openWorkoutDialog(/* JFrame frame, */ ExerciseSession session) {
        JDialog workoutDialog = new JDialog(this, "New Workout", true);
        workoutDialog.setSize(250, 150);
        workoutDialog.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JLabel workoutLabel = new JLabel("Workout: ");
        JTextField workoutField = new JTextField(10);

        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_END;
        workoutDialog.add(workoutLabel, c);
        c.gridx = 1;
        c.anchor = GridBagConstraints.LINE_START;
        workoutDialog.add(workoutField, c);

        JLabel weightLabel = new JLabel("Working Weight: ");
        JTextField weightField = new JTextField(10);
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.LINE_END;
        workoutDialog.add(weightLabel, c);
        c.gridx = 1;
        c.anchor = GridBagConstraints.LINE_START;
        workoutDialog.add(weightField, c);

        JLabel repsLabel = new JLabel("Repetitions: ");
        JTextField repsField = new JTextField(10);
        c.gridx = 0;
        c.gridy = 2;
        c.anchor = GridBagConstraints.LINE_END;
        workoutDialog.add(repsLabel, c);
        c.gridx = 1;
        c.anchor = GridBagConstraints.LINE_START;
        workoutDialog.add(repsField, c);

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));  // Align buttons to the right

        // Add Button
        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            String workout = workoutField.getText();
            String weight = weightField.getText();
            String reps = repsField.getText();

            // Validate input (optional)
            if (!workout.isEmpty() && !weight.isEmpty() && !reps.isEmpty()) {
                session.addWorkout(new Workout(workout, Double.parseDouble(weight), Integer.parseInt(reps)));
                JOptionPane.showMessageDialog(workoutDialog,
                        "Workout: " + workout + "\nWeight: " + weight + "\nReps: " + reps,
                        "Workout Saved", JOptionPane.INFORMATION_MESSAGE);
                workoutDialog.dispose(); // Close dialog after saving
            } else {
                JOptionPane.showMessageDialog(workoutDialog, "Please fill in all fields.",
                        "Missing Information", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(addButton);

        // Cancel Button
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> workoutDialog.dispose());  // Close dialog without saving
        buttonPanel.add(cancelButton);

        // Add button panel to the dialog
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.LINE_END;
        workoutDialog.add(buttonPanel, c);

        workoutDialog.setLocationRelativeTo(this);
        workoutDialog.setVisible(true);
    }
}