package sample;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit testing for ExerciseItem.
 *
 * @author Samuel Scarfe
 */

class ExerciseItemTest {

    @Test
    void nullName() {
        assertThrows(NullPointerException.class, () -> new ExerciseItem(null, 13));
    }

    @Test
    void emptyName() {
        assertThrows(IllegalArgumentException.class, () -> new ExerciseItem("", 13));
    }

    @Test
    void negativeBurnRate() {
        assertThrows(IllegalArgumentException.class, () -> new ExerciseItem("Jogging", -1));
    }

    @Test
    void successfulConstruction() {
        assertDoesNotThrow(() -> new ExerciseItem("Jogging", 1));
    }

    @Test
    void getName() {
        ExerciseItem jogging = new ExerciseItem("Jogging", 1);

        assertEquals("Jogging", jogging.getName());
    }

    @Test
    void getBurn_rate() {
        ExerciseItem jogging = new ExerciseItem("Jogging", 1);

        assertEquals(1, jogging.getBurnRate());
    }

    @Test
    void zeroCalculateBurn() {
        ExerciseItem jogging = new ExerciseItem("Jogging", 1);

        assertThrows(IllegalArgumentException.class, () -> jogging.calculateBurn(0));
    }

    @Test
    void negativeCalculateBurn() {
        ExerciseItem jogging = new ExerciseItem("Jogging", 1);

        assertThrows(IllegalArgumentException.class, () -> jogging.calculateBurn(-1));
    }

    @Test
    void successfulCalculateBurn() {
        ExerciseItem jogging = new ExerciseItem("Jogging", 1);

        assertEquals(1, jogging.calculateBurn(1));
    }

    @Test
    void successfulEmptyCalculateBurn() {
        ExerciseItem jogging = new ExerciseItem("Jogging", 1);

        assertEquals(60, jogging.calculateBurn());
    }
}