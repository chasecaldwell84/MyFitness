package MyFitness.ExerciseSession.Workout;

import MyFitness.Database;

public abstract class Workout {

    public Workout() {}

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

    public String getWorkoutName() {
        return workoutName;
    }
    public WorkoutType getWorkoutType() {
        return workoutType;
    }
}
