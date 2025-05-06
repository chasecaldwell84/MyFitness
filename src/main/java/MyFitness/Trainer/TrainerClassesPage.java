package MyFitness.Trainer;

import MyFitness.App;
import MyFitness.Database;
import MyFitness.NavBar;
import MyFitness.User.Trainer;
import MyFitness.RyanStuff.ModifyPlanPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TrainerClassesPage extends JPanel {

    private App frame;
    private Trainer trainer;

    public TrainerClassesPage(App frame, Trainer trainer) {
        this.frame = frame;
        this.trainer = trainer;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel titleLabel = new JLabel("My Created Classes", SwingConstants.CENTER);
        titleLabel.setFont(App.titleFont);
        add(titleLabel, BorderLayout.NORTH);

        List<String> classes = Database.getInstance().findClassesByTrainer(trainer.getUserName());

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        if (classes.isEmpty()) {
            listPanel.add(new JLabel("You haven’t created any classes yet."));
        } else {
            for (String c : classes) {
                JPanel classPanel = new JPanel();
                classPanel.setLayout(new BorderLayout(5, 5));

                JTextArea area = new JTextArea(c);
                area.setEditable(false);
                area.setLineWrap(true);
                area.setWrapStyleWord(true);
                area.setBorder(BorderFactory.createLineBorder(Color.GRAY));

                JButton editButton = new JButton("Edit Class & Plan");
                editButton.addActionListener(e -> {
                    int classId = extractClassId(c);
                    if (classId != -1) {
                        NavBar navBar = new NavBar(frame);
                        navBar.setLoggedInUser(trainer); // Important: set logged-in user

                        ModifyPlanPanel panel = new ModifyPlanPanel(frame, navBar, classId);
                        frame.getContentPane().removeAll();
                        frame.add(navBar, BorderLayout.NORTH);
                        frame.add(panel, BorderLayout.CENTER);
                        frame.revalidate();
                        frame.repaint();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Could not extract class ID.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });

                classPanel.add(area, BorderLayout.CENTER);
                classPanel.add(editButton, BorderLayout.SOUTH);
                listPanel.add(classPanel);
                listPanel.add(Box.createVerticalStrut(15));
            }
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            NavBar navBar = new NavBar(frame);
            navBar.setLoggedInUser(trainer);
            frame.getContentPane().removeAll();
            frame.add(navBar, BorderLayout.NORTH);
            frame.add(new TrainerMainPage(frame, navBar, trainer));
            frame.revalidate();
            frame.repaint();
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private int extractClassId(String classInfo) {
        try {
            for (String line : classInfo.split("\n")) {
                if (line.startsWith("Class ID:")) {
                    return Integer.parseInt(line.replace("Class ID:", "").trim());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
