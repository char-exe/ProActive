package sample;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Class for modelling a group user goal in the ProActive app by its target amount, concerned units, end date,
 * current progress, and completion status.
 *
 * @author Evan Clayton
 * @author Samuel Scarfe
 *
 * @version 1.1
 *
 * @see Goal
 *
 * 1.0 - First working version.
 * 1.1 - Refactored such that GroupGoal now inherits from IndividualGoal. Removed Group as an instance variable as an
 *       alternative to needing to refactor Group such that deep copies are made, and GroupGoal such that a clone is
 *       gotten rather than a reference to the Group. Instead a Group object can be pulled from the Database via
 *       groupId when needed.
 */
public class GroupGoal extends IndividualGoal {

    /**
     * ID for the Group that this goal is linked to.
     */
    private final int groupId;

    /**
     * Constructs a GroupGoal from a target amount, unit, end date, and groupId. Initialises progress to 0 and status to
     * ongoing. Intended for use at initial creation of a goal.
     *
     * @param target  the target amount of the goal.
     * @param unit    the units targeted by the goal.
     * @param endDate the end date of the goal.
     * @param groupId the id of the group associated with the goal.
     */
    public GroupGoal(float target, Unit unit, LocalDate endDate, int groupId) {
        super(target, unit, endDate);
        if (groupId < 1) {
            throw new IllegalArgumentException("groupID's start from 1");
        }

        this.groupId = groupId;
    }

    /**
     * Constructs a GroupGoal from a target amount, unit, end date, progress amount, and groupId. Intended for use
     * when loading user goals from the database.
     *
     * @param target   the target amount for this goal.
     * @param unit     the units targeted by this goal.
     * @param endDate  the end date of the goal.
     * @param progress the current progress of the goal.
     * @param groupId  the id of the group associated with the goal.
     */
    public GroupGoal(float target, Unit unit, LocalDate endDate, float progress, int groupId) {
        super(target, unit, endDate, progress);
        this.groupId = groupId;
    }

    /**
     * Gets the group_id for the goal (Note this is default 0 if no group is associated with this goal).
     *
     * @return the group_id for this goal.
     */
    public int getGroupId() {
        return groupId;
    }

    /**
     * Method to notify this GroupGoal's Group that this goal has been completed by a User.
     *
     * @param username the unique username of the User who has completed the goal.
     */
    public void notifyGroup(String username) {
        Group group = DatabaseHandler.getInstance().getGroupObjectFromGroupId(this.groupId);

        if (!(group == null)) {
            group.sendGroupNotifications(username, this);
        }
    }

    /**
     * Returns a boolean value representing whether this GroupGoal is equal to a passed Object.
     *
     * @param o the Object ot be tested for equality.
     * @return true if o is equal to this GroupGoal.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof GroupGoal)) {
            return false;
        }

        return this.target == ((GroupGoal) o).getTarget() && this.unit == ((GroupGoal) o).getUnit()
                && this.endDate.equals(((GroupGoal) o).getEndDate()) && this.groupId == ((GroupGoal) o).getGroupId();
    }

    /**
     * Returns a hashcode representation of this GroupGoal.
     *
     * @return a hashcode representation of this GroupGoal.
     */
    @Override
    public int hashCode() {
        return Objects.hash(target, unit, endDate, groupId);
    }
}

