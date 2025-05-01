package MyFitness.ExerciseSession.Workout;

import MyFitness.NavBar;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class LiftWorkout extends Workout {
    private ArrayList<LiftSet> sets;

    public class LiftSet {
        private double weight;
        private int reps;

        LiftSet(double weight, int reps) {
            this.weight = weight;
            this.reps = reps;
        }

        public double getWeight() {
            return weight;
        }

        public int getReps() {
            return reps;
        }
    }

    public LiftWorkout(JFrame frame, JPanel session, NavBar navBar, String workoutName) {
        super(frame, session, workoutName);
        this.workoutType = WorkoutType.LIFT;
        sets = new ArrayList<LiftSet>();

        GridBagConstraints c = new GridBagConstraints();

        JLabel label = new JLabel("Adding a set of " + workoutName + ": ");
        label.setFont(label.getFont().deriveFont(20f));
        c.gridx = 0;
        c.gridy = 0;
        add(label, c);

        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        inputPanel.add(new JLabel("Weight: "));
        JTextField weightField = new JTextField(20);
        inputPanel.add(weightField);
        inputPanel.add(new JLabel("Reps: "));
        JTextField repsField = new JTextField(20);
        inputPanel.add(repsField);
        c.gridy = 1;
        add(inputPanel, c);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            String weight = weightField.getText();
            String reps = repsField.getText();
            JDialog saveDialog = new JDialog(frame, "Save", true);
            if (!weight.isEmpty() && !reps.isEmpty()) {
                sets.add(new LiftSet(Double.parseDouble(weight), Integer.parseInt(reps)));
                JOptionPane.showMessageDialog(saveDialog,
                        "Workout: " + workoutName +"\nWeight: " + weight + "\nReps: " + reps,
                        "Workout Saved", JOptionPane.INFORMATION_MESSAGE);

                frame.getContentPane().removeAll();
                frame.getContentPane().add(navBar);
                frame.add(session);
                frame.revalidate();
                frame.repaint();


                // TESTING
                System.out.println("workoutName: " + workoutName);
                int i = 0;
                for (LiftSet set : sets) {
                    ++i;
                    System.out.println("Set " + i + ": " + set.weight + " " + set.reps);
                }


            } else {
                JOptionPane.showMessageDialog(saveDialog, "Please fill in all fields.",
                        "Missing Information", JOptionPane.ERROR_MESSAGE);
            }
            saveDialog.dispose();
        });
        buttonPanel.add(addButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.getContentPane().add(navBar);
            frame.add(session);
            frame.revalidate();
            frame.repaint();
        });
        buttonPanel.add(cancelButton);

        c.gridy = 2;
        add(buttonPanel, c);

        setVisible(true);
    }

    public ArrayList<LiftSet> getSets() {
        return sets;
    }
}
