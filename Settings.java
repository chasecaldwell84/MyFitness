package MyFitness;

import javax.swing.*;
import java.awt.*;

public class Settings extends JPanel {
    private User userType = null;

    public Settings(App frame) {
        frame.setTitle("Settings");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(frame.getWidth(), frame.getHeight());
        setLayout(new GridBagLayout());

        userType = frame.getUser();

        GridBagConstraints c = new GridBagConstraints();
        JLabel title = new JLabel("Settings", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        add(title, c);

        JLabel username = new JLabel(frame.getUser().userName, JLabel.RIGHT);
        JLabel pw = new JLabel(frame.getUser().password, JLabel.RIGHT);

        add(username, c);
        System.out.println(username.getText() + " :HELP");
        add(pw, c);
        System.out.println(pw.getText());
    }
}
