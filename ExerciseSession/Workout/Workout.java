package MyFitness.ExerciseSession.Workout;

import javax.swing.*;

public class Workout extends JPanel {
    public enum WorkoutType {
        LIFT,
        CARDIO
    }

    protected String workoutName;
    protected WorkoutType workoutType;

    public Workout() {
        this.workoutName = null;
        this.workoutType = null;
    }
}
