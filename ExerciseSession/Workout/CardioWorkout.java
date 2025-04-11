package MyFitness.ExerciseSession.Workout;

import javax.swing.*;
import java.awt.*;

public class CardioWorkout extends Workout {
    private double distance;
    private CardioDuration duration;

    public class CardioDuration {
        private int hours;
        private int minutes;
        private int seconds;

        CardioDuration(int hours, int minutes, int seconds) {
            this.hours = hours;
            this.minutes = minutes;
            this.seconds = seconds;
        }

        public String getTime(){
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
    }

    public CardioWorkout(JFrame frame, JPanel session, String workoutName) {
        super(frame, session, workoutName);
        this.workoutType = WorkoutType.CARDIO;

        GridBagConstraints c = new GridBagConstraints();

        c.insets = new Insets(10, 10, 10, 10);

        JLabel label = new JLabel("Adding Workout: " + workoutName);
        label.setFont(label.getFont().deriveFont(20f));
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(10, 10, 20, 10);
        add(label, c);

        JLabel durationLabel = new JLabel("Duration: ");
        durationLabel.setFont(durationLabel.getFont().deriveFont(16f));
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(0, 10, 5, 10);
        add(durationLabel, c);

        JPanel durationPanel = new JPanel(new GridLayout(3, 2));
        durationPanel.add(new JLabel("Hours: "));
        JTextField hoursField = new JTextField(20);
        durationPanel.add(hoursField);
        durationPanel.add(new JLabel("Minutes: "));
        JTextField minutesField = new JTextField(20);
        durationPanel.add(minutesField);
        durationPanel.add(new JLabel("Seconds: "));
        JTextField secondsField = new JTextField(20);
        durationPanel.add(secondsField);
        c.gridy = 2;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(0, 10, 20, 10);
        add(durationPanel, c);

        JPanel distancePanel = new JPanel(new GridLayout(1, 2));
        JLabel distanceLabel = new JLabel("Distance (miles): ");
        distanceLabel.setFont(distanceLabel.getFont().deriveFont(16f));
        distancePanel.add(distanceLabel);
        JTextField distanceField = new JTextField(20);
        distancePanel.add(distanceField);
        c.gridy = 3;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(0, 10, 10, 10);
        add(distancePanel, c);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            String hh = hoursField.getText().trim();
            String mm = minutesField.getText().trim();
            String ss = secondsField.getText().trim();
            String dist = distanceField.getText();

            JDialog saveDialog = new JDialog(frame, "Save", true);

            if (!hh.isEmpty() && !mm.isEmpty() && !ss.isEmpty() && !dist.isEmpty()) {
                distance = Double.parseDouble(dist);
                duration = new CardioDuration(Integer.parseInt(hh), Integer.parseInt(mm), Integer.parseInt(ss));
                JOptionPane.showMessageDialog(saveDialog,
                        "Workout: " + workoutName +"\nDuration: " + duration.getTime() + "\nDistance: " + distance,
                        "Workout Saved", JOptionPane.INFORMATION_MESSAGE);

                frame.getContentPane().removeAll();
//                frame.getContentPane().add(navBar);
                frame.add(session);
                frame.revalidate();
                frame.repaint();


                // TESTING
                System.out.println("workoutName: " + workoutName);
                System.out.println("duration: " + duration.getTime());
                System.out.println("distance: " + distance);
                System.out.println();


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
//                frame.getContentPane().add(navBar);
            frame.add(session);
            frame.revalidate();
            frame.repaint();
        });
        buttonPanel.add(cancelButton);

        c.gridy = 4;
        add(buttonPanel, c);
    }
}
