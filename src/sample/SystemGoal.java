package sample;

import java.time.LocalDate;


/**
 * A class to represent a goal created automatically by the application. System Goals function similarly to Individual
 * Goals, but with extra flags for their update period and accepted status. System Goals are intended to be stored in
 * and loaded from the database.
 */
public class SystemGoal extends Goal {

    private final UpdatePeriod updatePeriod;
    private final Category category;
    private boolean accepted;

    public enum UpdatePeriod {
        DAILY, WEEKLY
    }

    public enum Category {
        DAY_TO_DAY, STAY, PUSH
    }

    /**
     * Constructs a goal from a target amount, unit, and end date. Initialises progress to 0 and status to ongoing.
     * Intended for use at initial creation of a goal.
     *
     * @param target  the target amount of the goal.
     * @param unit    the units targeted by the goal.
     * @param endDate the end date of the goal.
     */
    public SystemGoal(float target, Unit unit, LocalDate endDate, UpdatePeriod updatePeriod, Category category) {
        super(target, unit, endDate);

        this.updatePeriod = updatePeriod;
        this.category = category;
        this.accepted = false;
    }

    /**
     * Constructs a goal from a target amount, unit, and end date. Initialises progress to 0 and status to ongoing.
     * Intended for use at initial creation of a goal.
     *
     * @param target  the target amount of the goal.
     * @param unit    the units targeted by the goal.
     * @param endDate the end date of the goal.
     */
    public SystemGoal(float target, Unit unit, LocalDate endDate, UpdatePeriod updatePeriod, Category category, boolean accepted) {
        super(target, unit, endDate);

        this.updatePeriod = updatePeriod;
        this.category = category;
        this.accepted = accepted;
    }

    public UpdatePeriod getUpdatePeriod() {
        return this.updatePeriod;
    }

    public Category getCategory() {
        return this.category;
    }

    public boolean isAccepted() {
        return this.accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
