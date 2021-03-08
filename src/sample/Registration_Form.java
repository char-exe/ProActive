package sample;

//Author:       Andrew Storey
//Start Date:   08/03/2021
//Summary:      The class to allow users to create accounts on the application.
//Updates:      Class Created

import java.util.Date;
import java.lang.String;

public class Registration_Form {

    private String firstName;
    private String lastName;
    private String email;
    private String passcode;

    private Date DoB;

    void sumbit_Pressed(){
        //validate user inputs with user class
            //if failed then need to know what will be bad for user
            //java scenes
        //call captcha
        //validate email with email handler
            //need a resend option
        //wait on email handler to carry on
        //tell database handler to create user with the details
        //end
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }

    public Date getDoB() {
        return DoB;
    }

    public void setDoB(Date doB) {
        DoB = doB;
    }
}
