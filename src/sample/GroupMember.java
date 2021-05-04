package sample;

public class GroupMember {

    private final Group group;
    private final User user;

    public GroupMember(Group group, User user){
        this.group = group;
        this.user = user;
    }

    public Group getGroup() { return group; }

    public User getUser() { return user; }
}
