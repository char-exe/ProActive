package sample;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit testing for User.
 *
 * Methods involving the DatabaseHandler are tested for incorrect inputs but not for successful outputs. In the current
 * project build they are more easily tested by passing data through the live system.
 *
 * @author Samuel Scarfe
 */

class UserTest {

    @Test
    void nullFirstnameConstruction() {
        assertThrows(NullPointerException.class, () -> new User(
                null,
                "Wayne",
                User.Sex.MALE,
                180,
                120,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        ));
    }

    @Test
    void emptyFirstnameConstruction() {
        assertThrows(IllegalArgumentException.class, () -> new User(
                "",
                "Wayne",
                User.Sex.MALE,
                180,
                120,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        ));
    }

    @Test
    void numbersFirstnameConstruction() {
        assertThrows(IllegalArgumentException.class, () -> new User(
                "Bruc3",
                "Wayne",
                User.Sex.MALE,
                180,
                120,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        ));
    }

    @Test
    void specialCharactersFirstnameConstruction() {
        assertThrows(IllegalArgumentException.class, () -> new User(
                "Bruce!",
                "Wayne",
                User.Sex.MALE,
                180,
                120,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        ));
    }

    @Test
    void nullSurnameConstruction() {
        assertThrows(NullPointerException.class, () -> new User(
                "Bruce",
                null,
                User.Sex.MALE,
                180,
                120,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        ));
    }

    @Test
    void emptySurnameConstruction() {
        assertThrows(IllegalArgumentException.class, () -> new User(
                "Bruce",
                "",
                User.Sex.MALE,
                180,
                120,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        ));
    }

    @Test
    void numbersSurnameConstruction() {
        assertThrows(IllegalArgumentException.class, () -> new User(
                "Bruce",
                "W4yne",
                User.Sex.MALE,
                180,
                120,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        ));
    }

    @Test
    void specialCharactersSurnameConstruction() {
        assertThrows(IllegalArgumentException.class, () -> new User(
                "Bruce",
                "Wayne?",
                User.Sex.MALE,
                180,
                120,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        ));
    }

    @Test
    void nullSexConstruction() {
        assertThrows(NullPointerException.class, () -> new User(
                "Bruce",
                "Wayne",
                null,
                180,
                120,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        ));
    }

