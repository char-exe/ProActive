package sample;

import java.util.HashSet;
import java.util.Set;

public class Group {

    private final String name;
    private final GroupOwner owner;
    private final Set<GroupAdmin> admins = new HashSet<>();
    private final Set<GroupMember> members = new HashSet<>();

    public Group(String name, GroupOwner owner){
        this.name = name;
        this.owner = owner;
    }

    public void addMember(GroupMember member){

    }

    public void removeMember(GroupMember member){

    }

    public void addAdmin(GroupAdmin admin){

    }

    public void removeAdmin(GroupAdmin admin){

    }

    public void sendInvite(String email){

    }

//    public String createInvite(){
//
//    }

    public void setGroupGoal(String type, int target){

    }

    public void deleteGroup(){

    }



    public String getName() {
        return name;
    }

    public Set<GroupMember> getMembers(){
        return members;
    }

    public Set<GroupAdmin> getAdmins() {
        return admins;
    }

    public GroupOwner getOwner() {
        return owner;
    }

}
