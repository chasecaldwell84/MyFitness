package MyFitness.Settings;

import MyFitness.App;
import MyFitness.Database;
import MyFitness.LoginSignUp.SignUp;
import MyFitness.User.Admin;
import MyFitness.User.User;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AdminPage extends JPanel {
    static private final Database db = Database.getInstance();
    private ArrayList<String> users;


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

        JLabel titleLabel = new JLabel("MyFitness.User List", SwingConstants.CENTER);
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

        JLabel filterUserLabel = new JLabel("Search MyFitness.User: ");
        filterUserLabel.setFont(App.boldLabelFontLarge);

        //FIXME filterfield not showing for windows user aka me
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

        JButton viewButton = new JButton("View MyFitness.User");
        viewButton.addActionListener(new viewUser(this));
        buttonPanel.add(viewButton);

        JButton addUserButton = new JButton("Add MyFitness.User");
        addUserButton.addActionListener(new addUser());
        buttonPanel.add(addUserButton);

        JButton deleteUserButton = new JButton("Delete MyFitness.User");
        deleteUserButton.addActionListener(new deleteUser());
        buttonPanel.add(deleteUserButton);

        add(titlePanel,BorderLayout.NORTH);
        add(listPanel,BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void readUsers(){
        List<User> allUsers = db.getAllUsers();

        users = new ArrayList<String>();

        allUsers.forEach(user -> {
            users.add(user.getUserName());
        });
        users.sort(Comparator.naturalOrder());
    }

    private class viewUser implements ActionListener {

        private AdminPage adminPage;

        public viewUser(AdminPage adminPage){
            this.adminPage = adminPage;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            if(userList.getSelectedValue() == null){
                JOptionPane.showMessageDialog(
                        frame,
                        "No MyFitness.User Selected.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            String username = userList.getSelectedValue();
            UserPage userPage = new UserPage(frame,db.findByUsername(username),adminPage);
            frame.getContentPane().remove(adminPage);
            frame.getContentPane().add(userPage);
            frame.revalidate();
            frame.repaint();

        }
    }

    private class addUser implements ActionListener {


        @Override
        public void actionPerformed(ActionEvent e) {

            SignUp signupWindow = new SignUp("Create MyFitness.User",true);
            signupWindow.setVisible(true);
            readUsers();
            userList.setListData(users.toArray(new String[0]));


        }
    }

    private class deleteUser implements  ActionListener {

        @Override
        public void actionPerformed(ActionEvent e){

            String selectedUserName = userList.getSelectedValue();
            if(db.findByUsername(selectedUserName) == frame.getUser()){
                JOptionPane.showMessageDialog(
                        frame,
                        "Cannot delete self",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
           else if(db.findByUsername(selectedUserName) instanceof Admin){
                JOptionPane.showMessageDialog(
                        frame,
                        "Cannot delete admin",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            String confirmDelete = "";
            confirmDelete = JOptionPane.showInputDialog(
                    frame,
                    "Type 'DELETE' to Confirm:",
                    "Delete  " + selectedUserName,
                    JOptionPane.PLAIN_MESSAGE
            );

            if(confirmDelete != null && confirmDelete.equals("DELETE")) {
                int confirmation = JOptionPane.showConfirmDialog(
                        frame,
                        "Are you sure you want to delete this user?",
                        "Delete " + selectedUserName,
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
                if(confirmation == JOptionPane.YES_OPTION) {
                    db.delete(selectedUserName);
                    readUsers();
                    userList.setListData(users.toArray(new String[0]));
                    JOptionPane.showMessageDialog(
                            frame,
                            "User " + selectedUserName + " was successfully deleted.",
                            "MyFitness.User Deleted",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
            else if(confirmDelete != null){
                JOptionPane.showMessageDialog(
                        frame,
                        "Input did not match 'DELETE', delete failed.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }


        }
    }

}
