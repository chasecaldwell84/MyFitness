package MyFitness.LoginSignUp;

import MyFitness.App;

import javax.swing.*;
import java.awt.*;

public class LandingPage extends JFrame {

    public LandingPage(App app) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("MyFitness");
        getContentPane().setLayout(null);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        //add logo image
        ImageIcon logoIcon = new ImageIcon("resources/images/MyFitnessLogoChose1.jpg");
        Image image = logoIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(image));

        GridBagConstraints logoConstraints = new GridBagConstraints();
        logoConstraints.gridx = 0;
        logoConstraints.gridy = 0;
        logoConstraints.gridwidth = 2;
        logoConstraints.insets = new Insets(10, 0, 10, 0); // Add spacing
        add(logoLabel, logoConstraints);
        //until here

        JLabel title = new JLabel("Welcome to MyFitness!");
        title.setFont(new Font("Arial", Font.BOLD, 30));
        //title.setBounds(70, 10, 500, 50);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        add(title, c);

        JButton login = new JButton("Login");
        Login loginPanel = new Login();
        /*loginPanel.setLayout(new FlowLayout(FlowLayout.LEFT));*/
        //login.setBounds(150, 235, 100, 30);
        login.addActionListener(e -> {
            loginPanel.setVisible(true);
            if(loginPanel.getAuthenticated()){
                loginPanel.dispose();
                app.setUser(loginPanel.getUser());
                //NOTE TESTING
                /*app.setTitle(loginPanel.getUser().userName);
                System.out.println("testing: " + app.getUser().userName);*/

                dispose();
                app.setVisible(true);
            }
        });

        JButton signUp = new JButton("Sign Up");
        SignUp signUpPanel = new SignUp();
        //signUp.setBounds(150, 265, 100, 30);
        signUp.addActionListener(e -> {
            signUpPanel.setVisible(true);
        });

        /*pack();*/
        GridBagConstraints loginPlace = new GridBagConstraints();
        loginPlace.gridx = 0;
        loginPlace.gridy = 2;
        loginPlace.gridwidth = 1;
        loginPlace.anchor = GridBagConstraints.SOUTHWEST;
        add(login, loginPlace);

        GridBagConstraints signUpPlace = new GridBagConstraints();
        signUpPlace.gridx = 1;
        signUpPlace.gridy = 2;
        signUpPlace.gridwidth = 1;
        signUpPlace.anchor = GridBagConstraints.SOUTHEAST;
        add(signUp, signUpPlace);

    }
}
