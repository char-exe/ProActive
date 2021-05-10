package sample;

/**
 * Class to represent an administrator of a Group.
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
}
