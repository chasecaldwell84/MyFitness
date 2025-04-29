package MyFitness;

import MyFitness.RyanStuff.Goal;
import MyFitness.User.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Database {
    private static final String DB_URL = "jdbc:derby:memory:Database;create=true";

    public Database() {
        try(Connection conn = DriverManager.getConnection(DB_URL)){
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(
                    "CREATE TABLE Users (" +
                            "USERNAME VARCHAR(255) NOT NULL PRIMARY KEY, " +
                            "PASSWORD VARCHAR(255) NOT NULL " +
                            ")"
            );
            stmt.executeUpdate(
                    "CREATE TABLE UserStats (" +
                            "STAT_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                            "USERNAME VARCHAR(255) NOT NULL, " +
                            "PRIMARY KEY (STAT_ID), " +
                            "FOREIGN KEY (USERNAME) REFERENCES Users(USERNAME) " +
                            ")"
            );
            //USERS->USERGOALS: GOAL1, GOAL2
            stmt.executeUpdate(
                    "CREATE TABLE UserGoals (" +
                            "GOAL_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                            "USERNAME VARCHAR(255) NOT NULL, " +
                            "GOAL_TYPE VARCHAR(225) NOT NULL, " +
                            "GOAL_VALUE INT NOT NULL, " +
                            "GOAL_LENGTH VARCHAR(225) NOT NULL, " +
                            "PRIMARY KEY (GOAL_ID), " +
                            "FOREIGN KEY (USERNAME) REFERENCES Users(USERNAME) " +
                            ")"
            );
            stmt.close();
        }
        catch(SQLException e){
            if (!e.getSQLState().equals("X0Y32")) // Table already exists
                e.printStackTrace();
        }
    }
    public void saveUser(User user){
        try(Connection conn = DriverManager.getConnection(DB_URL)) {
            if(user.getUserName() != null && findByUsername(user.getUserName()) != null){
                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE Users SET PASSWORD = ? WHERE USERNAME = ?"
                );
                ps.setString(1, user.getUserName());
                ps.setString(2, user.getPassword());
                ps.executeUpdate();
            }
            else{
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO Users VALUES (?, ?)",
                        Statement.RETURN_GENERATED_KEYS
                );
                ps.setString(1, user.getUserName());
                ps.setString(2, user.getPassword());
                ps.executeUpdate();

            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public User findByUsername(String username) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Users WHERE USERNAME = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getString("USERNAME"),
                        rs.getString("PASSWORD")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void delete(String username) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM Users WHERE USERNAME = ?");
            ps.setString(1, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*public void saveGoal(User user, Goal goal){
        try(Connection conn = DriverManager.getConnection(DB_URL)){
            if(goal.getGoalType() != null && findByGoalType(goal.getGoalType()) != null){
                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE UserGoals SET GOAL_TYPE=?, GOAL_VALUE=?, GOAL_LENGTH=? WHERE GOAL_ID=? AND USERNAME = ?"
                );
                ps.setString(1, goal.getGoalType());
                ps.setInt(2, goal.getGoalValue());
                ps.setString(3, goal.getGoalLength());
                ps.setInt(4,goal.getID());
                ps.setString(5, user.getUserName());

                ps.executeUpdate();
            }
            else {
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO UserGoals (USERNAME, GOAL_TYPE, GOAL_VALUE, GOAL_LENGTH ) VALUES (?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS
                );
                ps.setString(1, user.getUserName());
                ps.setString(2, goal.getGoalType());
                ps.setInt(3, goal.getGoalValue());
                ps.setString(4, goal.getGoalLength());
                ps.executeUpdate();

                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    goal.setID(keys.getInt(1));   // Save the generated GOAL_ID into the Goal object
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }*/
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
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
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
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return goals;
    }
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
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    //FIXME needs to return Goal type with all goals so the app can display
    public Goal getAllGoals(){
        return null;
    }
    //FIXME needs to save stats into userstats table
    public void saveStats(){

    }
    //FIXME needs to return stats
    public void getStats(){

    }

    public static void main(String[] args) {
        Database db = new Database();
        User john = new User("John", "1234");
        db.saveUser(john);
        db.saveUser(new User("Jane", "5678"));
        db.saveUser(new User("Jack", "5678"));
        System.out.println(db.findByUsername("John"));
        System.out.println(db.findByUsername("Jane"));
        db.delete("Jane");

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

    }
}
