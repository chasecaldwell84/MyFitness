package MyFitness.Settings;

import MyFitness.App;
import MyFitness.Database;
import MyFitness.User.User;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AdminPage extends JPanel {
    static private final Database db = Database.getInstance();
    private ArrayList<String> users;
    private ArrayList<String> userPasswords;


    private App frame;
    private JList<String> userList;

    public AdminPage(App frame){
        this.frame = frame;
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

        JPanel listPanel = new JPanel(new BorderLayout());

        userList = new JList<>((users.toArray(new String[0])));
        userList.setFont(App.boldLabelFontLarge);
        userList.setAlignmentX(Component.CENTER_ALIGNMENT);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollList = new JScrollPane(userList);
        scrollList.setPreferredSize(new Dimension(400, 450));

        listPanel.add(scrollList, BorderLayout.CENTER);
        listPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(45, 400, 90, 400),
                BorderFactory.createLineBorder(Color.BLACK, 5)
        ));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

        JLabel filterUserLabel = new JLabel("Search User: ");
        filterUserLabel.setFont(App.boldLabelFontLarge);

        JTextField filterField = new JTextField(60);
        filterField.getDocument().addDocumentListener(new DocumentListener() {
            private void filter() {
                String text = filterField.getText().trim().toLowerCase();
                List<String> filtered = new ArrayList<>();
                for (String name : users) {
                    if (name.toLowerCase().contains(text)) {
                        filtered.add(name);
                    }
                }
                userList.setListData(filtered.toArray(new String[0]));
            }
            @Override public void insertUpdate(DocumentEvent e) { filter(); }
            @Override public void removeUpdate(DocumentEvent e) { filter(); }
            @Override public void changedUpdate(DocumentEvent e) { filter(); }
        });
        filterPanel.add(filterUserLabel);
        filterPanel.add(filterField);
        listPanel.add(filterPanel, BorderLayout.SOUTH);




        JPanel buttonPanel = new JPanel();

        JButton viewButton = new JButton("View User");
        viewButton.addActionListener(new viewUser(this));
        buttonPanel.add(viewButton);

        add(titlePanel,BorderLayout.NORTH);
        add(listPanel,BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void readUsers(){
        List<User> allUsers = db.getAllUsers();

        users = new ArrayList<String>();
        userPasswords = new ArrayList<String>();

        allUsers.forEach(user -> {
            users.add(user.getUserName());
            userPasswords.add(user.getPassword());
        });
    }

    private class viewUser implements ActionListener {

        private AdminPage adminPage;

        public viewUser(AdminPage adminPage){
            this.adminPage = adminPage;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            String username = userList.getSelectedValue();
            UserPage userPage = new UserPage(frame,db.findByUsername(username),adminPage);
            frame.getContentPane().remove(adminPage);
            frame.getContentPane().add(userPage);
            frame.revalidate();
            frame.repaint();

        }
    }

}
