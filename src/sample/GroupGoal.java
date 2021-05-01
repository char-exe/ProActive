package sample;

import java.time.LocalDate;
import java.util.Set;

public class GroupGoal extends Goal{

    private Group group;
    private Set<User> goalMembers;

    public GroupGoal(float target, Unit unit, LocalDate endDate, Group group) {
        super(target, unit, endDate);
        this.group = group;
        this.goalMembers = group.getGoalMembers();
    }

    public GroupGoal(float target, Unit unit, LocalDate endDate, int progress, Group group) {
        super(target, unit, endDate, progress);
        this.group = group;
        this.goalMembers = group.getGoalMembers();
    }

    public void sendNotifications (User user, Goal goal) {

    }

}
