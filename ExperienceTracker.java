//Author: Larry O'Connor

package MyFitness;

import javax.swing.*;
import java.awt.*;

public class ExperienceTracker {

    static int userLevel = 0;
    static int userXP = 0;
    static int nextLevelXP = 0;

    static Font labelFont = new Font("Arial", Font.BOLD, 20);
    static Font titleFont = new Font("Arial", Font.BOLD, 40);

    //Create Experience UI
    public static void createGUI(){
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Experience Tracker");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(300, 400);

            JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

            JLabel titleLabel = new JLabel("Experience", SwingConstants.CENTER);
            titleLabel.setFont(titleFont);
            mainPanel.add(titleLabel, BorderLayout.NORTH);

            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

            userLevel = userXP/100;

            JLabel levelLabel = new JLabel("Level: " + userLevel);
            JLabel progressLabel = new JLabel("Next Level: " + (userXP-userLevel*100) + "/" + (100));
            JLabel totalXpLabel = new JLabel("Total XP: " + userXP);

            levelLabel.setFont(labelFont);
            progressLabel.setFont(labelFont);
            totalXpLabel.setFont(labelFont);

            levelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            progressLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            totalXpLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            infoPanel.add(Box.createVerticalStrut(25));
            infoPanel.add(levelLabel);
            infoPanel.add(Box.createVerticalStrut(40));
            infoPanel.add(progressLabel);
            infoPanel.add(Box.createVerticalStrut(40));
            infoPanel.add(totalXpLabel); //xp info border outline
            infoPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(0, 10, 0, 10),
                    BorderFactory.createLineBorder(Color.BLACK, 2)
            ));

            mainPanel.add(infoPanel, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            JButton exitButton = new JButton("back");
            JButton leaderboardButton = new JButton("leaderboard");

            //add buttons
            buttonPanel.add(exitButton);
            buttonPanel.add(leaderboardButton);

            mainPanel.add(buttonPanel, BorderLayout.SOUTH);

            //show frame
            frame.add(mainPanel);
            frame.setLocationRelativeTo(null); //center on screen
            frame.setVisible(true);
        });
    }
    public static void main(String[] args) {
        createGUI();
    }
}

