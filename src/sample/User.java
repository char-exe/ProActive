package sample;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.Date;

/**
 * A class to represent a user in a health tracking application.
 *
 * @author Owen Tasker
 * @author Charlie Jones
 *
 * @version 1.00
 *
 */
public class User {

    public enum Sex {
        Male,Female,Other
    }

    private String firstName;
    private String surName;
    private int age;
    private Sex sex;
    private float height;  //Changed from int on class description storing in CM
    private float weight;  //Changed from int on class description storing in KG
    private LocalDate dob;
    private String email;
    //private Set<Group> groupMemberships;  To be added in sprint 2 group not a suitable name object in Swing may be OK though
    private String userName;

    public User(String firstName, String surName, int age, Sex sex, float height, float weight, LocalDate dob, String email, String userName){
        this.firstName = firstName;
        this.surName = surName;
        this.sex = sex;
        this.height = height;
        this.weight = weight;
        this.dob = dob;
        this.age = age;
        this.email = email;
        this.userName = userName;
    }


    public static void main(String[] args) {
        LocalDate today = LocalDate.now();
        LocalDate birthday = LocalDate.of(1998, Month.APRIL, 25);

        Period p = Period.between(birthday, today);

        System.out.println(p.getYears());

    }




}