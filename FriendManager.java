package MyFitness;

import MyFitness.User.User;

import java.sql.Connection;
import java.util.*;
import java.util.List;
import java.sql.*;


public class FriendManager {
    private final Database db = Database.getInstance();


    public void addFriend(User user, User friend) {
        try (Connection conn = DriverManager.getConnection("jdbc:derby:Database")) {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO Friendships (USER1, USER2) VALUES (?, ?)"
            );
            ps.setString(1, user.getUserName());
            ps.setString(2, friend.getUserName());
            ps.executeUpdate();

            ps.setString(1, friend.getUserName()); // mirror friendship
            ps.setString(2, user.getUserName());
            ps.executeUpdate();

            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getFriends(User user) {
        List<User> friends = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:derby:Database")) {
            PreparedStatement ps = conn.prepareStatement("SELECT USER2 FROM Friendships WHERE USER1 = ?");
            ps.setString(1, user.getUserName());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String friendUsername = rs.getString("USER2");
                User friend = db.findByUsername(friendUsername);
                if (friend != null) friends.add(friend);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friends;
    }

    public void sendChallenge(User sender, User recipient, String challenge) {
        try (Connection conn = DriverManager.getConnection("jdbc:derby:Database")) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Challenges (SENDER, RECIPIENT, MESSAGE) VALUES (?, ?, ?)");
            ps.setString(1, sender.getUserName());
            ps.setString(2, recipient.getUserName());
            ps.setString(3, challenge);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getChallenges(User user) {
        List<String> challenges = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:derby:Database")) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Challenges WHERE RECIPIENT = ? ORDER BY TIMESTAMP DESC");
            ps.setString(1, user.getUserName());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String sender = rs.getString("SENDER");
                String message = rs.getString("MESSAGE");
                challenges.add("From " + sender + ": " + message);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return challenges;
    }

    public void removeFriend(User user, User friend) {
        try (Connection conn = DriverManager.getConnection("jdbc:derby:Database")) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM Friendships WHERE (USER1 = ? AND USER2 = ?) OR (USER1 = ? AND USER2 = ?)");
            ps.setString(1, user.getUserName());
            ps.setString(2, friend.getUserName());
            ps.setString(3, friend.getUserName());
            ps.setString(4, user.getUserName());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}


