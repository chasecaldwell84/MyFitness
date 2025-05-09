package MyFitness.ExerciseSession.Workout;

import java.util.ArrayList;

public class LiftWorkout extends Workout {
    private ArrayList<LiftSet> sets;

    public static class LiftSet {
        private double weight;
        private int reps;
        private Integer liftID;

        public LiftSet(double weight, int reps) {
            this.weight = weight;
            this.reps = reps;
        }

        public double getWeight() {
            return weight;
        }

        public int getReps() {
            return reps;
        }

        public void setLiftID(int liftID) { this.liftID = liftID; }

        public Integer getLiftID() { return liftID; }
    }

    public LiftWorkout(String workoutName) {
        this.workoutName = workoutName;
        this.workoutType = WorkoutType.LIFT;
        this.sets = new ArrayList<LiftSet>();
    }

    public void addSet(LiftSet set) {
        sets.add(set);
    }

    public ArrayList<LiftSet> getSets() {
        return sets;
    }
}
