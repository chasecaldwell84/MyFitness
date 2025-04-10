package MyFitness;

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
    public App(){
        setTitle("MyFitness");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        NavBar navBar = new NavBar(this);
        add(navBar, BorderLayout.NORTH);
    }
    /*TODO should be the class that implements the interface that is updated by the other classes*/
}
