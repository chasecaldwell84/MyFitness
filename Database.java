package MyFitness;

import MyFitness.ExerciseSession.Workout.CardioWorkout;
import MyFitness.ExerciseSession.Workout.LiftWorkout;
import MyFitness.ExerciseSession.Workout.Workout;
import MyFitness.RyanStuff.Goal;
import MyFitness.User.Admin;
import MyFitness.User.GeneralUser;
import MyFitness.User.Trainer;
import MyFitness.User.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Database {
    private static final String DB_URL = "jdbc:derby:Database;create=true";
    private static Database instance;

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }
    /*
    * Creates or connects to database, makes tables Users, UserStats, UserGoals
    */
    public Database() {
        try(Connection conn = DriverManager.getConnection(DB_URL)){
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(
                    "CREATE TABLE Users (" +
                            "USERNAME VARCHAR(255) NOT NULL PRIMARY KEY, " +
                            "PASSWORD VARCHAR(255) NOT NULL, " +
                            "USERTYPE VARCHAR(255) NOT NULL " +
                            ")"
            );
            stmt.executeUpdate(
                    "CREATE TABLE UserStats (" +
                            "STAT_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                            "USERNAME VARCHAR(255) NOT NULL, " +
                            "PRIMARY KEY (STAT_ID), " +
                            "FOREIGN KEY (USERNAME) REFERENCES Users(USERNAME) ON DELETE CASCADE" +
                            ")"
            );
            //USERS->USERGOALS: GOAL1, GOAL2
            stmt.executeUpdate(
                    "CREATE TABLE UserGoals (" +
                            "GOAL_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                            "USERNAME VARCHAR(255) NOT NULL, " +
                            "GOAL_TYPE VARCHAR(255) NOT NULL, " +
                            "GOAL_VALUE INT NOT NULL, " +
                            "GOAL_LENGTH VARCHAR(255) NOT NULL, " +
                            "PRIMARY KEY (GOAL_ID), " +
                            "FOREIGN KEY (USERNAME) REFERENCES Users(USERNAME) ON DELETE CASCADE " +
                            ")"
            );
            stmt.executeUpdate(
                    "CREATE TABLE Workouts (" +
                            "WORKOUT_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                            "USERNAME VARCHAR(255) NOT NULL, " +
                            "WORKOUTNAME VARCHAR(255) NOT NULL, "+
                            "WORKOUTTYPE VARCHAR(255) NOT NULL, " +
                            "PRIMARY KEY (WORKOUT_ID), " +
                            "FOREIGN KEY (USERNAME) REFERENCES Users(USERNAME) ON DELETE CASCADE " +
                            ")"
            );
            stmt.executeUpdate(
                    "CREATE TABLE WeightLifting (" +
                            "WEIGHTLIFTING_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                            "WORKOUT_ID INTEGER NOT NULL, " +
                            "WEIGHT DOUBLE PRECISION NOT NULL, " +
                            "REPS INTEGER NOT NULL, " +
                            "PRIMARY KEY (WEIGHTLIFTING_ID), " +
                            "FOREIGN KEY (WORKOUT_ID) REFERENCES Workouts(WORKOUT_ID) ON DELETE CASCADE " +
                            ")"
            );
            stmt.executeUpdate(
                    "CREATE TABLE Cardio (" +
                            "CARDIO_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                            "WORKOUT_ID INTEGER NOT NULL, " +
                            "DISTANCE DOUBLE PRECISION NOT NULL, " +
                            "HOURS INTEGER NOT NULL, " +
                            "MINUTES INTEGER NOT NULL, " +
                            "SECONDS INTEGER NOT NULL, " +
                            "PRIMARY KEY(CARDIO_ID), " +
                            "FOREIGN KEY (WORKOUT_ID) REFERENCES Workouts(WORKOUT_ID) ON DELETE CASCADE " +
                            ")"
            );
            stmt.close();
        }
        catch(SQLException e){
            if (!e.getSQLState().equals("X0Y32")) // Table already exists
                e.printStackTrace();
        }
    }
    /*
    * saves new user or updates user in database
    */
    public void saveUser(User user){
        try(Connection conn = DriverManager.getConnection(DB_URL)) {
            if(user.getUserName() != null && findByUsername(user.getUserName()) != null){
                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE Users SET PASSWORD = ? WHERE USERNAME = ?"
                );
                ps.setString(1, user.getUserName());
                ps.setString(2, user.getPassword());
                ps.executeUpdate();
                ps.close();
            }
            else{
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO Users VALUES (?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS
                );
                ps.setString(1, user.getUserName());
                ps.setString(2, user.getPassword());
                if (user instanceof Admin) {
                    ps.setString(3, "admin");
                } else if (user instanceof Trainer) {
                    ps.setString(3, "trainer");
                } else {
                    ps.setString(3, "general");
                }
                ps.executeUpdate();
                ps.close();


            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /*
     * Returns all users this is for admin settings page.
     */
    public List<User> getAllUsers(){
        List<User> users = new ArrayList<>();
        try(Connection conn = DriverManager.getConnection(DB_URL)){

            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Users");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if(Objects.equals(rs.getString("USERTYPE"), "admin")){
                    users.add( new Admin(rs.getString("USERNAME"), rs.getString("PASSWORD")));
                }
                else if(Objects.equals(rs.getString("USERTYPE"), "trainer")){
                    users.add(new Trainer(rs.getString("USERNAME"), rs.getString("PASSWORD")));
                }
                else if(Objects.equals(rs.getString("USERTYPE"), "general")){
                    users.add( new GeneralUser(rs.getString("USERNAME"), rs.getString("PASSWORD")));
                }
                else{
                    throw new SQLException();
                }
            }
            ps.close();
            rs.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return users;
    }
    /*
    * finds user based off of username returns password and username
    */
    public User findByUsername(String username) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Users WHERE USERNAME = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if(Objects.equals(rs.getString("USERTYPE"), "admin")){
                    return new Admin(rs.getString("USERNAME"), rs.getString("PASSWORD"));
                }
                else if(Objects.equals(rs.getString("USERTYPE"), "trainer")){
                    return new Trainer(rs.getString("USERNAME"), rs.getString("PASSWORD"));
                }
                else if(Objects.equals(rs.getString("USERTYPE"), "general")){
                    return new GeneralUser(rs.getString("USERNAME"), rs.getString("PASSWORD"));
                }
                else{
                    throw new SQLException();
                }
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    /*
    * Deletes user based off of username
    */
    public void delete(String username) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM Users WHERE USERNAME = ?");
            ps.setString(1, username);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /*
    * Saves goal to a specific user or updates goal for that specific user if
    * the type, and length matches
    */
    public void saveGoal(User user, Goal goal){
        try(Connection conn = DriverManager.getConnection(DB_URL)){
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT GOAL_ID FROM UserGoals Where USERNAME = ? AND GOAL_TYPE = ? AND GOAL_LENGTH = ?"
            );
            ps.setString(1, user.getUserName());
            ps.setString(2, goal.getGoalType());
            ps.setString(3, goal.getGoalLength());

            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int existingGoalID = rs.getInt("GOAL_ID");

                PreparedStatement update = conn.prepareStatement(
                        "UPDATE UserGoals SET GOAL_VALUE = ? WHERE GOAL_ID = ?"
                );
                update.setInt(1, goal.getGoalValue());
                update.setInt(2, existingGoalID);
                update.executeUpdate();
                ps.close();
                rs.close();
            }
            else {
                PreparedStatement ps2 = conn.prepareStatement(
                        "INSERT INTO UserGoals (USERNAME, GOAL_TYPE, GOAL_VALUE, GOAL_LENGTH ) VALUES (?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS
                );
                ps2.setString(1, user.getUserName());
                ps2.setString(2, goal.getGoalType());
                ps2.setInt(3, goal.getGoalValue());
                ps2.setString(4, goal.getGoalLength());
                ps2.executeUpdate();

                ResultSet keys = ps2.getGeneratedKeys();
                if (keys.next()) {
                    goal.setID(keys.getInt(1));   // Save the generated GOAL_ID into the Goal object
                }
                ps2.close();
                keys.close();
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /*
    * Returns all of the goals for one user
    */
    public List<Goal> findGoalsByUser(User user) {
        List<Goal> goals = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT GOAL_ID, GOAL_TYPE, GOAL_VALUE, GOAL_LENGTH FROM UserGoals WHERE USERNAME = ?"
            );
            ps.setString(1, user.getUserName());

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int goalID = rs.getInt("GOAL_ID");
                String goalType = rs.getString("GOAL_TYPE");
                int goalValue = rs.getInt("GOAL_VALUE");
                String goalLength = rs.getString("GOAL_LENGTH");

                Goal goal = new Goal(goalLength, goalType, goalValue, goalID);
                goals.add(goal);
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return goals;
    }
    /*
    * finds the goal based off of its type aka weight,sleep,calories
    */
    public Goal findByGoalType(String goalType){
        try(Connection conn = DriverManager.getConnection(DB_URL)){
            PreparedStatement ps = conn.prepareStatement("SELECT " +
                    " GOAL_ID, GOAL_TYPE, GOAL_VALUE, GOAL_LENGTH FROM UserGoals WHERE GOAL_TYPE = ?");
            ps.setString(1, goalType);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Goal(
                        rs.getString("GOAL_LENGTH"),
                        rs.getString("GOAL_TYPE"),
                        rs.getInt("GOAL_VALUE"),
                        rs.getInt("GOAL_ID")
                );
            }
            ps.close();
            rs.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

//    /*
    public void SaveWorkout(User user, Workout workout){
        try(Connection conn  = DriverManager.getConnection(DB_URL)){
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM Workouts WHERE USERNAME = ? AND WORKOUT_ID = ? AND WORKOUTNAME = ? AND WORKOUTTYPE = ?"
            );
            ps.setString(1, user.getUserName());
            ps.setInt(2, workout.getId());
            ps.setString(3, workout.getWorkoutName());
            if(workout.getWorkoutType() == Workout.WorkoutType.LIFT){
                LiftWorkout lifting = (LiftWorkout) workout;
                ps.setString(4, String.valueOf(Workout.WorkoutType.LIFT));

                for(LiftWorkout.LiftSet set : lifting.getSets()){
                    PreparedStatement ps2 = conn.prepareStatement(
                            "UPDATE WeightLifting SET WEIGHT = ? AND REPS = ?"
                    );
                    ps2.setDouble(1,set.getWeight());
                    ps2.setDouble(2,set.getReps());
                    ps2.executeUpdate();
                }

                ps.executeUpdate();
            }
            else {
                ps.setString(4, String.valueOf(Workout.WorkoutType.CARDIO));
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        if(workout instanceof LiftWorkout){
            LiftWorkout liftWorkout = (LiftWorkout) workout;
        }
        else{
            CardioWorkout cardioWorkout = (CardioWorkout) workout;
        }

    }//*/

    //FIXME needs to save stats into userstats table
    public void saveStats(){

    }
    //FIXME needs to return stats
    public void getStats(){

    }


    public static void main(String[] args) {
        Database db = new Database();
        GeneralUser john = new GeneralUser("John", "1234");
        GeneralUser Jane = new GeneralUser("Jane", "5678");
        GeneralUser Jack = new GeneralUser("Jack", "5678");
        db.saveUser(john);
        db.saveUser(Jane);
        db.saveUser(Jack);
        System.out.println(db.findByUsername("John"));
        System.out.println(db.findByUsername("Jane") + "\n");


        System.out.println(db.findByUsername("John"));
        Goal goal = new Goal("Year", "Weight", 170);

        System.out.println("Testing individual save and find: \n");
        db.saveGoal(john, goal);
        System.out.println(db.findByGoalType("Weight"));


        System.out.println("Testing multiple saves and find by just user: \n");
        Goal goal2 = new Goal("Month", "Sleep", 1000);
        Goal goal3 = new Goal("Year", "Calorie", 2000);
        Goal goal4 = new Goal("Day", "Weight", 2500);
        db.saveGoal(john, goal2);
        db.saveGoal(john, goal3);
        db.saveGoal(john, goal4);

        List<Goal> johnsGoals = db.findGoalsByUser(john);
        for (Goal g : johnsGoals) {
            System.out.println(g);
        }

        System.out.println("\n Testing updating existing goal should have weight year 100: \n");
        Goal update = new Goal("Year", "Weight", 100);
        db.saveGoal(john, update);
        List<Goal> johnsGoals2 = db.findGoalsByUser(john);
        for (Goal g : johnsGoals2) {
            System.out.println(g);
        }

        System.out.println("\n Testing deleting john user should delete goal data. \n");
        db.delete("John");
        List<Goal> johnsGoals3 = db.findGoalsByUser(john);
        for (Goal g : johnsGoals3) {
            System.out.println(g);
        }

        System.out.println("\n Testing user without any data \n");
        List<Goal> empty = db.findGoalsByUser(Jane);
        for (Goal g : empty) {
            System.out.println(g);
        }
        if(empty.isEmpty()){
            System.out.println("\n Correct \n");
        }

        System.out.println("\n Testing getting all users should have 2 \n");
        List<User> allUsers = db.getAllUsers();
        for (User u : allUsers) {
            System.out.println(u);
        }
        //Testing types of users
        Admin ad2 = new Admin("jeremy", "5678");
        Trainer t1 = new Trainer("Michael", "1234");
        db.saveUser(ad2);
        db.saveUser(t1);

        User test = db.findByUsername("jeremy");
        User TrainerTest = db.findByUsername("Michael");

        if(test instanceof Admin){
            System.out.println("ADMIN");
        }
        else{
            System.out.println("NOT WORKS");
        }

        if(TrainerTest instanceof Trainer){
            System.out.println("TRAINER");
        }
        else{
            System.out.println("NOT WORKS");
        }

        if(test instanceof Trainer){
            System.out.println("should not see this");
        }
        else{
            System.out.println("WORKS");
        }

        //NOTE: trying to save the same name twice to see what it returns
        GeneralUser test1 = new GeneralUser("John1", "1234");
        GeneralUser test2 = new GeneralUser("John1", "5678");
        db.saveUser(test1);
        db.saveUser(test2);





    }
}
