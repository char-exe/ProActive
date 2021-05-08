package sample;

import java.time.LocalDate;


/**
 * A class to represent a goal created automatically by the application. System Goals function similarly to Individual
 * Goals, but with extra flags for their update period and accepted status. System Goals are intended to be stored in
 * and loaded from the database.
 *
 * @author Samuel Scarfe
 *
 * @version 1.0
 *
 * 1.0 - First working version.
 */
public class SystemGoal extends Goal {

    /**
     * The update period for this SystemGoal.
     */
    private final UpdatePeriod updatePeriod;

    /**
     * The category for this SystemGoal.
     */
    private final Category category;

    /**
     * The accepted status for this SystemGoal
     */
    private boolean accepted;

    /**
     * Enum class to represent update periods for goals.
     */
    public enum UpdatePeriod {
        DAILY, WEEKLY
    }

    /**
     * Enum class to represent categories for goals.
     */
    public enum Category {
        DAY_TO_DAY, STAY, PUSH
    }

    /**
     * Constructs a SystemGoal from a target amount, unit, end date, update period, and category. Initialises accepted
     * status to false. Intended for use when created via automatic generation.
     *
     * @param target       the target amount of the SystemGoal.
     * @param unit         the units targeted by the SystemGoal.
     * @param endDate      the end date of the SystemGoal.
     * @param updatePeriod the update period for this SystemGoal.
     * @param category     the category for this SystemGoal.
     */
    public SystemGoal(float target, Unit unit, LocalDate endDate, UpdatePeriod updatePeriod, Category category) {
        super(target, unit, endDate);

        if (updatePeriod == null) {
            throw new NullPointerException();
        }
        if (category == null) {
            throw new NullPointerException();
        }

        this.updatePeriod = updatePeriod;
        this.category = category;
        this.accepted = false;
    }

    /**
     * Constructs a SystemGoal from a target amount, unit, end date, update period, category, and accepted status.
     * Intended for use when created loading system goals from the database.
     *
     * @param target       the target amount of the SystemGoal.
     * @param unit         the units targeted by the SystemGoal.
     * @param endDate      the end date of the SystemGoal.
     * @param updatePeriod the update period for this SystemGoal.
     * @param category     the category for this SystemGoal.
     * @param accepted     the accepted status for this SystemGoal.
     */
    public SystemGoal(
            float target, Unit unit, LocalDate endDate, UpdatePeriod updatePeriod, Category category, boolean accepted
    ) {
        this(target, unit, endDate, updatePeriod, category);

        this.accepted = accepted;
    }

    /**
     * Gets the update period for this SystemGoal.
     *
     * @return the update period for this SystemGoal.
     */
    public UpdatePeriod getUpdatePeriod() {
        return this.updatePeriod;
    }

    /**
     * Gets the category for this SystemGoal.
     *
     * @return the category for this SystemGoal.
     */
    public Category getCategory() {
        return this.category;
    }

    /**
     * Gets the accepted status for this SystemGoal.
     *
     * @return the accepted status for this SystemGoal.
     */
    public boolean isAccepted() {
        return this.accepted;
    }

    /**
     * Sets the accepted status for this SystemGoal.
     *
     * @param accepted the new accepted status for this SystemGoal.
     */
    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
