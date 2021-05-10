package sample;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import sample.*;

/**
 * A class to represent a user in a health tracking application, most/all error checking is completed
 * inside of the registration form.
 *
 * @author Owen Tasker
 * @author Charlie Jones
 * @author Samuel Scarfe
 * @author Evan Clayton
 *
 * @version 1.9
 *
 * 1.0 - Initial user class structure and their variables.
 * 1.1 - Added constructor, getters and setters.
 * 1.2 - Added Javadoc comments for methods
 * 1.3 - Updated Javadoc.
 * 1.4 - Added methods to check constructor inputs and throw necessary exceptions. Updated setAge to throw
 *       an exception if the dob passed is in the future.
 * 1.5 - Updated Javadoc.
 * 1.6 - Added groupMembership array and corresponding methods
 * 1.7 - Implemented adding and updating goals.
 * 1.8 - Implemented automatic goal generation.
 * 1.9 - Added functionality for including group goals.
 */
public class User {

    public enum Sex {
        MALE,FEMALE,OTHER;

        @Override
        public String toString(){
            String name = this.name();
            return name.charAt(0) + name.substring(1).toLowerCase();
        }

    }

    private String firstname;
    private String surname;
    private int age;
    private Sex sex;
    private float height;  //Changed from int on class description storing in CM
    private float weight;  //Changed from int on class description storing in KG
    private LocalDate dob;
    private final String email;
    private Set<Group> groupMemberships = new HashSet<Group>();
    private ArrayList<UserGoal> goals;
    private ArrayList<SystemGoal> systemGoals;
    private final String username;

    /**
     * Constructs a User object with all fields initialised.
     *
     * @param firstname Stores the firstname of the User
     * @param surname Stores the surname of the User
     * @param sex Stores the sex of the User
     * @param height Stores the height of the User
     * @param weight Stores the weight of the User
     * @param dob Stores the date of birth of the User
     * @param email Stores the unique email address of the User
     * @param username Stores the unique username of the User
     */
    public User(String firstname, String surname, Sex sex, float height, float weight, LocalDate dob, String email,
                String username){
        checkConstructorInputs(firstname, surname, sex, height, weight, dob, email, username);

        this.firstname = firstname;
        this.surname = surname;
        this.sex = sex;
        this.height = height;
        this.weight = weight;
        this.dob = dob;
        this.email = email;
        this.username = username;

        this.goals = DatabaseHandler.getInstance().selectGoals(username);

        this.setAge();  //Takes the current date and DOB and calculates the current age of the user
    }

    /**
     * Constructs a user object with only firstname, surname, sex, dob, email, and username initialised.
     * @param firstname Stores the firstname of the User
     * @param surname Stores the surname of the User
     * @param sex Stores the sex of the User
     * @param dob Stores the date of birth of the User
     * @param email Stores the unique email address of the User
     * @param username Stores the unique username of the User
     */
    public User(String firstname, String surname, Sex sex, LocalDate dob, String email, String username) {
        checkConstructorInputs(firstname, surname, sex, dob, email, username);

        this.firstname = firstname;
        this.surname = surname;
        this.sex = sex;
        this.dob = dob;
        this.email = email;
        this.username = username;

        this.goals = DatabaseHandler.getInstance().selectGoals(username);

        this.setAge();  //Takes the current date and DOB and calculates the current age of the user
    }

    /**
     * Constructs a user object with only firstname, surname, sex, dob, email, height, weight and username initialised.
     * @param firstname Stores the firstname of the User
     * @param surname Stores the surname of the User
     * @param sex Stores the sex of the User
     * @param dob Stores the date of birth of the User
     * @param email Stores the unique email address of the User
     * @param username Stores the unique username of the User
     * @param height Stores the height of the user
     * @param weight Stores the weight of the user
     *
     */
    public User(String firstname, String surname, Sex sex, LocalDate dob, String email, String username, float height, float weight) {
        checkConstructorInputs(firstname, surname, sex, dob, email, username);

        this.firstname = firstname;
        this.surname = surname;
        this.sex = sex;
        this.dob = dob;
        this.email = email;
        this.username = username;
        this.height = height;
        this.weight = weight;

        this.goals = DatabaseHandler.getInstance().selectGoals(username);

        this.setAge();  //Takes the current date and DOB and calculates the current age of the user
    }

    /**
     * Getter for firstname
     *
     * @return returns the User's firstname
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Getter for surname
     *
     * @return returns the User's surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Getter for age
     *
     * @return returns the User's age
     */
    public int getAge(){
        return this.age;
    }

