package sample;

import java.util.HashSet;
import java.util.Set;

public class Group {
    private String name;
    private Set<User> members = new HashSet<User>();
    private Set<User> goalMembers = new HashSet<User>();
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Set<User> getMembers() {
        return members;
    }

    public void setMembers(Set<User> members) {
        this.members = members;
    }

    public Set<User> getGoalMembers() {
        return goalMembers;
    }

    public void setGoalMembers(Set<User> goalMembers) {
        this.goalMembers = goalMembers;
    }
}
