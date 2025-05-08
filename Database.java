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
import java.util.*;


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
                            "SESSION_DATE VARCHAR(225) NOT NULL, " +
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
            stmt.executeUpdate(
                    "CREATE TABLE Friendships (" +
                            "USER1 VARCHAR(255) NOT NULL, " +
                            "USER2 VARCHAR(255) NOT NULL, " +
                            "PRIMARY KEY (USER1, USER2), " +
                            "FOREIGN KEY (USER1) REFERENCES Users(USERNAME) ON DELETE CASCADE, " +
                            "FOREIGN KEY (USER2) REFERENCES Users(USERNAME) ON DELETE CASCADE" +
                            ")"
            );

            stmt.executeUpdate(
                    "CREATE TABLE Challenges (" +
                            "SENDER VARCHAR(255) NOT NULL, " +
                            "RECIPIENT VARCHAR(255) NOT NULL, " +
                            "MESSAGE VARCHAR(255) NOT NULL, " +
                            "TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "FOREIGN KEY (SENDER) REFERENCES Users(USERNAME) ON DELETE CASCADE, " +
                            "FOREIGN KEY (RECIPIENT) REFERENCES Users(USERNAME) ON DELETE CASCADE" +
                            ")"
            );

            stmt.executeUpdate(
                    "CREATE TABLE GroupMemberships (" +
                            "GROUPNAME VARCHAR(255) NOT NULL, " +
                            "USERNAME VARCHAR(255) NOT NULL, " +
                            "PRIMARY KEY (GROUPNAME, USERNAME), " +
                            "FOREIGN KEY (USERNAME) REFERENCES Users(USERNAME) ON DELETE CASCADE" +
                            ")"
            );

            stmt.executeUpdate(
                        "CREATE TABLE TrainerClasses (" +
                            "CLASSNAME VARCHAR(255) PRIMARY KEY, " +
                            "TRAINER VARCHAR(255), " +
                            "DATE VARCHAR(255), " +
                            "TIME VARCHAR(255), " +
                                "DESCRIPTION VARCHAR(1000)"
            );
            stmt.executeUpdate(
                    "CREATE TABLE ClassRequests (" +
                            "CLASSNAME VARCHAR(255), " +
                            "USERNAME VARCHAR(255)"

            );
            stmt.executeUpdate(
                        "CREATE TABLE GroupChallenges (" +
                            "GROUPNAME VARCHAR(255), " +
                            "SENDER VARCHAR(255), " +
                            "MESSAGE VARCHAR(255)"


            );

            stmt.executeUpdate(
                    "CREATE TABLE GroupMessages (" +
                            "GROUPNAME VARCHAR(255), " +
                            "SENDER VARCHAR(255), " +
                            "RECIPIENT VARCHAR(255)," +
                            "MESSAGE VARCHAR(255)"

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
                ps.setString(1, user.getPassword());
                ps.setString(2, user.getUserName());
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
                //Dannis fix that for signup
                if (user instanceof Admin) {
                    ps.setString(3, "admin");
                } else if (user instanceof Trainer) {
                    ps.setString(3, "trainer");
                } else if (user instanceof GeneralUser) {
                    ps.setString(3, "user");
                } else {
                    ps.setString(3, "general");//useless just in case
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

    public void saveWorkout(User user, Workout workout, String sessionDate){
        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            // First, check if the workout already exists in the Workouts table
            String checkSql = "SELECT 1 FROM Workouts WHERE USERNAME = ? AND WORKOUT_ID = ? AND WORKOUTNAME = ? AND SESSION_DATE = ? AND WORKOUTTYPE = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, user.getUserName());
                checkStmt.setInt(2, workout.getId());
                checkStmt.setString(3, workout.getWorkoutName());
                checkStmt.setString(4, sessionDate);
                checkStmt.setString(5, workout.getWorkoutType().name());

                ResultSet rs = checkStmt.executeQuery();
                boolean exists = rs.next();
                rs.close();

                if (!exists) {
                    // Insert into Workouts table
                    String insertWorkoutSql = "INSERT INTO Workouts (USERNAME, WORKOUTNAME, SESSION_DATE, WORKOUTTYPE) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertWorkoutSql, Statement.RETURN_GENERATED_KEYS)) {
                        insertStmt.setString(1, user.getUserName());
                        insertStmt.setString(2, workout.getWorkoutName());
                        insertStmt.setString(3, sessionDate);
                        insertStmt.setString(4, workout.getWorkoutType().name());
                        insertStmt.executeUpdate();

                        // Get the generated workout ID
                        try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                int generatedId = generatedKeys.getInt(1);
                                workout.setId(generatedId); // Make sure this method exists
                            } else {
                                throw new SQLException("Creating workout failed, no ID obtained.");
                            }
                        }
                    }
                }

                if (workout.getWorkoutType() == Workout.WorkoutType.LIFT) {
                    LiftWorkout lifting = (LiftWorkout) workout;

                    for (LiftWorkout.LiftSet set : lifting.getSets()) {
                        boolean updated = false;

                        if (set.getLiftID() != null) {
                            String updateSql = "UPDATE WeightLifting SET WEIGHT = ?, REPS = ? WHERE WORKOUT_ID = ? AND WEIGHTLIFTING_ID = ?";
                            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                                updateStmt.setDouble(1, set.getWeight());
                                updateStmt.setDouble(2, set.getReps());
                                updateStmt.setInt(3, workout.getId());
                                updateStmt.setInt(4, set.getLiftID());
                                int rowsAffected = updateStmt.executeUpdate();
                                updated = rowsAffected > 0;
                            }
                        }

                        if (!updated) {
                            String insertSql = "INSERT INTO WeightLifting (WORKOUT_ID, WEIGHT, REPS) VALUES (?, ?, ?)";
                            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                                insertStmt.setInt(1, workout.getId());
                                insertStmt.setDouble(2, set.getWeight());
                                insertStmt.setDouble(3, set.getReps());
                                insertStmt.executeUpdate();

                                try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                                    if (generatedKeys.next()) {
                                        int generatedId = generatedKeys.getInt(1);
                                        set.setLiftID(generatedId);
                                    }
                                }
                            }
                        }
                    }
                }
                else if (workout.getWorkoutType() == Workout.WorkoutType.CARDIO) {
                    CardioWorkout cardio = (CardioWorkout) workout;

                    String updateSql = "UPDATE Cardio SET DISTANCE = ?, HOURS = ?, MINUTES = ?, SECONDS = ? WHERE WORKOUT_ID = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setDouble(1, cardio.getDistance());
                        updateStmt.setDouble(2, cardio.getHours());
                        updateStmt.setDouble(3, cardio.getMinutes());
                        updateStmt.setDouble(4, cardio.getSeconds());
                        updateStmt.setInt(5, workout.getId());

                        int rowsAffected = updateStmt.executeUpdate();

                        if (rowsAffected == 0) {
                            String insertSql = "INSERT INTO Cardio (WORKOUT_ID, DISTANCE, HOURS, MINUTES, SECONDS) VALUES (?, ?, ?, ?, ?)";
                            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                                insertStmt.setInt(1, workout.getId());
                                insertStmt.setDouble(2, cardio.getDistance());
                                insertStmt.setDouble(3, cardio.getHours());
                                insertStmt.setDouble(4, cardio.getMinutes());
                                insertStmt.setDouble(5, cardio.getSeconds());
                                insertStmt.executeUpdate();
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Set<Workout> getWorkouts(User user, String sessionDate) {
        Set<Workout> workouts = new HashSet<>();

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String workoutQuery = "SELECT * FROM Workouts WHERE USERNAME = ? AND SESSION_DATE = ?";
            try (PreparedStatement ps = conn.prepareStatement(workoutQuery)) {
                ps.setString(1, user.getUserName());
                ps.setString(2, sessionDate);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int workoutId = rs.getInt("WORKOUT_ID");
                    String workoutName = rs.getString("WORKOUTNAME");
                    String workoutType = rs.getString("WORKOUTTYPE");

                    if (workoutType.equals("LIFT")) {
                        LiftWorkout liftWorkout = new LiftWorkout(workoutName);
                        liftWorkout.setId(workoutId);

                        String liftQuery = "SELECT * FROM WeightLifting WHERE WORKOUT_ID = ?";
                        try (PreparedStatement liftPs = conn.prepareStatement(liftQuery)) {
                            liftPs.setInt(1, workoutId);
                            ResultSet liftRs = liftPs.executeQuery();
                            while (liftRs.next()) {
                                double weight = liftRs.getDouble("WEIGHT");
                                int reps = liftRs.getInt("REPS");
                                liftWorkout.addSet(new LiftWorkout.LiftSet(weight, reps));
                            }
                            liftRs.close();
                        }

                        workouts.add(liftWorkout);
                    } else if (workoutType.equals("CARDIO")) {
                        String cardioQuery = "SELECT * FROM Cardio WHERE WORKOUT_ID = ?";
                        try (PreparedStatement cardioPs = conn.prepareStatement(cardioQuery)) {
                            cardioPs.setInt(1, workoutId);
                            ResultSet cardioRs = cardioPs.executeQuery();

                            if (cardioRs.next()) {
                                double distance = cardioRs.getDouble("DISTANCE");
                                int hours = cardioRs.getInt("HOURS");
                                int minutes = cardioRs.getInt("MINUTES");
                                int seconds = cardioRs.getInt("SECONDS");

                                CardioWorkout cardioWorkout = new CardioWorkout(workoutName, distance, hours, minutes, seconds);
                                cardioWorkout.setId(workoutId);
                                workouts.add(cardioWorkout);
                            }

                            cardioRs.close();
                        }
                    }
                }

                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return workouts;
    }

    public String[] getTrainerClasses(String trainerName) {
        List<String> classes = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:derby:Database")) {
            PreparedStatement ps = conn.prepareStatement("SELECT CLASSNAME FROM TrainerClasses WHERE TRAINER = ?");
            ps.setString(1, trainerName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                classes.add(rs.getString("CLASSNAME"));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classes.toArray(new String[0]);
    }

    public String getClassDescription(String className) {
        StringBuilder sb = new StringBuilder();
        try (Connection conn = DriverManager.getConnection("jdbc:derby:Database")) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM TrainerClasses WHERE CLASSNAME = ?");
            ps.setString(1, className);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                sb.append("Class: ").append(rs.getString("CLASSNAME")).append("\n")
                        .append("Trainer: ").append(rs.getString("TRAINER")).append("\n")
                        .append("Date: ").append(rs.getString("DATE")).append("\n")
                        .append("Time: ").append(rs.getString("TIME")).append("\n")
                        .append("Description: ").append(rs.getString("DESCRIPTION"));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public void requestJoinClass(String username, String className) {
        try (Connection conn = DriverManager.getConnection("jdbc:derby:Database")) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO ClassRequests (CLASSNAME, USERNAME) VALUES (?, ?)");
            ps.setString(1, className);
            ps.setString(2, username);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //FIXME needs to save stats into userstats table
    public void saveStats(){

    }
    //FIXME needs to return stats
    public void getStats(){

    }


    /*public static void main(String[] args) {
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

    }*/
}
