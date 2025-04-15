package MyFitness;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserPage extends JPanel {

    private static App frame;
    public static JLabel passwordLabel;
    public static JButton showPassword;

    public UserPage(App frame){
        setSize(500,500);
        setLayout(new BorderLayout(10, 10));
        this.frame = frame;
        createGUI();
    }

    private void createGUI(){
        JLabel titleLabel = new JLabel("User Settings", SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(App.titleFont);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel,BoxLayout.Y_AXIS));
        titlePanel.add(Box.createVerticalStrut(80));
        titlePanel.add(titleLabel);

        JPanel userSettingsPanel = new JPanel();
        userSettingsPanel.setLayout(new BoxLayout(userSettingsPanel,BoxLayout.Y_AXIS));

        JLabel usernameTitle = new JLabel("Username:");
        JLabel passwordTitle = new JLabel("Password: ");

        JLabel usernameLabel = new JLabel(frame.getUser().getUserName());
        passwordLabel = new JLabel("(HIDDEN)");

        usernameTitle.setFont(App.boldLabelFontLarge);
        usernameTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordTitle.setFont(App.boldLabelFontLarge);
        passwordTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameLabel.setFont(App.labelFontLarge);
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordLabel.setFont(App.labelFontLarge);
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        userSettingsPanel.add(Box.createVerticalStrut(25));
        userSettingsPanel.add(usernameTitle);
        userSettingsPanel.add(usernameLabel);
        userSettingsPanel.add(Box.createVerticalStrut(25));
        userSettingsPanel.add(passwordTitle);
        userSettingsPanel.add(passwordLabel);

        userSettingsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(45, 250, 90, 250),
                BorderFactory.createLineBorder(Color.BLACK, 5)
        ));



        JPanel buttonPanel = new JPanel();

        JButton changeUserButton = new JButton("Change Username");
        showPassword = new JButton("Show Password");
        showPassword.addActionListener(new showPassword());
        buttonPanel.add(changeUserButton);
        buttonPanel.add(showPassword);

        add(titlePanel,BorderLayout.NORTH);
        add(userSettingsPanel,BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    static class showPassword implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("password button pressed");
            if(passwordLabel.getText().equals("(HIDDEN)")){
                System.out.println("show");
                passwordLabel.setText(frame.getUser().getPassword());
                showPassword.setText("Hide Password");
            }
            else {
                System.out.println("hide");
                passwordLabel.setText("(HIDDEN)");
                showPassword.setText("Show Password");
            }
            frame.revalidate();
            frame.repaint();
        }
    }

}
