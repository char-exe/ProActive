package sample;

import java.util.ArrayList;

/**
 * Class to represent a member of a Group.
 *
 * @author ??
 * @author Samuel Scarfe
 *
 * @version 1.1
 *
 * 1.0 - First working version.
 * 1.1 - Javadoc, equals, and hashCode.
 */
public class GroupMember {

    /**
     * The Group this GroupMember is a member of.
     */
    protected final Group group;
    /**
     * The User represented by this GroupMember.
     */
    protected final User user;
    /**
     * This GroupMember's goals.
     */
    protected final ArrayList<Goal> goals = new ArrayList<>();

    /**
     * Constructs a GroupMember from a Group and a User.
     *
     * @param group the Group that this GroupMember belongs to.
     * @param user the User represented by this GroupMember.
     */
    public GroupMember(Group group, User user) {
        this.group = group;
        this.user = user;
    }

    /**
     * Gets the Group associated with this GroupMember.
     *
     * @return the Group associated with this GroupMember.
     */
    public Group getGroup() {
        return group;
    }

    /**
     * Gets the User represented by this GroupMember.
     *
     * @return the User represented by this GroupMember.
     */
    public User getUser() {
        return user;
    }

    /**
     * Returns a boolean value representing whether this GroupMember is equal to a passed Object.
     *
     * @param o the Object to be tested for equality.
     * @return true if the User representing this GroupMember is equal to o. False otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof GroupMember)) {
            return false;
        }

        return this.user.equals(o);
    }

    /**
     * Returns a hashcode representation of this GroupMember.
     *
     * @return a hashcode representation of this GroupMember.
     */
    @Override
    public int hashCode() {
        return this.user.hashCode();
    }
}
