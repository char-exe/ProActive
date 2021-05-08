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
    void isAccepted() {
        GroupGoal groupGoal = new GroupGoal(1, Goal.Unit.PROTEIN, LocalDate.now(), 1, 1);

        assertTrue(groupGoal.isAccepted());

    }

    @Test
    void setAccepted() {
        GroupGoal groupGoal = new GroupGoal(1, Goal.Unit.PROTEIN, LocalDate.now(), 1, 1);
        groupGoal.setAccepted(false);

        assertFalse(groupGoal.isAccepted());
    }

    @Test
    void getGroupId() {
        GroupGoal groupGoal = new GroupGoal(1, Goal.Unit.PROTEIN, LocalDate.now(), 1, 1);

        assertEquals(1, groupGoal.getGroupId());
    }
}