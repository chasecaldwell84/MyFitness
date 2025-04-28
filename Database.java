package MyFitness;

import MyFitness.RyanStuff.Goal;
import MyFitness.User.User;

import java.sql.*;

public class Database {
    private static final String DB_URL = "jdbc:derby:memory:Database;create=true";

    public Database() {
        try(Connection conn = DriverManager.getConnection(DB_URL)){
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(
                    "CREATE TABLE Users (" +
                            "USER_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) , " +
                            "USERNAME VARCHAR(255) NOT NULL UNIQUE, " +
                            "PASSWORD VARCHAR(255) NOT NULL " +
                            ")"
            );
            stmt.executeUpdate(
                    "CREATE TABLE UserStats (" +
                            "STAT_ID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                            "SLEEP_HOURS INT, " +
                            "CALORIES_BURNED INT, " +
                            "STEPS_TAKEN INT, " +
                            "STAT_DATE DATE, " +
                            "FOREIGN KEY (USER_ID) REFERENCES User(USER_ID) " +
                            ")"
            );
            //USERS->USERGOALS: GOAL1, GOAL2
            stmt.executeUpdate(
                    "CREATE TABLE UserGoals (" +
                            "GOAL_ID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                            "GOAL_TYPE VARCHAR(225) NOT NULL , " +
                            "GOAL_VALUE INT NOT NULL , " +
                            "GOAL_LENGTH, " +
                            "FOREIGN KEY (USER_ID) REFERENCES User(USER_ID) " +
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

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    user.setUserName(rs.getString(1));
                }
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
    //FIXME needs to save goal to UserGoals table
    /*public void saveGoal(Goal goal){
        try(Connection conn = DriverManager.getConnection(DB_URL)){
            PreparedStatement ps = conn.prepareStatement();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }*/
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
        db.saveUser(new User("John", "1234"));
        db.saveUser(new User("Jane", "5678"));
        db.saveUser(new User("Jack", "5678"));
        System.out.println(db.findByUsername("John"));
        System.out.println(db.findByUsername("Jane"));
        db.delete("John");
        System.out.println(db.findByUsername("John"));

    }
}
