package MyFitness.ExerciseSession.Workout;

public class CardioWorkout extends Workout {
    private double distance;
    private CardioDuration duration;

    public class CardioDuration {
        private long hours;
        private int minutes;
        private int seconds;

        CardioDuration(long hours, int minutes, int seconds) {
            this.hours = hours;
            this.minutes = minutes;
            this.seconds = seconds;
        }
    }

    CardioWorkout(String workoutName, long hours, int minutes, int seconds, double distance) {
        this.workoutType = WorkoutType.CARDIO;
        this.workoutName = workoutName;
        this.distance = distance;
        this.duration = new CardioDuration(hours, minutes, seconds);
    }
}
