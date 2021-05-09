package sample;

import java.time.LocalDate;

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
     * Boolean status for whether this goal has been accepted by the User that it has been presented to.
     */
    private boolean accepted;

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
        this.accepted = false;
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
        this.accepted = true;
    }

    /**
     * Constructs a goal from a target amount, unit, end date, progress amount, and current status. Intended for use
     * when pulling group goals from the database.
     *
     * @param target   the target amount of the goal.
     * @param unit     the units targeted by the goal.
     * @param endDate  the end date of the goal.
     * @param progress the current progress of the goal.
     * @param groupId  the id of the group associated with the goal.
     */
    public GroupGoal(float target, Unit unit, LocalDate endDate, float progress, int groupId, boolean accepted) {
        this(target, unit, endDate, progress, groupId);

        this.accepted = accepted;
    }

    /**
     * Gets the accepted status for this goal.
     *
     * @return the accepted status for this goal.
     */
    public boolean isAccepted() {
        return accepted;
    }

    /**
     * Sets the accepted status for this goal.
     *
     * @param accepted the new accepted status for this goal.
     */
    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    /**
     * Gets the group_id for the goal (Note this is default 0 if no group is associated with this goal).
     *
     * @return the group_id for this goal.
     */
    public int getGroupId() {
        return groupId;
    }
}

