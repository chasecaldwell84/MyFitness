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
        frame.add(Username, BorderLayout.NORTH);

        frame.setSize(500, 500);
        frame.setVisible(true);
    }
}
