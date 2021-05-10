package sample;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit testing for DatabaseHandler. Methods are tested for only tested here for incorrect inputs. In the current build,
 * successful outputs are more easily tested via real data in the live system.
 *
 * @author Samuel Scarfe
 */

class DatabaseHandlerTest {

    @Test
    void getInstance() {
        assertNotNull(DatabaseHandler.getInstance());
    }

    @Test
    void nullUserCreateUserEntry() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        byte[] hash = new byte[16];
        byte[] salt = new byte[16];

        assertThrows(NullPointerException.class, () -> dh.createUserEntry(null, hash, salt));
    }

    @Test
    void nullHashCreateUserEntry() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        User batman = new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        );
        byte[] salt = new byte[16];

        assertThrows(NullPointerException.class, () -> dh.createUserEntry(batman, null, salt));
    }

    @Test
    void nullSaltCreateUserEntry() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        User batman = new User(
                "Bruce",
                "Wayne",
                User.Sex.MALE,
                LocalDate.of(1998, 3, 9),
                "manbat@gmail.com",
                "bwayne1998"
        );
        byte[] hash = new byte[16];

        assertThrows(NullPointerException.class, () -> dh.createUserEntry(batman, hash, null));
    }

    @Test
    void nullUsernameGetUserIDFromUsername() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.getUserIDFromUsername(null));
    }

    @Test
    void negativeIdGetUsernameFromUserId() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(IllegalArgumentException.class, () -> dh.getUsernameFromUserID(-1));
    }

    @Test
    void nullUsernameGetHashFromUsername() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.getHashFromUsername(null));
    }

    @Test
    void nullUsernameGetSaltFromUsername() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.getSaltFromUsername(null));
    }

    @Test
    void nullUsernameCheckUserNameUnique() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.checkUserNameUnique(null));
    }

    @Test
    void nullUsernameInsertWeightValue() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.insertWeightValue(
                null, 1.0f, LocalDate.now())
        );
    }

    @Test
    void zeroWeightInsertWeightValue() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(IllegalArgumentException.class, () -> dh.insertWeightValue(
                "batman", 0, LocalDate.now())
        );
    }

    @Test
    void negativeWeightInsertWeightValue() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(IllegalArgumentException.class, () -> dh.insertWeightValue(
                "batman", -1, LocalDate.now())
        );
    }

    @Test
    void nullDateInsertWeightValue() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.insertWeightValue(
                "batman", 1.0f, null)
        );
    }

    @Test
    void futureDateUsernameInsertWeightValue() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(IllegalArgumentException.class, () -> dh.insertWeightValue(
                "batman", 1.0f, LocalDate.now().plusDays(1))
        );
    }

    @Test
    void nullTokenAddTokenEntry() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.addTokenEntry(null));
    }

    @Test
    void nullTokenGetTokenResult() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.getTokenResult(null));
    }

    @Test
    void nullItemNameGetNutritionItem() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.getNutritionItem(null));
    }

    @Test
    void nullItemNameGetExerciseItem() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.getExerciseItem(null));
    }

    @Test
    void nullTokenDeleteToken() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.deleteToken(null));
    }

    @Test
    void nullTableEditValue() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.editValue(
                null,
                "first_name",
                "Christian",
                "username",
                "bwayne1998"
        ));
    }

    @Test
    void nullColumnEditValue() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.editValue(
                "user",
                null,
                "Christian",
                "username",
                "bwayne1998"
        ));
    }

    @Test
    void nullValEditValue() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.editValue(
                "user",
                "first_name",
                null,
                "username",
                "bwayne1998"
        ));
    }

    @Test
    void nullIdentifyingValEditValue() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.editValue(
                "user",
                "first_name",
                "Christian",
                null,
                "bwayne1998"
        ));
    }

    @Test
    void nullIdentifyingColEditValue() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.editValue(
                "user",
                "first_name",
                "Christian",
                "username",
                null
        ));
    }

    @Test
    void nullUsernameCreateUserObjectFromUsername() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.createUserObjectFromUsername(null));
    }

    @Test
    void nullUsernameGetIntakeEntries() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.getIntakeEntries(null, LocalDate.now()));
    }

    @Test
    void nullLatestGetIntakeEntries() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.getIntakeEntries("bwayne1998", null));
    }

    @Test
    void nullUsernameGetSpentEntries() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.getSpentEntries(null, LocalDate.now()));
    }

    @Test
    void nullLatestGetSpentEntries() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.getSpentEntries("bwayne1998", null));
    }

    @Test
    void nullUsernameGetBurnedEntries() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.getBurnedEntries(null, LocalDate.now()));
    }

    @Test
    void nullLatestGetBurnedEntries() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.getBurnedEntries("bwayne1998", null));
    }

    @Test
    void nullUsernameGetWeightEntries() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.getWeightEntries(null, LocalDate.now()));
    }

    @Test
    void nullLatestGetWeightEntries() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.getWeightEntries("bwayne1998", null));
    }

    @Test
    void nullNameGetExerciseId() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.getExerciseId(null));
    }

    @Test
    void nullUsernameInsertExercise() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.insertExercise(
                null, "Walking", 1)
        );
    }

    @Test
    void nullExerciseInsertExercise() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.insertExercise(
                "bwayne1998", null, 1)
        );
    }

    @Test
    void zeroDurationInsertExercise() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(IllegalArgumentException.class, () -> dh.insertExercise(
                "bwayne1998", "Walking", 0)
        );
    }

    @Test
    void negativeDurationInsertExercise() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(IllegalArgumentException.class, () -> dh.insertExercise(
                "bwayne1998", "Walking", -1)
        );
    }

    @Test
    void nullNameGetFoodId() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.getFoodId(null));
    }

    //(String username, String meal, String food, int quantity, LocalDate date)
    @Test
    void nullUsernameAddFoodEntry() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.addFoodEntry(
                null, "Breakfast", "Bread", 1, LocalDate.now())
        );
    }

    @Test
    void nullMealAddFoodEntry() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.addFoodEntry(
                "bwayne1998", null, "Bread", 1, LocalDate.now())
        );
    }

    @Test
    void nullFoodAddFoodEntry() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.addFoodEntry(
                "bwayne1998", "Breakfast", null, 1, LocalDate.now())
        );
    }

    @Test
    void zeroQuantityAddFoodEntry() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(IllegalArgumentException.class, () -> dh.addFoodEntry(
                "bwayne1998", "Breakfast", "Bread", 0, LocalDate.now())
        );
    }

    @Test
    void negativeQuantityAddFoodEntry() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(IllegalArgumentException.class, () -> dh.addFoodEntry(
                "bwayne1998", "Breakfast", "Bread", -1, LocalDate.now())
        );
    }

    @Test
    void nullDateAddFoodEntry() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.addFoodEntry(
                "bwayne1998", "Breakfast", "Bread", 1, null)
        );
    }

    @Test
    void futureDateAddFoodEntry() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(IllegalArgumentException.class, () -> dh.addFoodEntry(
                "bwayne1998", "Breakfast", "Bread", 1, LocalDate.now().plusDays(1))
        );
    }

    @Test
    void nullUsernameInsertGoal() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        IndividualGoal goal = new IndividualGoal(1, Goal.Unit.PROTEIN, LocalDate.now().plusDays(1));

        assertThrows(NullPointerException.class, () -> dh.insertGoal(null, goal));
    }

    @Test
    void nullGoalInsertGoal() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.insertGoal("bwayne1998", null));
    }

    @Test
    void nullUsernameSelectGoals() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.selectGoals(null));
    }

    @Test
    void nullUsernameUpdateGoal() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        UserGoal goal = new IndividualGoal(1, Goal.Unit.PROTEIN, LocalDate.now().plusDays(1));

        assertThrows(NullPointerException.class, () -> dh.updateGoal(null, goal, 1));
    }

    @Test
    void nullGoalUpdateGoal() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.updateGoal("bwayne1998", null, 1));
    }

    @Test
    void zeroAmountUpdateGoal() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        UserGoal goal = new IndividualGoal(1, Goal.Unit.PROTEIN, LocalDate.now().plusDays(1));

        assertThrows(IllegalArgumentException.class, () -> dh.updateGoal("bwayne1998", goal, 0));
    }

    @Test
    void negativeAmountUpdateGoal() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        UserGoal goal = new IndividualGoal(1, Goal.Unit.PROTEIN, LocalDate.now().plusDays(1));

        assertThrows(IllegalArgumentException.class, () -> dh.updateGoal("bwayne1998", goal, 0));
    }

    @Test
    void nullUsernameGetWaterIntakeInCups() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.getWaterIntakeInCups(null, LocalDate.now()));
    }

    @Test
    void nullDateGetWaterIntakeInCups() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.getWaterIntakeInCups("bwayne1998", null));
    }

    @Test
    void futureDateGetWaterIntakeInCups() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(IllegalArgumentException.class, () -> dh.getWaterIntakeInCups(
                "bwayne1998", LocalDate.now().plusDays(1))
        );
    }

    @Test
    void nullUsernameSetWaterIntake() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.setWaterIntake(
                null, LocalDate.now(), 1
        ));
    }

    @Test
    void nullDateSetWaterIntake() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.setWaterIntake(
                "bwayne1998", null, 1
        ));
    }

    @Test
    void futureDateSetWaterIntake() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(IllegalArgumentException.class, () -> dh.setWaterIntake(
                "bwayne1998", LocalDate.now().plusDays(1), 1
        ));
    }

    @Test
    void negativeCupsSetWaterIntake() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(IllegalArgumentException.class, () -> dh.setWaterIntake(
                "bwayne1998", LocalDate.now(), -1
        ));
    }

    @Test
    void nullNutritionItemAddNutritionItem() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.addNutritionItem(null));
    }

    @Test
    void nullItemAddExerciseItem() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.addExerciseItem(null, 0));
    }

    @Test
    void negativeBurnRateAddExerciseItem() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(IllegalArgumentException.class, () -> dh.addExerciseItem("Crime-fighting", -1));
    }

    //(String username, LocalDate endDate, SystemGoal.Category category)
    @Test
    void nullUsernameSelectSystemGoals() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.selectSystemGoals(
                null, LocalDate.now(), SystemGoal.Category.DAY_TO_DAY)
        );
    }

    @Test
    void nullEndDateSelectSystemGoals() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.selectSystemGoals(
                "bwayne1998", null, SystemGoal.Category.DAY_TO_DAY)
        );
    }

    @Test
    void nullCategorySelectSystemGoals() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.selectSystemGoals(
                "bwayne1998", LocalDate.now(), null)
        );
    }

    @Test
    void nullUsernameSelectDailyFitnessGoals() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.selectDailyFitnessGoals(null, LocalDate.now()));
    }

    @Test
    void nullEndDateSelectDailyFitnessGoals() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.selectDailyFitnessGoals("bwayne1998", null));
    }

    @Test
    void nullUsernameSelectWeeklyFitnessGoals() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.selectWeeklyFitnessGoals(null, LocalDate.now()));
    }

    @Test
    void nullEndDateSelectWeeklyFitnessGoals() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.selectWeeklyFitnessGoals("bwayne1998", null));
    }

    @Test
    void nullUnitGetRecommendedIntake() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.getRecommendedIntake(null, 1, "Male"));
    }

    @Test
    void negativeAgeGetRecommendedIntake() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(IllegalArgumentException.class, () -> dh.getRecommendedIntake(
                Goal.Unit.PROTEIN, -1, "Male")
        );
    }

    @Test
    void nullSexGetRecommendedIntake() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.getRecommendedIntake(Goal.Unit.PROTEIN, 1, null));
    }

    @Test
    void nullUsernameSelectMaxCompletedGoals() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.selectMaxCompletedGoals(null, LocalDate.now()));
    }

    @Test
    void nullEarliestSelectMaxCompletedGoals() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.selectMaxCompletedGoals("bwayne1998", null));
    }

    @Test
    void nullUsernameSelectAverageWorkRate() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.selectAverageWorkRate(
                null, Goal.Unit.WALKING, 0)
        );
    }

    @Test
    void nullUnitSelectAverageWorkRate() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.selectAverageWorkRate(
                "bwayne1998", null, 0)
        );
    }

    @Test
    void negativeDaysEarlierSelectAverageWorkRate() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(IllegalArgumentException.class, () -> dh.selectAverageWorkRate(
                "bwayne1998", Goal.Unit.WALKING, -1)
        );
    }

    @Test
    void nullUsernameRefreshSystemGoals() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

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

        assertThrows(NullPointerException.class, () -> dh.refreshSystemGoals(null, systemGoals));
    }

    @Test
    void nullSystemGoalsRefreshSystemGoals() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.refreshSystemGoals("bwayne1998", null));
    }

    @Test
    void nullUsernameQuitGoalInDatabase() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        UserGoal goal = new IndividualGoal(1, Goal.Unit.PROTEIN, LocalDate.now().plusDays(1));

        assertThrows(NullPointerException.class, () -> dh.quitGoalInDatabase(null, goal));
    }

    @Test
    void nullGoalQuitGoalInDatabase() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.quitGoalInDatabase("bwayne1998", null));
    }

    @Test
    void nullGroupNameGetGroupIDFromName() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.getGroupIDFromName(null));
    }

    @Test
    void nullUserNameRemoveAdmin() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.removeAdmin(null, "Justice League"));
    }

    @Test
    void nullGroupNameRemoveAdmin() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.removeAdmin("bwayne1998", null));
    }

    @Test
    void nullUserNameAddAdmin() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.addAdmin(null, "Justice League"));
    }

    @Test
    void nullGroupNameAddAdmin() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.addAdmin("bwayne1998", null));
    }

    @Test
    void nullUserNameJoinGroup() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.joinGroup(null, "Justice League"));
    }

    @Test
    void nullGroupNameJoinGroup() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.joinGroup("bwayne1998", null));
    }

    @Test
    void nullTokenValAddInvToken() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.addInvToken(
                null, 1, "Justice League", "bwayne1998")
        );
    }

    @Test
    void negativeTimeAddInvToken() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(IllegalArgumentException.class, () -> dh.addInvToken(
                "T0K3N", -1, "Justice League", "bwayne1998")
        );
    }

    @Test
    void nullGroupNameAddInvToken() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.addInvToken(
                "T0K3N", 1, null, "bwayne1998")
        );
    }

    @Test
    void nullUserNameAddInvToken() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.addInvToken(
                "T0K3N", 1, "Justice League", null)
        );
    }

    @Test
    void nullGroupGetGroupRoleFromUsername() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class,() -> dh.getGroupRoleFromUsername(null, "bwayne1998"));
    }

    @Test
    void nullUsernameGetGroupRoleFromUsername() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class,() -> dh.getGroupRoleFromUsername(null, "bwayne1998"));
    }

    @Test
    void nullUsernameGetEmailFromUsername() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.getEmailFromUsername(null));
    }

    @Test
    void nullUserSelectGroupGoals() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.selectGroupGoals(null));
    }

    @Test
    void nullUsernameInsertGroupGoal() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        GroupGoal groupGoal = new GroupGoal(1, Goal.Unit.PROTEIN, LocalDate.now().plusDays(1), 1);

        assertThrows(NullPointerException.class, () -> dh.insertGroupGoal(null, groupGoal));
    }

    @Test
    void nullGoalInsertGroupGoal() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.insertGroupGoal("bwayne1998", null));
    }


    @Test
    void nullEmailGetUsernameFromEmail() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.getUsernameFromEmail(null));
    }
