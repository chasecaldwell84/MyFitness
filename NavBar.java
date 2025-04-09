package MyFitness;

import javax.swing.*;
import java.awt.*;

public class NavBar extends JPanel {
    public NavBar(JFrame frame) {
        //NOTE: TESTING
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
        JButton exerciseButton = new JButton("Exercise Journal");
        exerciseButton.addActionListener(e -> {
            //FIXME need to do this for the different pages
            frame.getContentPane().removeAll();
            frame.getContentPane().add(this);
            frame.add(new JLabel("Exercise Journal"));
            frame.revalidate();
            frame.repaint();

        });

        JButton goalButton = new JButton("Goals");
        goalButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.getContentPane().add(this);
            frame.add(new JLabel("Goals"));
            frame.revalidate();
            frame.repaint();
        });
        add(Home);
        add(exerciseButton);
        add(goalButton);

    }
}
