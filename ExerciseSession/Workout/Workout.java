package MyFitness.ExerciseSession.Workout;

import MyFitness.NavBar;

import javax.swing.*;
import java.awt.*;

public class Workout extends JPanel {
    public enum WorkoutType {
        LIFT,
        CARDIO
    }

    protected String workoutName;
    protected WorkoutType workoutType;

    public Workout(JFrame frame, JPanel session, String workoutName) {
        this.workoutName = workoutName;
        setSize(frame.getWidth(), frame.getHeight());
        setLayout(new GridBagLayout());
    }

    public String getWorkoutName() {
        return workoutName;
    }
}
