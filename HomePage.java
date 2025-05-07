package MyFitness;

import javax.swing.*;
import java.awt.*;

public class HomePage extends JPanel {
    public HomePage(App frame) {
        setLayout(new BorderLayout());
        setBackground(new Color(250, 250, 250));
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Logo banner
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

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome, " + frame.getUser().getUserName() + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        welcomeLabel.setForeground(new Color(33, 37, 41));

        JLabel subtitle = new JLabel("Your all-in-one health, fitness, and social experience.", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.ITALIC, 18));
        subtitle.setForeground(new Color(100, 100, 100));

        // Feature buttons
        JPanel buttonGrid = new JPanel(new GridLayout(2, 3, 25, 25));
        buttonGrid.setOpaque(false);
        buttonGrid.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        buttonGrid.add(createIconButton("💪 Workouts", "Log your exercise sessions"));
        buttonGrid.add(createIconButton("🍎 Nutrition", "Track calories and meals"));
        buttonGrid.add(createIconButton("🎯 Goals", "Set and achieve fitness targets"));
        buttonGrid.add(createIconButton("🌐 Social", "Connect and compete with others"));
        buttonGrid.add(createIconButton("📊 Stats", "View your health trends"));
        buttonGrid.add(createIconButton("⚙️ Settings", "Update your preferences"));

        // Layout combining
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(Box.createVerticalStrut(20));
        center.add(welcomeLabel);
        center.add(Box.createVerticalStrut(10));
        center.add(subtitle);
        center.add(Box.createVerticalStrut(30));
        center.add(buttonGrid);

        add(banner, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
    }

    private JButton createIconButton(String text, String tooltip) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        button.setToolTipText(tooltip);
        button.setFocusPainted(false);
        button.setBackground(new Color(235, 245, 255));
        button.setForeground(new Color(40, 40, 40));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
}
