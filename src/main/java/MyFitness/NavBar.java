package MyFitness;

import MyFitness.RyanStuff.CalorieTracker;
import MyFitness.Settings.AdminPage;
import MyFitness.RyanStuff.StatsTracker;
import MyFitness.RyanStuff.CreateGoals;
import MyFitness.Settings.UserPage;
import MyFitness.Settings.*;
import MyFitness.Statistics.StatisticsPage;
import MyFitness.User.Admin;
import MyFitness.Trainer.*;
import MyFitness.User.*;

import javax.swing.*;
import java.awt.*;

public class NavBar extends JPanel {
    private User loggedInUser;

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    public User getLoggedInUser() {
        return this.loggedInUser;
    }

    public NavBar(App frame) {
        setLayout(new FlowLayout(FlowLayout.LEFT));

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

        JButton classDashboardButton = new JButton("Class Dashboard");
        classDashboardButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.setTitle("Class Dashboard");
            if (frame.getUser() instanceof Trainer) {
                Trainer trainer = (Trainer) frame.getUser();
                JPanel trainerPanel = new JPanel();
                trainerPanel.setLayout(new BoxLayout(trainerPanel, BoxLayout.Y_AXIS));
                trainerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                JLabel title = new JLabel("Class Dashboard (Trainer)");
                title.setFont(new Font("Arial", Font.BOLD, 24));
                title.setAlignmentX(Component.CENTER_ALIGNMENT);

                JButton createClass = new JButton("Create New Class");
                createClass.setAlignmentX(Component.CENTER_ALIGNMENT);
                createClass.addActionListener(ae -> {
                    frame.getContentPane().removeAll();
                    frame.add(new NavBar(frame), BorderLayout.NORTH);
                    frame.add(new CreateClassPage(frame, trainer));
                    frame.revalidate();
                    frame.repaint();
                });

                JButton viewClasses = new JButton("View My Classes");
                viewClasses.setAlignmentX(Component.CENTER_ALIGNMENT);
                viewClasses.addActionListener(ae -> {
                    frame.getContentPane().removeAll();
                    frame.add(new NavBar(frame), BorderLayout.NORTH);
                    frame.add(new TrainerClassesPage(frame, trainer));
                    frame.revalidate();
                    frame.repaint();
                });

                trainerPanel.add(title);
                trainerPanel.add(Box.createVerticalStrut(20));
                trainerPanel.add(createClass);
                trainerPanel.add(Box.createVerticalStrut(10));
                trainerPanel.add(viewClasses);
                frame.add(new NavBar(frame), BorderLayout.NORTH);
                frame.add(trainerPanel);
            } else {
                GeneralUser user = (GeneralUser) frame.getUser();
                JPanel userPanel = new JPanel();
                userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
                userPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                JLabel label = new JLabel("Class Dashboard (User)");
                label.setFont(new Font("Arial", Font.BOLD, 24));
                label.setAlignmentX(Component.CENTER_ALIGNMENT);

                JButton addClassBtn = new JButton("Add Class");
                addClassBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
                addClassBtn.addActionListener(ae -> {
                    frame.getContentPane().removeAll();
                    frame.add(new NavBar(frame), BorderLayout.NORTH);
                    frame.add(new AddClassPanel(frame, user));
                    frame.revalidate();
                    frame.repaint();
                });

                JButton seeMyClassBtn = new JButton("See My Class");
                seeMyClassBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
                seeMyClassBtn.addActionListener(ae -> {
                    frame.getContentPane().removeAll();
                    frame.add(new NavBar(frame), BorderLayout.NORTH);
                    frame.add(new SeeMyClassPanel(frame, user));
                    frame.revalidate();
                    frame.repaint();
                });

                userPanel.add(label);
                userPanel.add(Box.createVerticalStrut(20));
                userPanel.add(addClassBtn);
                userPanel.add(Box.createVerticalStrut(10));
                userPanel.add(seeMyClassBtn);
                frame.add(new NavBar(frame), BorderLayout.NORTH);
                frame.add(userPanel);
            }
            frame.revalidate();
            frame.repaint();
        });

        add(Home);
        add(classDashboardButton);
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
