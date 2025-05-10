package MyFitness;

import MyFitness.ExerciseSession.Workout.CardioWorkout;
import MyFitness.ExerciseSession.Workout.LiftWorkout;
import MyFitness.ExerciseSession.Workout.Workout;
import MyFitness.RyanStuff.Goal;
import MyFitness.RyanStuff.SleepReport;
import MyFitness.RyanStuff.WeightReport;
import MyFitness.Statistics.Statistic;
import MyFitness.User.Admin;
import MyFitness.User.GeneralUser;
import MyFitness.User.Trainer;
import MyFitness.User.User;

import java.sql.*;
import java.util.*;


public class Database {
    private static final String DB_URL = "jdbc:derby:Database;create=true";
    private static Database instance;
    private List<User> allUsers = new ArrayList<>();

    public void loadUsers() {
        allUsers.clear();
        allUsers.addAll(getAllUsers());
        System.out.println("Reloaded users: " + allUsers.size());
    }


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
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(
                    "CREATE TABLE Users (" +
                            "USERNAME VARCHAR(255) NOT NULL PRIMARY KEY, " +
                            "PASSWORD VARCHAR(255) NOT NULL, " +
                            "USERTYPE VARCHAR(255) NOT NULL " +
                            ")"
            );
            stmt.executeUpdate(
                    "CREATE TABLE Sleep (" +
                            "SLEEP_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                            "USERNAME VARCHAR(255) NOT NULL, " +
                            "SLEEPHOURS INTEGER NOT NULL, " +
                            "SLEEPMINUTES INTEGER NOT NULL, " +
                            "PRIMARY KEY (SLEEP_ID), " +
                            "FOREIGN KEY (USERNAME) REFERENCES Users(USERNAME) ON DELETE CASCADE" +
                            ")"
            );
            stmt.executeUpdate(
                    "CREATE TABLE Weight (" +
                            "WEIGHT_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                            "USERNAME VARCHAR(255) NOT NULL, " +
                            "WEIGHTVALUE DOUBLE NOT NULL, " +
                            "PRIMARY KEY (WEIGHT_ID), " +
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
                            "WORKOUTNAME VARCHAR(255) NOT NULL, " +
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
            //for WorkoutClasses
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet tables = meta.getTables(null, null, "WORKOUTCLASSES", null);
            if (!tables.next()) {
                stmt.executeUpdate(
                        "CREATE TABLE WorkoutClasses (" +
                                "CLASS_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                                "TRAINER_USERNAME VARCHAR(255) NOT NULL, " +
                                "TITLE VARCHAR(255) NOT NULL, " +
                                "DESCRIPTION VARCHAR(1000), " +
                                "DATETIME VARCHAR(255) NOT NULL, " +
                                "SEATS INTEGER NOT NULL, " +
                                "DAYS VARCHAR(255), " +
                                "SESSION_LENGTH INT, " +
                                "NUM_WEEKS INT, " +
                                "IS_SELF_PACED BOOLEAN DEFAULT FALSE, " +
                                "PRIMARY KEY (CLASS_ID), " +
                                "FOREIGN KEY (TRAINER_USERNAME) REFERENCES Users(USERNAME) ON DELETE CASCADE" +
                                ")"
                );
            }
            tables.close();

            //for classplans
            stmt.executeUpdate(
                    "CREATE TABLE ClassPlans (" +
                            "PLAN_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                            "CLASS_ID INTEGER NOT NULL, " +
                            "EXERCISE_NAME VARCHAR(255) NOT NULL, " +
                            "INSTRUCTION VARCHAR(255), " +
                            "DURATION INT, " +
                            "DESCRIPTION VARCHAR(1000), " +
                            "PRIMARY KEY (PLAN_ID), " +
                            "FOREIGN KEY (CLASS_ID) REFERENCES WorkoutClasses(CLASS_ID) ON DELETE CASCADE" +
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
                    "CREATE TABLE GroupChallenges (" +
                            "GROUPNAME VARCHAR(255), " +
                            "SENDER VARCHAR(255), " +
                            "MESSAGE VARCHAR(255)" +
                            ")"
            );


            stmt.executeUpdate(
                    "CREATE TABLE GroupMessages (" +
                            "GROUPNAME VARCHAR(255), " +
                            "SENDER VARCHAR(255), " +
                            "RECIPIENT VARCHAR(255)," +
                            "MESSAGE VARCHAR(255)" +
                    ")"

            );

            stmt.executeUpdate(
                    "CREATE TABLE GroupDescriptions (" +
                            "    GROUPNAME VARCHAR(255) PRIMARY KEY," +
                            "    DESCRIPTION VARCHAR(1000)" +
                    ")"
            );

            stmt.executeUpdate(
                    "CREATE TABLE GroupOwners (" +
                            "    GROUPNAME VARCHAR(255)," +
                            "    OWNER VARCHAR(255)" +
                    ")"
            );
            stmt.executeUpdate(
                    "CREATE TABLE GroupJoinRequests (" +
                            "    GROUPNAME VARCHAR(255)," +
                            "    USERNAME VARCHAR(255)" +
                            ")"
            );


            stmt.executeUpdate(
                    "CREATE TABLE ClassMembers (" +
                            "CLASS_ID INTEGER NOT NULL, " +
                            "USERNAME VARCHAR(255) NOT NULL, " +
                            "PRIMARY KEY (CLASS_ID, USERNAME), " +
                            "FOREIGN KEY (CLASS_ID) REFERENCES WorkoutClasses(CLASS_ID) ON DELETE CASCADE, " +
                            "FOREIGN KEY (USERNAME) REFERENCES Users(USERNAME) ON DELETE CASCADE" +
                            ")"
            );
            stmt.close();
        } catch (SQLException e) {
            if (!e.getSQLState().equals("X0Y32")) // Table already exists
                e.printStackTrace();
        }
    }

    /*
     * saves new user or updates user in database
     */
    public void saveUser(User user) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (user.getUserName() != null && findByUsername(user.getUserName()) != null) {
                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE Users SET PASSWORD = ? WHERE USERNAME = ?"
                );
                ps.setString(1, user.getPassword());
                ps.setString(2, user.getUserName());
                ps.executeUpdate();
                ps.close();
            } else {
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * Returns all users this is for admin settings page.
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Users");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (Objects.equals(rs.getString("USERTYPE"), "admin")) {
                    users.add(new Admin(rs.getString("USERNAME"), rs.getString("PASSWORD")));
                } else if (Objects.equals(rs.getString("USERTYPE"), "trainer")) {
                    users.add(new Trainer(rs.getString("USERNAME"), rs.getString("PASSWORD")));
                } else if (Objects.equals(rs.getString("USERTYPE"), "general")) {
                    users.add(new GeneralUser(rs.getString("USERNAME"), rs.getString("PASSWORD")));
                } else if (Objects.equals(rs.getString("USERTYPE"), "user")) {
                    users.add(new GeneralUser(rs.getString("USERNAME"), rs.getString("PASSWORD")));
                } else {
                    throw new SQLException();
                }
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
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
                if (Objects.equals(rs.getString("USERTYPE"), "admin")) {
                    return new Admin(rs.getString("USERNAME"), rs.getString("PASSWORD"));
                } else if (Objects.equals(rs.getString("USERTYPE"), "trainer")) {
                    return new Trainer(rs.getString("USERNAME"), rs.getString("PASSWORD"));
                } else if (Objects.equals(rs.getString("USERTYPE"), "general")) {
                    return new GeneralUser(rs.getString("USERNAME"), rs.getString("PASSWORD"));
                } else if (Objects.equals(rs.getString("USERTYPE"), "user")) {//fix
                    return new GeneralUser(rs.getString("USERNAME"), rs.getString("PASSWORD"));
                } else {
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
    public void saveGoal(User user, Goal goal) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT GOAL_ID FROM UserGoals Where USERNAME = ? AND GOAL_TYPE = ? AND GOAL_LENGTH = ?"
            );
            ps.setString(1, user.getUserName());
            ps.setString(2, goal.getGoalType());
            ps.setString(3, goal.getGoalLength());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int existingGoalID = rs.getInt("GOAL_ID");

                PreparedStatement update = conn.prepareStatement(
                        "UPDATE UserGoals SET GOAL_VALUE = ? WHERE GOAL_ID = ?"
                );
                update.setInt(1, goal.getGoalValue());
                update.setInt(2, existingGoalID);
                update.executeUpdate();
                ps.close();
                rs.close();
            } else {
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

        } catch (SQLException e) {
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
    public Goal findByGoalType(String goalType) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveWorkout(User user, Workout workout, String sessionDate) {
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
                } else if (workout.getWorkoutType() == Workout.WorkoutType.CARDIO) {
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

    public Set<Workout> getWorkoutsByDate(User user, String sessionDate) {
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


    public Set<Workout> getAllWorkouts(User user) {
        Set<Workout> workouts = new HashSet<>();

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String workoutQuery = "SELECT * FROM Workouts WHERE USERNAME = ?";
            try (PreparedStatement ps = conn.prepareStatement(workoutQuery)) {
                ps.setString(1, user.getUserName());
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

    //class stuff, author:Dannis Wu
    public void saveClass(String trainerUsername, String title, String description, String dateTime,
                          int seats, String days, int sessionLength, int numWeeks, boolean isSelfPaced) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO WorkoutClasses (TRAINER_USERNAME, TITLE, DESCRIPTION, DATETIME, SEATS, DAYS, SESSION_LENGTH, NUM_WEEKS, IS_SELF_PACED) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );
            ps.setString(1, trainerUsername);
            ps.setString(2, title);
            ps.setString(3, description);
            ps.setString(4, dateTime);
            ps.setInt(5, seats);
            ps.setString(6, days);
            ps.setInt(7, sessionLength);
            ps.setInt(8, numWeeks);
            ps.setBoolean(9, isSelfPaced);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // class stuff
    public List<Map<String, Object>> getAllClasses() {
        List<Map<String, Object>> classes = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM WorkoutClasses");

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                int classId = rs.getInt("CLASS_ID");
                row.put("CLASS_ID", classId);
                row.put("TITLE", rs.getString("TITLE"));
                row.put("TRAINER_USERNAME", rs.getString("TRAINER_USERNAME"));
                row.put("SEATS", rs.getInt("SEATS"));
                row.put("DESCRIPTION", rs.getString("DESCRIPTION"));
                row.put("IS_SELF_PACED", rs.getBoolean("IS_SELF_PACED"));


                // Count current enrollment
                PreparedStatement countPs = conn.prepareStatement("SELECT COUNT(*) FROM ClassMembers WHERE CLASS_ID = ?");
                countPs.setInt(1, classId);
                ResultSet countRs = countPs.executeQuery();
                int current = 0;
                if (countRs.next()) {
                    current = countRs.getInt(1);
                }
                row.put("CURRENT_ENROLLMENT", current);

                countRs.close();
                countPs.close();

                classes.add(row);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classes;
    }


    //savePlanToClass
    public void savePlanToClass(int classId, String exerciseName, String instruction, int duration, String notes) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO ClassPlans (CLASS_ID, EXERCISE_NAME, INSTRUCTION, DURATION, DESCRIPTION) VALUES (?, ?, ?, ?, ?)"
            );
            ps.setInt(1, classId);
            ps.setString(2, exerciseName);
            ps.setString(3, instruction);
            ps.setInt(4, duration);
            ps.setString(5, notes);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //get plans for class
    public List<String[]> getPlansForClass(int classId) {
        List<String[]> plans = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT EXERCISE_NAME, INSTRUCTION, DURATION, DESCRIPTION FROM ClassPlans WHERE CLASS_ID = ?"
            );
            ps.setInt(1, classId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] row = new String[4];
                row[0] = rs.getString("EXERCISE_NAME");
                row[1] = rs.getString("INSTRUCTION");
                row[2] = String.valueOf(rs.getInt("DURATION"));
                row[3] = rs.getString("DESCRIPTION");
                plans.add(row);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plans;
    }

    public String[] getExercisePlan(String planName, String trainerUsername) {
        String[] result = new String[4];
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT EXERCISE_NAME, INSTRUCTION, DURATION, DESCRIPTION FROM ClassPlans " +
                            "WHERE EXERCISE_NAME = ? AND CLASS_ID IN (SELECT CLASS_ID FROM WorkoutClasses WHERE TRAINER_USERNAME = ?)"
            );
            ps.setString(1, planName);
            ps.setString(2, trainerUsername);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result[0] = rs.getString("EXERCISE_NAME");
                result[1] = rs.getString("INSTRUCTION");
                result[2] = String.valueOf(rs.getInt("DURATION"));
                result[3] = rs.getString("DESCRIPTION");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void updateExercisePlan(String planName, String exercise, String instruction, int duration, String notes, String trainerUsername) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE ClassPlans SET EXERCISE_NAME = ?, INSTRUCTION = ?, DURATION = ?, DESCRIPTION = ? " +
                            "WHERE EXERCISE_NAME = ? AND CLASS_ID IN (SELECT CLASS_ID FROM WorkoutClasses WHERE TRAINER_USERNAME = ?)"
            );
            ps.setString(1, exercise);
            ps.setString(2, instruction);
            ps.setInt(3, duration);
            ps.setString(4, notes);
            ps.setString(5, planName);
            ps.setString(6, trainerUsername);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> findClassesByTrainer(String trainerUsername) {
        List<String> classList = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM WorkoutClasses WHERE TRAINER_USERNAME = ?"
            );
            ps.setString(1, trainerUsername);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String dateTime = rs.getString("DATETIME");
                String[] parts = dateTime.split(" ");
                String date = parts.length > 0 ? parts[0] : "N/A";
                String time = parts.length > 1 ? parts[1] : "N/A";
                boolean selfPaced = rs.getBoolean("IS_SELF_PACED");

                String details = String.format(
                        "Class ID: %d\nTitle: %s\nDescription: %s\nStart Date: %s\nTime: %s\nSeats: %d\nDays: %s\nSession Length: %d minutes\nDuration: %d weeks\nSelf-Paced: %s",
                        rs.getInt("CLASS_ID"),
                        rs.getString("TITLE"),
                        rs.getString("DESCRIPTION"),
                        date,
                        time,
                        rs.getInt("SEATS"),
                        rs.getString("DAYS"),
                        rs.getInt("SESSION_LENGTH"),
                        rs.getInt("NUM_WEEKS"),
                        selfPaced ? "Yes" : "No"
                );
                classList.add(details);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classList;
    }

    public boolean updateClassInfo(int classId, String title, String description, String date, String time, int seats, String days, int sessionLength, int numWeeks, boolean isSelfPaced) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE WorkoutClasses SET TITLE=?, DESCRIPTION=?, DATETIME=?, SEATS=?, DAYS=?, SESSION_LENGTH=?, NUM_WEEKS=?, IS_SELF_PACED=? WHERE CLASS_ID=?"
            );
            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, date + " " + time);
            ps.setInt(4, seats);
            ps.setString(5, days);
            ps.setInt(6, sessionLength);
            ps.setInt(7, numWeeks);
            ps.setBoolean(8, isSelfPaced);
            ps.setInt(9, classId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public String[] getClassInfo(int classId) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM WorkoutClasses WHERE CLASS_ID = ?"
            );
            ps.setInt(1, classId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new String[]{
                        rs.getString("TITLE"),
                        rs.getString("DESCRIPTION"),
                        rs.getString("DATETIME"),
                        String.valueOf(rs.getInt("SEATS")),
                        rs.getString("DAYS"),
                        String.valueOf(rs.getInt("SESSION_LENGTH")),
                        String.valueOf(rs.getInt("NUM_WEEKS")),
                        String.valueOf(rs.getBoolean("IS_SELF_PACED"))
                };
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteExercisePlan(String exercise, String trainerUsername) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM ClassPlans WHERE EXERCISE_NAME = ? AND CLASS_ID IN (SELECT CLASS_ID FROM WorkoutClasses WHERE TRAINER_USERNAME = ?)"
            );
            ps.setString(1, exercise);
            ps.setString(2, trainerUsername);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Map<String, Object>> getTrainerClassesWithId(String trainerUsername) {
        List<Map<String, Object>> classList = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM WorkoutClasses WHERE TRAINER_USERNAME = ?"
            );
            ps.setString(1, trainerUsername);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> entry = new HashMap<>();
                entry.put("CLASS_ID", rs.getInt("CLASS_ID"));
                entry.put("TITLE", rs.getString("TITLE"));
                entry.put("DAYS", rs.getString("DAYS"));
                entry.put("SESSION_LENGTH", rs.getInt("SESSION_LENGTH"));
                entry.put("NUM_WEEKS", rs.getInt("NUM_WEEKS"));
                classList.add(entry);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classList;
    }

    public List<Integer> getClassIdsJoinedByUser(String username) {
        List<Integer> classIds = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement ps = conn.prepareStatement("SELECT CLASS_ID FROM ClassMembers WHERE USERNAME = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                classIds.add(rs.getInt("CLASS_ID"));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classIds;
    }

    // not use
    public String getJoinedClassInfo(int classId) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM WorkoutClasses WHERE CLASS_ID = ?");
            ps.setInt(1, classId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String[] dateTimeParts = rs.getString("DATETIME").split(" ");
                String date = dateTimeParts.length > 0 ? dateTimeParts[0] : "N/A";
                String time = dateTimeParts.length > 1 ? dateTimeParts[1] : "N/A";
                boolean isSelfPaced = rs.getBoolean("IS_SELF_PACED");

                return "Class ID: " + rs.getInt("CLASS_ID") + "\n"
                        + "Title: " + rs.getString("TITLE") + "\n"
                        + "Description: " + rs.getString("DESCRIPTION") + "\n"
                        + "Start Date: " + date + "\n"
                        + "Time: " + time + "\n"
                        + "Seats: " + rs.getInt("SEATS") + "\n"
                        + "Days: " + rs.getString("DAYS") + "\n"
                        + "Length: " + rs.getInt("SESSION_LENGTH") + " minutes\n"
                        + "Weeks: " + rs.getInt("NUM_WEEKS") + "\n"
                        + "Self-Paced: " + (isSelfPaced ? "Yes" : "No") + "\n"
                        + "Trainer: " + rs.getString("TRAINER_USERNAME");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "No info found.";
    }

    public List<String> getJoinedClassInfo(String username) {
        List<String> infoList = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM WorkoutClasses WHERE CLASS_ID IN (" +
                            "SELECT CLASS_ID FROM ClassMembers WHERE USERNAME = ?)"
            );
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                boolean isSelfPaced = rs.getBoolean("IS_SELF_PACED");
                String selfPacedText = isSelfPaced ? "Yes" : "No";
                String info = "Class ID: " + rs.getInt("CLASS_ID") +
                        " | Title: " + rs.getString("TITLE") +
                        " | Trainer: " + rs.getString("TRAINER_USERNAME") +
                        " | Seats: " + rs.getInt("SEATS") +
                        " | Description: " + rs.getString("DESCRIPTION") +
                        " | Self-Paced: " + selfPacedText;
                infoList.add(info);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return infoList;
    }


    public String joinClass(String username, int classId) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            // Already joined check
            PreparedStatement checkStmt = conn.prepareStatement(
                    "SELECT * FROM ClassMembers WHERE CLASS_ID = ? AND USERNAME = ?"
            );
            checkStmt.setInt(1, classId);
            checkStmt.setString(2, username);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) return "already_joined";
            rs.close();
            checkStmt.close();

            // Seat availability check
            PreparedStatement seatStmt = conn.prepareStatement(
                    "SELECT SEATS FROM WorkoutClasses WHERE CLASS_ID = ?"
            );
            seatStmt.setInt(1, classId);
            ResultSet seatRs = seatStmt.executeQuery();
            if (!seatRs.next()) return "class_not_found";
            int totalSeats = seatRs.getInt("SEATS");
            seatRs.close();
            seatStmt.close();

            // Enrollment count
            PreparedStatement countStmt = conn.prepareStatement(
                    "SELECT COUNT(*) AS COUNT FROM ClassMembers WHERE CLASS_ID = ?"
            );
            countStmt.setInt(1, classId);
            ResultSet countRs = countStmt.executeQuery();
            countRs.next();
            int currentCount = countRs.getInt("COUNT");
            countRs.close();
            countStmt.close();

            if (currentCount >= totalSeats) return "class_full";

            // Insert into ClassMembers
            PreparedStatement insertStmt = conn.prepareStatement(
                    "INSERT INTO ClassMembers (CLASS_ID, USERNAME) VALUES (?, ?)"
            );
            insertStmt.setInt(1, classId);
            insertStmt.setString(2, username);
            insertStmt.executeUpdate();
            insertStmt.close();

            return "success";

        } catch (SQLException e) {
            e.printStackTrace();
            return "db_error";
        }
    }


    public int getEnrollmentCount(int classId) {
        int count = 0;
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM ClassMembers WHERE CLASS_ID = ?");
            ps.setInt(1, classId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
                System.out.println("Enrollment count for class " + classId + ": " + count);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public String getClassPlanForUser(int classId) {
        StringBuilder planBuilder = new StringBuilder();
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT EXERCISE_NAME, INSTRUCTION, DURATION, DESCRIPTION FROM ClassPlans WHERE CLASS_ID = ?"
            );
            ps.setInt(1, classId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                planBuilder.append("Exercise: ").append(rs.getString("EXERCISE_NAME")).append("\n")
                        .append("Instruction: ").append(rs.getString("INSTRUCTION")).append("\n")
                        .append("Duration: ").append(rs.getInt("DURATION")).append(" min\n")
                        .append("Description: ").append(rs.getString("DESCRIPTION")).append("\n\n");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error retrieving class plan.";
        }
        return planBuilder.length() > 0 ? planBuilder.toString() : "No plan found for this class.";
    }

    public List<Map<String, Object>> getSelfPacedClasses() {
        List<Map<String, Object>> classes = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM WorkoutClasses WHERE IS_SELF_PACED = TRUE");

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                int classId = rs.getInt("CLASS_ID");
                row.put("CLASS_ID", classId);
                row.put("TITLE", rs.getString("TITLE"));
                row.put("TRAINER_USERNAME", rs.getString("TRAINER_USERNAME"));
                row.put("SEATS", rs.getInt("SEATS"));
                row.put("DESCRIPTION", rs.getString("DESCRIPTION"));
                row.put("IS_SELF_PACED", rs.getBoolean("IS_SELF_PACED"));

                // Count enrollment
                PreparedStatement countPs = conn.prepareStatement("SELECT COUNT(*) FROM ClassMembers WHERE CLASS_ID = ?");
                countPs.setInt(1, classId);
                ResultSet countRs = countPs.executeQuery();
                int current = 0;
                if (countRs.next()) {
                    current = countRs.getInt(1);
                }
                row.put("CURRENT_ENROLLMENT", current);
                countRs.close();
                countPs.close();

                classes.add(row);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classes;
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

    public void saveSleep(User user, SleepReport sleepReport) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT SLEEP_ID FROM Sleep WHERE USERNAME = ?"
            );
            ps.setString(1, user.getUserName());
            ResultSet rs = ps.executeQuery();

            /*if (rs.next()) {
                int existingStatsId = rs.getInt("SLEEP_ID");

                PreparedStatement update = conn.prepareStatement(
                        "UPDATE Sleep SET SLEEPHOURS = ?, SLEEPMINUTES = ? WHERE SLEEP_ID = ?"
                );
                update.setInt(1, sleepReport.getHours());
                update.setInt(2, sleepReport.getMinutes());
                update.setInt(3, existingStatsId);
                update.executeUpdate();

                update.close();
            } else {*/
            PreparedStatement insert = conn.prepareStatement(
                    "INSERT INTO Sleep (USERNAME, SLEEPHOURS, SLEEPMINUTES) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            insert.setString(1, user.getUserName());
            insert.setInt(2, sleepReport.getHours());
            insert.setInt(3, sleepReport.getMinutes());
            insert.executeUpdate();

            ResultSet keys = insert.getGeneratedKeys();
            if (keys.next()) {
                sleepReport.setID(keys.getInt(1));
            }

            keys.close();
            insert.close();


            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<SleepReport> getAllSleepReports(User user) {
        List<SleepReport> sleepReports = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT SLEEP_ID, SLEEPHOURS, SLEEPMINUTES FROM Sleep WHERE USERNAME = ?"
            );
            ps.setString(1, user.getUserName());

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int sleepId = rs.getInt("SLEEP_ID");
                int hours = rs.getInt("SLEEPHOURS");
                int minutes = rs.getInt("SLEEPMINUTES");

                SleepReport sleepReport = new SleepReport();
                sleepReport.addSleep(hours, minutes);
                sleepReport.setID(sleepId);
                sleepReports.add(sleepReport);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sleepReports;
    }

    public void saveWeight(User user, WeightReport report) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement insert = conn.prepareStatement(
                    "INSERT INTO Weight (USERNAME, WEIGHTVALUE) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            insert.setString(1, user.getUserName());
            insert.setDouble(2, report.getWeight());
            //insert.setDate(3, java.sql.Date.valueOf(report.getDate())); // assuming LocalDate in report
            insert.executeUpdate();

            ResultSet keys = insert.getGeneratedKeys();
            if (keys.next()) {
                report.setID(keys.getInt(1));
            }

            keys.close();
            insert.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<WeightReport> getAllWeightReports(User user) {
        List<WeightReport> weightReports = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT WEIGHT_ID, WEIGHTVALUE FROM Weight WHERE USERNAME = ?"
            );
            ps.setString(1, user.getUserName());

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("WEIGHT_ID");
                double value = rs.getDouble("WEIGHTVALUE");
                //LocalDate date = rs.getDate("WEIGHTDATE").toLocalDate();

                WeightReport report = new WeightReport();
                report.setID(id);
                report.setWeight(value);

                weightReports.add(report);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return weightReports;
    }


    //FIXME needs to save stats into userstats table
    public void saveStats(){

    }
    //FIXME needs to return stats
    public List<Statistic> getAllStats(String username){
        List<Statistic> stats = new ArrayList<>();
        return stats;
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