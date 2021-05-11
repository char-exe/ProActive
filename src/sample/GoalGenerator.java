package sample;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

/**
 * Class to automatically generate goals for user's. Generates nutritional goals based on the UK government's
 * dietary recommendations, and fitness goals based on the user's prior activity.
 *
 * @author Samuel Scarfe
 *
 * @version 1.1
 *
 * 1.0 - First working version
 * 1.1 - Added day to day goal setting for a much wider range of nutrients.
 */
public class GoalGenerator {

    /**
     * The user that the goals are generated for.
     */
    private final User user;

    /**
     * A database handler with which to engage the database.
     */
    private final DatabaseHandler db;

    /**
     * Constructs a GoalGenerator from a passed User instance. Initialises a DatabaseHandler instance.
     * @param user the user to generate goals for
     */
    public GoalGenerator(User user) {
        if (user == null) {
            throw new NullPointerException();
        }
        this.user = user;
        this.db = DatabaseHandler.getInstance();
    }

    /**
     * Method to retrieve from the database or generate new SystemGoals for this GoalGenerator's User. If no fitness
     * goals have been completed, then no Daily or Weekly fitness goals will be generated.
     *
     * @return an ArrayList of generated SystemGoals.
     */
    public ArrayList<SystemGoal> generateGoals() {
        //Check if Day to Day exist
        ArrayList<SystemGoal> dayToDay = user.getDayToDayGoals();
        if (dayToDay.size() == 0) { //If not, generate
            if (user.getAge() > 0) {
                dayToDay = generateDayToDay();
            }
        }

        ArrayList<SystemGoal> goals = new ArrayList<>(dayToDay);

        //Check if Daily fitness exist
        ArrayList<SystemGoal> dailyFitness = user.getDailyFitness();
        if (dailyFitness.size() == 0) { //If not, generate.
            dailyFitness = generateDailyFitness();
        }

        //Check if weekly fitness exist
        ArrayList<SystemGoal> weeklyFitness = user.getWeeklyFitness();
        if (weeklyFitness.size() == 0) { //if not, generate.
            weeklyFitness = generateWeeklyFitness(dailyFitness);
        }

        goals.addAll(dailyFitness);
        goals.addAll(weeklyFitness);
        
        return goals;
    }

    /**
     * Private helper method to generate day to day goals for this GoalGenerator's User.
     * @return An ArrayList of SystemGoals with nutritional target's in line with the UK Governments dietary
     * recommendations for age and gender.
     */
    private ArrayList<SystemGoal> generateDayToDay() {
        ArrayList<SystemGoal> goals = new ArrayList<>();
        Goal.Unit[] units = {
                Goal.Unit.CALORIES,   Goal.Unit.PROTEIN,     Goal.Unit.CARBS,      Goal.Unit.FIBRE,
                Goal.Unit.SODIUM,     Goal.Unit.POTASSIUM,   Goal.Unit.CALCIUM,    Goal.Unit.MAGNESIUM,
                Goal.Unit.PHOSPHORUS, Goal.Unit.IRON,        Goal.Unit.COPPER,     Goal.Unit.ZINC,
                Goal.Unit.CHLORIDE,   Goal.Unit.SELENIUM,    Goal.Unit.IODINE,     Goal.Unit.VITAMIN_A,
                Goal.Unit.VITAMIN_D,  Goal.Unit.THIAMIN,     Goal.Unit.RIBOFLAVIN, Goal.Unit.NIACIN,
                Goal.Unit.VITAMIN_B6, Goal.Unit.VITAMIN_B12, Goal.Unit.FOLATE,     Goal.Unit.VITAMIN_C
        };

        //For each unit
        for (Goal.Unit unit : units) {
            //get the User's RDI
            float target = db.getRecommendedIntake(unit, user.getAge(), user.getSex());
            //generate a goal for today with the RDI
            if (target > 0) {
                goals.add(new SystemGoal(target, unit, LocalDate.now().plusDays(1), SystemGoal.UpdatePeriod.DAILY, SystemGoal.Category.DAY_TO_DAY));
            }
        }

        return goals;
    }

    /**
     * Private helper method to generate Daily fitness goals for this GoalGenerator's User. Generates goals based
     * on user's previously completed goals considering both activity and target. If the user has completed no goals
     * then no goals will be generated. If the calculated target is below the activity's threshold then it is set
     * to the threshold.
     *
     * @return An ArrayList of System goals containing daily fitness targets.
     */
    private ArrayList<SystemGoal> generateDailyFitness() {
        //Get user's completed exercise goals for the past month with max target
        Random random = new Random();
        ArrayList<SystemGoal> systemGoals = new ArrayList<>();
        ArrayList<Goal.Unit> completedGoals = user.getCompletedGoals(LocalDate.now().minusDays(28));
        System.out.println(completedGoals);
        //Select up to three at random
        while (systemGoals.size() < 3 && completedGoals.size() > 0) {
            int index = random.nextInt(completedGoals.size());
            Goal.Unit current = completedGoals.remove(index);

            //Create new SystemGoals by checking the user's average work rate in that task
            float averageWorkRate = user.getAverageWorkRate(current, 28);

            if (averageWorkRate < current.getMinimum()) { //Average work rate is below minimum threshold
                averageWorkRate = current.getMinimum(); //Set to minimum threshold
            }

            systemGoals.add(new SystemGoal(
                    averageWorkRate,
                    current,
                    LocalDate.now().plusDays(1),
                    SystemGoal.UpdatePeriod.DAILY,
                    SystemGoal.Category.STAY)
            );
        }

        if (systemGoals.size() < 3) { //If less than three goals have been created
            systemGoals.add(new SystemGoal( //Create and add a generic exercise goal
                    30,
                    Goal.Unit.EXERCISE,
                    LocalDate.now().plusDays(1),
                    SystemGoal.UpdatePeriod.DAILY,
                    SystemGoal.Category.STAY)
            );
        }

        //Create push harder goals by multiplying targets for the stay on pace goals by 1.1
        ArrayList<SystemGoal> pushGoals = new ArrayList<>();

        for (SystemGoal systemGoal : systemGoals) {
            pushGoals.add(new SystemGoal(
                    (int) systemGoal.getTarget() * 1.1f, //Cast to an int to remove decimal parts
                    systemGoal.getUnit(),
                    systemGoal.getEndDate(),
                    systemGoal.getUpdatePeriod(),
                    SystemGoal.Category.PUSH));
        }

        systemGoals.addAll(pushGoals);

        return systemGoals;
    }

    /**
     * Private helper method to generate weekly fitness goals for this Goal Generator's User based on the provided
     * daily goals.
     *
     * @param dailyGoals the generated daily goals.
     * @return An ArrayList of SystemGoals equivalent to the passed daily goals but with a week to achieve them and
     * target's 5 times larger.
     */
    private ArrayList<SystemGoal> generateWeeklyFitness(ArrayList<SystemGoal> dailyGoals) {
        ArrayList<SystemGoal> weeklyGoals = new ArrayList<>();

        //For every daily goal, create a weekly equivalent by multiplying the target by 5.
        for (SystemGoal goal : dailyGoals) {
            weeklyGoals.add(new SystemGoal(
                    goal.getTarget()*5,
                    goal.getUnit(),
                    goal.getEndDate().plusDays(7),
                    SystemGoal.UpdatePeriod.WEEKLY,
                    goal.getCategory())
            );
        }

        return weeklyGoals;
    }
}
