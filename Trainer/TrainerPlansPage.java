package MyFitness.Trainer;

import MyFitness.App;

import javax.swing.*;
import java.awt.*;

public class TrainerPlansPage extends JPanel {
    public TrainerPlansPage(App app) {
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Trainer Plans Page", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        add(label, BorderLayout.CENTER);
    }
}

