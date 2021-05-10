package sample;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit testing for GroupGoal.
 *
 * @author Samuel Scarfe
 */
class GroupGoalTest {

    @Test
    void zeroTarget() {
        assertThrows(IllegalArgumentException.class, () -> new GroupGoal(
                0, Goal.Unit.PROTEIN, LocalDate.now(), 1)
        );
    }

    @Test
    void negativeTarget() {
        assertThrows(IllegalArgumentException.class, () -> new GroupGoal(
                -1, Goal.Unit.PROTEIN, LocalDate.now(), 1)
        );
    }

    @Test
    void nullUnit() {
        assertThrows(NullPointerException.class, () -> new GroupGoal(
                1, null, LocalDate.now(), 1)
        );
    }

    @Test
    void nullEndDate() {
        assertThrows(NullPointerException.class, () -> new GroupGoal(
                1, Goal.Unit.PROTEIN, null, 1)
        );
    }

    @Test
    void illegalGroupId() {
        assertThrows(IllegalArgumentException.class, () -> new GroupGoal(
                1, Goal.Unit.PROTEIN, LocalDate.now(), 0)
        );
    }

    @Test
    void negativeProgress() {
        assertThrows(IllegalArgumentException.class, () -> new GroupGoal(
                1, Goal.Unit.PROTEIN, LocalDate.now(), -1, 1)
        );
    }

    @Test
    void successfulConstruction() {
        assertDoesNotThrow(() -> new GroupGoal(1, Goal.Unit.PROTEIN, LocalDate.now(), 1, 1));
    }

    @Test
    void getGroupId() {
        GroupGoal groupGoal = new GroupGoal(1, Goal.Unit.PROTEIN, LocalDate.now(), 1, 1);

        assertEquals(1, groupGoal.getGroupId());
    }

    @Test
    void getTarget() {
        GroupGoal groupGoal = new GroupGoal(1, Goal.Unit.PROTEIN, LocalDate.now(), 1, 1);

        assertEquals(1, groupGoal.getTarget());
    }

    @Test
    void getUnit() {
        GroupGoal groupGoal = new GroupGoal(1, Goal.Unit.PROTEIN, LocalDate.now(), 1, 1);

        assertEquals(Goal.Unit.PROTEIN, groupGoal.getUnit());
    }

    @Test
    void getEndDate() {
        GroupGoal groupGoal = new GroupGoal(1, Goal.Unit.PROTEIN, LocalDate.now(), 1, 1);

        assertEquals(LocalDate.now(), groupGoal.getEndDate());
    }

    @Test
    void getProgress() {
        GroupGoal groupGoal = new GroupGoal(1, Goal.Unit.PROTEIN, LocalDate.now(), 1, 1);

        assertEquals(1, groupGoal.getProgress());
    }

    @Test
    void incompleteUnder() {
        GroupGoal groupGoal = new GroupGoal(1, Goal.Unit.PROTEIN, LocalDate.now(), 0, 1);

        assertFalse(groupGoal.isCompleted());
    }

    @Test
    void completeOn() {
        GroupGoal groupGoal = new GroupGoal(1, Goal.Unit.PROTEIN, LocalDate.now(), 1, 1);

        assertTrue(groupGoal.isCompleted());
    }

    @Test
    void completeOver() {
        GroupGoal groupGoal = new GroupGoal(1, Goal.Unit.PROTEIN, LocalDate.now(), 2, 1);

        assertTrue(groupGoal.isCompleted());
    }

    @Test
    void activeTomorrowIncomplete() {
        GroupGoal groupGoal = new GroupGoal(1, Goal.Unit.PROTEIN, LocalDate.now().plusDays(1), 0, 1);

        assertTrue(groupGoal.isActive());
    }

    @Test
    void inactiveTomorrowComplete() {
        GroupGoal groupGoal = new GroupGoal(1, Goal.Unit.PROTEIN, LocalDate.now().plusDays(1), 1, 1);

        assertFalse(groupGoal.isActive());
    }

    @Test
    void inactiveTodayIncomplete() {
        GroupGoal groupGoal = new GroupGoal(1, Goal.Unit.PROTEIN, LocalDate.now(), 0, 1);

        assertFalse(groupGoal.isActive());
    }

    @Test
    void inactiveTodayComplete() {
        GroupGoal groupGoal = new GroupGoal(1, Goal.Unit.PROTEIN, LocalDate.now(), 1, 1);

        assertFalse(groupGoal.isActive());
    }

    @Test
    void inactiveYesterdayIncomplete() {
        GroupGoal groupGoal = new GroupGoal(1, Goal.Unit.PROTEIN, LocalDate.now().minusDays(1), 0, 1);

        assertFalse(groupGoal.isActive());
    }

