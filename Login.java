package MyFitness;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Login {

    private static Boolean Authenticated = false;

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
                try {
                    Scanner scanner = new Scanner(new File("./src/main/resources/UserAuth.csv"));
                    String[] headers = scanner.nextLine().split(",");
                    int UsernameINDEX = -1, PasswordINDEX = -1;
                    for(int i = 0; i < headers.length; i++){
                        String column = headers[i].trim().toLowerCase();
                        if(column.equals("username")){
                            UsernameINDEX = i;
                        }
                        else if(column.equals("password")){
                            PasswordINDEX = i;
                        }
                    }
                    if(UsernameINDEX == -1 || PasswordINDEX == -1){
                        JOptionPane.showMessageDialog(frame, "System ERROR");
                        return;
                    }

                    int counter = 0;
                    while(scanner.hasNextLine() && !Authenticated){
                        String line = scanner.nextLine().trim();
                        String [] values = line.split(",");

                        /*if(values.length != headers.length){}*/
                        String Usernameinput = values[UsernameINDEX].trim();
                        String Passwordinput = values[PasswordINDEX].trim();
                        if(Usernameinput.equals(username)){
                            counter++;
                        }
                        if(Passwordinput.equals(password)){
                            counter++;
                        }
                        if(counter == 2){
                            Authenticated = true;
                        }
                    }

                    if(Authenticated){
                        JOptionPane.showMessageDialog(frame, "You have successfully logged in");
                    }
                    else{
                        JOptionPane.showMessageDialog(frame, "You have an incorrect username/password");
                    }
                    scanner.close();
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
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
