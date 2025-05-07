package MyFitness;

import MyFitness.RyanStuff.CalorieTracker;
import MyFitness.RyanStuff.CreateGoals;
import MyFitness.User.Admin;
import MyFitness.User.Settings;
import MyFitness.User.User;
import MyFitness.User.GeneralUser;

import javax.swing.*;
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
        socialButton.addActionListener(e -> showSocialPanel());

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

    private void showSocialPanel() {
        JPanel socialPanel = new JPanel();
        socialPanel.setLayout(new BoxLayout(socialPanel, BoxLayout.Y_AXIS));
        socialPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Social Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(33, 37, 41)); // dark gray
        socialPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
        JButton addFriendButton = styledButton("➕ Add Friend");
        JButton viewFriendsButton = styledButton("👥 View Friends");
        JButton sendChallengeButton = styledButton("🏁 Send Challenge");
        JButton viewChallengesButton = styledButton("📋 View Challenges");
        JButton browseGroupsButton = styledButton("🌐 Browse Groups");

        //JLabel statusLabel = new JLabel(" ");
        //statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        statusLabel.setForeground(Color.GRAY);
        socialPanel.add(statusLabel, BorderLayout.SOUTH);

        // Add tooltips
        addFriendButton.setToolTipText("Find and add a new friend");
        viewFriendsButton.setToolTipText("See your current friends list");
        sendChallengeButton.setToolTipText("Send a challenge to a friend");
        viewChallengesButton.setToolTipText("View all incoming challenges");
        browseGroupsButton.setToolTipText("Explore available fitness and social groups");

        // Add buttons
        buttonPanel.add(addFriendButton);
        buttonPanel.add(viewFriendsButton);
        buttonPanel.add(sendChallengeButton);
        buttonPanel.add(viewChallengesButton);
        buttonPanel.add(browseGroupsButton);

        socialPanel.add(buttonPanel);
        socialPanel.add(Box.createVerticalStrut(15));
        socialPanel.add(statusLabel);

        socialPanel.add(buttonPanel, BorderLayout.CENTER);


        addFriendButton.addActionListener(ae -> {
            FriendManager fm = frame.getFriendManager();
            User currentUser = frame.getUser();
            List<User> allUsers = Database.getInstance().getAllUsers();
            List<User> currentFriends = fm.getFriends(currentUser);

            List<User> selectableUsers = new ArrayList<>();
            for (User u : allUsers) {
                if (!u.getUserName().equals(currentUser.getUserName()) &&
                        currentFriends.stream().noneMatch(f -> f.getUserName().equals(u.getUserName()))) {
                    selectableUsers.add(u);
                }
            }

            if (selectableUsers.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No available users to add as friends.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String[] userOptions = selectableUsers.stream()
                    .map(User::getUserName)
                    .toArray(String[]::new);

            String selected = (String) JOptionPane.showInputDialog(
                    frame,
                    "Select a user to add as friend:",
                    "Add Friend",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    userOptions,
                    userOptions[0]
            );

            if (selected != null) {
                User chosen = selectableUsers.stream()
                        .filter(u -> u.getUserName().equals(selected))
                        .findFirst().orElse(null);

                if (chosen != null) {
                    fm.addFriend(currentUser, chosen);
                    statusLabel.setText(selected + " added as a friend!");
                    showSocialPanel();
                }
            }
        });

        viewFriendsButton.addActionListener(ae -> {
            FriendManager fm = frame.getFriendManager();
            User currentUser = frame.getUser();
            List<User> friends = fm.getFriends(currentUser);
            if (friends.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "You have no friends yet.", "Your Friends", JOptionPane.INFORMATION_MESSAGE);
            } else {
                StringBuilder list = new StringBuilder("Your Friends:\n");
                for (User friend : friends) {
                    list.append("• ").append(friend.getUserName()).append("\n");
                }
                JOptionPane.showMessageDialog(frame, list.toString(), "Your Friends", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        sendChallengeButton.addActionListener(ae -> {
            FriendManager fm = frame.getFriendManager();
            User currentUser = frame.getUser();
            List<User> friends = fm.getFriends(currentUser);

            if (friends.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No friends available to challenge.", "Send Challenge", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String[] options = friends.stream().map(User::getUserName).toArray(String[]::new);
            String selectedFriend = (String) JOptionPane.showInputDialog(
                    frame,
                    "Select a friend to challenge:",
                    "Send Challenge",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (selectedFriend != null) {
                String challengeText = JOptionPane.showInputDialog(frame, "Enter your challenge:");

                if (challengeText != null && !challengeText.trim().isEmpty()) {
                    User recipient = friends.stream()
                            .filter(f -> f.getUserName().equals(selectedFriend))
                            .findFirst().orElse(null);
                    if (recipient != null) {
                        fm.sendChallenge(currentUser, recipient, challengeText.trim());
                        JOptionPane.showMessageDialog(frame, "Challenge sent to " + selectedFriend + "!");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Challenge cannot be empty.");
                }
            }
        });

        viewChallengesButton.addActionListener(ae -> {
            List<String> challenges = frame.getFriendManager().getChallenges(frame.getUser());
            if (challenges.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No challenges available.", "Your Challenges", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, String.join("\n", challenges), "Your Challenges", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        browseGroupsButton.addActionListener(ae -> {
            GroupManager gm = frame.getGroupManager();
            User currentUser = frame.getUser();
            Set<String> allGroups = gm.getAllGroupNames();

            String[] options = allGroups.toArray(new String[0]);
            String selectedGroup = (String) JOptionPane.showInputDialog(
                    frame,
                    "Select a group to join or view members:",
                    "Browse Groups",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options.length > 0 ? options[0] : null
            );

            if (selectedGroup != null) {
                if (!gm.isUserInGroup(currentUser, selectedGroup)) {
                    int confirm = JOptionPane.showConfirmDialog(frame,
                            "You are not in this group. Join now?",
                            "Join Group",
                            JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        gm.joinGroup(currentUser, selectedGroup);
                        JOptionPane.showMessageDialog(frame, "Joined group: " + selectedGroup);
                    }
                } else {
                    Set<User> members = gm.getMembers(selectedGroup);
                    StringBuilder memberList = new StringBuilder("Members in " + selectedGroup + ":\n");
                    for (User member : members) {
                        memberList.append("• ").append(member.getUserName()).append("\n");
                    }
                    JOptionPane.showMessageDialog(frame, memberList.toString());
                }
            }
        });

        frame.getContentPane().removeAll();
        frame.getContentPane().add(this);
        frame.add(socialPanel);
        frame.setTitle("Social Menu");
        frame.revalidate();
        frame.repaint();
    }

    private JButton styledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        button.setFocusPainted(false);
        button.setBackground(new Color(240, 240, 240));
        button.setForeground(new Color(33, 37, 41));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

}
