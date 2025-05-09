package MyFitness;

import MyFitness.User.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ChallengeSender extends JPanel {
    private JComboBox<String> friendDropdown;
    private JTextField challengeInput;
    private JButton sendButton;

    private FriendManager friendManager;
    private User currentUser;
    private List<User> friendList;

    public ChallengeSender(FriendManager fm, User currentUser) {
        this.friendManager = fm;
        this.currentUser = currentUser;
        this.friendList = friendManager.getFriends(currentUser);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Send Challenge", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(titleLabel, gbc);

        // Friend selector
        JLabel friendLabel = new JLabel("Select a Friend:");
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(friendLabel, gbc);

        friendDropdown = new JComboBox<>();
        for (User friend : friendList) {
            friendDropdown.addItem(friend.getUserName());
        }
        gbc.gridx = 1;
        add(friendDropdown, gbc);

        // Challenge input
        JLabel challengeLabel = new JLabel("Challenge:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(challengeLabel, gbc);

        challengeInput = new JTextField();
        gbc.gridx = 1;
        add(challengeInput, gbc);

        // Send button
        sendButton = new JButton("Send Challenge");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(sendButton, gbc);

        // Button logic
        sendButton.addActionListener((ActionEvent e) -> {
            String friendName = (String) friendDropdown.getSelectedItem();
            String challenge = challengeInput.getText().trim();

            if (challenge.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Challenge cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            User friendUser = friendList.stream()
                    .filter(u -> u.getUserName().equals(friendName))
                    .findFirst()
                    .orElse(null);

            if (friendUser != null) {
                friendManager.sendChallenge(currentUser, friendUser, challenge);
                JOptionPane.showMessageDialog(this,
                        "Challenge sent to " + friendName + ":\n" + challenge,
                        "Challenge Sent",
                        JOptionPane.INFORMATION_MESSAGE);
                challengeInput.setText("");
            }
        });
    }

   /* public static void main(String[] args) {
        // test
        SwingUtilities.invokeLater(() -> {
            User chase = new MyFitness.User.GeneralUser("chase", "pass123");
            User ryan = new MyFitness.User.GeneralUser("ryan", "pass456");
            User michael = new MyFitness.User.GeneralUser("michael", "pass789");

            FriendManager fm = new FriendManager();
            fm.addFriend(chase, ryan);
            fm.addFriend(chase, michael);

            JFrame frame = new JFrame("Challenge Sender");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 300);
            frame.setLocationRelativeTo(null);
            frame.add(new ChallengeSender(fm, chase));
            frame.setVisible(true);
        });
    }*/
}