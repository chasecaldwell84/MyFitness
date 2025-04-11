package MyFitness.RyanStuff;

public class Goal {
    private String goalLength;
    private String goalType;
    private int goalValue;

    public Goal(String goalLength, String goalType, int goalValue) {
        this.goalLength = goalLength;
        this.goalType = goalType;
        this.goalValue = goalValue;
    }

    @Override
    public String toString() {
        return goalLength + " long goal for " + goalType + ": " + goalValue;
    }
}