package MyFitness.RyanStuff;

import MyFitness.User.User;

public class Goal {
    public String getGoalLength() {
        return goalLength;
    }

    public void setGoalLength(String goalLength) {
        this.goalLength = goalLength;
    }

    public String getGoalType() {
        return goalType;
    }

    public void setGoalType(String goalType) {
        this.goalType = goalType;
    }

    public int getGoalValue() {
        return goalValue;
    }

    public void setGoalValue(int goalValue) {
        this.goalValue = goalValue;
    }

    private String goalLength;
    private String goalType;
    private int goalValue;
    private User user;
    private int ID;
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }


    public Goal(String goalLength, String goalType, int goalValue, User user) {
        this.goalLength = goalLength;
        this.goalType = goalType;
        this.goalValue = goalValue;
        this.ID = 0;
        this.user = user;
    }
    public Goal(String goalLength, String goalType, int goalValue, int goalID) {
        this.goalLength = goalLength;
        this.goalType = goalType;
        this.goalValue = goalValue;
        this.ID = goalID;
    }

    @Override
    public String toString() {
        return goalLength + " long goal for " + goalType + ": " + goalValue;
    }
}