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
 *
 * @version 1.2
 *
 * 1.0 - Initial user class structure and their variables.
 * 1.1 - Added constructor, getters and setters.
 * 1.2 - Added Javadoc comments for methods
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
    private final LocalDate dob;
    private final String email;
    //private Set<Group> groupMemberships = new ArrayList();  To be added in sprint 2 group not a suitable name, object in Swing may be OK though
    private final String username;

    //Constructors
    /**
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

    public User(String firstname, String surname, Sex sex, LocalDate dob, String email, String username){
        this.firstname = firstname;
        this.surname = surname;
        this.sex = sex;
        this.dob = dob;
        this.email = email;
        this.username = username;


        this.setAge();  //Takes the current date and DOB and calculates the current age of the user
    }

    //Getters
    /**
     *
     * @return returns the User's firstname
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     *
     * @return returns the User's surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     *
     * @return returns the User's age
     */
    public int getAge(){
        return this.age;
    }

    /**
     *
     * @return returns the User's sex in String format
     */
    public String getSex() {
        return sex.toString();
    }

    /**
     *
     * @return returns the User's height
     */
    public float getHeight(){
        return height;
    }

    /**
     *
     * @return returns the User's weight
     */
    public float getWeight() {
        return weight;
    }

    /**
     *
     * @return returns the User's Date of Birth
     */
    public LocalDate getDob() {
        return dob;
    }

    /**
     *
     * @return returns the User's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @return returns the User's username
     */
    public String getUsername() {
        return username;
    }
    /**
     *
     * @return returns the users real name, formed by concatenating the first and last name
     */
    public String getRealName(){
        return firstname + " " + surname;
    }

    //Setters
    /**
     *
     * @param firstname the User's new firstname
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     *
     * @param surname the User's new surname
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     *
     * @param sex the User's new sex
     */
    public void setSex(Sex sex) {
        this.sex = sex;
    }

    /**
     *
     * @param height the User's new height
     */
    public void setHeight(float height) {
        this.height = height;
    }

    /**
     *
     * @param weight the User's new weight
     */
    public void setWeight(float weight) {
        this.weight = weight;
    }


    /**
     *
     * Takes Users DOB and compares it to the current date, calculates and sets the age dynamically
     */
    public void setAge(){
        LocalDate today = LocalDate.now();
        LocalDate birthday = LocalDate.of(this.dob.getYear(), this.dob.getMonth(), this.dob.getDayOfMonth());

        this.age = Period.between(birthday, today).getYears();
    }
    public void setAge(LocalDate dob){
        LocalDate today = LocalDate.now();
        LocalDate birthday = LocalDate.of(dob.getYear(), dob.getMonth(), dob.getDayOfMonth());
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
     *
     * @return returns a String containing Users firstname, surname and sex
     */
    @Override
    public String toString(){
        return this.firstname + " " + this.surname + " " + this.age + " " + this.getSex();
    }


    /**
     * Test Harness
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        User user = new User("test","test", Sex.MALE, 0.1f, 0.1f,
                LocalDate.of(1999, Month.DECEMBER, 28), "test", "test");

        System.out.println(user.getDob().toString());

    }
}