package MyFitness.ExerciseSession;

import MyFitness.ExerciseSession.Workout.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

public class ExerciseSession extends JPanel {
    String date;
    Set<Workout> workouts;

    public ExerciseSession(JFrame frame, JPanel journal) {
        this.date = null;
        this.workouts = new HashSet<>();

        // TODO: Make UI

        setSize(frame.getWidth(), frame.getHeight());
        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        //Set up Title
        JLabel title = new JLabel("Exercise Journal", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        add(title, c);

        // Input fields (centered)
        JLabel dateLabel = new JLabel("Date: ");
        JTextField dateField = new JTextField(14);

        c.gridy = 1;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.LINE_END;
        add(dateLabel, c);

        c.gridx = 1;
        c.anchor = GridBagConstraints.LINE_START;
        add(dateField, c);




        // TODO: add the add workout button and save session button
        // NOTE: need to know what type of workout to create.







        // Back Button to return to the journal
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll();
//                frame.getContentPane().add(navBar);
                frame.add(journal);
                frame.revalidate();
                frame.repaint();
            }
        });

        JPanel exitButtonPanel = new JPanel();
        exitButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        exitButtonPanel.add(backButton);

        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.LAST_LINE_END;
        c.weightx = 1;
        c.weighty = 1;
        add(exitButtonPanel, c);








        setVisible(true);

    }

    public void setDate(String date){
        this.date = date;
    }

//    NOTE: This is old code that still needs to be refactored.
//    public void addWorkout(Workout workout) {
//        if(!workouts.contains(workout)){
//            workouts.add(workout);
//        }
//        else {
//            // NOTE: want to add a set to the list of workouts
//        }
//    }
//
//
//    @Override
//    public String toString() {
//        StringBuilder sessionData = new StringBuilder();
//        sessionData.append(date).append("\n");
//        for (Workout workout : workouts) {
//            sessionData.append(workout.toString()).append("\n");
//        }
//        return sessionData.toString();
//    }
}
