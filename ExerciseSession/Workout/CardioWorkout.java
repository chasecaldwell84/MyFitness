package MyFitness.ExerciseSession.Workout;

public class CardioWorkout extends Workout {
    private double distance;
    private CardioDuration duration;

    public static class CardioDuration {
        private int hours;
        private int minutes;
        private int seconds;

        public CardioDuration(int hours, int minutes, int seconds) {
            this.hours = hours;
            this.minutes = minutes;
            this.seconds = seconds;
        }

        public String getTime(){
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setDuration(CardioDuration duration) {
        this.duration = duration;
    }

    public double getDistance() {
        return distance;
    }

    public String getDuration() {
        return duration.getTime();
    }

    public int getHours(){
        return duration.hours;
    }
    public int getMinutes(){
        return duration.minutes;
    }
    public int getSeconds(){
        return duration.seconds;
    }

    public CardioWorkout(String workoutName, double distance, int hours, int minutes, int seconds) {
        this.workoutName = workoutName;
        this.distance = distance;
        this.duration = new CardioDuration(hours, minutes, seconds);
        this.workoutType = WorkoutType.CARDIO;
    }
}
