package MyFitness;

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

    public void setUser(User user) {
        this.user = user;
    }
    public User getUser() {
        return user;
    }

    private FriendManager friendManager = new FriendManager();
    public FriendManager getFriendManager() { return friendManager; }

    private List<User> allUsers = new ArrayList<User>();
    public List<User> getAllUsers() { return allUsers; }
    public void setAllUsers(List<User> users) { this.allUsers = users; }
    
    public App() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setTitle("MyFitness");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); //NOTE automatic full screen
        setSize((int)screenSize.getWidth(), (int)screenSize.getHeight());

        this.navBar = new NavBar(this);
        add(this.navBar, BorderLayout.NORTH);

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
        getContentPane().add(new NavBar(this), BorderLayout.NORTH);
        add(new UserPage(this));
        revalidate();
        repaint();
    }

    // app.showAdminPage();

    /*TODO should be the class that implements the interface that is updated by the other classes*/
}
