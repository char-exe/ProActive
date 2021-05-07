package sample;

import java.time.LocalDate;

public abstract class UserGoal extends Goal {

    /**
     * The active status of the goal.
     */
    protected boolean active;
    /**
     * The completion status of the goal
     */
    protected boolean completed;
    /**
     * The current progress of the goal.
     */
    protected float progress;

    /**
     * Constructs a goal from a target amount, unit, and end date. Initialises progress to 0 and status to ongoing.
     * Intended for use at initial creation of a goal.
     *
     * @param target  the target amount of the goal.
     * @param unit    the units targeted by the goal.
     * @param endDate the end date of the goal.
     */
    public UserGoal(float target, Unit unit, LocalDate endDate) {
        super(target, unit, endDate);
        this.completed = progress >= target;
        this.active    = endDate.isAfter(LocalDate.now()) && !this.completed;
        this.progress  = 0;
    }

    public UserGoal(float target, Unit unit, LocalDate endDate, float progress) {
        super(target, unit, endDate);
        this.progress = progress;
        this.completed = progress >= target;
        this.active    = endDate.isAfter(LocalDate.now()) && !this.completed;
    }

    public UserGoal() {
        super();
        this.progress = -1;
    }


    /**
     * Gets the progress amount for this goal.
     *
     * @return the progress amount for this goal.
     */
    public float getProgress() {
        return this.progress;
    }

    /**
     * Gets the active status of this goal.
     *
     * @return the active status of this goal.
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * Gets the completed status of this goal.
     *
     * @return the completed status of this goal.
     */
    public boolean isCompleted() {
        return this.completed;
    }

    /**
     * Increments the current progress by the passed amount, provided the goal is marked as ongoing. Updates the goal
     * status to completed if the target has been met.
     *
     * @param update the amount to increment progress by
     */
    public boolean updateProgress(Unit unit, float update) {
        if (unit == null) {
            throw new NullPointerException();
        }
        if (update < 1) {
            throw new IllegalArgumentException();
        }

        if (this.active && !this.completed && this.unit == unit) {

            this.progress = this.progress + update;

            if (this.progress >= this.target) {
                this.completed = true;
                this.active = false;
            }
            return true;
        }
        return false;
    }

    /**
     * Method to mark a goal as inactive and update its end date.
     */
    public void quitGoal() {
        if (this.completed) {
            throw new IllegalStateException("Goal is already complete");
        }

        this.endDate = LocalDate.now();
        this.active = false;
    }
}
