package MyFitness.ExerciseSession;

import MyFitness.App;
import MyFitness.Database;
import MyFitness.ExerciseSession.Workout.*;
import MyFitness.NavBar;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class ExerciseSession extends JPanel {
    private String date;
    private Set<Workout> workouts;
    private JTextArea workoutDisplay;

    public ExerciseSession(App frame, JPanel journal, NavBar navBar) {
        this.date = null;
        this.workouts = new HashSet<>();

        setSize(frame.getWidth(), frame.getHeight());
        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(20,20,20,20);
        Dimension buttonsSize = new Dimension(120, 30);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Exercise Session");
        title.setFont(new Font("Arial", Font.BOLD, 20));

        JButton liftButton = new JButton("Weight Lifting");
        JButton cardioButton = new JButton("Cardio");
        JPanel workoutPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel nameLabel = new JLabel("Enter Workout Name: ");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        namePanel.add(nameLabel);

        JTextField nameField = new JTextField(27);
        namePanel.add(nameField);

        namePanel.setVisible(false);

        JPanel liftInputPanel = new JPanel(new GridLayout(2, 2));

        JLabel weightLabel = new JLabel("Weight: ");
        weightLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        liftInputPanel.add(weightLabel);

        JTextField weightField = new JTextField(20);
        ((AbstractDocument) weightField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                if (isValidInput(fb.getDocument().getText(0, fb.getDocument().getLength()), string, offset)) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                String oldText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = new StringBuilder(oldText).replace(offset, offset + length, text).toString();
                if (newText.matches("\\d*\\.?\\d*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            private boolean isValidInput(String currentText, String newText, int offset) {
                StringBuilder sb = new StringBuilder(currentText);
                sb.insert(offset, newText);
                return sb.toString().matches("\\d*\\.?\\d*");
            }
        });
        liftInputPanel.add(weightField);

        JLabel repsLabel = new JLabel("Reps: ");
        repsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        liftInputPanel.add(repsLabel);

        JTextField repsField = new JTextField(20);
        ((AbstractDocument) repsField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                if (isValidInput(fb.getDocument().getText(0, fb.getDocument().getLength()), string, offset)) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                String oldText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = new StringBuilder(oldText).replace(offset, offset + length, text).toString();
                if (newText.matches("\\d*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            private boolean isValidInput(String currentText, String newText, int offset) {
                StringBuilder sb = new StringBuilder(currentText);
                sb.insert(offset, newText);
                return sb.toString().matches("\\d*");
            }
        });
        liftInputPanel.add(repsField);

        JPanel liftButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton liftAddButton = new JButton("Add");
        liftAddButton.addActionListener(e -> {
            String workoutName = nameField.getText();
            String weight = weightField.getText();
            String reps = repsField.getText();
            JDialog saveDialog = new JDialog(frame, "Add", true);

            if(!workoutName.isEmpty() && !weight.isEmpty() && !reps.isEmpty()) {
                boolean isNewWorkout = true;
                LiftWorkout currWorkout = null;

                for (Workout workout : workouts) {
                    if (workout.getWorkoutName() != null
                            && workout.getWorkoutName().equals(workoutName)
                            && workout.getClass() == LiftWorkout.class) {
                        isNewWorkout = false;
                        currWorkout = (LiftWorkout) workout;
                        currWorkout.addSet(new LiftWorkout.LiftSet(Double.parseDouble(weight), Integer.parseInt(reps)));
                        break;
                    }
                }

                if(isNewWorkout) {
                    currWorkout = new LiftWorkout(workoutName);
                    currWorkout.addSet(new LiftWorkout.LiftSet(Double.parseDouble(weight), Integer.parseInt(reps)));
                    workouts.add(currWorkout);
                }

                updateDisplay();

                panel.remove(liftInputPanel);
                panel.remove(liftButtonsPanel);
                panel.revalidate();
                panel.repaint();
                liftButton.setEnabled(true);
                cardioButton.setEnabled(true);
                namePanel.setVisible(false);
                workoutPanel.setVisible(false);

                JOptionPane.showMessageDialog(saveDialog,
                        "Workout: " + workoutName +"\nWeight: " + weight + "\nReps: " + reps,
                        "Workout Saved", JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                JOptionPane.showMessageDialog(saveDialog, "Please fill in all fields.",
                        "Missing Information", JOptionPane.ERROR_MESSAGE);
            }
        });
        liftButtonsPanel.add(liftAddButton);

        JButton liftCancelButton = new JButton("Cancel");
        liftCancelButton.addActionListener(e -> {
            panel.remove(liftInputPanel);
            panel.remove(liftButtonsPanel);
            panel.revalidate();
            panel.repaint();
            liftButton.setEnabled(true);
            cardioButton.setEnabled(true);
        });
        liftButtonsPanel.add(liftCancelButton);

        JPanel distancePanel = new JPanel(new GridLayout(1, 2));

        JLabel distanceLabel = new JLabel("Distance (miles): ");
        distancePanel.add(distanceLabel);

        JTextField distanceField = new JTextField(20);
        ((AbstractDocument) distanceField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                if (isValidInput(fb.getDocument().getText(0, fb.getDocument().getLength()), string, offset)) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                String oldText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = new StringBuilder(oldText).replace(offset, offset + length, text).toString();
                if (newText.matches("\\d*\\.?\\d*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            private boolean isValidInput(String currentText, String newText, int offset) {
                StringBuilder sb = new StringBuilder(currentText);
                sb.insert(offset, newText);
                return sb.toString().matches("\\d*\\.?\\d*");
            }
        });
        distancePanel.add(distanceField);

        JLabel durationLabel = new JLabel("Duration: ");

        JPanel cardioInputPanel = new JPanel(new GridLayout(3, 2));

        JLabel hoursLabel = new JLabel("Hours: ");
        hoursLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        cardioInputPanel.add(hoursLabel);

        JTextField hoursField = new JTextField(20);
        ((AbstractDocument) hoursField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                if (isValidInput(fb.getDocument().getText(0, fb.getDocument().getLength()), string, offset)) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                String oldText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = new StringBuilder(oldText).replace(offset, offset + length, text).toString();
                if (newText.matches("\\d*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            private boolean isValidInput(String currentText, String newText, int offset) {
                StringBuilder sb = new StringBuilder(currentText);
                sb.insert(offset, newText);
                return sb.toString().matches("\\d*");
            }
        });
        cardioInputPanel.add(hoursField);

        JLabel minutesLabel = new JLabel("Minutes: ");
        minutesLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        cardioInputPanel.add(minutesLabel);

        JTextField minutesField = new JTextField(20);
        ((AbstractDocument) minutesField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                if (isValidInput(fb.getDocument().getText(0, fb.getDocument().getLength()), string, offset)) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                String oldText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = new StringBuilder(oldText).replace(offset, offset + length, text).toString();
                if (newText.matches("\\d*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            private boolean isValidInput(String currentText, String newText, int offset) {
                StringBuilder sb = new StringBuilder(currentText);
                sb.insert(offset, newText);
                return sb.toString().matches("\\d*");
            }
        });
        cardioInputPanel.add(minutesField);

        JLabel secondsLabel = new JLabel("Seconds: ");
        secondsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        cardioInputPanel.add(secondsLabel);

        JTextField secondsField = new JTextField(20);
        ((AbstractDocument) secondsField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                if (isValidInput(fb.getDocument().getText(0, fb.getDocument().getLength()), string, offset)) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                String oldText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = new StringBuilder(oldText).replace(offset, offset + length, text).toString();
                if (newText.matches("\\d*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            private boolean isValidInput(String currentText, String newText, int offset) {
                StringBuilder sb = new StringBuilder(currentText);
                sb.insert(offset, newText);
                return sb.toString().matches("\\d*");
            }
        });
        cardioInputPanel.add(secondsField);

        JPanel cardioButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton cardioAddButton = new JButton("Add");
        cardioAddButton.addActionListener(e -> {
            String workoutName = nameField.getText();
            String distance = distanceField.getText();
            String hours = hoursField.getText();
            String minutes = minutesField.getText();
            String seconds = secondsField.getText();
            JDialog saveDialog = new JDialog(frame, "Add", true);

            if(!workoutName.isEmpty() && !distance.isEmpty() && !hours.isEmpty() && !minutes.isEmpty() && !seconds.isEmpty()) {
                boolean isNewWorkout = true;
                CardioWorkout currWorkout = null;

                for (Workout workout : workouts) {
                    if (workout.getWorkoutName() != null
                            && workout.getWorkoutName().equals(workoutName)
                            && workout.getClass() == CardioWorkout.class) {
                        isNewWorkout = false;
                        currWorkout = (CardioWorkout) workout;
                        currWorkout.setDistance(Double.parseDouble(distance));
                        currWorkout.setDuration(new CardioWorkout.CardioDuration(Integer.parseInt(hours), Integer.parseInt(minutes), Integer.parseInt(seconds)));
                        break;
                    }
                }

                if(isNewWorkout) {
                    currWorkout = new CardioWorkout(workoutName, Double.parseDouble(distance), Integer.parseInt(hours), Integer.parseInt(minutes), Integer.parseInt(seconds));
                    workouts.add(currWorkout);
                }

                updateDisplay();

                panel.remove(distancePanel);
                panel.remove(durationLabel);
                panel.remove(cardioInputPanel);
                panel.remove(cardioButtonsPanel);
                panel.revalidate();
                panel.repaint();
                liftButton.setEnabled(true);
                cardioButton.setEnabled(true);
                namePanel.setVisible(false);
                workoutPanel.setVisible(false);

                JOptionPane.showMessageDialog(saveDialog,
                        "Workout: " + workoutName +"\nDuration: " + currWorkout.getDuration() + "\nDistance: " + distance,
                        "Workout Saved", JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                JOptionPane.showMessageDialog(saveDialog, "Please fill in all fields.",
                        "Missing Information", JOptionPane.ERROR_MESSAGE);
            }


        });
        cardioButtonsPanel.add(cardioAddButton);

        JButton cardioCancelButton = new JButton("Cancel");
        cardioCancelButton.addActionListener(e -> {
            panel.remove(distancePanel);
            panel.remove(durationLabel);
            panel.remove(cardioInputPanel);
            panel.remove(cardioButtonsPanel);
            panel.revalidate();
            panel.repaint();
            liftButton.setEnabled(true);
            cardioButton.setEnabled(true);
        });
        cardioButtonsPanel.add(cardioCancelButton);

        JLabel workoutLabel = new JLabel("Select Workout Type: ");
        workoutLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        workoutPanel.add(workoutLabel);

        liftButton.setPreferredSize(buttonsSize);
        liftButton.addActionListener(e -> {
            liftButton.setEnabled(false);
            cardioButton.setEnabled(false);

            liftInputPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(liftInputPanel);

            liftButtonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(liftButtonsPanel);

            panel.revalidate();
            panel.repaint();
        });
        workoutPanel.add(liftButton);

        cardioButton.setPreferredSize(buttonsSize);
        cardioButton.addActionListener(e -> {
            liftButton.setEnabled(false);
            cardioButton.setEnabled(false);

            distancePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(distancePanel);

            durationLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(durationLabel);

            cardioInputPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(cardioInputPanel);

            cardioButtonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(cardioButtonsPanel);

            panel.revalidate();
            panel.repaint();
        });
        workoutPanel.add(cardioButton);
        workoutPanel.setVisible(false);

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton addWorkout = new JButton("Add Workout");
        addWorkout.setPreferredSize(buttonsSize);
        addWorkout.addActionListener(e -> {
            namePanel.setVisible(true);
            workoutPanel.setVisible(true);
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
            monthBox.addItem(null);
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

            // Save and Cancel Buttons
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

//                    this.date = cal.getTime();
                    java.util.Date selectedDate = cal.getTime();
                    this.setDate(new java.text.SimpleDateFormat("yyyy-MM-dd").format(selectedDate));
                    JOptionPane.showMessageDialog(frame, "Date set to: " + date);

                    workouts.forEach(workout -> {
                        Database.getInstance().saveWorkout(frame.getUser(), workout, date);
                    });

                    dialog.dispose();

                    frame.getContentPane().removeAll();
                    frame.getContentPane().add(navBar);
                    frame.add(journal);
                    frame.revalidate();
                    frame.repaint();

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

        JButton backButton = new JButton("Cancel");
        backButton.setPreferredSize(buttonsSize);
        backButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.getContentPane().add(navBar);
            frame.add(journal);
            frame.revalidate();
            frame.repaint();
        });
        buttons.add(backButton);


        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.add(title);
        titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titlePanel);

        buttons.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(buttons);

        panel.add(Box.createVerticalStrut(20));

        namePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(namePanel);

        panel.add(Box.createVerticalStrut(10));

        workoutPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(workoutPanel);

        panel.add(Box.createVerticalStrut(10));

        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.fill = GridBagConstraints.NONE;
        c.weighty = 1.0;
        c.weightx = 1.0;
        add(Box.createVerticalStrut(80));

        c.gridy = 1;
        add(panel, c);

        c.gridx = 1;
        workoutDisplay = new JTextArea(40, 40);
        workoutDisplay.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(workoutDisplay);
        add(scrollPane, c);

        setVisible(true);
    }

    public void setDate(String date){
        this.date = date;
    }

    public void updateDisplay(){
        StringBuilder sb = new StringBuilder();
        for (Workout w : workouts) {
            sb.append("- ").append(w.getWorkoutType()).append(": ").append(w.getWorkoutName()).append("\n");

            if (w instanceof LiftWorkout) {
                for (LiftWorkout.LiftSet set : ((LiftWorkout) w).getSets()) {
                    sb.append("    Weight: ").append(set.getWeight()).append(" lbs, Reps: ").append(set.getReps()).append("\n");
                }
            } else if (w instanceof CardioWorkout) {
                CardioWorkout cw = (CardioWorkout) w;
                sb.append("    Distance: ").append(cw.getDistance()).append(" miles, Duration: ")
                        .append(cw.getHours()).append("h ")
                        .append(cw.getMinutes()).append("m ")
                        .append(cw.getSeconds()).append("s\n");
            }
        }
        workoutDisplay.setText(sb.toString());
    }
}
