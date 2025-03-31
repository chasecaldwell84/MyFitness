package MyFitness;

import javax.swing.*;
import java.awt.*;

public class Login {

    public static void main(String[] args) {
        createGUI();
    }
    public static void createGUI(){
        JFrame frame = new JFrame("Login");

        frame.setLayout(new BorderLayout());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Load the image from resources, logo
        ImageIcon icon = new ImageIcon("resources/images/MyFitnessLogoChose1.jpg");
        // Resize image
        Image img = icon.getImage().getScaledInstance(200, 100, Image.SCALE_SMOOTH);
        icon = new ImageIcon(img);
        JLabel imageLabel = new JLabel(icon);
        JPanel imagePanel = new JPanel();
        imagePanel.add(imageLabel);

        JLabel title = new JLabel("Login Page", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(title, BorderLayout.NORTH);

        JPanel Username = new JPanel();
        Username.setLayout(new FlowLayout());

        JLabel UsernameLabel = new JLabel("Username: ");
        JTextField UsernameField = new JTextField(10);

        JLabel PasswordLabel = new JLabel("Password: ");
        JPasswordField PasswordField = new JPasswordField(10);
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String username = UsernameField.getText();
            String password = PasswordField.getText();
            if(password == null || username.equals("") || password.equals("")){
                JOptionPane.showMessageDialog(frame, "Please enter a valid username/password");
            }
            else{
                //NOTE this is when you call the csv parser to check

            }

        });


        Username.add(UsernameLabel);
        Username.add(UsernameField);
        Username.add(PasswordLabel);
        Username.add(PasswordField);
        Username.add(submitButton);
        frame.add(Username, BorderLayout.CENTER);

        //Below code for create Sign up button
        JButton signUpButton = new JButton("Sign-Up");
        signUpButton.addActionListener(e -> {
            SignUp signUp = new SignUp();
            signUp.setVisible(true);
        });


        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(signUpButton);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        //sign-up code end here, below is the window size,

        frame.setSize(500, 500);
        frame.setVisible(true);
    }
}
