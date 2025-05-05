package MyFitness.ExerciseSession;

import MyFitness.ExerciseSession.Workout.*;
import MyFitness.NavBar;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class ExerciseSession extends JPanel {
    String date;
    Set<Workout> workouts;

    public ExerciseSession(JFrame frame, JPanel journal, NavBar navBar) {
        this.date = null;
        this.workouts = new HashSet<>();

        JPanel session = this;

        setSize(frame.getWidth(), frame.getHeight());
        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        Dimension buttonsSize = new Dimension(120, 30);

        //Set up Title
        JLabel title = new JLabel("Exercise Session", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        add(title, c);

//        // Input fields (centered)
//        JLabel dateLabel = new JLabel("Date: ");
//        JTextField dateField = new JTextField(14);
//        c.gridy = 1;
//        c.gridwidth = 1;
//        c.anchor = GridBagConstraints.LINE_END;
//        add(dateLabel, c);
//        c.gridx = 1;
//        c.anchor = GridBagConstraints.LINE_START;
//        add(dateField, c);

        JPanel namePanel = new JPanel(new FlowLayout());
        namePanel.add(new JLabel("Enter Workout Name: "));
        JTextField nameField = new JTextField(14);
        namePanel.add(nameField);
        namePanel.setVisible(false);
        c.gridy = 3;
        c.gridwidth = 1;
        add(namePanel, c);

        JLabel instructionLabel = new JLabel("Select Workout Type: ");
        instructionLabel.setFont(new Font("Arial", Font.BOLD, 20));
        instructionLabel.setVisible(false);
        c.gridy = 4;
        add(instructionLabel, c);

        JPanel workoutButtons = new JPanel(new FlowLayout());

        JButton liftButton = new JButton("Weight Lifting");
        liftButton.setPreferredSize(buttonsSize);
        liftButton.addActionListener(e -> {
            namePanel.setVisible(false);
            instructionLabel.setVisible(false);
            workoutButtons.setVisible(false);

            frame.getContentPane().removeAll();

            Workout currWorkout = null;
            String newWorkoutName = nameField.getText().trim();
            boolean isNewWorkout = true;

            for (Workout workout : workouts) {
                if(workout.getWorkoutName() != null && workout.getWorkoutName().equals(newWorkoutName)) {
                    isNewWorkout = false;
                    currWorkout = workout;
                    break;
                }
            }

            if(isNewWorkout){
                currWorkout = new LiftWorkout(frame, session, navBar, newWorkoutName);
                workouts.add(currWorkout);
            }

            frame.add(navBar);
            frame.getContentPane().add(currWorkout);
            frame.revalidate();
            frame.repaint();
        });
        workoutButtons.add(liftButton);

        JButton cardioButton = new JButton("Cardio");
        cardioButton.setPreferredSize(buttonsSize);
        cardioButton.addActionListener(e -> {
            namePanel.setVisible(false);
            instructionLabel.setVisible(false);
            workoutButtons.setVisible(false);

            frame.getContentPane().removeAll();

            Workout currWorkout = null;
            String newWorkoutName = nameField.getText().trim();
            boolean isNewWorkout = true;

            for (Workout workout : workouts) {
                if(workout.getWorkoutName() != null && workout.getWorkoutName().equals(newWorkoutName)) {
                    isNewWorkout = false;
                    currWorkout = workout;
                    break;
                }
            }

            if(isNewWorkout){
                currWorkout = new CardioWorkout(frame, session, navBar, newWorkoutName);
                workouts.add(currWorkout);
            }

            frame.add(navBar);
            frame.getContentPane().add(currWorkout);
            frame.revalidate();
            frame.repaint();
        });
        workoutButtons.add(cardioButton);

        c.gridy = 5;
        workoutButtons.setVisible(false);
        add(workoutButtons, c);

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());

        JButton addWorkout = new JButton("Add Workout");
        addWorkout.setPreferredSize(buttonsSize);
        addWorkout.addActionListener(e -> {
            namePanel.setVisible(true);
            instructionLabel.setVisible(true);
            workoutButtons.setVisible(true);
        });
        buttons.add(addWorkout);

        JButton saveSession = new JButton("Save Session");
        saveSession.setPreferredSize(buttonsSize);
        saveSession.addActionListener(e -> {
            JDialog dialog = new JDialog(frame, "Select Date", true);
            dialog.setLayout(new GridBagLayout());
            GridBagConstraints dc = new GridBagConstraints();
            dc.insets = new Insets(5, 5, 5, 5);

            // Months (0–11 in Calendar, displayed as Jan–Dec)
            String[] months = new java.text.DateFormatSymbols().getMonths();
            JComboBox<String> monthBox = new JComboBox<>();
            monthBox.addItem("Select...");
            for (int i = 0; i < 12; i++) {
                monthBox.addItem(months[i]);
            }

            // Days (1–31)
            JComboBox<Integer> dayBox = new JComboBox<>();
            dayBox.addItem(null);
            for (int i = 1; i <= 31; i++) {
                dayBox.addItem(i);
            }

            // Years (e.g., from currentYear - 5 to currentYear + 5)
            JComboBox<Integer> yearBox = new JComboBox<>();
            yearBox.addItem(null);
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            for (int i = currentYear - 5; i <= currentYear + 5; i++) {
                yearBox.addItem(i);
            }

            // Add components to dialog
            dc.gridx = 0; dc.gridy = 0;
            dialog.add(new JLabel("Month:"), dc);
            dc.gridx = 1;
            dialog.add(monthBox, dc);

            dc.gridx = 0; dc.gridy = 1;
            dialog.add(new JLabel("Day:"), dc);
            dc.gridx = 1;
            dialog.add(dayBox, dc);

            dc.gridx = 0; dc.gridy = 2;
            dialog.add(new JLabel("Year:"), dc);
            dc.gridx = 1;
            dialog.add(yearBox, dc);

            // OK and Cancel Buttons
            JButton save = new JButton("Save");
            JButton cancel = new JButton("Cancel");
            dc.gridx = 0; dc.gridy = 3;
            dialog.add(save, dc);
            dc.gridx = 1;
            dialog.add(cancel, dc);

            save.addActionListener(ev -> {
                int selectedMonthIndex = monthBox.getSelectedIndex() - 1; // Adjust for "Select..."
                Integer selectedDay = (Integer) dayBox.getSelectedItem();
                Integer selectedYear = (Integer) yearBox.getSelectedItem();

                if (selectedMonthIndex < 0 || selectedDay == null || selectedYear == null) {
                    JOptionPane.showMessageDialog(dialog, "Please select month, day, and year.", "Incomplete Input", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    Calendar cal = Calendar.getInstance();
                    cal.setLenient(false); // Ensure invalid dates throw exceptions
                    cal.set(Calendar.YEAR, selectedYear);
                    cal.set(Calendar.MONTH, selectedMonthIndex);
                    cal.set(Calendar.DAY_OF_MONTH, selectedDay);
                    cal.getTime(); // Force parsing to catch errors

                    java.util.Date selectedDate = cal.getTime();
                    this.setDate(new java.text.SimpleDateFormat("yyyy-MM-dd").format(selectedDate));
                    JOptionPane.showMessageDialog(frame, "Date set to: " + date);
                    dialog.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Invalid date selected.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            cancel.addActionListener(ev -> dialog.dispose());

            dialog.pack();
            dialog.setLocationRelativeTo(frame);
            dialog.setVisible(true);
        });

        buttons.add(saveSession);

        c.gridy = 2;

        add(buttons, c);

        // Back Button to return to the journal
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.getContentPane().add(navBar);
            frame.add(journal);
            frame.revalidate();
            frame.repaint();
        });

        JPanel exitButtonPanel = new JPanel();
        exitButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        exitButtonPanel.add(backButton);


        c.gridy = 6;
        c.anchor = GridBagConstraints.LAST_LINE_END;
        c.weightx = 1;
        c.weighty = 1;
        add(exitButtonPanel, c);

        setVisible(true);
    }

    public void setDate(String date){
        this.date = date;
    }
}
