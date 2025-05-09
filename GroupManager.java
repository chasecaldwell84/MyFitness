package MyFitness;

import MyFitness.User.User;
import java.sql.*;
import java.util.*;

public class GroupManager {
    private final Database db = Database.getInstance();

    public void createGroup(User user, String groupName, String description) {
        joinGroup(user, groupName);
        try (Connection conn = DriverManager.getConnection("jdbc:derby:Database")) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO GroupDescriptions (GROUPNAME, DESCRIPTION) VALUES (?, ?)");
            ps.setString(1, groupName);
            ps.setString(2, description);
            ps.executeUpdate();
            ps.close();

            ps = conn.prepareStatement("INSERT INTO GroupOwners (GROUPNAME, OWNER) VALUES (?, ?)");
            ps.setString(1, groupName);
            ps.setString(2, user.getUserName());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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

    public String getGroupDescription(String groupName) {
        try (Connection conn = DriverManager.getConnection("jdbc:derby:Database")) {
            PreparedStatement ps = conn.prepareStatement("SELECT DESCRIPTION FROM GroupDescriptions WHERE GROUPNAME = ?");
            ps.setString(1, groupName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("DESCRIPTION");
            ps.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "No description available.";
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

    public void postGroupChallenge(String group, String sender, String message) {
        try (Connection conn = DriverManager.getConnection("jdbc:derby:Database")) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO GroupChallenges (GROUPNAME, SENDER, MESSAGE) VALUES (?, ?, ?)");
            ps.setString(1, group);
            ps.setString(2, sender);
            ps.setString(3, message);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void sendGroupMessage(String group, String from, String to, String message) {
        try (Connection conn = DriverManager.getConnection("jdbc:derby:Database")) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO GroupMessages (GROUPNAME, SENDER, RECIPIENT, MESSAGE) VALUES (?, ?, ?, ?)");
            ps.setString(1, group);
            ps.setString(2, from);
            ps.setString(3, to);
            ps.setString(4, message);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteGroup(String groupName) {
        try (Connection conn = DriverManager.getConnection("jdbc:derby:Database")) {
            String[] tables = {"GroupMemberships", "GroupDescriptions", "GroupOwners", "GroupJoinRequests", "GroupChallenges", "GroupMessages"};
            for (String table : tables) {
                PreparedStatement ps = conn.prepareStatement("DELETE FROM " + table + " WHERE GROUPNAME = ?");
                ps.setString(1, groupName);
                ps.executeUpdate();
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void rejectJoinRequest(User user, String groupName) {
        try (Connection conn = DriverManager.getConnection("jdbc:derby:Database")) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM GroupJoinRequests WHERE GROUPNAME = ? AND USERNAME = ?");
            ps.setString(1, groupName);
            ps.setString(2, user.getUserName());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void approveJoinRequest(User user, String groupName) {
        joinGroup(user, groupName);
        try (Connection conn = DriverManager.getConnection("jdbc:derby:Database")) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM GroupJoinRequests WHERE GROUPNAME = ? AND USERNAME = ?");
            ps.setString(1, groupName);
            ps.setString(2, user.getUserName());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getJoinRequests(String groupName) {
        List<User> requests = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:derby:Database")) {
            PreparedStatement ps = conn.prepareStatement("SELECT USERNAME FROM GroupJoinRequests WHERE GROUPNAME = ?");
            ps.setString(1, groupName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User u = db.findByUsername(rs.getString("USERNAME"));
                if (u != null) requests.add(u);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    public void requestToJoinGroup(User user, String groupName) {
        try (Connection conn = DriverManager.getConnection("jdbc:derby:Database")) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO GroupJoinRequests (GROUPNAME, USERNAME) VALUES (?, ?)");
            ps.setString(1, groupName);
            ps.setString(2, user.getUserName());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isGroupOwner(User user, String groupName) {
        try (Connection conn = DriverManager.getConnection("jdbc:derby:Database")) {
            PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM GroupOwners WHERE GROUPNAME = ? AND OWNER = ?");
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
