package MyFitness.ExerciseSession;

import MyFitness.ExerciseSession.Workout.*;

import javax.swing.*;
import java.util.HashSet;
import java.util.Set;

public class ExerciseSession extends JPanel {
    String date;
    Set<Workout> workouts;

    public ExerciseSession(String date) {
        this.date = date;
        this.workouts = new HashSet<>();

        //TODO: Make UI

    }

    public void setDate(String date){
        this.date = date;
    }

//    NOTE: This is old code that still needs to be refactored.
//    public void addWorkout(Workout workout) {
//        if(!workouts.contains(workout)){
//            workouts.add(workout);
//        }
//        else {
//            // NOTE: want to add a set to the list of workouts
//        }
//    }
//
//
//    @Override
//    public String toString() {
//        StringBuilder sessionData = new StringBuilder();
//        sessionData.append(date).append("\n");
//        for (Workout workout : workouts) {
//            sessionData.append(workout.toString()).append("\n");
//        }
//        return sessionData.toString();
//    }
}
