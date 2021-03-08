package sample;

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

    private String firstName;
    private String surName;
    private int age;
    private Sex sex;
    private double height;  //Changed from int on class description
    private double weight;  //Changed from int on class description
    private Date dob;
    private String email;
    // private Set<Group>;  To be added in sprint 2
    private String userName;
    private String realName;



    public enum Sex {
        Male,Female,Other
    }

}