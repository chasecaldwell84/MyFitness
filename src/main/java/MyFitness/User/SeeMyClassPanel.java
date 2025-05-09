//Author:Dannis Wu
package MyFitness.User;

import MyFitness.App;
import MyFitness.Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class SeeMyClassPanel extends JPanel {
    private final JTextArea outputArea = new JTextArea();
    private final JComboBox<Integer> classDropdown;
    private final GeneralUser user;

    public SeeMyClassPanel(App frame, GeneralUser user) {
        this.user = user;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel title = new JLabel("See My Class Panel", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        // Show All Button
        JButton showAllBtn = new JButton("Show All My Class Info");
        showAllBtn.addActionListener(this::showAllClassInfo);
        bottomPanel.add(showAllBtn);

        // Dropdown Panel
        JPanel dropdownPanel = new JPanel(new FlowLayout());
        dropdownPanel.add(new JLabel("Choose Class ID:"));

        classDropdown = new JComboBox<>();
        refreshDropdown(); // Fill dropdown with user's joined class IDs
        dropdownPanel.add(classDropdown);

        JButton viewPlanBtn = new JButton("View Plan");
        viewPlanBtn.addActionListener(this::showSelectedClassPlan);
        dropdownPanel.add(viewPlanBtn);

        bottomPanel.add(dropdownPanel);

        // Refresh Dropdown Button
        JButton refreshBtn = new JButton("Refresh Class List");
        refreshBtn.addActionListener(e -> refreshDropdown());
        bottomPanel.add(refreshBtn);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void refreshDropdown() {
        classDropdown.removeAllItems();
        List<Integer> classIds = Database.getInstance().getClassIdsJoinedByUser(user.getUserName());
        for (Integer id : classIds) {
            classDropdown.addItem(id);
        }
    }

    private void showAllClassInfo(ActionEvent e) {
        outputArea.setText("");
        List<String> classes = Database.getInstance().getJoinedClassInfo(user.getUserName());
        if (classes.isEmpty()) {
            outputArea.setText("❗ You haven't joined any classes.");
        } else {
            for (String info : classes) {
                outputArea.append(info + "\n-----------------------\n");
            }
        }
    }

    private void showSelectedClassPlan(ActionEvent e) {
        Integer selectedId = (Integer) classDropdown.getSelectedItem();
        if (selectedId != null) {
            String planDetails = Database.getInstance().getClassPlanForUser(selectedId);
            outputArea.setText(planDetails != null ? planDetails : "No plan found for this class.");
        }
    }
}