    /**
     * Getter for sex
     *
     * @return returns the User's sex in String format
     */
    public String getSex() {
        return sex.toString();
    }

    /**
     * Getter for height
     *
     * @return returns the User's height
     */
    public float getHeight(){
        return height;
    }

    /**
     * Getter for weight
     *
     * @return returns the User's weight
     */
    public float getWeight() {
        return weight;
    }

    /**
     * Getter for Date Of Birth
     *
     * @return returns the User's date of birth
     */
    public LocalDate getDob() {
        return dob;
    }

    /**
     * Getter for email address
     *
     * @return returns the User's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Getter for username
     *
     * @return returns the User's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Method for getting the full name of a user based on their first and last name
     *
     * @return returns the users full name
     */
    public String getRealName(){
        return firstname + " " + surname;
    }

    /**
     * Method for getting the full list of groups the user belongs to.
     * @return returns the list of group objects
     */
    public Set<Group> getGroupMemberships() { return groupMemberships; }

    /**
     * Gets the list of goals for this user.
     *
     * @return this user's goals as an ArrayList.
     */
    public ArrayList<UserGoal> getGoals() {
        return this.goals;
    }

    /**
     * Gets the list of system goals for this user.
     *
     * @return this user's system goals as an ArrayList.
     */
    public ArrayList<SystemGoal> getSystemGoals() {
        return this.systemGoals;
    }


    /**
     * Sets the User's firstname to the passed parameter.
     *
     * @param firstname the User's new firstname
     */
    public void setFirstname(String firstname) {
        if (firstname == null) {
            throw new NullPointerException();
        }

        validateFirstnameRegex(firstname);

        this.firstname = firstname;
    }

    /**
     * Sets the User's surname to the passed parameter.
     *
     * @param surname the User's new surname
     */
    public void setSurname(String surname) {
        if (surname == null) {
            throw new NullPointerException();
        }

        validateSurnameRegex(surname);

        this.surname = surname;
    }

    /**
     * Sets the User's sex to the passed parameter.
     *
     * @param sex the User's new sex
     */
    public void setSex(Sex sex) {
        if (sex == null) {
            throw new NullPointerException();
        }

        this.sex = sex;
    }

    /**
     * Sets the User's height to the passed parameter.
     *
     * @param height the User's new height
     */
    public void setHeight(float height) {
        if (height <= 0) {
            throw new IllegalArgumentException();
        }

        this.height = height;
    }

    /**
     * Sets the User's weight to the passed parameter.
     *
     * @param weight the User's new weight
     */
    public void setWeight(float weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException();
        }

