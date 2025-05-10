//Author: Dannis Wu
package MyFitness.User;

import MyFitness.App;
import MyFitness.Database;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class AddClassPanel extends JPanel {

    public AddClassPanel(App frame, User user) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel title = new JLabel("Available Classes", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));

        JTextField searchField = new JTextField();
        JButton searchBtn = new JButton("Search");
        JCheckBox selfPacedFilter = new JCheckBox("Show only Self-Paced Classes");

        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        JPanel topPanel = new JPanel(new BorderLayout());
        searchPanel.add(new JLabel("Search by ID, Title, or Trainer:"), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchBtn, BorderLayout.EAST);
        topPanel.add(searchPanel, BorderLayout.CENTER);
        topPanel.add(selfPacedFilter, BorderLayout.SOUTH);
        centerPanel.add(topPanel, BorderLayout.NORTH);

        DefaultListModel<String> classListModel = new DefaultListModel<>();
        JList<String> classList = new JList<>(classListModel);
        JScrollPane scrollPane = new JScrollPane(classList);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        JButton joinBtn = new JButton("Join Selected Class");
        centerPanel.add(joinBtn, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        // Load all classes
        List<Map<String, Object>> allClasses = Database.getInstance().getAllClasses();
        updateClassList(classListModel, allClasses);

        // Search functionality
        searchBtn.addActionListener(e -> {
            String keyword = searchField.getText().trim().toLowerCase();
            List<Map<String, Object>> filtered = selfPacedFilter.isSelected() ?
                    Database.getInstance().getSelfPacedClasses() :
                    Database.getInstance().getAllClasses();

            classListModel.clear();
            for (Map<String, Object> cls : filtered) {
                String classId = String.valueOf(cls.get("CLASS_ID"));
                String titleText = String.valueOf(cls.get("TITLE")).toLowerCase();
                String trainer = String.valueOf(cls.get("TRAINER_USERNAME")).toLowerCase();
                if (classId.contains(keyword) || titleText.contains(keyword) || trainer.contains(keyword)) {
                    classListModel.addElement(formatClass(cls));
                }
            }
        });

        // Filter checkbox functionality
        selfPacedFilter.addActionListener(e -> {
            List<Map<String, Object>> filtered = selfPacedFilter.isSelected() ?
                    Database.getInstance().getSelfPacedClasses() :
                    Database.getInstance().getAllClasses();
            updateClassList(classListModel, filtered);
        });

        // Join functionality
        joinBtn.addActionListener(e -> {
            String selected = classList.getSelectedValue();
            if (selected == null) return;

            try {
                int classId = Integer.parseInt(selected.split(" ")[2]);
                String status = Database.getInstance().joinClass(user.getUserName(), classId);

                switch (status) {
                    case "success":
                        JOptionPane.showMessageDialog(frame, "🎉 Joined class successfully!");
                        updateClassList(classListModel, Database.getInstance().getAllClasses());
                        break;
                    case "already_joined":
                        JOptionPane.showMessageDialog(frame, "⚠️ You've already joined this class.");
                        break;
                    case "class_full":
                        JOptionPane.showMessageDialog(frame, "❌ Class is full. Try another one.");
                        break;
                    case "class_not_found":
                        JOptionPane.showMessageDialog(frame, "❓ Class not found.");
                        break;
                    case "db_error":
                    default:
                        JOptionPane.showMessageDialog(frame, "💥 Something went wrong. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid class selection.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void updateClassList(DefaultListModel<String> model, List<Map<String, Object>> classes) {
        model.clear();
        for (Map<String, Object> cls : classes) {
            model.addElement(formatClass(cls));
        }
    }

    private String formatClass(Map<String, Object> cls) {
        int classId = (int) cls.get("CLASS_ID");
        int totalSeats = (int) cls.get("SEATS");
        int enrolled = Database.getInstance().getEnrollmentCount(classId);
        String description = (String) cls.get("DESCRIPTION");
        boolean isSelfPaced = (boolean) cls.get("IS_SELF_PACED");
        String selfPacedText = isSelfPaced ? "Yes" : "No";

        return "Class ID: " + classId
                + " | Title: " + cls.get("TITLE")
                + " | Trainer: " + cls.get("TRAINER_USERNAME")
                + " | Seats: " + enrolled + " / " + totalSeats
                + " | Description: " + description
                + " | Self-Paced: " + selfPacedText;
    }
}
