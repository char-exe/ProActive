package sample;

public class GroupAdmin extends GroupMember{
    public GroupAdmin(Group group, User user) {
        super(group, user);
    }

    @Override
    public String toString(){
        return this.getUser().getUsername();
    }
}
