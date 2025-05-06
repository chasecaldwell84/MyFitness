package MyFitness;

import javax.swing.*;
import java.awt.*;

public class HomePage extends JPanel {
    public HomePage(App frame) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Top Image Banner with OS-aware pathing
        ImageIcon logoIcon;
        String operatingSystem = System.getProperty("os.name");
        if (operatingSystem.startsWith("Windows")) {
            logoIcon = new ImageIcon("./src/main/java/MyFitness/resources/images/MyFitnessLogoChose1.jpg");
        } else {
            logoIcon = new ImageIcon("resources/images/MyFitnessLogoChose1.jpg");
        }
        Image scaled = logoIcon.getImage().getScaledInstance(AppConfig.WIDTH / 2, AppConfig.WIDTH / 3, Image.SCALE_SMOOTH);
        JLabel banner = new JLabel(new ImageIcon(scaled));
        banner.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel welcomeLabel = new JLabel("Welcome to MyFitness, " + frame.getUser().getUserName() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 36));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitle = new JLabel("Your all in one fitness, health, and social experience.");
        subtitle.setFont(new Font("Arial", Font.ITALIC, 20));
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel buttonGrid = new JPanel(new GridLayout(2, 3, 30, 30));
        buttonGrid.add(createIconButton("💪 Workouts", "Log your exercise and strength sessions"));
        buttonGrid.add(createIconButton("🍎 Nutrition", "Track calories and macros"));
        buttonGrid.add(createIconButton("🎯 Goals", "Set and crush health goals"));
        buttonGrid.add(createIconButton("🌐 Social", "Connect and compete with friends"));
        buttonGrid.add(createIconButton("📊 Stats", "View your performance stats"));
        buttonGrid.add(createIconButton("⚙️ Settings", "Edit your profile and preferences"));

        JPanel centerCombined = new JPanel();
        centerCombined.setLayout(new BoxLayout(centerCombined, BoxLayout.Y_AXIS));
        centerCombined.add(Box.createVerticalStrut(20));
        centerCombined.add(welcomeLabel);
        centerCombined.add(Box.createVerticalStrut(30));
        centerCombined.add(buttonGrid);
        centerCombined.add(Box.createVerticalStrut(20));

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(subtitle, BorderLayout.NORTH);

        add(banner, BorderLayout.NORTH);
        add(centerCombined, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    private JButton createIconButton(String text, String tooltip) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 20));
        button.setToolTipText(tooltip);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(220, 100));
        button.setBackground(new Color(220, 240, 255));
        button.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        return button;
    }
}