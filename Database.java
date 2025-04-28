package MyFitness;

import MyFitness.User.User;

import java.sql.*;

public class Database {
    private static final String DB_URL = "jdbc:derby:memory:Database;create=true";

    public Database() {
        try(Connection conn = DriverManager.getConnection(DB_URL)){
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(
                    "CREATE TABLE Users (" +
                            "USERNAME VARCHAR(255) NOT NULL, " +
                            "PASSWORD VARCHAR(255) NOT NULL " +
                            ")"
            );
            stmt.close();
        }
        catch(SQLException e){
            if (!e.getSQLState().equals("X0Y32")) // Table already exists
                e.printStackTrace();
        }
    }
    public void save(User user){
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
    public static void main(String[] args) {
        Database db = new Database();
        db.save(new User("John", "1234"));
        db.save(new User("Jane", "5678"));
        db.save(new User("Jack", "5678"));
        System.out.println(db.findByUsername("John"));
        System.out.println(db.findByUsername("Jane"));
        db.delete("John");
        System.out.println(db.findByUsername("John"));

    }
}
