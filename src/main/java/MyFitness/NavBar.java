package MyFitness;

import MyFitness.Settings.AdminPage;
import MyFitness.RyanStuff.StatsTracker;
import MyFitness.RyanStuff.CreateGoals;
import MyFitness.Settings.UserPage;
import MyFitness.Statistics.StatisticsPage;
import MyFitness.User.Admin;

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
            //frame.getContentPane().add(this);
            frame.getContentPane().setLayout(new BorderLayout());
            frame.getContentPane().add(this, BorderLayout.NORTH);

            HomePage home = new HomePage(frame);
            frame.getContentPane().add(home, BorderLayout.CENTER);
            //frame.add(home);
            frame.revalidate();
            frame.repaint();
        });

        NavBar thisNavBar = this;
        JButton exerciseButton = new JButton("Exercise Journal");
        exerciseButton.addActionListener(e -> {

            frame.getContentPane().removeAll();
            //frame.getContentPane().add(this);
            frame.getContentPane().setLayout(new BorderLayout());
            frame.getContentPane().add(thisNavBar, BorderLayout.NORTH);
            ExerciseJournal ex = new ExerciseJournal(frame, thisNavBar, frame.getUser());
            frame.getContentPane().add(ex, BorderLayout.CENTER);

            frame.add(ex);
            frame.revalidate();
            frame.repaint();

        });
        JButton CalorieTracker = new JButton("Stats Tracker");
        CalorieTracker.addActionListener(e -> {

            frame.getContentPane().removeAll();
            frame.getContentPane().setLayout(new BorderLayout());
            frame.getContentPane().add(this, BorderLayout.NORTH);

            StatsTracker tracker = new StatsTracker(frame);
            frame.getContentPane().add(tracker, BorderLayout.CENTER);


            frame.revalidate();
            frame.repaint();
        });

        JButton goalButton = new JButton("Goals");
        goalButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.getContentPane().setLayout(new BorderLayout());

            frame.getContentPane().add(this, BorderLayout.NORTH);
            CreateGoals goalsPannel = new CreateGoals(frame);
            frame.getContentPane().add(goalsPannel, BorderLayout.CENTER);

            frame.revalidate();
            frame.repaint();
        });

        JButton socialButton = new JButton("Social");
        socialButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.getContentPane().setLayout(new BorderLayout());
            frame.getContentPane().add(this, BorderLayout.NORTH);
            SocialPanel socialPanel = new SocialPanel(frame);
            frame.getContentPane().add(socialPanel, BorderLayout.CENTER);

            //frame.setTitle("Social Menu");

            frame.revalidate();
            frame.repaint();
        });

        JButton settings = new JButton("Settings");
        settings.addActionListener(e -> {
            frame.setTitle("Settings");
            frame.getContentPane().removeAll();
            //frame.getContentPane().add(this);
            frame.getContentPane().setLayout(new BorderLayout());
            frame.getContentPane().add(this, BorderLayout.NORTH);

            if(frame.getUser() instanceof Admin) {
                AdminPage adminPage = new AdminPage(frame);
                //frame.add(adminPage);
                frame.getContentPane().add(adminPage, BorderLayout.CENTER);
            } else {
                UserPage userPage = new UserPage(frame);
                //frame.add(userPage);
                frame.getContentPane().add(userPage, BorderLayout.CENTER);
            }

            frame.revalidate();
            frame.repaint();

        });

        JButton statistics = new JButton("Statistics");
        statistics.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.getContentPane().setLayout(new BorderLayout());
            frame.getContentPane().add(this, BorderLayout.NORTH);
            StatisticsPage statsPanel = new StatisticsPage(frame);
            frame.getContentPane().add(statsPanel, BorderLayout.CENTER);

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

    public void showHomePage(App frame){
        frame.getContentPane().removeAll();
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(this, BorderLayout.NORTH);
        HomePage home = new HomePage(frame);
        frame.getContentPane().add(home, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }
}
