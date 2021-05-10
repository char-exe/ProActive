package sample;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit testing for IndividualGoal
 *
 * @author Samuel Scarfe
 */

class IndividualGoalTest {

    @Test
    void zeroTarget() {
        assertThrows(IllegalArgumentException.class, () -> new IndividualGoal(0, Goal.Unit.PROTEIN, LocalDate.now()));
    }

    @Test
    void negativeTarget() {
        assertThrows(IllegalArgumentException.class, () -> new IndividualGoal(-1, Goal.Unit.PROTEIN, LocalDate.now()));
    }

    @Test
    void nullUnit() {
        assertThrows(NullPointerException.class, () -> new IndividualGoal(1, null, LocalDate.now()));
    }

    @Test
    void nullEndDate() {
        assertThrows(NullPointerException.class, () -> new IndividualGoal(1, Goal.Unit.PROTEIN, null));
    }

    @Test
    void successfulShortSuper() {
        assertDoesNotThrow(() -> new IndividualGoal(1, Goal.Unit.PROTEIN, LocalDate.now()));
    }

    @Test
    void negativeProgress() {
        assertThrows(IllegalArgumentException.class, () -> new IndividualGoal(1, Goal.Unit.PROTEIN, LocalDate.now(), -1));
    }

    @Test
    void successfulLongSuper() {
        assertDoesNotThrow(() -> new IndividualGoal(1, Goal.Unit.PROTEIN, LocalDate.now(), 1));
    }


    @Test
    void successfulSystemGoalConstructor() {
        SystemGoal systemGoal = new SystemGoal(
                100,
                Goal.Unit.PROTEIN,
                LocalDate.now().plusDays(1),
                SystemGoal.UpdatePeriod.DAILY,
                SystemGoal.Category.DAY_TO_DAY
        );

        assertDoesNotThrow(() -> new IndividualGoal(systemGoal));
    }

    @Test
    void getTarget() {
        IndividualGoal individualGoal = new IndividualGoal(1, Goal.Unit.PROTEIN, LocalDate.now(), 1);

        assertEquals(1, individualGoal.getTarget());
    }

    @Test
    void getUnit() {
        IndividualGoal individualGoal = new IndividualGoal(1, Goal.Unit.PROTEIN, LocalDate.now(), 1);

        assertEquals(Goal.Unit.PROTEIN, individualGoal.getUnit());
    }

    @Test
    void getEndDate() {
        IndividualGoal individualGoal = new IndividualGoal(1, Goal.Unit.PROTEIN, LocalDate.now(), 1);

        assertEquals(LocalDate.now(), individualGoal.getEndDate());
    }

    @Test
    void getProgress() {
        IndividualGoal individualGoal = new IndividualGoal(1, Goal.Unit.PROTEIN, LocalDate.now(), 1);

        assertEquals(1, individualGoal.getProgress());
    }

    @Test
    void incompleteUnder() {
        IndividualGoal individualGoal = new IndividualGoal(1, Goal.Unit.PROTEIN, LocalDate.now().plusDays(1), 0);

        assertFalse(individualGoal.isCompleted());
    }

    @Test
    void completeOn() {
        IndividualGoal individualGoal = new IndividualGoal(1, Goal.Unit.PROTEIN, LocalDate.now().plusDays(1), 1);

        assertTrue(individualGoal.isCompleted());
    }

    @Test
    void completeOver() {
        IndividualGoal individualGoal = new IndividualGoal(1, Goal.Unit.PROTEIN, LocalDate.now().plusDays(1), 2);

        assertTrue(individualGoal.isCompleted());
    }

    @Test
    void activeTomorrowIncomplete() {
        IndividualGoal individualGoal = new IndividualGoal(1, Goal.Unit.PROTEIN, LocalDate.now().plusDays(1));

        assertTrue(individualGoal.isActive());
    }

    @Test
    void inactiveTomorrowComplete() {
        IndividualGoal individualGoal = new IndividualGoal(1, Goal.Unit.PROTEIN, LocalDate.now().plusDays(1), 1);

        assertFalse(individualGoal.isActive());
    }

    @Test
    void inactiveTodayIncomplete() {
        IndividualGoal individualGoal = new IndividualGoal(1, Goal.Unit.PROTEIN, LocalDate.now());

        assertFalse(individualGoal.isActive());
    }

    @Test
    void inactiveTodayComplete() {
        IndividualGoal individualGoal = new IndividualGoal(1, Goal.Unit.PROTEIN, LocalDate.now(), 1);

        assertFalse(individualGoal.isActive());
    }

    @Test
    void inactiveYesterdayIncomplete() {
        IndividualGoal individualGoal = new IndividualGoal(1, Goal.Unit.PROTEIN, LocalDate.now().minusDays(1));

        assertFalse(individualGoal.isActive());
    }

