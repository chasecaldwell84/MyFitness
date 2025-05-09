package MyFitness;

import MyFitness.ExerciseSession.ExerciseSession;
import MyFitness.ExerciseSession.Workout.CardioWorkout;
import MyFitness.ExerciseSession.Workout.LiftWorkout;
import MyFitness.ExerciseSession.Workout.Workout;
import MyFitness.User.User;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;


/* TODO: Refactor code completely. Don't use static. Extend JPanel.
*   Configure so that the user can input a lift or cardio workout. Configure so
*   that input is more strict and won't be as prone to user error.  */

/* TODO UPDATE: Mostly done with this phase of refactoring. No longer using
*   static. Now extending JPanel. Is now configured to enter a lift or cardio
*   workout. Still need to make input more strict and less prone to user error.
*   While the initial version of ExerciseJournal could write to a csv, this
*   version does not yet write to any file or database. Will be implementing
*   later. Once functionality is finished, make the UI look better. */

public class ExerciseJournal extends JPanel {

    /*public static void main(String[] args){
        JFrame frame = new JFrame("Exercise Journal");
        ExerciseJournal exerciseJournal = new ExerciseJournal(frame);
        frame.add(exerciseJournal);
        frame.setVisible(true);
    }*/

    private JComboBox<String> monthBox;
    private JComboBox<Integer> dayBox;
    private JComboBox<Integer> yearBox;
    private JTextArea workoutDisplay;

    public ExerciseJournal(App frame, NavBar navBar, User user) {
        frame.setTitle("Exercise Journal");
        setLayout(new GridBagLayout());


        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JLabel title = new JLabel("Exercise Journal");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(title);

        JButton addSession = new JButton("Add Exercise Session");
        addSession.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.add(navBar);
            frame.add(new ExerciseSession(frame, this, navBar, user));
            frame.revalidate();
            frame.repaint();
        });
        headerPanel.add(addSession);

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.NONE;
        add(headerPanel, c);


        // --- Date Dropdowns in One Row (No Labels) ---
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        datePanel.add(new JLabel("Date:"));

        // Month dropdown
        monthBox = new JComboBox<>();
        monthBox.addItem(null);
        for (int i = 0; i < 12; i++) {
            monthBox.addItem(new DateFormatSymbols().getMonths()[i]);
        }
        datePanel.add(monthBox);

        // Day dropdown
        dayBox = new JComboBox<>();
        dayBox.addItem(null);
        for (int i = 1; i <= 31; i++) {
            dayBox.addItem(i);
        }
        datePanel.add(dayBox);

        // Year dropdown
        yearBox = new JComboBox<>();
        yearBox.addItem(null);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear - 5; i <= currentYear + 5; i++) {
            yearBox.addItem(i);
        }
        datePanel.add(yearBox);

        // Add to main layout
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        add(datePanel, c);


        // --- Search Button ---
        JButton searchButton = new JButton("Search Workouts");
        c.gridx = 0; c.gridy = 5; c.gridwidth = 2;
        add(searchButton, c);

        // --- Text Area to Display Workouts ---
        workoutDisplay = new JTextArea(10, 40);
        workoutDisplay.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(workoutDisplay);
        c.gridy = 6;
        add(scrollPane, c);

        // --- Action Listener ---
        searchButton.addActionListener(ev -> {
            try {
                int month = monthBox.getSelectedIndex() - 1; // Adjust for "Select..."
                Integer day = (Integer) dayBox.getSelectedItem();
                Integer year = (Integer) yearBox.getSelectedItem();

                if (month < 0 || day == null || year == null) {
                    throw new Exception();
                }

                Calendar cal = Calendar.getInstance();
                cal.set(year, month, day);
                String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());

                Set<Workout> workouts = Database.getInstance().getWorkouts(user, formattedDate);
                if (workouts.isEmpty()) {
                    workoutDisplay.setText("No workouts found for " + formattedDate);
                } else {
                    StringBuilder sb = new StringBuilder("Workouts on " + formattedDate + ":\n");
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

            } catch (Exception ex) {
                workoutDisplay.setText("Please select a valid date.");
            }
        });

        setVisible(true);
    }
}
