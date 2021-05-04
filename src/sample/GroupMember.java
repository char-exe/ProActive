package sample;

import java.util.ArrayList;

public class GroupMember {

    private final Group group;
    private final User user;
    private final ArrayList<Goal> goals = new ArrayList<>();

    public GroupMember(Group group, User user){
        this.group = group;
        this.user = user;
    }

    public User getUser(){
        return user;
    }

}
