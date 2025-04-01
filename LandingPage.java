package MyFitness;

import javax.swing.*;
import java.awt.*;

public class LandingPage {
    private static JFrame frame;
    public static void init() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("MyFitness");
        frame.getContentPane().setLayout(null);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        JButton login = new JButton("Login");
        JButton signUp = new JButton("Sign Up");
        login.setBounds(10, 10, 100, 30);

        login.addActionListener(e -> Login.createGUI());

        signUp.setBounds(10, 50, 100, 30);
        //FIXME action listener needs to call signup.createGUI();
/*
        signUp.addActionListener(e ->);
*/

        frame.add(login);
        frame.add(signUp);
        frame.setVisible(true);
    }
    public static void dispose(){
        frame.dispose();
    }
}
