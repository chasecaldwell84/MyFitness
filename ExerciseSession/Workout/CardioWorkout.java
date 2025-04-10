package MyFitness.ExerciseSession.Workout;

import javax.swing.*;

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

    CardioWorkout(JFrame frame, JPanel session) {
        super(frame, session);
        this.workoutType = WorkoutType.CARDIO;
//        this.workoutName = workoutName;
//        this.distance = distance;
//        this.duration = new CardioDuration(hours, minutes, seconds);
    }
}
