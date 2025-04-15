package MyFitness;

import MyFitness.RyanStuff.CalorieTracker;
import MyFitness.RyanStuff.CreateGoals;
import MyFitness.User.Admin;
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
            frame.setTitle("Settings");


            frame.getContentPane().removeAll();
            frame.getContentPane().add(this);

            if(frame.getUser() instanceof Admin) {
                AdminPage adminPage = new AdminPage();
                frame.add(adminPage);
            }
            else {
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
        add(settings);
        add(statistics);
    }
}