    @Test
    void inactiveYesterdayComplete() {
        IndividualGoal individualGoal = new IndividualGoal(1, Goal.Unit.PROTEIN, LocalDate.now().minusDays(1), 1);

        assertFalse(individualGoal.isActive());
    }

    @Test
    void quitCompletedGoal() {
        IndividualGoal individualGoal = new IndividualGoal(1, Goal.Unit.PROTEIN, LocalDate.now().plusDays(1), 1);

        assertThrows(IllegalStateException.class, individualGoal::quitGoal);
    }

    @Test
    void successfulQuitGoal() {
        IndividualGoal individualGoal = new IndividualGoal(1, Goal.Unit.PROTEIN, LocalDate.now().plusDays(1), 0);

        individualGoal.quitGoal();

        assertEquals(LocalDate.now(), individualGoal.getEndDate());
        assertFalse(individualGoal.isActive());
    }

    @Test
    void nullUnitUpdate() {
        IndividualGoal individualGoal = new IndividualGoal(1, Goal.Unit.PROTEIN, LocalDate.now().plusDays(1), 0);

        assertThrows(NullPointerException.class, () -> individualGoal.updateProgress(null, 1));
    }

    @Test
    void zeroValueUpdate() {
        IndividualGoal individualGoal = new IndividualGoal(1, Goal.Unit.PROTEIN, LocalDate.now().plusDays(1), 0);

        assertThrows(IllegalArgumentException.class, () -> individualGoal.updateProgress(Goal.Unit.PROTEIN, 0));
    }

    @Test
    void negativeValueUpdate() {
        IndividualGoal individualGoal = new IndividualGoal(1, Goal.Unit.PROTEIN, LocalDate.now().plusDays(1), 0);

        assertThrows(IllegalArgumentException.class, () -> individualGoal.updateProgress(Goal.Unit.PROTEIN, -1));
    }

    @Test
    void NotActiveCompletedIncorrectUnitUpdate() {
        IndividualGoal individualGoal = new IndividualGoal(1, Goal.Unit.PROTEIN, LocalDate.now(), 1);

        assertFalse(() -> individualGoal.updateProgress(Goal.Unit.CARBS, 1));
    }

    @Test
    void NotActiveNotCompletedIncorrectUnitUpdate() {
        IndividualGoal individualGoal = new IndividualGoal(1, Goal.Unit.PROTEIN, LocalDate.now(), 0);

        assertFalse(() -> individualGoal.updateProgress(Goal.Unit.CARBS, 1));
    }

    @Test
    void NotActiveCompletedCorrectUnitUpdate() {
        IndividualGoal individualGoal = new IndividualGoal(1, Goal.Unit.PROTEIN, LocalDate.now(), 1);

        assertFalse(() -> individualGoal.updateProgress(Goal.Unit.PROTEIN, 1));
    }

    @Test
    void ActiveNotCompletedIncorrectUnitUpdate() {
        IndividualGoal individualGoal = new IndividualGoal(1, Goal.Unit.PROTEIN, LocalDate.now().plusDays(1), 0);

        assertFalse(() -> individualGoal.updateProgress(Goal.Unit.CARBS, 1));
    }

    @Test
    void NotActiveNotCompletedCorrectUnitUpdate() {
        IndividualGoal individualGoal = new IndividualGoal(1, Goal.Unit.PROTEIN, LocalDate.now(), 0);

        assertFalse(() -> individualGoal.updateProgress(Goal.Unit.PROTEIN, 1));
    }

    @Test
    void successfulUpdate() {
        IndividualGoal individualGoal = new IndividualGoal(1, Goal.Unit.PROTEIN, LocalDate.now().plusDays(1), 0);

        assertTrue(() -> individualGoal.updateProgress(Goal.Unit.PROTEIN, 1));
    }

    @Test
    void updateMarksCompletedAndActive() {
        IndividualGoal individualGoal = new IndividualGoal(1, Goal.Unit.PROTEIN, LocalDate.now().plusDays(1), 0);

        individualGoal.updateProgress(Goal.Unit.PROTEIN, 1);

        assertTrue(individualGoal.isCompleted());
        assertFalse(individualGoal.isActive());
    }

    @Test
    void UpdateDoesNotMarkCompletedAndActive() {
        IndividualGoal individualGoal = new IndividualGoal(2, Goal.Unit.PROTEIN, LocalDate.now().plusDays(1), 0);

        individualGoal.updateProgress(Goal.Unit.PROTEIN, 1);

        assertFalse(individualGoal.isCompleted());
        assertTrue(individualGoal.isActive());
    }
}