package MyFitness;

import MyFitness.Settings.UserPage;
import MyFitness.Trainer.TrainerMainPage;
import MyFitness.User.Trainer;
import MyFitness.User.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class App extends JFrame {
    private User user;
    private NavBar navBar;

    public static Font boldLabelFontLarge = new Font("Arial", Font.BOLD, 20);
    public static Font labelFontLarge = new Font("Arial", Font.PLAIN, 20);
    public static Font titleFont = new Font("Arial", Font.BOLD, 40);
    public static Font statsFont = new Font("Courier", Font.PLAIN,30);

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

    private List<User> allUsers = new ArrayList<>();
    public List<User> getAllUsers() { return allUsers; }
    public void setAllUsers(List<User> users) { this.allUsers = users; }

    public App() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setTitle("MyFitness");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // automatic full screen
        setSize((int)screenSize.getWidth(), (int)screenSize.getHeight());

        getContentPane().setLayout(new BorderLayout());

        navBar = new NavBar(this);
        add(navBar, BorderLayout.NORTH);
    }

    public void showTrainerMainPage() {
        getContentPane().removeAll();
        getContentPane().add(navBar, BorderLayout.NORTH);
        add(new TrainerMainPage(this, navBar, (Trainer) user)); // cast user to Trainer
        revalidate();
        repaint();
    }

    public void showUserPage() {
        getContentPane().removeAll();
        getContentPane().add(navBar, BorderLayout.NORTH);
        add(new UserPage(this));
        revalidate();
        repaint();
    }

    // Future method: public void showAdminPage();
}
