package sample;

/**
 * Class to represent an owner of Group.
 *
 * @author ??
 * @author Samuel Scarfe
 *
 * @version 1.1
 *
 * 1.0 - First working version.
 * 1.1 - Javadoc, equals.
 */
public class GroupOwner extends GroupAdmin {

    /**
     * Constructs a GroupOwner from a Group and a User.
     *
     * @param group the Group owned by this GroupOwner
     * @param user the User represented by this GroupOwner
     */
    public GroupOwner(Group group, User user) {
        super(group, user);
    }

    /**
     * Returns a String representation of this GroupOwner
     *
     * @return a String representation of this GroupOwner
     */
    @Override
    public String toString(){
        return this.user.toString();
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
        if (!(o instanceof GroupOwner)) {
            return false;
        }

        return this.user.equals(o);
    }
}
