package MyFitness;

import MyFitness.RyanStuff.CalorieTracker;
import MyFitness.RyanStuff.CreateGoals;
import MyFitness.User.Settings;

import javax.swing.*;
import java.awt.*;

public class NavBar extends JPanel {
    public NavBar(App frame) {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        //NOTE: if we want backButton need to store previous frames in like a stack
        /*JButton backButton = new JButton("Back");*/

        JButton Home = new JButton("Home");
        Home.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.getContentPane().add(this);
            frame.add(new JLabel("Home Page"));
            frame.revalidate();
            frame.repaint();
        });

        NavBar thisNavBar = this;
        JButton exerciseButton = new JButton("Exercise Journal");
        exerciseButton.addActionListener(e -> {
            ExerciseJournal ex = new ExerciseJournal(frame, thisNavBar);
            frame.getContentPane().removeAll();
            frame.getContentPane().add(this);
            frame.add(ex);
            frame.revalidate();
            frame.repaint();

        });
        JButton CalorieTracker = new JButton("Calorie Tracker");
        CalorieTracker.addActionListener(e -> {
            CalorieTracker calorieTrackerPannel = new CalorieTracker(frame);
            frame.getContentPane().removeAll();

            frame.getContentPane().add(this);
            frame.add(calorieTrackerPannel);

            frame.revalidate();
            frame.repaint();
        });
        JButton goalButton = new JButton("Goals");
        goalButton.addActionListener(e -> {
            CreateGoals goalsPannel = new CreateGoals(frame);
            frame.getContentPane().removeAll();

            frame.getContentPane().add(this);
            frame.add(goalsPannel);

            frame.revalidate();
            frame.repaint();
        });

        JButton settings = new JButton("Settings");
        //NOTE: pannel has to be created in the actionListner or else it doesnt update the information
        settings.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.getContentPane().add(this);

            frame.add(frame.getUser().getSettings());
            frame.setTitle("Settings");
            frame.revalidate();
            frame.repaint();
        });
        add(Home);
        add(exerciseButton);
        add(CalorieTracker);
        add(goalButton);
        add(settings);
    }
}
