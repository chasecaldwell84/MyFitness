package MyFitness;

import MyFitness.User.User;

import javax.swing.*;
import java.awt.*;

public class App extends JFrame {
    private User user = new User();

    public void setUser(User user) {
        this.user = user;
    }
    public User getUser() {
        return user;
    }

    private FriendManager friendManager = new FriendManager();
    public FriendManager getFriendManager() { return friendManager; }
    
    public App(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setTitle("MyFitness");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); //NOTE automatic full screen
        setSize((int)screenSize.getWidth(), (int)screenSize.getHeight());
        NavBar navBar = new NavBar(this);
        add(navBar, BorderLayout.NORTH);
    }
    /*TODO should be the class that implements the interface that is updated by the other classes*/
}
