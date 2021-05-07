package sample;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit testing for GoalGenerator. Test only for successful construction as the only other public method requires
 * a connection to the database through the user, this method is instead best tested by passing real data through the
 * live system.
 *
 * @author Samuel Scarfe
 */

class GoalGeneratorTest {

    @Test
    void nullUser() {
        assertThrows(NullPointerException.class, () -> new GoalGenerator(null));
    }
    @Test
    void successfulConstruction() {
        User batman = new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        );

        assertDoesNotThrow(() -> new GoalGenerator(batman));
    }

}