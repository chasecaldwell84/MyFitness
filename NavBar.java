package MyFitness;

import MyFitness.RyanStuff.CalorieTracker;
import MyFitness.RyanStuff.CreateGoals;
import MyFitness.User.Admin;
import MyFitness.User.Trainer;
import MyFitness.User.User;

import javax.swing.*;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.awt.*;
import java.util.Set;

public class NavBar extends JPanel {
    private final App frame;

    public NavBar(App frame) {
        this.frame = frame;
        setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton Home = new JButton("Home");
        Home.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.getContentPane().add(this);
            HomePage home = new HomePage(frame);
            frame.add(home);
            frame.revalidate();
            frame.repaint();
        });

        NavBar thisNavBar = this;
        JButton exerciseButton = new JButton("Exercise Journal");
        exerciseButton.addActionListener(e -> {
            ExerciseJournal ex = new ExerciseJournal(frame, thisNavBar, frame.getUser());
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

        JButton socialButton = new JButton("Social");
        socialButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            //frame.getContentPane().setLayout(new BorderLayout());
            //frame.getContentPane().add(this, BorderLayout.NORTH);
            frame.getContentPane().add(this);
            frame.add(new SocialPanel(frame));

            //frame.setTitle("Social Menu");

            frame.revalidate();
            frame.repaint();
        });

        JButton settings = new JButton("Settings");
        settings.addActionListener(e -> {
            frame.setTitle("Settings");
            frame.getContentPane().removeAll();
            frame.getContentPane().add(this);
            if (frame.getUser() instanceof Admin) {
                AdminPage adminPage = new AdminPage();
                frame.add(adminPage);
            } else {
                UserPage userPage = new UserPage(frame);
                frame.add(userPage);
            }
            frame.revalidate();
            frame.repaint();
        });

        JButton statistics = new JButton("Statistics");
        statistics.addActionListener(e -> {
            StatisticsPage statsPanel = new StatisticsPage(frame);
            frame.getContentPane().removeAll();
            frame.getContentPane().add(this);
            frame.add(statsPanel);
            frame.revalidate();
            frame.repaint();
        });

        add(Home);
        add(exerciseButton);
        add(CalorieTracker);
        add(goalButton);
        add(socialButton);
        add(settings);
        add(statistics);
    }
}
