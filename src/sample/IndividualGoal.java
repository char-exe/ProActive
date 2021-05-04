package sample;

import java.time.LocalDate;

/**
 * Class for modelling an individual user goal in the ProActive app by its target amount, concerned units, end date,
 * current progress, and completion status.
 *
 * @author Samuel Scarfe
 * @author Evan Clayton
 *
 * @version 1.2
 *
 * @see Goal
 *
 * 1.0 - First working version.
 * 1.1 - Implemented automatic goal generation
 * 1.2 - Now supports conversion from group goal to individual goal and added constructors with a group_id field.
 */
public class IndividualGoal extends Goal {

    /**
     * Constructs a goal from a target amount, unit, and end date. Initialises progress to 0 and status to ongoing.
     * Intended for use at initial creation of a goal.
     *
     * @param target  the target amount of the goal.
     * @param unit    the units targeted by the goal.
     * @param endDate the end date of the goal.
     */
    public IndividualGoal(float target, Unit unit, LocalDate endDate) {
        super(target, unit, endDate);
    }

    /**
     * Constructs a goal from a target amount, unit, end date, progress amount, and current status. Intended for use
     * when pulling user goals from the database.
     *
     * @param target   the target amount of the goal.
     * @param unit     the units targeted by the goal.
     * @param endDate  the end date of the goal.
     * @param progress the current progress of the goal.
     */
    public IndividualGoal(float target, Unit unit, LocalDate endDate, float progress) {
        super(target, unit, endDate, progress);
    }

    /**
     * Constructs a goal from a target amount, unit, end date, progress amount, and current status. Intended for use
     * when pulling user instances of group goals from the database.
     *
     * @param target   the target amount of the goal.
     * @param unit     the units targeted by the goal.
     * @param endDate  the end date of the goal.
     * @param progress the current progress of the goal.
     * @param group_id the id of the group associated with the group goal this goal is derived from.
     */
    public IndividualGoal(float target, Unit unit, LocalDate endDate, float progress, int group_id) {
        super(target, unit, endDate, progress, group_id);
    }

    /**
     * Constructs a goal from a target amount, unit, and end date. Initialises progress to 0 and status to ongoing.
     * Intended for use at initial creation of a goal derived from a group goal.
     *
     * @param target  the target amount of the goal.
     * @param unit    the units targeted by the goal.
     * @param endDate the end date of the goal.
     * @param group_id the id of the group associated with the group goal this goal is derived from.
     */
    public IndividualGoal(float target, Unit unit, LocalDate endDate, int group_id) {
        super(target, unit, endDate, group_id);
    }


    /**
     * Constructs an IndividualGoal from a SystemGoal.
     *
     * @param systemGoal a goal generated automatically by the system and accepted by the user.
     */
    public IndividualGoal(SystemGoal systemGoal) {
        super(systemGoal.getTarget(), systemGoal.getUnit(), systemGoal.getEndDate());
    }

    /**
     * Constructs an IndividualGoal from a GroupGoal.
     * @param groupGoal
     */
    public IndividualGoal(GroupGoal groupGoal) {
        super(groupGoal.getTarget(), groupGoal.getUnit(), groupGoal.getEndDate());
        this.group_id = groupGoal.getGroup_id();

    }

}
