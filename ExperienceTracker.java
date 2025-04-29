//Author: Larry O'Connor

package MyFitness;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExperienceTracker {

    private static int userLevel = 0;
    private static int userXP = 0;
    private static int nextLevelXP = 0;

    static Font labelFont = new Font("Arial", Font.BOLD, 20);
    static Font titleFont = new Font("Arial", Font.BOLD, 40);

    JFrame frame;


    public ExperienceTracker(){
        createGUI();
    }

    //Create Experience UI
    public void createGUI(){
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Experience Tracker");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
            exitButton.addActionListener(new closeXPUI());
            buttonPanel.add(leaderboardButton);

            mainPanel.add(buttonPanel, BorderLayout.SOUTH);

            //show frame
            frame.add(mainPanel);
            frame.setLocationRelativeTo(null); //center on screen
            frame.setVisible(true);
        });
    }

    class closeXPUI implements  ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.dispose();
        }
    }



    public static void main(String[] args) {
    }
}

