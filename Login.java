package MyFitness;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Login {

    private static Boolean Authenticated = false;
    private static JDialog dialog;
    public static Boolean getAuthenticated() {
        return Authenticated;
    }
    /*public static void main(String[] args) {
        createGUI();
    }*/
    public static void createGUI(JFrame frame) {
        dialog = new JDialog(frame, "Login", true);

        dialog.setLayout(new BorderLayout());

        dialog.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        JLabel title = new JLabel("Login Page", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        dialog.add(title, BorderLayout.NORTH);

        JPanel Username = new JPanel();
        Username.setLayout(new FlowLayout());

        JLabel UsernameLabel = new JLabel("Username: ");
        JTextField UsernameField = new JTextField(10);

        JLabel PasswordLabel = new JLabel("Password: ");
        JPasswordField PasswordField = new JPasswordField(10);
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            Authenticated = false;
            String username = UsernameField.getText();
            String password = PasswordField.getText();
            if(password == null || username.equals("") || password.equals("")){
                JOptionPane.showMessageDialog(dialog, "Please enter a valid username/password");
            }
            else{
                try {
                    authenicating(username,password);
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        Username.add(UsernameLabel);
        Username.add(UsernameField);
        Username.add(PasswordLabel);
        Username.add(PasswordField);
        Username.add(submitButton);
        dialog.add(Username, BorderLayout.NORTH);

        dialog.setSize(500, 500);
        dialog.setVisible(true);

    }

    public static void authenicating(String username, String password) throws FileNotFoundException {
        Scanner scanner = null;
        try{
            scanner = new Scanner(new File("./src/main/java/MyFitness/resources/UserAuth.csv"));
        }
        catch(FileNotFoundException e){
            System.out.println(e + " File not found");
            return;
        }
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
            JOptionPane.showMessageDialog(dialog, "System ERROR");
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
            JOptionPane.showMessageDialog(dialog, "You have successfully logged in");
            //NOTE App is init here and it might not be the best place to init
            App.init();
            dialog.dispose();
            LandingPage.dispose();
        }
        else{
            JOptionPane.showMessageDialog(dialog, "You have an incorrect username/password");
        }
        scanner.close();
    }
}
