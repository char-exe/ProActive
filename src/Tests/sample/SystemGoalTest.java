package sample;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit testing for SystemGoal.
 *
 * @author Samuel Scarfe
 */

class SystemGoalTest {

    @Test
    void zeroTarget() {
        assertThrows(IllegalArgumentException.class, () -> new SystemGoal(
                0,
                Goal.Unit.PROTEIN,
                LocalDate.now(),
                SystemGoal.UpdatePeriod.DAILY,
                SystemGoal.Category.DAY_TO_DAY)
        );
    }

    @Test
    void negativeTarget() {
        assertThrows(IllegalArgumentException.class, () -> new SystemGoal(
                -1,
                Goal.Unit.PROTEIN,
                LocalDate.now(),
                SystemGoal.UpdatePeriod.DAILY,
                SystemGoal.Category.DAY_TO_DAY)
        );
    }

    @Test
    void nullUnit() {
        assertThrows(NullPointerException.class, () -> new SystemGoal(
                1,
                null,
                LocalDate.now(),
                SystemGoal.UpdatePeriod.DAILY,
                SystemGoal.Category.DAY_TO_DAY)
        );
    }

    @Test
    void nullEndDate() {
        assertThrows(NullPointerException.class, () -> new SystemGoal(
                1,
                Goal.Unit.PROTEIN,
                null,
                SystemGoal.UpdatePeriod.DAILY,
                SystemGoal.Category.DAY_TO_DAY)
        );
    }

    @Test
    void nullUpdatePeriod() {
        assertThrows(NullPointerException.class, () -> new SystemGoal(
                1,
                Goal.Unit.PROTEIN,
                LocalDate.now(),
                null,
                SystemGoal.Category.DAY_TO_DAY)
        );
    }

    @Test
    void nullCategory() {
        assertThrows(NullPointerException.class, () -> new SystemGoal(
                1,
                Goal.Unit.PROTEIN,
                LocalDate.now(),
                SystemGoal.UpdatePeriod.DAILY,
                null)
        );
    }

    @Test
    void successfulShortConstruction() {
        assertDoesNotThrow(() -> new SystemGoal(
                1,
                Goal.Unit.PROTEIN,
                LocalDate.now(),
                SystemGoal.UpdatePeriod.DAILY,
                SystemGoal.Category.DAY_TO_DAY)
        );
    }

    @Test
    void successfulLongConstruction() {
        assertDoesNotThrow(() -> new SystemGoal(
                1,
                Goal.Unit.PROTEIN,
                LocalDate.now(),
                SystemGoal.UpdatePeriod.DAILY,
                SystemGoal.Category.DAY_TO_DAY,
                true)
        );
    }

    @Test
    void getTarget() {
        SystemGoal systemGoal = new SystemGoal(
                1,
                Goal.Unit.PROTEIN,
                LocalDate.now(),
                SystemGoal.UpdatePeriod.DAILY,
                SystemGoal.Category.DAY_TO_DAY,
                true
        );

        assertEquals(1, systemGoal.getTarget());
    }

    @Test
    void getUnit() {
        SystemGoal systemGoal = new SystemGoal(
                1,
                Goal.Unit.PROTEIN,
                LocalDate.now(),
                SystemGoal.UpdatePeriod.DAILY,
                SystemGoal.Category.DAY_TO_DAY,
                true
        );

        assertEquals(Goal.Unit.PROTEIN, systemGoal.getUnit());
    }

    @Test
    void getEndDate() {
        SystemGoal systemGoal = new SystemGoal(
                1,
                Goal.Unit.PROTEIN,
                LocalDate.now(),
                SystemGoal.UpdatePeriod.DAILY,
                SystemGoal.Category.DAY_TO_DAY,
                true
        );

        assertEquals(LocalDate.now(), systemGoal.getEndDate());
    }
    @Test
    void getProgress() {
        SystemGoal systemGoal = new SystemGoal(
                1,
                Goal.Unit.PROTEIN,
                LocalDate.now(),
                SystemGoal.UpdatePeriod.DAILY,
                SystemGoal.Category.DAY_TO_DAY,
                true
        );

        assertEquals(0, systemGoal.getProgress());
    }

    @Test
    void testToStringNotMinutes() {
        SystemGoal systemGoal = new SystemGoal(
                1,
                Goal.Unit.PROTEIN,
                LocalDate.now(),
                SystemGoal.UpdatePeriod.DAILY,
                SystemGoal.Category.DAY_TO_DAY,
                true
        );

        assertEquals("1.0 grams of protein by 2021-05-05", systemGoal.toString());
    }

    @Test
    void testToStringMinutes() {
        SystemGoal systemGoal = new SystemGoal(
                1,
                Goal.Unit.EXERCISE,
                LocalDate.now(),
                SystemGoal.UpdatePeriod.DAILY,
                SystemGoal.Category.DAY_TO_DAY,
                true
        );

        assertEquals("1 minutes of exercise by 2021-05-05", systemGoal.toString());
    }
    //
    //    public UpdatePeriod getUpdatePeriod() {
    //        return this.updatePeriod;
    //    }
    @Test
    void getUpdatePeriod() {
        SystemGoal systemGoal = new SystemGoal(
                1,
                Goal.Unit.PROTEIN,
                LocalDate.now(),
                SystemGoal.UpdatePeriod.DAILY,
                SystemGoal.Category.DAY_TO_DAY,
                true
        );

        assertEquals(SystemGoal.UpdatePeriod.DAILY, systemGoal.getUpdatePeriod());
    }
    //
    //    public Category getCategory() {
    //        return this.category;
    //    }
    @Test
    void getCategory() {
        SystemGoal systemGoal = new SystemGoal(
                1,
                Goal.Unit.PROTEIN,
                LocalDate.now(),
                SystemGoal.UpdatePeriod.DAILY,
                SystemGoal.Category.DAY_TO_DAY,
                true
        );

        assertEquals(SystemGoal.Category.DAY_TO_DAY, systemGoal.getCategory());
    }

    @Test
    void isAccepted() {
        SystemGoal systemGoal = new SystemGoal(
                1,
                Goal.Unit.PROTEIN,
                LocalDate.now(),
                SystemGoal.UpdatePeriod.DAILY,
                SystemGoal.Category.DAY_TO_DAY,
                true
        );

        assertTrue(systemGoal.isAccepted());
    }

    @Test
    void setAccepted() {
        SystemGoal systemGoal = new SystemGoal(
                1,
                Goal.Unit.PROTEIN,
                LocalDate.now(),
                SystemGoal.UpdatePeriod.DAILY,
                SystemGoal.Category.DAY_TO_DAY,
                true
        );

        systemGoal.setAccepted(false);

        assertFalse(systemGoal.isAccepted());
    }

}