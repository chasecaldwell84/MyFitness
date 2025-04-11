package MyFitness.LoginSignUp;

import MyFitness.User.Admin;
import MyFitness.User.Trainer;
import MyFitness.User.User;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Login extends JDialog {

    private Boolean Authenticated = false;
    private User user = null;

    public User getUser() {
        return user;
    }
    public Boolean getAuthenticated() {
        return Authenticated;
    }

    public Login() {
        setTitle("Login");
        setModal(true);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //NOTE maybe change to hide.on.close
        setSize(500, 500);
        //dialog = new JDialog(frame, "Login", true);

        //dialog.setLayout(new BorderLayout());

        //dialog.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        JLabel title = new JLabel("Login Page", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        //dialog.add(title, BorderLayout.NORTH);

        add(title, BorderLayout.NORTH);

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
            if(password == null || username.isEmpty() || password.isEmpty()){
                JOptionPane.showMessageDialog(this, "Please enter a valid username/password");
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
        add(Username, BorderLayout.NORTH);

    }

    public void authenicating(String username, String password) throws FileNotFoundException {
        Scanner scanner = null;
        try{
            scanner = new Scanner(new File("./src/main/java/MyFitness/resources/UserAuth.csv"));
        }
        catch(FileNotFoundException e){
            System.out.println(e + " File not found");
            return;
        }
        String[] headers = scanner.nextLine().split(",");
        int UsernameINDEX = -1, PasswordINDEX = -1, TypeIndex = -1;
        for(int i = 0; i < headers.length; i++){
            String column = headers[i].trim().toLowerCase();
            if(column.equals("username")){
                UsernameINDEX = i;
            }
            else if(column.equals("password")){
                PasswordINDEX = i;
            }
            else if(column.equals("type")){
                TypeIndex = i;
            }
        }
        if(UsernameINDEX == -1 || PasswordINDEX == -1 || TypeIndex == -1){
            JOptionPane.showMessageDialog(this, "System ERROR");
            return;
        }

        int counter = 0;
        String TypeInput = "";
        while(scanner.hasNextLine() && !Authenticated){
            String line = scanner.nextLine().trim();
            String [] values = line.split(",");

            /*if(values.length != headers.length){}*/
            String Usernameinput = values[UsernameINDEX].trim();
            String Passwordinput = values[PasswordINDEX].trim();
            TypeInput = values[TypeIndex].trim();
            if(Usernameinput.equals(username)){
                counter++;
            }
            if(Passwordinput.equals(password)){
                counter++;
            }
            if(counter == 2){
                Authenticated = true;
            }
            else{
                counter = 0;
            }
        }

        if(Authenticated){
            JOptionPane.showMessageDialog(this, "You have successfully logged in");
            if(TypeInput.isEmpty()){
                JOptionPane.showMessageDialog(this, "SYSTEM ERROR Not assigned type");
            }
            else if(TypeInput.equals("User")){
                user = new User(username, password);
            }
            else if(TypeInput.equals("Trainer")){
                user = new Trainer(username, password);
                //System.out.println("Trainer: " + user.getUserName());
            }
            else if(TypeInput.equals("Admin")){
                user = new Admin(username, password);
                //System.out.println("Admin");
            }
            dispose();
        }
        else{
            JOptionPane.showMessageDialog(this, "You have an incorrect username/password");
        }
        scanner.close();
    }
}
