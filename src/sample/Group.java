package sample;

import javax.mail.Session;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Class to represent a Group in the ProActive app by it's name, owner, admins, members, and GroupGoals.
 *
 * @author ??
 * @author Samuel Scarfe
 *
 * @version 1.1
 *
 * 1.0 - First working version.
 * 1.1 - Javadoc and refactoring. Equals and hashcode.
 */
public class Group {

    /**
     * The unique name of this Group.
     */
    private final String name;
    /**
     * The owner of this Group.
     */
    private GroupOwner owner;
    /**
     * This Group's admins.
     */
    private Set<GroupAdmin> admins = new HashSet<>();
    /**
     * This Group's members.
     */
    private Set<GroupMember> members = new HashSet<>();
    /**
     * This Group's GroupGoals.
     */
    private final ArrayList<GroupGoal> groupGoals;

    /**
     * Constructs a Group from a name and an owner. Initialises GroupGoals from the database.
     * @param name  the name of this Group.
     * @param owner the owner of this Group.
     */
    public Group(String name, GroupOwner owner) {
        if (name == null) {
            throw new NullPointerException();
        }
        if (owner == null) {
            throw new NullPointerException();
        }

        this.name = name;
        this.owner = owner;
        this.groupGoals = DatabaseHandler.getInstance().loadGroupGoals(this.name);
    }

    /**
     * Constructs a Group from a name. Initialises owner to null. Initialises GroupGoals from the database.
     * @param name the name of this group.
     */
    public Group(String name) {
        if (name == null) {
            throw new NullPointerException();
        }

        this.name = name;
        this.owner = null;
        this.groupGoals = DatabaseHandler.getInstance().loadGroupGoals(this.name);
    }

    /**
     * Gets the name of this Group.
     * @return the name of this Group.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets a set containing this Group's members.
     *
     * @return a set containing this Group's members.
     */
    public Set<GroupMember> getMembers() {
        return Set.copyOf(members);
    }

    /**
     * Gets a set containing this Group's admins.
     *
     * @return a set containing this Group's admins.
     */
    public Set<GroupAdmin> getAdmins() {
        return Set.copyOf(admins);
    }

    /**
     * Gets this Group's GroupOwner.
     *
     * @return this Group's GroupOwner.
     */
    public GroupOwner getOwner() {
        return owner;
    }

    /**
     * Gets this Groups GroupGoals in an ArrayList.
     *
     * @return an ArrayList containing this Group's GroupGoals.
     */
    public ArrayList<GroupGoal> getGroupGoals() {
        return groupGoals;
    }

    /**
     * Sets this Group's GroupMembers to the passed Set.
     *
     * @param members this Group's new set of members.
     */
    public void setMembers(Set<GroupMember> members) {
        if (members == null) {
            throw new NullPointerException();
        }

        this.members = members;
    }

    /**
     * Sets this Group's GroupAdmins to the passed Set.
     *
     * @param admins this Group's new set of admins.
     */
    public void setAdmins(Set<GroupAdmin> admins) {
        if (admins == null) {
            throw new NullPointerException();
        }

        this.admins = admins;
    }

    /**
     * Sets this Group's GroupOwner to the passed GroupOwner.
     *
     * @param owner this Group's new GroupOwner.
     */
    public void setOwner(GroupOwner owner) {
        if (owner == null) {
            throw new NullPointerException();
        }

        this.owner = owner;
    }

    /**
     * Method to send goal notifications after the completion of a group goal by a group member. The group members
     * that are not the user who completed the goal all informed of the goal's completion by an email.
     * @param username the username of the user that completed the group goal.
     * @param goal the goal that was completed.
     */
    public void sendGroupNotifications (String username, Goal goal) {
        if (username == null) {
            throw new NullPointerException();
        }
        if (goal == null) {
            throw new NullPointerException();
        }

        for (GroupMember member : this.members) {
            notifyMember(username, goal, member);
        }
        for (GroupAdmin admin : this.admins) {
            notifyMember(username, goal, admin);
        }

        notifyMember(username, goal, owner);
    }

    /**
     * Private helper method to notify Group members about completion of a goal.
     *
     * @param username the username of the user that completed the GroupGoal.
     * @param goal the goal that was completed.
     * @param member the member to be notified.
     */
    private void notifyMember(String username, Goal goal, GroupMember member) {
        if (username == null) {
            throw new NullPointerException();
        }
        if (goal == null) {
            throw new NullPointerException();
        }
        if (member == null) {
            throw new NullPointerException();
        }

        if (!member.getUser().getUsername().equals(username)) {
            EmailHandler emailHandler = EmailHandler.getInstance();
            Session session = emailHandler.createSession();
            emailHandler.sendGroupGoalCompletion(session, member.getUser().getEmail(), goal, username);
        }
    }

    /**
     * Returns a String representation of this Group.
     *
     * @return a String representation of this Group.
     */
    @Override
    public String toString() {
        return this.name + "{" + this.owner + ", " + this.admins + ", " + this.members + "}";
    }

    /**
     * Returns a boolean value representing whether this Group is equal to a passed Object o.
     *
     * @param o the Object to be tested for equality.
     * @return true if the passed Object is a Group with the same name as this Group. false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof Group)) {
            return false;
        }

        return this.name.equals(((Group) o).getName());
    }

    /**
     * Returns a hashcode representation of this Group.
     *
     * @return a hashcode representation of this Group.
     */
    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
