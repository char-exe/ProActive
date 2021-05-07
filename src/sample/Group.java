package sample;

import java.util.HashSet;
import java.util.Set;

public class Group {

    private final String name;
    private GroupOwner owner;
    private Set<GroupAdmin> admins = new HashSet<>();
    private Set<GroupMember> members = new HashSet<>();

    public Group(String name, GroupOwner owner){
        this.name = name;
        this.owner = owner;
    }

    public Group(String name){
        this.name = name;
        this.owner = null;
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

    /**
     * Method to send goal notifications after the completion of a group goal by a group member. The group members
     * that are not the user who completed the goal all informed of the goal's completion by an email.
     * @param user the user that completed the group goal.
     * @param goal the goal that was completed.
     */
    public void sendGroupNotifications (User user, Goal goal) {
        for (GroupMember member : this.getMembers()) {
            if (member.getUser().getUsername() != user.getUsername()) {
                NotificationHandler.getInstance().sendGroupEmail(member.getUser().getEmail(), goal, user);
            }
        }
    }


    public String getName() {
        return name;
    }

    public void setMembers(Set<GroupMember> members){
        this.members = members;
    }
    public void setAdmins(Set<GroupAdmin> admins){
        this.admins = admins;
    }
    public void setOwner(GroupOwner owner){
        this.owner = owner;
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

    @Override
    public String toString(){
        return this.name + "{" + this.owner + ", " + this.admins + ", " + this.members + "}";
    }
}
