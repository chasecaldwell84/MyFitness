
package MyFitness;

import MyFitness.RyanStuff.CalorieTracker;
import MyFitness.RyanStuff.CreateGoals;
import MyFitness.Settings.*;
import MyFitness.Statistics.StatisticsPage;
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

        JButton CalorieTracker = new JButton("Stats Tracker");
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
            JPanel socialPanel = new JPanel();
            socialPanel.setLayout(new BoxLayout(socialPanel, BoxLayout.Y_AXIS));
            socialPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel titleLabel = new JLabel("Social Dashboard");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            socialPanel.add(titleLabel);
            socialPanel.add(Box.createVerticalStrut(20));

            JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 15, 15));
            JButton addFriendButton = new JButton("➕ Add Friend");
            JButton viewFriendsButton = new JButton("👥 View Friends");
            JButton sendChallengeButton = new JButton("🏁 Send Challenge");
            JButton viewChallengesButton = new JButton("📋 View Challenges");

            addFriendButton.setToolTipText("Find and add a new friend");
            viewFriendsButton.setToolTipText("See your current friends list");
            sendChallengeButton.setToolTipText("Send a challenge to a friend");
            viewChallengesButton.setToolTipText("View all incoming challenges");

            buttonPanel.add(addFriendButton);
            buttonPanel.add(viewFriendsButton);
            buttonPanel.add(sendChallengeButton);
            buttonPanel.add(viewChallengesButton);

            socialPanel.add(buttonPanel);
            socialPanel.add(Box.createVerticalStrut(15));

            addFriendButton.addActionListener(ae -> {
                String friendUsername = JOptionPane.showInputDialog(frame, "Enter friend's username:");
                if (friendUsername == null || friendUsername.trim().isEmpty()) return;

                FriendManager fm = frame.getFriendManager();
                User currentUser = frame.getUser();

                User friend = frame.getAllUsers().stream()
                        .filter(u -> u.getUserName().equalsIgnoreCase(friendUsername.trim()))
                        .findFirst().orElse(null);

                if (friend == null) {
                    JOptionPane.showMessageDialog(frame, "User not found.");
                    return;
                }

                fm.addFriend(currentUser, friend);
                JOptionPane.showMessageDialog(frame, friend.getUserName() + " added as a friend!");
            });

            viewFriendsButton.addActionListener(ae -> {
                java.util.List<User> friends = frame.getFriendManager().getFriends(frame.getUser());
                if (friends.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "No friends found.", "Your Friends", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    StringBuilder list = new StringBuilder();
                    for (User friend : friends) {
                        list.append(friend.getUserName()).append("\n");
                    }
                    JOptionPane.showMessageDialog(frame, list.toString(), "Your Friends", JOptionPane.INFORMATION_MESSAGE);
                }
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
                if (challenges.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "No challenges available.", "Your Challenges", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, String.join("\n", challenges), "Your Challenges", JOptionPane.INFORMATION_MESSAGE);
                }
            });

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
                frame.add(new AdminPage(frame));
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
        frame.getContentPane().add(this);
        HomePage home = new HomePage(frame);
        frame.add(home);
        frame.revalidate();
        frame.repaint();
    }
}
