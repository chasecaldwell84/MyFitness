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
        login.setBounds(250, 235, 100, 30);

        login.addActionListener(e -> Login.createGUI(frame));

        signUp.setBounds(250, 265, 100, 30);
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
