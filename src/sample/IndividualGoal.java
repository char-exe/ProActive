package sample;

import java.time.LocalDate;

public class IndividualGoal extends Goal {

    /**
     * Constructs a goal from a target amount, unit, and end date. Initialises progress to 0 and status to ongoing.
     * Intended for use at initial creation of a goal.
     *
     * @param target  the target amount of the goal.
     * @param unit    the units targeted by the goal.
     * @param endDate the end date of the goal.
     */
    public IndividualGoal(int target, Unit unit, LocalDate endDate) {
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
     * @param status   the completion status of the goal.
     */
    public IndividualGoal(int target, Unit unit, LocalDate endDate, int progress, Status status) {
        super(target, unit, endDate, progress, status);
    }
}
