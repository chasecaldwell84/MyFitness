package MyFitness;

import MyFitness.User.User;
import java.sql.*;
import java.util.*;

public class GroupManager {
    private final Database db = Database.getInstance();

    public void joinGroup(User user, String groupName) {
        try (Connection conn = DriverManager.getConnection("jdbc:derby:Database")) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO GroupMemberships (GROUPNAME, USERNAME) VALUES (?, ?)");
            ps.setString(1, groupName);
            ps.setString(2, user.getUserName());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void leaveGroup(User user, String groupName) {
        try (Connection conn = DriverManager.getConnection("jdbc:derby:Database")) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM GroupMemberships WHERE GROUPNAME = ? AND USERNAME = ?");
            ps.setString(1, groupName);
            ps.setString(2, user.getUserName());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Set<String> getAllGroupNames() {
        Set<String> groupNames = new HashSet<>();
        try (Connection conn = DriverManager.getConnection("jdbc:derby:Database")) {
            PreparedStatement ps = conn.prepareStatement("SELECT DISTINCT GROUPNAME FROM GroupMemberships");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                groupNames.add(rs.getString("GROUPNAME"));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groupNames;
    }

    public Set<User> getMembers(String groupName) {
        Set<User> members = new HashSet<>();
        try (Connection conn = DriverManager.getConnection("jdbc:derby:Database")) {
            PreparedStatement ps = conn.prepareStatement("SELECT USERNAME FROM GroupMemberships WHERE GROUPNAME = ?");
            ps.setString(1, groupName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String username = rs.getString("USERNAME");
                User user = db.findByUsername(username);
                if (user != null) members.add(user);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }

    public boolean isUserInGroup(User user, String groupName) {
        try (Connection conn = DriverManager.getConnection("jdbc:derby:Database")) {
            PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM GroupMemberships WHERE GROUPNAME = ? AND USERNAME = ?");
            ps.setString(1, groupName);
            ps.setString(2, user.getUserName());
            ResultSet rs = ps.executeQuery();
            boolean result = rs.next();
            rs.close();
            ps.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
