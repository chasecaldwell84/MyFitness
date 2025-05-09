package MyFitness.RyanStuff;

public class CalorieReport {
    //variables for calories consumed and daily goal
    private int caloriesConsumed;
    private int dailyGoal;
    private int ID;
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }


    //constructor to initialize the calorie report with a daily goal
    public CalorieReport(int dailyGoal) {
        this.dailyGoal = dailyGoal;
        this.caloriesConsumed = 0;
    }

    //method to add calories consumed throughout the day
    public void addCalories(int calories) {
        if (calories > 0) {
            this.caloriesConsumed += calories;
        }
    }

    //getter for total calories
    public int getTotalCalories() {
        return caloriesConsumed;
    }

    //getter for remaining calories
    public int getRemainingCalories() {
        return Math.max(dailyGoal - caloriesConsumed, 0); //prevent negative remaining calories
    }

    //getter for daily goal
    public int getDailyGoal() {
        return dailyGoal;
    }
}