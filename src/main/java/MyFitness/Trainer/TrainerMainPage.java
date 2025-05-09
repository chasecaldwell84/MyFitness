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
        JLabel titleLabel = new JLabel("Class Dashboard (Trainer)", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));  // match NavBar styling
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);  // ensure proper centering

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));  // spacing from navbar
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(20));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 100, 20, 100));

        JButton createClassButton = new JButton("Create New Class");
        JButton viewMyClassesButton = new JButton("View My Classes");

        createClassButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewMyClassesButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(createClassButton);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(viewMyClassesButton);

        createClassButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.add(new NavBar(frame), BorderLayout.NORTH);
            frame.add(new CreateClassPage(frame, trainer));
            frame.revalidate();
            frame.repaint();
        });

        viewMyClassesButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.add(new NavBar(frame), BorderLayout.NORTH);
            frame.add(new TrainerClassesPage(frame, trainer));
            frame.revalidate();
            frame.repaint();
        });

        // Final layout
        setLayout(new BorderLayout());
        add(titlePanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }


}
