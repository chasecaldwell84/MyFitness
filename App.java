package MyFitness;

import MyFitness.User.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class App extends JFrame {
    private User user;
    public static Font boldLabelFontLarge = new Font("Arial", Font.BOLD, 20);
    public static Font labelFontLarge = new Font("Arial", Font.PLAIN, 20);
    public static Font titleFont = new Font("Arial", Font.BOLD, 40);
    public static Font statsFont = new Font("Courier", Font.PLAIN,30);

    private static NavBar navBar;

    public void setUser(User user) {
        this.user = user;
    }
    public User getUser() {
        return user;
    }

    public NavBar getNavBar(){
        return navBar;
    }

    private FriendManager friendManager = new FriendManager();
    public FriendManager getFriendManager() { return friendManager; }

    private GroupManager groupManager = new GroupManager();
    public GroupManager getGroupManager() { return groupManager; }

    private List<User> allUsers = new ArrayList<User>();
    public List<User> getAllUsers() { return allUsers; }
    public void setAllUsers(List<User> users) { this.allUsers = users; }
    
    public App() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setTitle("MyFitness");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); //NOTE automatic full screen
        setSize((int)screenSize.getWidth(), (int)screenSize.getHeight());

        getContentPane().setLayout(new BorderLayout());

        navBar = new NavBar(this);
        add(navBar, BorderLayout.NORTH);
    }
    /*TODO should be the class that implements the interface that is updated by the other classes*/
}
