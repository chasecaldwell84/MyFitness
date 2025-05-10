//Author:Dannis Wu
package MyFitness.Trainer;

import MyFitness.App;
import MyFitness.Database;
import MyFitness.NavBar;
import MyFitness.User.Trainer;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CreateClassPage extends JPanel {

    private App frame;
    private Trainer trainer;

    public CreateClassPage(App frame, Trainer trainer) {
        this.frame = frame;
        this.trainer = trainer;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        buildForm();
    }

    private void buildForm() {
        JLabel titleLabel = new JLabel("Create New Class", SwingConstants.CENTER);
        titleLabel.setFont(App.titleFont);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(10, 2, 10, 10));

        JTextField titleField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField dateField = new JTextField("YYYY/MM/DD");
        JTextField timeField = new JTextField("HH:MM (24hr)");
        JTextField lengthField = new JTextField();
        JTextField weeksField = new JTextField();
        JTextField seatsField = new JTextField();

        JCheckBox[] dayCheckboxes = new JCheckBox[]{
                new JCheckBox("Mon"), new JCheckBox("Tue"), new JCheckBox("Wed"),
                new JCheckBox("Thu"), new JCheckBox("Fri"), new JCheckBox("Sat"), new JCheckBox("Sun")
        };

        JPanel daysPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        for (JCheckBox cb : dayCheckboxes) {
            daysPanel.add(cb);
        }

        JCheckBox selfPacedBox = new JCheckBox("This is a Self-Paced Class");

        formPanel.add(new JLabel("Class Title:"));
        formPanel.add(titleField);
        formPanel.add(new JLabel("Description:"));
        formPanel.add(descriptionField);
        formPanel.add(new JLabel("Date(ex. yyyy/mm/dd):"));
        formPanel.add(dateField);
        formPanel.add(new JLabel("Time:"));
        formPanel.add(timeField);
        formPanel.add(new JLabel("Session Length (minutes):"));
        formPanel.add(lengthField);
        formPanel.add(new JLabel("Number of Weeks:"));
        formPanel.add(weeksField);
        formPanel.add(new JLabel("Seats:"));
        formPanel.add(seatsField);
        formPanel.add(new JLabel("Class Days:"));
        formPanel.add(daysPanel);
        formPanel.add(new JLabel("Self-Paced Option:"));
        formPanel.add(selfPacedBox);

        JButton createButton = new JButton("Create Class");
        createButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String desc = descriptionField.getText().trim();
            String date = dateField.getText().trim();
            String time = timeField.getText().trim();
            String lengthStr = lengthField.getText().trim();
            String weeksStr = weeksField.getText().trim();
            String seatsStr = seatsField.getText().trim();
            boolean isSelfPaced = selfPacedBox.isSelected();

            StringBuilder days = new StringBuilder();
            for (JCheckBox cb : dayCheckboxes) {
                if (cb.isSelected()) {
                    if (days.length() > 0) days.append(", ");
                    days.append(cb.getText());
                }
            }

            if (title.isEmpty() || desc.isEmpty() || date.isEmpty() || time.isEmpty() ||
                    lengthStr.isEmpty() || weeksStr.isEmpty() || seatsStr.isEmpty() || days.length() == 0) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields and select at least one class day.");
                return;
            }

            LocalDate parsedDate;
            try {
                if (date.contains("/")) {
                    DateTimeFormatter slashFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    parsedDate = LocalDate.parse(date, slashFormatter);
                } else {
                    parsedDate = LocalDate.parse(date); // yyyy-MM-dd
                }
                LocalTime.parse(time);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date or time format. Use YYYY-MM-DD or YYYY/MM/DD.");
                return;
            }

            int length, weeks, seats;
            try {
                length = Integer.parseInt(lengthStr);
                weeks = Integer.parseInt(weeksStr);
                seats = Integer.parseInt(seatsStr);
                if (length <= 0 || weeks <= 0 || seats <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Session length, weeks, and seats must be positive numbers.");
                return;
            }

            String dateTime = parsedDate.toString() + " " + time;
            String daysStr = days.toString();

            Database.getInstance().saveClass(
                    trainer.getUserName(),
                    title,
                    desc,
                    dateTime,
                    seats,
                    daysStr,
                    length,
                    weeks,
                    isSelfPaced
            );

            JOptionPane.showMessageDialog(this, "Class created successfully!");

            frame.getContentPane().removeAll();
            NavBar navBar = new NavBar(frame);
            frame.add(navBar, BorderLayout.NORTH);
            frame.add(new TrainerMainPage(frame, navBar, trainer));
            frame.revalidate();
            frame.repaint();
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(createButton);

        add(titleLabel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}
