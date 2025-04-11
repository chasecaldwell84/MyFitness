package MyFitness;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AdminPage extends JFrame {

    private ArrayList<String> users;
    private ArrayList<String> userPasswords;

    static Font labelFont = new Font("Arial", Font.BOLD, 20);
    static Font titleFont = new Font("Arial", Font.BOLD, 40);

    public AdminPage(){
        setSize(500,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Admin Userlist");
        setLocationRelativeTo(null);
        createGUI();
    }

    private void createGUI(){
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        readUsers();

        JLabel titleLabel = new JLabel("User List", SwingConstants.CENTER);
        titleLabel.setFont(titleFont);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();

        JList<String> userList = new JList<>((users.toArray(new String[0])));
        userList.setFont(labelFont);
        userList.setAlignmentX(Component.CENTER_ALIGNMENT);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollList = new JScrollPane(userList);
        scrollList.setPreferredSize(new Dimension(250, 350));

        listPanel.add(scrollList, BorderLayout.CENTER);
        listPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 10, 0, 10),
                BorderFactory.createLineBorder(Color.BLACK, 2)
        ));

        mainPanel.add(listPanel);

        JPanel buttonPanel = new JPanel();

        JButton backButton = new JButton("back");
        JButton searchButton = new JButton("Search User");
        JButton viewButton = new JButton("View User");
        buttonPanel.add(searchButton);
        buttonPanel.add(viewButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);



        add(mainPanel);
        setVisible(true);
    }

    private void readUsers(){

        users = new ArrayList<String>();
        userPasswords = new ArrayList<String>();

        //placeholder filler, will replace later with actual reading of userAuth.csv
        for(int i = 0; i < 20; i++){
            users.add("Placeholder("+i+")");
            userPasswords.add("password"+i);
        }
    }

    public static void main(String[] args) {
        AdminPage adminPage = new AdminPage();
    }

}
