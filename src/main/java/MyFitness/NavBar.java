package MyFitness;

import MyFitness.RyanStuff.CalorieTracker;
import MyFitness.RyanStuff.CreateGoals;
import MyFitness.User.Admin;
import MyFitness.User.Settings;
import MyFitness.User.Trainer;
import MyFitness.User.User;
import MyFitness.Trainer.*;

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
            frame.getContentPane().add(this);
            frame.add(new JLabel("Home Page"));
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
            JPanel socialPanel = new JPanel(new GridLayout(3, 1, 10, 10));

            JButton addFriendButton = new JButton("Add Friend");
            JButton sendChallengeButton = new JButton("Send Challenge");
            JButton viewChallengesButton = new JButton("View Challenges");

            addFriendButton.addActionListener(ae -> {
                String friendUsername = JOptionPane.showInputDialog(frame, "Enter friend's username:");
                if (friendUsername == null || friendUsername.trim().isEmpty()) return;

                FriendManager fm = frame.getFriendManager();
                User currentUser = frame.getUser();
                User friend = frame.getAllUsers().stream()
                        .filter(u -> u.getUserName().equalsIgnoreCase(friendUsername.trim()))
                        .findFirst()
                        .orElse(null);

                if (friend == null) {
                    JOptionPane.showMessageDialog(frame, "User not found.");
                    return;
                }

                fm.addFriend(currentUser, friend);
                JOptionPane.showMessageDialog(frame, friend.getUserName() + " added as a friend!");
            });

            sendChallengeButton.addActionListener(ae -> {
                ChallengeSender sender = new ChallengeSender(frame.getFriendManager(), frame.getUser());
                frame.getContentPane().removeAll();
                frame.getContentPane().add(this);
                frame.add(sender);
                frame.revalidate();
                frame.repaint();
            });

            viewChallengesButton.addActionListener(ae -> {
                java.util.List<String> challenges = frame.getFriendManager().getChallenges(frame.getUser());
                JOptionPane.showMessageDialog(frame, String.join("\n", challenges), "Your Challenges", JOptionPane.INFORMATION_MESSAGE);
            });

            socialPanel.add(addFriendButton);
            socialPanel.add(sendChallengeButton);
            socialPanel.add(viewChallengesButton);

            frame.getContentPane().removeAll();
            frame.getContentPane().add(this);
            frame.add(socialPanel);
            frame.setTitle("Social Menu");
            frame.revalidate();
            frame.repaint();
        });

        JButton settings = new JButton("Settings");
        settings.addActionListener(e -> {
            frame.setTitle("Settings");
            frame.getContentPane().removeAll();
            frame.getContentPane().add(this);
            if (frame.getUser() instanceof Admin) {
                frame.add(new AdminPage());
            } else {
                frame.add(new UserPage(frame));
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

        JButton classDashboardButton = new JButton("Class Dashboard");
        classDashboardButton.addActionListener(e -> {
            frame.getContentPane().removeAll();

            if (frame.getUser() instanceof Trainer) {
                Trainer trainer = (Trainer) frame.getUser();

                JPanel trainerPanel = new JPanel();
                trainerPanel.setLayout(new BoxLayout(trainerPanel, BoxLayout.Y_AXIS));
                trainerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

                JLabel title = new JLabel("Class Dashboard(Trainer)");
                title.setFont(new Font("Arial", Font.BOLD, 24));
                title.setAlignmentX(Component.CENTER_ALIGNMENT);

                JButton createClass = new JButton("Create New Class");
                createClass.setAlignmentX(Component.CENTER_ALIGNMENT);
                createClass.addActionListener(ae -> {
                    CreateClassPage createPage = new CreateClassPage(frame, trainer);
                    NavBar newBar = new NavBar(frame);
                    newBar.setLoggedInUser(this.getLoggedInUser());
                    frame.getContentPane().removeAll();
                    frame.add(newBar, BorderLayout.NORTH);
                    frame.add(createPage);
                    frame.revalidate();
                    frame.repaint();
                });

                JButton viewClasses = new JButton("View My Classes");
                viewClasses.setAlignmentX(Component.CENTER_ALIGNMENT);
                viewClasses.addActionListener(ae -> {
                    TrainerClassesPage classesPage = new TrainerClassesPage(frame, trainer);
                    NavBar newBar = new NavBar(frame);
                    newBar.setLoggedInUser(this.getLoggedInUser());
                    frame.getContentPane().removeAll();
                    frame.add(newBar, BorderLayout.NORTH);
                    frame.add(classesPage);
                    frame.revalidate();
                    frame.repaint();
                });

                trainerPanel.add(title);
                trainerPanel.add(Box.createVerticalStrut(20));
                trainerPanel.add(createClass);
                trainerPanel.add(Box.createVerticalStrut(10));
                trainerPanel.add(viewClasses);

                NavBar newBar = new NavBar(frame);
                newBar.setLoggedInUser(this.getLoggedInUser());
                frame.add(newBar, BorderLayout.NORTH);
                frame.add(trainerPanel);
            } else {
                JPanel userPanel = new JPanel();
                userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
                userPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

                JLabel label = new JLabel("Class Dashboard (User)");
                label.setFont(new Font("Arial", Font.BOLD, 24));
                label.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel msg = new JLabel("You can browse or join classes here (feature TBD)");
                msg.setAlignmentX(Component.CENTER_ALIGNMENT);

                userPanel.add(label);
                userPanel.add(Box.createVerticalStrut(20));
                userPanel.add(msg);

                NavBar newBar = new NavBar(frame);
                newBar.setLoggedInUser(this.getLoggedInUser());
                frame.add(newBar, BorderLayout.NORTH);
                frame.add(userPanel);
            }

            frame.setTitle("Class Dashboard");
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
}
