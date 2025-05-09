package MyFitness;

import MyFitness.User.User;
import java.util.*;

public class GroupManager {
    private final Map<String, Set<User>> groups = new HashMap<>();

    public void createGroup(String groupName) {
        groups.putIfAbsent(groupName, new HashSet<>());
    }

    public void joinGroup(User user, String groupName) {
        createGroup(groupName);
        groups.get(groupName).add(user);
    }

    public void leaveGroup(User user, String groupName) {
        Set<User> members = groups.get(groupName);
        if (members != null) {
            members.remove(user);
            if (members.isEmpty()) {
                groups.remove(groupName);
            }
        }
    }

    public Set<String> getAllGroupNames() {
        return new HashSet<>(groups.keySet());
    }

    public Set<User> getMembers(String groupName) {
        return groups.getOrDefault(groupName, new HashSet<>());
    }

    public boolean isUserInGroup(User user, String groupName) {
        return groups.getOrDefault(groupName, Collections.emptySet()).contains(user);
    }
}