    @Test
    void inactiveYesterdayComplete() {
        GroupGoal groupGoal = new GroupGoal(1, Goal.Unit.PROTEIN, LocalDate.now().minusDays(1), 1, 1);

        assertFalse(groupGoal.isActive());
    }

    @Test
    void quitCompletedGoal() {
        GroupGoal groupGoal = new GroupGoal(1, Goal.Unit.PROTEIN, LocalDate.now(), 1, 1);

        assertThrows(IllegalStateException.class, groupGoal::quitGoal);
    }

    @Test
    void successfulQuitGoal() {
        GroupGoal groupGoal = new GroupGoal(1, Goal.Unit.PROTEIN, LocalDate.now().plusDays(1), 0, 1);

        groupGoal.quitGoal();

        assertEquals(LocalDate.now(), groupGoal.getEndDate());
        assertFalse(groupGoal.isActive());
    }

    @Test
    void nullUnitUpdate() {
        GroupGoal groupGoal = new GroupGoal(1, Goal.Unit.PROTEIN, LocalDate.now().plusDays(1), 0, 1);

        assertThrows(NullPointerException.class, () -> groupGoal.updateProgress(null, 1));
    }

    @Test
    void zeroValueUpdate() {
        GroupGoal groupGoal = new GroupGoal(1, Goal.Unit.PROTEIN, LocalDate.now().plusDays(1), 0, 1);

        assertThrows(IllegalArgumentException.class, () -> groupGoal.updateProgress(Goal.Unit.PROTEIN, 0));
    }

    @Test
    void negativeValueUpdate() {
        GroupGoal groupGoal = new GroupGoal(1, Goal.Unit.PROTEIN, LocalDate.now().plusDays(1), 0, 1);

        assertThrows(IllegalArgumentException.class, () -> groupGoal.updateProgress(Goal.Unit.PROTEIN, -1));
    }

    @Test
    void NotActiveCompletedIncorrectUnitUpdate() {
        GroupGoal groupGoal = new GroupGoal(1, Goal.Unit.PROTEIN, LocalDate.now(), 1, 1);

        assertFalse(() -> groupGoal.updateProgress(Goal.Unit.CARBS, 1));
    }

    @Test
    void NotActiveNotCompletedIncorrectUnitUpdate() {
        GroupGoal groupGoal = new GroupGoal(1, Goal.Unit.PROTEIN, LocalDate.now(), 0, 1);

        assertFalse(() -> groupGoal.updateProgress(Goal.Unit.CARBS, 1));
    }

    @Test
    void NotActiveCompletedCorrectUnitUpdate() {
        IndividualGoal individualGoal = new IndividualGoal(1, Goal.Unit.PROTEIN, LocalDate.now(), 1);

        assertFalse(() -> individualGoal.updateProgress(Goal.Unit.PROTEIN, 1));
    }

    @Test
    void ActiveNotCompletedIncorrectUnitUpdate() {
        GroupGoal groupGoal = new GroupGoal(1, Goal.Unit.PROTEIN, LocalDate.now().plusDays(1), 0, 1);

        assertFalse(() -> groupGoal.updateProgress(Goal.Unit.CARBS, 1));
    }

    @Test
    void NotActiveNotCompletedCorrectUnitUpdate() {
        GroupGoal groupGoal = new GroupGoal(1, Goal.Unit.PROTEIN, LocalDate.now(), 0, 1);

        assertFalse(() -> groupGoal.updateProgress(Goal.Unit.PROTEIN, 1));
    }

    @Test
    void successfulUpdate() {
        GroupGoal groupGoal = new GroupGoal(1, Goal.Unit.PROTEIN, LocalDate.now().plusDays(1), 0, 1);

        assertTrue(() -> groupGoal.updateProgress(Goal.Unit.PROTEIN, 1));
    }

    @Test
    void updateMarksCompletedAndActive() {
        GroupGoal groupGoal = new GroupGoal(1, Goal.Unit.PROTEIN, LocalDate.now().plusDays(1), 0, 1);

        groupGoal.updateProgress(Goal.Unit.PROTEIN, 1);

        assertTrue(groupGoal.isCompleted());
        assertFalse(groupGoal.isActive());
    }

    @Test
    void UpdateDoesNotMarkCompletedAndActive() {
        GroupGoal groupGoal = new GroupGoal(2, Goal.Unit.PROTEIN, LocalDate.now().plusDays(1), 0, 1);

        groupGoal.updateProgress(Goal.Unit.PROTEIN, 1);

        assertFalse(groupGoal.isCompleted());
        assertTrue(groupGoal.isActive());
    }
}