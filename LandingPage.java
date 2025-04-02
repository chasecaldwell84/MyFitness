package MyFitness;

import javax.swing.*;
import java.awt.*;

public class LandingPage extends JFrame {

    public LandingPage(App app) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("MyFitness");
        getContentPane().setLayout(null);
        setSize(500, 500);
        setLocationRelativeTo(null);

        JButton login = new JButton("Login");
        Login loginPanel = new Login();
        login.setBounds(250, 235, 100, 30);
        login.addActionListener(e -> {
            loginPanel.setVisible(true);
            if(loginPanel.getAuthenticated()){
                loginPanel.dispose();
                dispose();
                app.setVisible(true);
            }
        });

        JButton signUp = new JButton("Sign Up");
        SignUp signUpPanel = new SignUp();
        signUp.setBounds(250, 265, 100, 30);
        signUp.addActionListener(e -> {
            signUpPanel.setVisible(true);

        });


        add(login);
        add(signUp);
    }
}
