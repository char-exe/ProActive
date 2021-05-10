package sample;

/**
 * Class to represent an owner of Group.
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
}
