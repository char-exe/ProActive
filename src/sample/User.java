package sample;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;

/**
 * A class to represent a user in a health tracking application, most/all error checking is completed
 * inside of the registration form.
 *
 * @author Owen Tasker
 * @author Charlie Jones
 * @author Samuel Scarfe
 *
 * @version 1.5
 *
 * 1.0 - Initial user class structure and their variables.
 * 1.1 - Added constructor, getters and setters.
 * 1.2 - Added Javadoc comments for methods
 * 1.3 - Updated Javadoc.
 * 1.4 - Added methods to check constructor inputs and throw necessary exceptions. Updated setAge to throw
 *       an exception if the dob passed is in the future.
 * 1.5 - Updated Javadoc.
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
    //private Set<Group> groupMemberships = new ArrayList();  To be added in sprint 2 group not a suitable name, object in Swing may be OK though
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

    //Class-Specific Methods
    /*
    public ArrayList<Goal> getGoals(){
        To be implemented in Sprint 2
    }

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
    private void checkConstructorInputs(String firstname, String surname, Sex sex, LocalDate dob, String email,
                                        String username) {
        if (firstname == null) {
            throw new NullPointerException();
        }
        if (surname == null) {
            throw new NullPointerException();
        }
        if (sex == null){
            throw new NullPointerException();
        }
        if (dob == null){
            throw new NullPointerException();
        }
        if (dob.compareTo(LocalDate.now()) > 0) {
            throw new IllegalArgumentException();
        }
        if (email == null)
        {
            throw new NullPointerException();
        }
        if (username == null)
        {
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
     * Test Harness
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        User user = new User("test","test", Sex.MALE, 0.1f, 0.1f,
                LocalDate.of(1999, Month.DECEMBER, 28), "test@gmail.com", "testy");

        System.out.println(user.getDob().toString());
        System.out.println(user);

    }
}