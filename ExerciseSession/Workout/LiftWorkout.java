package MyFitness.ExerciseSession.Workout;

import java.util.ArrayList;

public class LiftWorkout extends Workout {
    private ArrayList<LiftSet> sets;

    public class LiftSet {
        private double weight;
        private int reps;

        LiftSet(double weight, int reps) {
            this.weight = weight;
            this.reps = reps;
        }
    }

    public LiftWorkout(String workoutName, double weight, int reps) {
        this.workoutType = WorkoutType.LIFT;
        this.workoutName = workoutName;
        sets = new ArrayList<LiftSet>();
        sets.add(new LiftSet(weight, reps));
    }

    public void addLiftSet(double weight, int reps) {
        sets.add(new LiftSet(weight, reps));
    }
}
