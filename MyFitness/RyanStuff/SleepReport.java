package MyFitness.RyanStuff;

public class SleepReport {
    private int hours;
    private int minutes;

    public SleepReport() {
        this.hours = 0;
        this.minutes = 0;
    }

    public void addSleep(int addHours, int addMinutes) {
        if(addHours >= 0 && addMinutes >= 0 && addMinutes < 60) {
            this.hours += addHours;
            this.minutes += addMinutes;
            fix();
        }
    }

    private void fix() {
        if(minutes >= 60) {
            hours += minutes / 60;
            minutes = minutes % 60;
        }
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void reset(){
        this.hours = 0;
        this.minutes = 0;
    }
}