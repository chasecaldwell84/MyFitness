package MyFitness.Settings;

import MyFitness.App;
import MyFitness.User.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserPage extends JPanel {

    private App frame;
    private AdminPage adminPage;

    public JLabel passwordLabel;
    public JButton showPassword;

    private User viewedUser;


    public UserPage(App frame){
        this.viewedUser = frame.getUser();
        setSize(500,500);
        setLayout(new BorderLayout(10, 10));
        this.frame = frame;
        createGUI();
    }

    public UserPage(App frame, User viewedUser,AdminPage adminPage){
        this.adminPage = adminPage;
        this.viewedUser = viewedUser;
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

        JLabel usernameTitle = new JLabel("Username");
        JLabel userTypeTitle = new JLabel("User Type");
        JLabel passwordTitle = new JLabel("Password");

        JLabel usernameLabel = new JLabel(viewedUser.getUserName());
        JLabel userTypeLabel = new JLabel(viewedUser.getClass().getSimpleName());
        passwordLabel = new JLabel("(HIDDEN)");

        usernameTitle.setFont(App.boldLabelFontLarge);
        usernameTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        userTypeTitle.setFont(App.boldLabelFontLarge);
        userTypeTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordTitle.setFont(App.boldLabelFontLarge);
        passwordTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameLabel.setFont(App.labelFontLarge);
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userTypeLabel.setFont(App.labelFontLarge);
        userTypeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordLabel.setFont(App.labelFontLarge);
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        userSettingsPanel.add(Box.createVerticalStrut(25));
        userSettingsPanel.add(usernameTitle);
        userSettingsPanel.add(usernameLabel);
        userSettingsPanel.add(Box.createVerticalStrut(25));
        userSettingsPanel.add(userTypeTitle);
        userSettingsPanel.add(userTypeLabel);
        userSettingsPanel.add(Box.createVerticalStrut(25));
        userSettingsPanel.add(passwordTitle);
        userSettingsPanel.add(passwordLabel);

        userSettingsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(45, 250, 90, 250),
                BorderFactory.createLineBorder(Color.BLACK, 5)
        ));



        JPanel buttonPanel = new JPanel();

        if(frame.getUser() instanceof Admin && !((viewedUser instanceof Admin) && !(viewedUser.getUserName().equals(frame.getUser().getUserName())))){
            JButton changePasswordButton = new JButton("Change Password");
            changePasswordButton.addActionListener(new changePassword());
            buttonPanel.add(changePasswordButton);
        }
        showPassword = new JButton("Show Password");
        showPassword.addActionListener(new showPassword());
        buttonPanel.add(showPassword);
        if(frame.getUser() instanceof Admin){
            JButton backButton = new JButton("Back");
            backButton.addActionListener(new backToAdminPage(this));
            buttonPanel.add(backButton);

        }

        add(titlePanel,BorderLayout.NORTH);
        add(userSettingsPanel,BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private class showPassword implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(passwordLabel.getText().equals("(HIDDEN)")){
                System.out.println("show");
                passwordLabel.setText(viewedUser.getPassword());
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

    private class changePassword implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String newPassword = JOptionPane.showInputDialog(
                    frame,
                    "Enter New Password:",
                    "Change Password",
                    JOptionPane.PLAIN_MESSAGE
            );

            int confirmation = JOptionPane.showConfirmDialog(
                    frame,
                    "Confirm Password Change:",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirmation == JOptionPane.YES_OPTION) {
                viewedUser.setPassword(newPassword);
                if(!passwordLabel.getText().equals("(HIDDEN)")){
                    passwordLabel.setText(viewedUser.getPassword());
                }
            }
        }
    }
    private class backToAdminPage implements ActionListener {

        UserPage userPage;

        public backToAdminPage(UserPage userPage){
            this.userPage = userPage;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            frame.getContentPane().remove(userPage);
            frame.getContentPane().add(adminPage);
            frame.revalidate();
            frame.repaint();
        }
    }

}
