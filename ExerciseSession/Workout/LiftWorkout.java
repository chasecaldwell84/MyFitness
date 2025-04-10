package MyFitness.ExerciseSession.Workout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    }

    public LiftWorkout(JFrame frame, JPanel session, String workoutName) {
        super(frame, session);
        this.workoutType = WorkoutType.LIFT;
        sets = new ArrayList<LiftSet>();

        GridBagConstraints c = new GridBagConstraints();

        JLabel label = new JLabel("Enter");
        label.setFont(label.getFont().deriveFont(20f));
        add(label);
        setVisible(true);


        // Back Button to return to the journal
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll();
//                frame.getContentPane().add(navBar);
                frame.add(session);
                frame.revalidate();
                frame.repaint();
            }
        });

        JPanel exitButtonPanel = new JPanel();
        exitButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        exitButtonPanel.add(backButton);

        c.gridx = 1;
        c.gridy = 5;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.LAST_LINE_END;
        c.weightx = 1;
        c.weighty = 1;
        add(exitButtonPanel, c);

//        this.workoutName = workoutName;



//        sets.add(new LiftSet(weight, reps));



    }

    public void addLiftSet(double weight, int reps) {
        sets.add(new LiftSet(weight, reps));
    }
}
