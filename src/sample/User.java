package sample;

import java.time.LocalDate;
import java.time.Period;

/**
 * A class to represent a user in a health tracking application.
 *
 * @author Owen Tasker
 * @author Charlie Jones
 *
 * @version 1.1
 *
 * 1.0 - Initial user class structure and their variables.
 * 1.1 - Added constructor, getters and setters.
 *
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
    //private Set<Group> groupMemberships = new ArrayList();  To be added in sprint 2 group not a suitable name object in Swing may be OK though
    private final String username;

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

        this.setAge();
    }


//    public static void main(String[] args) {
//        User user = new User("test","test", Sex.MALE, 0.1f, 0.1f,
//                LocalDate.of(1999,Month.DECEMBER, 28), "test", "test");
//
//        System.out.println(user.getAge());
//
//        System.out.println(user.getSex());
//
//        System.out.println(user);
//
//    }

    public void setAge(){
        LocalDate today = LocalDate.now();
        LocalDate birthday = LocalDate.of(this.dob.getYear(), this.dob.getMonth(), this.dob.getDayOfMonth());

        this.age = Period.between(birthday, today).getYears();
    }

    public String getFirstname() {
        return firstname;
    }

    public String getSurname() {
        return surname;
    }

    public int getAge(){
        return this.age;
    }

    public String getSex() {
        return sex.toString();
    }

    public float getHeight(){
        return height;
    }

    public float getWeight() {
        return weight;
    }

    public LocalDate getDob() {
        return dob;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }


    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }


//    public ArrayList<Goal> getGoals(){
//
//    }

//    public void joinGroup(Group group){
//
//    }


    @Override
    public String toString(){
        return this.firstname + " " + this.surname + " " + this.age + " " + this.getSex();
    }

}