    @Test
    void nullDobConstruction() {
        assertThrows(NullPointerException.class, () -> new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                180,
                120,
                null,
                "manbat@gmail.com",
                "bwayne1998"
        ));
    }

    @Test
    void nullEmailConstruction() {
        assertThrows(NullPointerException.class, () -> new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                180,
                120,
                LocalDate.of(1998, 3, 9),
                null,
                "bwayne1998"
        ));
    }

    @Test
    void noDomainEmailConstruction() {
        assertThrows(IllegalArgumentException.class, () -> new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                180,
                120,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail",
                "bwayne1998"
        ));
    }

    @Test
    void noServerEmailConstruction() {
        assertThrows(IllegalArgumentException.class, () -> new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                180,
                120,
                LocalDate.of(1998, 3, 9),
                "manbat@.com",
                "bwayne1998"
        ));
    }

    @Test
    void noAtEmailConstruction() {
        assertThrows(IllegalArgumentException.class, () -> new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                180,
                120,
                LocalDate.of(1998, 3, 9),
                "manbatgmail.com",
                "bwayne1998"
        ));
    }

    @Test
    void noUserEmailConstruction() {
        assertThrows(IllegalArgumentException.class, () -> new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                180,
                120,
                LocalDate.of(1998, 3, 9),
                "@gmail.com",
                "bwayne1998"
        ));
    }

    @Test
    void nullUsernameConstruction() {
        assertThrows(NullPointerException.class, () -> new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                180,
                120,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                null
        ));
    }

    @Test
    void shortUsernameConstruction() {
        assertThrows(IllegalArgumentException.class, () -> new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                180,
                120,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bway"
        ));
    }

    @Test
    void longUsernameConstruction() {
        assertThrows(IllegalArgumentException.class, () -> new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                180,
                120,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "brucewayneforever"
        ));
    }

    @Test
    void specialCharsUsernameConstruction() {
        assertThrows(IllegalArgumentException.class, () -> new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                180,
                120,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998!"
        ));
    }

    @Test
    void zeroHeightConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                0,
                120,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        ));
    }

    @Test
    void negativeHeightConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                -1,
                120,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        ));
    }

    @Test
    void zeroWeightConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                180,
                0,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        ));
    }

    @Test
    void negativeWeightConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                180,
                -1,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        ));
    }

    @Test
    void successfulLongConstruction() {
        assertDoesNotThrow(() -> new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                180,
                120,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        ));
    }

    @Test
    void successfulShortConstruction() {
        assertDoesNotThrow(() -> new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        ));
    }

    @Test
    void getFirstname() {
        User batman = new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                180,
                120,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        );

        assertEquals("Bruce", batman.getFirstname());
    }


    @Test
    void getSurname() {
        User batman = new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                180,
                120,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        );

        assertEquals("Wayne", batman.getSurname());
    }

    @Test
    void getAge() {
        User batman = new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                180,
                120,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        );

        assertEquals(23, batman.getAge());
    }

    @Test
    void getSex() {
        User batman = new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                180,
                120,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        );

        assertEquals("Male", batman.getSex());
    }

    @Test
    void getHeight() {
        User batman = new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                180,
                120,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        );

        assertEquals(180, batman.getHeight());
    }

    @Test
    void getWeight() {
        User batman = new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                180,
                120,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        );

        assertEquals(120, batman.getWeight());
    }

    @Test
    void getDob() {
        User batman = new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                180,
                120,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        );

        assertEquals(LocalDate.of(1998, 3, 9), batman.getDob());
    }

    @Test
    void getEmail() {
        User batman = new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                180,
                120,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        );

        assertEquals("manbat@gmail.com", batman.getEmail());
    }

    @Test
    void getUsername() {
        User batman = new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                180,
                120,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        );

        assertEquals("bwayne1998", batman.getUsername());
    }

    @Test
    void getRealname() {
        User batman = new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                180,
                120,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        );

        assertEquals("Bruce Wayne", batman.getRealName());
    }

    @Test
    void nullSetFirstname() {
        User batman = new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        );

        assertThrows(NullPointerException.class, () -> batman.setFirstname(null));
    }

    @Test
    void successfulSetFirstname() {
        User batman = new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        );

        batman.setFirstname("Christian");

        assertEquals("Christian", batman.getFirstname());
    }

    @Test
    void nullSetSurname() {
        User batman = new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        );

        assertThrows(NullPointerException.class, () -> batman.setSurname(null));
    }

    @Test
    void successfulSetSurname() {
        User batman = new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        );

        batman.setSurname("Bale");

        assertEquals("Bale", batman.getSurname());
    }

    @Test
    void nullSetSex() {
        User batman = new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        );

        assertThrows(NullPointerException.class, () -> batman.setSex(null));
    }

    @Test
    void successfulSetSex() {
        User batman = new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        );

        batman.setSex(User.Sex.OTHER);

        assertEquals("Other", batman.getSex());
    }

    @Test
    void zeroSetHeight() {
        User batman = new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        );

        assertThrows(IllegalArgumentException.class, () -> batman.setHeight(0));
    }

    @Test
    void negativeSetHeight() {
        User batman = new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        );

        assertThrows(IllegalArgumentException.class, () -> batman.setHeight(-1));
    }

    @Test
    void successfulSetHeight() {
        User batman = new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        );

        batman.setHeight(180);

        assertEquals(180, batman.getHeight());
    }

    @Test
    void zeroSetWeight() {
        User batman = new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        );

        assertThrows(IllegalArgumentException.class, () -> batman.setWeight(0));
    }

    @Test
    void negativeSetWeight() {
        User batman = new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        );

        assertThrows(IllegalArgumentException.class, () -> batman.setWeight(-1));
    }

    @Test
    void successfulSetWeight() {
        User batman = new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        );

        batman.setWeight(120);

        assertEquals(120, batman.getWeight());
    }

    @Test
    void testToString() {
        User batman = new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        );

        assertEquals("Bruce Wayne 23 Male", batman.toString());
    }

    @Test
    void successfulSetSystemGoals() {
        User batman = new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        );

        ArrayList<SystemGoal> systemGoals = new ArrayList<>();

        systemGoals.add(new SystemGoal(
                1.0f, Goal.Unit.PROTEIN,
                LocalDate.now().plusDays(1),
                SystemGoal.UpdatePeriod.DAILY,
                SystemGoal.Category.DAY_TO_DAY)
        );
        systemGoals.add(new SystemGoal(
                1.0f, Goal.Unit.PROTEIN,
                LocalDate.now().plusDays(1),
                SystemGoal.UpdatePeriod.DAILY,
                SystemGoal.Category.DAY_TO_DAY)
        );
        systemGoals.add(new SystemGoal(
                1.0f, Goal.Unit.PROTEIN,
                LocalDate.now().plusDays(1),
                SystemGoal.UpdatePeriod.DAILY,
                SystemGoal.Category.DAY_TO_DAY)
        );

        batman.setSystemGoals(systemGoals);

        assertEquals(systemGoals, batman.getSystemGoals());
    }

    @Test
    void successfulSetGroupGoals() {
        User batman = new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        );

        ArrayList<GroupGoal> groupGoals = new ArrayList<>();

        groupGoals.add(new GroupGoal(1.0f, Goal.Unit.PROTEIN, LocalDate.now().plusDays(1)));
        groupGoals.add(new GroupGoal(1.0f, Goal.Unit.PROTEIN, LocalDate.now().plusDays(1)));
        groupGoals.add(new GroupGoal(1.0f, Goal.Unit.PROTEIN, LocalDate.now().plusDays(1)));

        batman.setGroupGoals(groupGoals);

        assertEquals(groupGoals, batman.getGroupGoals());
    }

    @Test
    void nullAddGoal() {
        User batman = new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        );

        assertThrows(NullPointerException.class, () -> batman.addGoal(null));
    }

    @Test
    void illegalAddGoal() {
        User batman = new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        );

        assertThrows(IllegalArgumentException.class, () -> batman.addGoal(new SystemGoal(
                1.0f, Goal.Unit.PROTEIN,
                LocalDate.now().plusDays(1),
                SystemGoal.UpdatePeriod.DAILY,
                SystemGoal.Category.DAY_TO_DAY))
        );
    }

    @Test
    void nullEarliestMaxCompleted() {
        User batman = new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        );

        assertThrows(NullPointerException.class, () -> batman.getMaxCompletedGoals(null));
    }

    @Test
    void futureEarliestMaxCompleted() {
        User batman = new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        );

        assertThrows(IllegalArgumentException.class, ()-> batman.getMaxCompletedGoals(LocalDate.now().plusDays(1)));
    }

    @Test
    void nullUnitAverageWorkRate() {
        User batman = new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        );

        assertThrows(NullPointerException.class, () -> batman.getAverageWorkRate(null, 1));
    }

    @Test
    void zeroDaysAverageWorkRate() {
        User batman = new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        );

        assertThrows(NullPointerException.class, () -> batman.getAverageWorkRate(null, 0));
    }

    @Test
    void negativeDaysAverageWorkRate() {
        User batman = new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        );

        assertThrows(NullPointerException.class, () -> batman.getAverageWorkRate(null, -1));
    }

    @Test
    void nullQuitGoal() {
        User batman = new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        );

        assertThrows(NullPointerException.class, () -> batman.quitGoal(null));
    }
}