        this.weight = weight;
    }

    /**
     * Sets the User's dob to the passed parameter.
     *
     * @param dob the user's new date of birth.
     */
    public void setDob(LocalDate dob) {
        if (dob == null) {
            throw new NullPointerException();
        }
        if (dob.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException();
        }
        this.dob = dob;
        this.setAge();
    }

    /**
     * Takes Users DOB and compares it to the current date, calculates and sets the age dynamically
     */
    private void setAge(){
        LocalDate today = LocalDate.now();
        LocalDate birthday = LocalDate.of(this.dob.getYear(), this.dob.getMonth(), this.dob.getDayOfMonth());

        this.age = Period.between(birthday, today).getYears();
    }

    /**
     * Method to set this user's system goals to the provided ArrayList.
     *
     * @param systemGoals this user's system goals.
     */
    public void setSystemGoals(ArrayList<SystemGoal> systemGoals) {
        this.systemGoals = systemGoals;
    }


    /**
     * Adds a goal to this user's goal list and then to the database.
     *
     * @param goal the goal to be added.
     */
    public void addGoal(UserGoal goal) {
        if (goal == null) {
            throw new NullPointerException();
        }

        this.goals.add(goal);
        DatabaseHandler.getInstance().insertGoal(this.getUsername(), goal);
    }

    /**
     * Queries the user's goals to see if any are suitable for update by the passed unit and amount.
     *
     * @param unit   the unit which has been input as part of a logged activity.
     * @param amount the amount of the unit which has been logged.
     */
    public void updateGoals(Goal.Unit unit, int amount) {
        //for each goal
        for (UserGoal userGoal : goals) {
            //if the goal is updated
            if (userGoal.updateProgress(unit, amount, this.username)) {
                //update the goal in the database
                DatabaseHandler.getInstance().updateGoal(username, userGoal, amount);
            }
        }
    }

    /**
     * Queries the user's goals to see if any are suitable for update by the passed unit and amount.
     *
     * @param unit   the unit which has been input as part of a logged activity.
     * @param amount the amount of the unit which has been logged.
     */
    public void updateGoals(Goal.Unit unit, float amount) {
        if (unit == null) {
            throw new NullPointerException();
        }
        if (amount < 0) {
            throw new IllegalArgumentException();
        }
        //for each goal
        for (UserGoal userGoal : goals) {
            //if the goal is updated
            if (userGoal.updateProgress(unit, amount, this.username)) {
                //update the goal in the database
                DatabaseHandler.getInstance().updateGoal(username, userGoal, amount);
            }
        }
    }

    /**
     * Method to query the database for this user's day to day system goals.
     * @return an ArrayList of SystemGoals for this user's day to day system goals.
     */
    public ArrayList<SystemGoal> getDayToDayGoals() {
        return DatabaseHandler.getInstance().selectSystemGoals(
                this.username, LocalDate.now().plusDays(1), SystemGoal.Category.DAY_TO_DAY
        );
    }

    /**
     * Method to query the database for this user's daily fitness system goals.
     * @return an ArrayList of SystemGoals for this user's daily fitness system goals.
     */
    public ArrayList<SystemGoal> getDailyFitness() {
        return DatabaseHandler.getInstance().selectDailyFitnessGoals(this.username, LocalDate.now().plusDays(1));
    }

    /**
     * Method to query the database for this user's weekly fitness system goals.
     * @return an ArrayList of SystemGoals for this user's weekly fitness system goals.
     */
    public ArrayList<SystemGoal> getWeeklyFitness() {
        return DatabaseHandler.getInstance().selectWeeklyFitnessGoals(this.username, LocalDate.now().plusDays(7)        );
    }

    /**
     * Method to query the database for this user's goal of each unit with the maximum achieved target within a
     * date range.
     *
     * @param earliest The number of days prior to today to query
     * @return an ArrayList of SystemGoals.
     */
    public ArrayList<IndividualGoal> getMaxCompletedGoals(LocalDate earliest) {
        if (earliest == null) {
            throw new NullPointerException();
        }
        if (earliest.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException();
        }

        return DatabaseHandler.getInstance().selectMaxCompletedGoals(this.username, earliest);
    }

    /**
     * Method to get a user's average work rate in a particular activity within a date range.
     *
     * @param unit the activity to be queried.
     * @param daysEarlier the earliest date to be queried.
     * @return a float representing the average work rate for that unit over the time period.
     */
    public float getAverageWorkRate(Goal.Unit unit, int daysEarlier) {
        if (unit == null) {
            throw new NullPointerException();
        }
        if (daysEarlier < 1) {
            throw new IllegalArgumentException();
        }
        return DatabaseHandler.getInstance().selectAverageWorkRate(this.username, unit, daysEarlier);
    }

    /**
     * Method to save this user's system goals in the database. Intended for use whenever their values change such
     * that their state will persist between logins.
     */
    public void saveSystemGoals() {
        DatabaseHandler.getInstance().refreshSystemGoals(this.username, this.systemGoals);
    }


    /**
     * Method to save this user's group goals in the database. Intended for use whenever their values change such that
     * their state will persist between logins.
     */
    //public void saveGroupGoals() { DatabaseHandler.getInstance().refreshGroupGoals(this.username, this.groupGoals);}


    /**
     * Method to mark a goal as not active and update it's end date to today's date, functionally equivalent to
     * quitting it.
     *
     * @param userGoal the goal to quit.
     */
    public void quitGoal(UserGoal userGoal) {
        if (userGoal == null) {
            throw new NullPointerException();
        }
        for (UserGoal ug : this.goals) {
            if (ug == userGoal) {
                ug.quitGoal();
                DatabaseHandler.getInstance().quitGoalInDatabase(this.username, userGoal);
            }
        }
    }

    /**
     * Method to check if a user has a goal identical to a new goal that they are trying to add.
     * @param newGoal The goal object trying to be added.
     * @return True if there is an identical goal, otherwise false.
     */
    public boolean hasGoal(Goal newGoal) {
        for (Goal goal:goals){
            if (newGoal.equals(goal)) {
                return true;
            }
        }
        return false;
    }

    /*

    public void joinGroup(Group group){

        To be implemented in Sprint 2
    }
    */

    /**
     * Returns a String representation of this User object.
     *
     * @return returns a String containing Users firstname, surname and sex
     */
    @Override
    public String toString(){
        return this.firstname + " " + this.surname + " " + this.age + " " + this.getSex();
    }

    /**
     * Private method for validating parameters passed to create an instance of a User.
     *
     * @param firstname Stores the firstname of the User
     * @param surname Stores the surname of the User
     * @param sex Stores the sex of the User
     * @param height Stores the height of the User
     * @param weight Stores the weight of the User
     * @param dob Stores the date of birth of the User
     * @param email Stores the unique email address of the User
     * @param username Stores the unique username of the User
     */
    private void checkConstructorInputs(String firstname, String surname, Sex sex, float height, float weight,
                                        LocalDate dob, String email, String username) {
        checkConstructorInputs(firstname, surname, sex, dob, email, username);

        if (height <= 0) {
            throw new IllegalArgumentException();
        }
        if (weight <= 0) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Private method for validating parameters passed to create an instance of a User.
     *
     * @param firstname Stores the firstname of the User
     * @param surname Stores the surname of the User
     * @param sex Stores the sex of the User
     * @param dob Stores the date of birth of the User
     * @param email Stores the unique email address of the User
     * @param username Stores the unique username of the User
     */
    private void checkConstructorInputs(
            String firstname, String surname, Sex sex, LocalDate dob, String email, String username) {
        if (firstname == null) {
            throw new NullPointerException();
        }
        if (surname == null) {
            throw new NullPointerException();
        }
        if (sex == null) {
            throw new NullPointerException();
        }
        if (dob == null) {
            throw new NullPointerException();
        }
        if (dob.compareTo(LocalDate.now()) > 0) {
            throw new IllegalArgumentException();
        }
        if (email == null) {
            throw new NullPointerException();
        }
        if (username == null) {
            throw new NullPointerException();
        }

        validateRegex(firstname, surname, email, username);
    }

    /**
     * Private method for validating the fields of a User object to which regex is applied during registration.
     *
     * @param firstname Stores the firstname of the User
     * @param surname Stores the surname of the User
     * @param email Stores the unique email address of the User
     * @param username Stores the unique username of the User
     */
    private void validateRegex(String firstname, String surname, String email, String username) {
        final String EMAILREGEX     = "^\\w+.?\\w+@\\w+[.]\\w+([.]\\w+){0,2}$";
        final String USERNAMEREGEX  = "^[a-zA-Z0-9_-]{5,16}$";

        validateFirstnameRegex(firstname);
        validateSurnameRegex(surname);
        if (!email.matches(EMAILREGEX)) {
            throw new IllegalArgumentException("Email does not conform to required format.");
        }
        if (!username.matches(USERNAMEREGEX)) {
            throw new IllegalArgumentException("Username contains illegal characters.");
        }
    }

    /**
     * Private method for validating a User's firstname against the regex used during registration.
     *
     * @param firstname Stores the firstname of the User
     */
    private void validateFirstnameRegex(String firstname) {
        final String FIRSTNAMEREGEX = "[A-Z][a-z]*";
        if (!firstname.matches(FIRSTNAMEREGEX)) {
            throw new IllegalArgumentException("First name contains illegal characters.");
        }
    }

    /**
     * Private method for validating a User's surname against the regex used during registration.
     *
     * @param surname Stores the surname of the User
     */
    private void validateSurnameRegex(String surname) {
        final String LASTNAMEREGEX  = "[a-zA-Z]+([ '-][a-zA-Z]+)*";
        if (!surname.matches(LASTNAMEREGEX)) {
            throw new IllegalArgumentException("First name contains illegal characters.");
        }
    }

    /**
     * Public method for adding a group membership to the user's list of memberships.
     *
     * @param group Takes a the group object for the group being joined.
     */
    public void joinGroup(Group group) {
        if (groupMemberships.contains(group) == false) {
            groupMemberships.add(group);
        }
    }

    /**
     * Method for removing a user from a group
     * @param group Group object for the group the user is to leave.
     */
    public void leaveGroup(Group group) {
        if (groupMemberships.contains(group)){
            groupMemberships.remove(group);
        }
    }

    /**
     * Method to check for group membership.
     * @param group Group object for the group you are checking the user is a member of.
     * @return returns true if they are a member, false if they are not.
     */
    public boolean isGroupMemberOf(Group group){
        if (groupMemberships.contains(group)){
            return true;
        }
        else {
            return false;
        }
    }
}