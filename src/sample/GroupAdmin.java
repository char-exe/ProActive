package sample;

/**
 * Class to represent an administrator of a Group.
 *
 * @author ??
 * @author Samuel Scarfe
 *
 * @version 1.1
 *
 * 1.0 - First working version.
 * 1.1 - Javadoc, equals.
 */
public class GroupAdmin extends GroupMember {

    /**
     * Constructs a GroupAdmin from a Group and a User.
     * @param group the Group administrated by this GroupAdmin.
     * @param user the User represented by this GroupAdmin.
     */
    public GroupAdmin(Group group, User user) {
        super(group, user);
    }

    /**
     * Returns a String representation of this GroupAdmin.
     *
     * @return a String representation of this GroupAdmin.
     */
    @Override
    public String toString() {
        return this.getUser().getUsername();
    }

    /**
     * Returns a boolean value representing whether this GroupAdmin is equal to a passed Object.
     *
     * @param o the Object to be tested for equality.
     * @return true if the User representing this GroupAdmin is equal to o. False otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof GroupAdmin)) {
            return false;
        }

        return this.user.equals(o);
    }
}
