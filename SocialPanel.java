package MyFitness;

import MyFitness.User.Trainer;
import MyFitness.User.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SocialPanel extends JPanel {
    public SocialPanel(App frame) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Social Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(33, 37, 41)); // dark gray
        //this.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
        JButton addFriendButton = styledButton("➕ Add Friend");
        JButton manageFriendsButton = styledButton("👥 Manage Friends");
        JButton sendChallengeButton = styledButton("🏁 Send Challenge");
        JButton viewChallengesButton = styledButton("📋 View Challenges");
        JButton manageGroupsButton = styledButton("🌐 Manage Groups");
        JButton manageClassesButton = styledButton("📚 Manage Classes");

        //JLabel statusLabel = new JLabel(" ");
        //statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        statusLabel.setForeground(Color.GRAY);
        this.add(statusLabel, BorderLayout.SOUTH);

        // Add tooltips
        addFriendButton.setToolTipText("Find and add a new friend");
        manageFriendsButton.setToolTipText("See your current friends list");
        sendChallengeButton.setToolTipText("Send a challenge to a friend");
        viewChallengesButton.setToolTipText("View all incoming challenges");
        manageGroupsButton.setToolTipText("Explore available fitness and social groups");
        manageClassesButton.setToolTipText("Explore available fitness and social classes");

        // Add buttons
        buttonPanel.add(addFriendButton);
        buttonPanel.add(manageFriendsButton);
        buttonPanel.add(sendChallengeButton);
        buttonPanel.add(viewChallengesButton);
        buttonPanel.add(manageGroupsButton);
        buttonPanel.add(manageClassesButton);

        this.add(buttonPanel);
        this.add(Box.createVerticalStrut(15));
        this.add(statusLabel);

        this.add(buttonPanel, BorderLayout.CENTER);


        addFriendButton.addActionListener(ae -> {
            FriendManager fm = frame.getFriendManager();
            User currentUser = frame.getUser();
            java.util.List<User> allUsers = Database.getInstance().getAllUsers();
            java.util.List<User> currentFriends = fm.getFriends(currentUser);

            java.util.List<User> selectableUsers = new ArrayList<>();
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
                    // frame.setContentPane( new SocialPanel(frame, navbar));
                   // frame.getContentPane().removeAll();
                    //frame.add(navbar, BorderLayout.NORTH);
                    //frame.add(new SocialPanel(frame), BorderLayout.CENTER);
                   // frame.revalidate();
                   // frame.repaint();
                }
            }
        });

        manageFriendsButton.addActionListener(ae -> {
            FriendManager fm = frame.getFriendManager();
            User currentUser = frame.getUser();
            java.util.List<User> friends = fm.getFriends(currentUser);

            if (friends.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "You have no friends yet.", "Manage Friends", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            JDialog dialog = new JDialog(frame, "Manage Friends", true);
            dialog.setLayout(new BorderLayout());
            dialog.setSize(400, 400);
            dialog.setLocationRelativeTo(frame);

            JPanel listPanel = new JPanel();
            listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

            for (User friend : friends) {
                JPanel row = new JPanel(new BorderLayout(10, 10));
                JLabel name = new JLabel(friend.getUserName());
                JButton unfriendBtn = new JButton("Unfriend");

                unfriendBtn.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(
                            dialog,
                            "Are you sure you want to unfriend " + friend.getUserName() + "?",
                            "Confirm Unfriend",
                            JOptionPane.YES_NO_OPTION
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        fm.removeFriend(currentUser, friend);
                        dialog.dispose();
                        // Refresh list
                        manageFriendsButton.doClick();
                    }
                });

                row.add(name, BorderLayout.WEST);
                row.add(unfriendBtn, BorderLayout.EAST);
                row.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                listPanel.add(row);
            }

            JScrollPane scroll = new JScrollPane(listPanel);
            dialog.add(scroll, BorderLayout.CENTER);

            JButton closeBtn = new JButton("Close");
            closeBtn.addActionListener(e -> dialog.dispose());
            dialog.add(closeBtn, BorderLayout.SOUTH);

            dialog.setVisible(true);
        });

        sendChallengeButton.addActionListener(ae -> {
            FriendManager fm = frame.getFriendManager();
            User currentUser = frame.getUser();
            java.util.List<User> friends = fm.getFriends(currentUser);

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
            java.util.List<String> challenges = frame.getFriendManager().getChallenges(frame.getUser());
            if (challenges.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No challenges available.", "Your Challenges", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, String.join("\n", challenges), "Your Challenges", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        manageGroupsButton.addActionListener(ae -> {
            GroupManager gm = frame.getGroupManager();
            User currentUser = frame.getUser();

            String[] groupOptions = {"Create Group", "View Available Groups", "View My Groups"};
            String action = (String) JOptionPane.showInputDialog(frame, "Select an action:", "Group Options",
                    JOptionPane.PLAIN_MESSAGE, null, groupOptions, groupOptions[0]);

            if (action == null) return;

            switch (action) {
                case "Create Group":
                    String newGroupName = JOptionPane.showInputDialog(frame, "Enter new group name:");
                    if (newGroupName != null && !newGroupName.trim().isEmpty()) {
                        gm.joinGroup(currentUser, newGroupName.trim());
                        JOptionPane.showMessageDialog(frame, "Group '" + newGroupName + "' created. You are now the owner.");
                    }
                    break;

                case "View Available Groups":
                    Set<String> allGroups = gm.getAllGroupNames();
                    String[] availableOptions = allGroups.toArray(new String[0]);
                    String selectedGroup = (String) JOptionPane.showInputDialog(
                            frame, "Select group to join or view:", "Available Groups",
                            JOptionPane.PLAIN_MESSAGE, null, availableOptions,
                            availableOptions.length > 0 ? availableOptions[0] : null
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
                            StringBuilder sb = new StringBuilder("Members in " + selectedGroup + ":\n");
                            for (User member : members) {
                                sb.append("• ").append(member.getUserName());
                                if (!member.getUserName().equals(currentUser.getUserName())) {
                                    sb.append("  [Message] [Challenge]");
                                } else {
                                    sb.append("  (You)");
                                }
                                sb.append("\n");
                            }
                            JOptionPane.showMessageDialog(frame, sb.toString());
                        }
                    }
                    break;

                case "View My Groups":
                    Set<String> myGroups = new HashSet<>();
                    for (String group : gm.getAllGroupNames()) {
                        if (gm.isUserInGroup(currentUser, group)) myGroups.add(group);
                    }
                    if (myGroups.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "You are not a member of any groups.");
                        return;
                    }
                    String[] myOptions = myGroups.toArray(new String[0]);
                    String mySelected = (String) JOptionPane.showInputDialog(
                            frame, "Select a group to manage:", "My Groups",
                            JOptionPane.PLAIN_MESSAGE, null, myOptions, myOptions[0]
                    );
                    if (mySelected != null) {
                        Set<User> members = gm.getMembers(mySelected);
                        JPanel memberPanel = new JPanel();
                        memberPanel.setLayout(new BoxLayout(memberPanel, BoxLayout.Y_AXIS));

                        JTextField challengeField = new JTextField();
                        JButton postChallengeBtn = new JButton("Post Challenge to Group");
                        postChallengeBtn.addActionListener(e -> {
                            String msg = challengeField.getText().trim();
                            if (!msg.isEmpty()) {
                                gm.postGroupChallenge(mySelected, currentUser.getUserName(), msg);
                                JOptionPane.showMessageDialog(frame, "Challenge posted to group!");
                            }
                        });

                        memberPanel.add(new JLabel("Group Challenge:"));
                        memberPanel.add(challengeField);
                        memberPanel.add(postChallengeBtn);

                        for (User m : members) {
                            JPanel row = new JPanel(new BorderLayout());
                            row.add(new JLabel(m.getUserName()), BorderLayout.WEST);
                            if (!m.getUserName().equals(currentUser.getUserName())) {
                                JButton messageBtn = new JButton("Message");
                                messageBtn.addActionListener(e -> {
                                    String msg = JOptionPane.showInputDialog(frame, "Send message to " + m.getUserName() + ":");
                                    if (msg != null && !msg.trim().isEmpty()) {
                                        gm.sendGroupMessage(mySelected, currentUser.getUserName(), m.getUserName(), msg);
                                        JOptionPane.showMessageDialog(frame, "Message sent to " + m.getUserName());
                                    }
                                });
                                JButton kickBtn = new JButton("Kick");
                                kickBtn.addActionListener(e -> {
                                    int confirm = JOptionPane.showConfirmDialog(frame, "Kick " + m.getUserName() + "?", "Confirm", JOptionPane.YES_NO_OPTION);
                                    if (confirm == JOptionPane.YES_OPTION) {
                                        gm.leaveGroup(m, mySelected);
                                        JOptionPane.showMessageDialog(frame, m.getUserName() + " was removed.");
                                    }
                                });
                                JPanel actions = new JPanel();
                                actions.add(messageBtn);
                                actions.add(kickBtn);
                                row.add(actions, BorderLayout.EAST);
                            }
                            memberPanel.add(row);
                        }

                        JOptionPane.showMessageDialog(frame, memberPanel, "Group Members: " + mySelected, JOptionPane.PLAIN_MESSAGE);
                    }
                    break;
            }
        });

        manageClassesButton.addActionListener(ae -> {
            java.util.List<User> users = Database.getInstance().getAllUsers();
            List<Trainer> trainers = new ArrayList<>();
            for (User u : users) if (u instanceof Trainer) trainers.add((Trainer) u);

            if (trainers.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No trainer classes available.", "Manage Classes", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String[] trainerNames = trainers.stream().map(Trainer::getUserName).toArray(String[]::new);
            String selectedTrainer = (String) JOptionPane.showInputDialog(frame, "Choose Trainer:", "Trainer Selection", JOptionPane.PLAIN_MESSAGE, null, trainerNames, trainerNames[0]);

            if (selectedTrainer != null) {
                String[] availableClasses = Database.getInstance().getTrainerClasses(selectedTrainer);

                if (availableClasses.length == 0) {
                    JOptionPane.showMessageDialog(frame, "No classes offered by this trainer.");
                    return;
                }

                String selectedClass = (String) JOptionPane.showInputDialog(frame, "Choose a class to join:", "Class Selection", JOptionPane.PLAIN_MESSAGE, null, availableClasses, availableClasses[0]);

                if (selectedClass != null) {
                    String description = Database.getInstance().getClassDescription(selectedClass);
                    int confirm = JOptionPane.showConfirmDialog(frame, description + "\n\nWould you like to request to join?", "Join Class", JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        Database.getInstance().requestJoinClass(frame.getUser().getUserName(), selectedClass);
                        JOptionPane.showMessageDialog(frame, "Request to join '" + selectedClass + "' sent.");
                    }
                }
            }
        });


        //frame.getContentPane().removeAll();
        //frame.getContentPane().add(this);
        //frame.add(this);
        //frame.setTitle("Social Menu");
        //frame.revalidate();
        //frame.repaint();
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
