//Author:Dannis Wu
package MyFitness.User;

import MyFitness.App;
import MyFitness.Database;

import javax.swing.*;
import java.awt.*;

public class UserClassDashboardPanel extends JPanel {
    private final App frame;
    private final GeneralUser user;

    public UserClassDashboardPanel(App frame, GeneralUser user) {
        this.frame = frame;
        this.user = user;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Class Dashboard (User)");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);

        add(Box.createVerticalStrut(30));

        JButton addClassButton = new JButton("Add Class");
        addClassButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addClassButton.addActionListener(e -> {
            AddClassPanel addPanel = new AddClassPanel(frame, user);
            frame.getContentPane().removeAll();
            frame.add(new MyFitness.NavBar(frame), BorderLayout.NORTH);
            frame.add(addPanel);
            frame.revalidate();
            frame.repaint();
        });

        JButton seeMyClassButton = new JButton("See My Class");
        seeMyClassButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        seeMyClassButton.addActionListener(e -> {
            SeeMyClassPanel seePanel = new SeeMyClassPanel(frame, user);
            frame.getContentPane().removeAll();
            frame.add(new MyFitness.NavBar(frame), BorderLayout.NORTH);
            frame.add(seePanel);
            frame.revalidate();
            frame.repaint();
        });

        add(addClassButton);
        add(Box.createVerticalStrut(15));
        add(seeMyClassButton);
    }
}

