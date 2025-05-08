package MyFitness.LoginSignUp;

import MyFitness.Database;
import MyFitness.User.Admin;
import MyFitness.User.GeneralUser;
import MyFitness.User.Trainer;
import MyFitness.User.User;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Login extends JDialog {
    private Database db = Database.getInstance();
    private Boolean Authenticated = false;
    private User user = null;

    //private Font labelFont = new Font("Arial", Font.BOLD, 14);
    private Font loginTitleFont = new Font("Arial", Font.BOLD, 40);


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
        setSize(500, 300);
        setLocationRelativeTo(null);

        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(loginTitleFont);
        titlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(Box.createVerticalStrut(80));
        titlePanel.add(titleLabel);

        //dialog = new JDialog(frame, "Login", true);
        //dialog.setLayout(new BorderLayout());
        //dialog.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        //JLabel title = new JLabel("Login Page", JLabel.CENTER);
        //title.setFont(new Font("Arial", Font.BOLD, 20));
        //dialog.add(title, BorderLayout.NORTH);
        //add(title, BorderLayout.NORTH);

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
        JPanel submitPanel = new JPanel();
        submitPanel.add(submitButton);

        add(titlePanel,BorderLayout.NORTH);
        add(Username, BorderLayout.CENTER);
        add(submitPanel, BorderLayout.SOUTH);

    }

    /*public void authenicating(String username, String password) throws FileNotFoundException {
        Scanner scanner = null;
        String operatingSystem = System.getProperty("os.name");
        try{
            if(operatingSystem.startsWith("Windows")) {
                scanner = new Scanner(new File("./src/main/java/MyFitness/resources/UserAuth.csv"));
            }
            else {
                scanner = new Scanner(new File("resources/UserAuth.csv"));
            }
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

            *//*if(values.length != headers.length){}*//*
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
    }*/
    /*public void authenicating(String username, String password) throws FileNotFoundException {
        User user1 = db.findByUsername(username);

        if(user1 != null && user1.getPassword() != null && user1.getPassword().equals(password)){
            Authenticated = true;
        }
        else{
            Authenticated = false;
        }


        if(Authenticated){
            JOptionPane.showMessageDialog(this, "You have successfully logged in");

            if(user1 instanceof GeneralUser){
                user = new GeneralUser(username, password);
            }
            else if(user1 instanceof Trainer){
                user = new Trainer(username, password);
            }
            else if(user1 instanceof Admin){
                user = new Admin(username, password);
            }
            dispose();
        }
        else{
            JOptionPane.showMessageDialog(this, "You have an incorrect username/password");
        }

    }*/

    public void authenicating(String username, String password) throws FileNotFoundException {
        User foundUser = db.findByUsername(username);

        if (foundUser != null && foundUser.getPassword().equals(password)) {
            Authenticated = true;
            user = foundUser;

            JOptionPane.showMessageDialog(this, "You have successfully logged in.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect username or password.");
            Authenticated = false;
        }
    }




    }
