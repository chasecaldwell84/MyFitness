package MyFitness.ExerciseSession.Workout;

import MyFitness.Database;
import MyFitness.NavBar;

import javax.swing.*;
import java.awt.*;

public abstract class Workout extends JPanel {

    public enum WorkoutType {
        LIFT,
        CARDIO
    }

    protected String workoutName;
    protected WorkoutType workoutType;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    protected int Id;
    protected Database db = Database.getInstance();

    public Workout(JFrame frame, JPanel session, String workoutName) {
        this.workoutName = workoutName;
        setSize(frame.getWidth(), frame.getHeight());
        setLayout(new GridBagLayout());
    }

    public String getWorkoutName() {
        return workoutName;
    }
    public WorkoutType getWorkoutType() {
        return workoutType;
    }
}
