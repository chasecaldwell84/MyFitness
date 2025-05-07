
package MyFitness.RyanStuff;

import MyFitness.App;
import MyFitness.Database;
import MyFitness.NavBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ModifyPlanPanel extends JPanel {
    private final Database db = Database.getInstance();
    private final DefaultListModel<String> planListModel = new DefaultListModel<>();
    private final JList<String> planList = new JList<>(planListModel);
    private final JTextField titleField = new JTextField(10);
    private final JTextField descField = new JTextField(10);
    private final JTextField dateField = new JTextField(10);
    private final JTextField timeField = new JTextField(10);
    private final JTextField seatsField = new JTextField(5);
    private final JTextField daysField = new JTextField(10);
    private final JTextField lengthField = new JTextField(5);
    private final JTextField weeksField = new JTextField(5);
    private final JTextField exerciseField = new JTextField(10);
    private final JTextField instructionField = new JTextField(10);
    private final JTextField durationField = new JTextField(10);
    private final JTextArea planDescArea = new JTextArea(3, 20);

    private final int classId;
    private final String trainerUsername;

    public ModifyPlanPanel(App frame, NavBar navBar, int classId) {
        this.classId = classId;
        this.trainerUsername = navBar.getLoggedInUser().getUserName();

        frame.setTitle("Modify Class Plan");
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);

        JLabel title = new JLabel("Modify Plan for Class ID: " + classId, JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        add(title, c);

        addLabelAndField("Class Title:", titleField, c, 1);
        addLabelAndField("Description:", descField, c, 2);
        addLabelAndField("Start Date (YYYY-MM-DD):", dateField, c, 3);
        addLabelAndField("Time (HH:MM):", timeField, c, 4);
        addLabelAndField("Seats:", seatsField, c, 5);
        addLabelAndField("Days (e.g., Mon, Wed):", daysField, c, 6);
        addLabelAndField("Session Length (min):", lengthField, c, 7);
        addLabelAndField("Duration (weeks):", weeksField, c, 8);

        JButton saveClassButton = new JButton("Save Class Info");
        saveClassButton.addActionListener(this::saveClassInfo);
        c.gridx = 1; c.gridy = 9; c.gridwidth = 1;
        add(saveClassButton, c);

        c.gridx = 0; c.gridy = 10; c.gridwidth = 2;
        add(new JLabel("Existing Plans for This Class:"), c);

        JScrollPane scrollPane = new JScrollPane(planList);
        scrollPane.setPreferredSize(new Dimension(400, 100));
        c.gridy = 11;
        add(scrollPane, c);

        planList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = planList.getSelectedValue();
                if (selected != null) {
                    String[] parts = selected.split("\\|");
                    if (parts.length == 4) {
                        exerciseField.setText(parts[0].split(":")[1].trim());
                        instructionField.setText(parts[1].split(":")[1].trim());
                        durationField.setText(parts[2].split(":")[1].trim());
                        planDescArea.setText(parts[3].split(":")[1].trim());
                    }
                }
            }
        });

        addLabelAndField("Exercise:", exerciseField, c, 12);
        addLabelAndField("Instruction(eg. 3 sets of 12 reps):", instructionField, c, 13);
        addLabelAndField("Duration(mins):", durationField, c, 14);

        JLabel descLabel = new JLabel("Plan Description:");
        c.gridx = 0; c.gridy = 15;
        add(descLabel, c);
        c.gridx = 1;
        JScrollPane descScrollPane = new JScrollPane(planDescArea);
        planDescArea.setLineWrap(true);
        planDescArea.setWrapStyleWord(true);
        add(descScrollPane, c);

        JButton savePlanButton = new JButton("Save Plan");
        savePlanButton.addActionListener(this::savePlan);
        c.gridx = 1; c.gridy = 16;
        add(savePlanButton, c);

        JButton deletePlanButton = new JButton("Delete Plan");
        deletePlanButton.addActionListener(this::deletePlan);
        c.gridy = 17;
        add(deletePlanButton, c);

        loadClassInfo();
        loadPlanList();
    }

    private void addLabelAndField(String label, JTextField field, GridBagConstraints c, int row) {
        c.gridwidth = 1;
        c.gridx = 0; c.gridy = row; add(new JLabel(label), c);
        c.gridx = 1; add(field, c);
    }

    private void loadClassInfo() {
        String[] info = db.getClassInfo(classId);
        if (info != null) {
            titleField.setText(info[0]);
            descField.setText(info[1]);
            String[] dt = info[2].split(" ");
            if (dt.length == 2) {
                dateField.setText(dt[0]);
                timeField.setText(dt[1]);
            }
            seatsField.setText(info[3]);
            daysField.setText(info[4]);
            lengthField.setText(info[5]);
            weeksField.setText(info[6]);
        }
    }

    private void saveClassInfo(ActionEvent e) {
        try {
            boolean success = db.updateClassInfo(
                    classId,
                    titleField.getText().trim(),
                    descField.getText().trim(),
                    dateField.getText().trim(),
                    timeField.getText().trim(),
                    Integer.parseInt(seatsField.getText().trim()),
                    daysField.getText().trim(),
                    Integer.parseInt(lengthField.getText().trim()),
                    Integer.parseInt(weeksField.getText().trim())
            );
            JOptionPane.showMessageDialog(this, success ? "Class info updated!" : "Update failed.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadPlanList() {
        planListModel.clear();
        List<String[]> plans = db.getPlansForClass(classId);
        for (String[] plan : plans) {
            planListModel.addElement("Exercise: " + plan[0] + " | Instruction: " + plan[1] + " | Duration(mins): " + plan[2] + " | Description: " + plan[3]);
        }
    }

    private void savePlan(ActionEvent e) {
        try {
            String exercise = exerciseField.getText().trim();
            String instruction = instructionField.getText().trim();
            int duration = Integer.parseInt(durationField.getText().trim());
            String description = planDescArea.getText().trim();

            String[] existing = db.getExercisePlan(exercise, trainerUsername);

            if (existing[0] != null) {
                db.updateExercisePlan(exercise, exercise, instruction, duration, trainerUsername, description);
            } else {
                db.savePlanToClass(classId, exercise, instruction, duration, description);
            }
            JOptionPane.showMessageDialog(this, "Plan saved/updated!");
            loadPlanList();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Invalid input or error saving plan", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletePlan(ActionEvent e) {
        String selected = planList.getSelectedValue();
        if (selected == null) return;
        String exercise = exerciseField.getText().trim();
        int confirm = JOptionPane.showConfirmDialog(this, "Delete selected plan?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            db.deleteExercisePlan(exercise, trainerUsername);
            JOptionPane.showMessageDialog(this, "Plan deleted.");
            exerciseField.setText("");
            instructionField.setText("");
            durationField.setText("");
            planDescArea.setText("");
            loadPlanList();
        }
    }
}
