package MyFitness;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AdminPage extends JPanel {

    private ArrayList<String> users;
    private ArrayList<String> userPasswords;


    public AdminPage(){
        setSize(500,500);
        setLayout(new BorderLayout(10, 10));
        createGUI();
    }

    private void createGUI(){
        readUsers();

        JLabel titleLabel = new JLabel("User List", SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(App.titleFont);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel,BoxLayout.Y_AXIS));
        titlePanel.add(Box.createVerticalStrut(80));
        titlePanel.add(titleLabel);

        JPanel listPanel = new JPanel();

        JList<String> userList = new JList<>((users.toArray(new String[0])));
        userList.setFont(App.boldLabelFontLarge);
        userList.setAlignmentX(Component.CENTER_ALIGNMENT);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollList = new JScrollPane(userList);
        scrollList.setPreferredSize(new Dimension(400, 600));

        listPanel.add(scrollList, BorderLayout.CENTER);
        listPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(45, 250, 90, 250),
                BorderFactory.createLineBorder(Color.BLACK, 5)
        ));



        JPanel buttonPanel = new JPanel();

        JButton searchButton = new JButton("Search User");
        JButton viewButton = new JButton("View User");
        buttonPanel.add(searchButton);
        buttonPanel.add(viewButton);

        add(titlePanel,BorderLayout.NORTH);
        add(listPanel,BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void readUsers(){

        users = new ArrayList<String>();
        userPasswords = new ArrayList<String>();

        //placeholder filler, will replace later with actual reading of userAuth.csv
        for(int i = 0; i < 50; i++){
            users.add("Placeholder("+i+")");
            userPasswords.add("password"+i);
        }
    }

}
