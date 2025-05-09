package MyFitness;

import MyFitness.Settings.AdminPage;
import MyFitness.RyanStuff.CalorieTracker;
import MyFitness.RyanStuff.CreateGoals;
import MyFitness.Settings.UserPage;
import MyFitness.Statistics.StatisticsPage;
import MyFitness.User.Admin;
import MyFitness.User.User;

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
            JButton browseGroupsButton = new JButton("🌐 Browse Groups");

            // Add hover tooltips
            addFriendButton.setToolTipText("Find and add a new friend");
            viewFriendsButton.setToolTipText("See your current friends list");
            sendChallengeButton.setToolTipText("Send a challenge to a friend");
            viewChallengesButton.setToolTipText("View all incoming challenges");
            browseGroupsButton.setToolTipText("Explore available fitness and social groups");

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
                    JOptionPane.showMessageDialog(frame, "MyFitness.User not found.");
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
        //NOTE: pannel has to be created in the actionListner or else it doesnt update the information
        settings.addActionListener(e -> {
            frame.setTitle("Settings");


            frame.getContentPane().removeAll();
            frame.getContentPane().add(this);

            if(frame.getUser() instanceof Admin) {
                AdminPage adminPage = new AdminPage(frame);
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