/*
    @Test
    void zeroIdInsertGroupInvite() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(IllegalArgumentException.class, () -> dh.insertGroupInvite(0, "T0K3N"));
    }

    @Test
    void negativeIdInsertGroupInvite() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(IllegalArgumentException.class, () -> dh.insertGroupInvite(-1, "T0K3N"));
    }

    @Test
    void nullTokenInsertGroupInvite() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.insertGroupInvite(1, null));
    }

 */

    @Test
    void nullGroupNameGetGroupObjectFromGroupName() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.getGroupObjectFromGroupName(null));
    }

    @Test
    void zeroGroupIdGetGroupNameFromID() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(IllegalArgumentException.class, () -> dh.getGroupNameFromID(0));
    }

    @Test
    void negativeGroupIdGetGroupNameFromID() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(IllegalArgumentException.class, () -> dh.getGroupNameFromID(-1));
    }

    @Test
    void zeroGroupIdGetGroupObjectFromGroupId() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(IllegalArgumentException.class, () -> dh.getGroupObjectFromGroupId(0));
    }

    @Test
    void negativeGroupIdGetGroupObjectFromGroupId() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(IllegalArgumentException.class, () -> dh.getGroupObjectFromGroupId(-1));
    }

    @Test
    void nullUsernameGetUserGroups() {
        DatabaseHandler dh = DatabaseHandler.getInstance();

        assertThrows(NullPointerException.class, () -> dh.getUserGroups(null));
    }
}