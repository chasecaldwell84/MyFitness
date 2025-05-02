package MyFitness;

import MyFitness.RyanStuff.CalorieTracker;
import MyFitness.RyanStuff.CreateGoals;
import MyFitness.User.Admin;
import MyFitness.User.Settings;
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
            frame.add(new JLabel("Home Page"));
            frame.revalidate();
            frame.repaint();
        });

        NavBar thisNavBar = this;
        User currUser = frame.getUser();

        //change nav bar is trainer is logged in
        if(currUser instanceof MyFitness.User.Trainer){
            JButton trainerPageButton = new JButton("Trainer Page");
            trainerPageButton.addActionListener(e -> {
                frame.setTitle("Trainer Panel");
                frame.getContentPane().removeAll();
                frame.getContentPane().add(this);
                frame.add(new MyFitness.RyanStuff.TrainerPage(frame, this, (MyFitness.User.Trainer) currUser));
                frame.revalidate();
                frame.repaint();
            });
            add(trainerPageButton);
        }

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

        JButton socialButton = new JButton("Social");
        socialButton.addActionListener(e -> {
            JPanel socialPanel = new JPanel(new GridLayout(3, 1, 10, 10));

            JButton addFriendButton = new JButton("Add Friend");
            JButton sendChallengeButton = new JButton("Send Challenge");
            JButton viewChallengesButton = new JButton("View Challenges");

            addFriendButton.addActionListener(ae -> {
                String friendUsername = JOptionPane.showInputDialog(frame, "Enter friend's username:");
                if (friendUsername == null || friendUsername.trim().isEmpty()) {
                    return;
                }

                FriendManager fm = frame.getFriendManager();
                User currentUser = frame.getUser();

                // Find the friend by username from all known users (you'll need a way to access them)
                // For now, assume you have access to a list of all users:
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
                JOptionPane.showMessageDialog(frame, "Added Friend");
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
        add(socialButton);
        add(settings);
        add(statistics);

        User currentUser2 = frame.getUser();
        if(currentUser2 instanceof MyFitness.User.Trainer){
            JButton trainerPageButton = new JButton("Trainer Page");
            trainerPageButton.addActionListener(e -> {
                frame.setTitle("Trainer Page");
                frame.getContentPane().removeAll();
                frame.getContentPane().add(this);
                frame.add(new MyFitness.RyanStuff.TrainerPage(frame, this, (MyFitness.User.Trainer) currentUser2));
                frame.revalidate();
                frame.repaint();
            });
            add(trainerPageButton);
        }
    }
}
