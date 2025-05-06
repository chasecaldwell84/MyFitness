package MyFitness.Trainer;

import MyFitness.App;
import MyFitness.NavBar;
import MyFitness.User.Trainer;

import javax.swing.*;
import java.awt.*;

public class TrainerMainPage extends JPanel {

    private App frame;
    private NavBar navBar;
    private Trainer trainer;

    public TrainerMainPage(App frame, NavBar navBar, Trainer trainer) {
        this.frame = frame;
        this.navBar = navBar;
        this.trainer = trainer;

        setSize(800, 600);
        setLayout(new BorderLayout(10, 10));
        createGUI();
    }

    private void createGUI() {
        JLabel titleLabel = new JLabel("Trainer Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(App.titleFont);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.add(Box.createVerticalStrut(50));
        titlePanel.add(titleLabel);

        // Class Management Buttons
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        JButton createClassButton = new JButton("Create New Class");
        JButton viewMyClassesButton = new JButton("View My Classes");

        createClassButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewMyClassesButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(Box.createVerticalStrut(30));
        centerPanel.add(createClassButton);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(viewMyClassesButton);

        createClassButton.addActionListener(e -> {
            CreateClassPage createClassPage = new CreateClassPage(frame, trainer);
            frame.getContentPane().removeAll();
            frame.add(new NavBar(frame), BorderLayout.NORTH);
            frame.add(createClassPage);
            frame.revalidate();
            frame.repaint();
        });

        viewMyClassesButton.addActionListener(e -> {
            TrainerClassesPage trainerClassesPage = new TrainerClassesPage(frame, trainer);
            frame.getContentPane().removeAll();
            frame.add(new NavBar(frame), BorderLayout.NORTH);
            frame.add(trainerClassesPage);
            frame.revalidate();
            frame.repaint();
        });

        // Reuse user settings panel
        JPanel bottomPanel = new JPanel();
        JButton changeUsernameButton = new JButton("Change Username");
        JButton showPasswordButton = new JButton("Show Password");

        bottomPanel.add(changeUsernameButton);
        bottomPanel.add(showPasswordButton);

        add(titlePanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}
