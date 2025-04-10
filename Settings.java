package MyFitness;

import MyFitness.User.User;

import javax.swing.*;
import java.awt.*;

public class Settings extends JPanel {

    public Settings(App frame) {
        frame.setTitle("Settings");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(frame.getWidth(), frame.getHeight());
        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        JLabel title = new JLabel("Settings", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        add(title, c);

        JLabel username = new JLabel(frame.getUser().getUserName(), JLabel.RIGHT);
        JLabel pw = new JLabel(frame.getUser().getPassword(), JLabel.RIGHT);

        add(username);
        //System.out.println(username.getText() + " :HELP");
        add(pw);
        //System.out.println(pw.getText());
    }
}
