package sample;

public class GroupOwner extends GroupAdmin{
    User user;
    Group group;

    public GroupOwner(Group group, User user) {
        super(group, user);
        this.user = user;
        this.group = group;
    }
    @Override
    public String toString(){
        return this.user.toString();
    }
}
