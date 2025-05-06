package MyFitness;

import MyFitness.User.User;
import java.util.*;
import java.util.List;

public class FriendManager {
    private Map<User, List<User>> friendsMap = new HashMap<>();
    private Map<User, List<String>> challengesMap = new HashMap<>();

    public void addFriend(User user, User friend) {
        friendsMap.computeIfAbsent(user, k -> new ArrayList<>()).add(friend);
        friendsMap.computeIfAbsent(friend, k -> new ArrayList<>()).add(user);
    }

    public List<User> getFriends(User user) {
        return friendsMap.getOrDefault(user, new ArrayList<>());
    }

    public void sendChallenge(User sender, User recipient, String challenge) {
        if (!getFriends(sender).contains(recipient)) {
            System.out.println("Cannot send challenge. You are not friends.");
            return;
        }

        String message = "Challenge from " + sender.getUserName() + ": " + challenge;
        challengesMap.computeIfAbsent(recipient, k -> new ArrayList<>()).add(message);
        System.out.println("Challenge sent to " + recipient.getUserName());
    }

    public List<String> getChallenges(User user) {
        return challengesMap.getOrDefault(user, new ArrayList<>());
    }
}


