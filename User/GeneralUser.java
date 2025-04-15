package MyFitness.User;

public class GeneralUser extends User {
    double weight;
    double calorieIntake;
    double sleepHours;

    public GeneralUser () {
        super();
        weight = 0;
        calorieIntake = 0;
        sleepHours = 0;
    }

    public GeneralUser(String userName, String password) {
        super(userName, password);
        this.weight = 0;
        this.calorieIntake = 0;
        this.sleepHours = 0;
    }


    public void trackHealthData () {}
    
    public void setGoals () {}
    
    public void viewReports () {}
    
    public void recordWorkout () {}


}
