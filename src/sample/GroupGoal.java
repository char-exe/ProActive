package sample;

import java.time.LocalDate;

/**
 * Class for modelling a group user goal in the ProActive app by its target amount, concerned units, end date,
 * current progress, and completion status.
 *
 * @author Evan Clayton
 *
 * @version
 *
 * @see Goal
 *
 *
 */
public class GroupGoal extends Goal{

    /**
     * Constructs a goal from a target amount, unit, and end date. Initialises progress to 0 and status to ongoing.
     * Intended for use at initial creation of a goal.
     *
     * @param target  the target amount of the goal.
     * @param unit    the units targeted by the goal.
     * @param endDate the end date of the goal.
     */
    public GroupGoal(float target, Unit unit, LocalDate endDate) { super(target, unit, endDate); }

    /**
     * Constructs a goal from a target amount, unit, end date, progress amount, and current status. Intended for use
     * when pulling user goals from the database.
     *
     * @param target   the target amount of the goal.
     * @param unit     the units targeted by the goal.
     * @param endDate  the end date of the goal.
     * @param progress the current progress of the goal.
     */
    public GroupGoal(float target, Unit unit, LocalDate endDate, int progress) { super(target, unit, endDate, progress); }
}
