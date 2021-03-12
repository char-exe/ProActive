package sample;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

/**
 *  A class to allow for the emailing of codes and other information to users of the ProActive app
 *
 * @author Owen Tasker
 *
 * @version 1.1
 *
 * 1.0 - Initial EmailHandler class.
 * 1.1 - Added Object oriented functionality and Javadoc comments.
 *
 */

public class EmailHandler {

    private final String email;
    private final String pass;

    //Constructors
    public EmailHandler(String email, String pass){
        this.email = email;
        this.pass = pass;
    }

    ///Getters
    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }

    //Class Specific Methods

    /**
     * Method to set up the email settings
     *
     * @return returns a Properties object suitable to begin sending emails
     */
    private Properties SetUpEmailHandler() {

        //Create Properties object, this will store information such as the source smtp server, port and others
        Properties prop = new Properties();

        prop.setProperty("mail.smtp.host", "smtp.gmail.com");
        prop.setProperty("mail.smtp.port", "587");
        prop.setProperty("mail.smtp.auth", "true");
        prop.setProperty("mail.smtp.starttls.enable", "true");

        return prop;
    }

    /**
     * Method to send a basic verification email to a user
     *
     * @param prop Takes a Properties object to know where it is meant to be sending information
     * @param to Takes an email to send to
     * @param verificationCode Takes a verification code to be created in a tokenHandler, will be used to confirm
     *                         a user meant to join the app
     */
    private void sendVerification(Properties prop, String to, String verificationCode){

        String email = this.getEmail();
        String pass = this.getPass();

        //Create a session to send this email
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email, pass);
                    }
                });

        //Send Verification Email
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(to)
            );
            message.setSubject("Thank you for creating an account with ProActive");
            message.setText("Thank you for creating an account, type this code into the application to verify your account! " + verificationCode);

            Transport.send(message);

            System.out.println("verification email sent");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        //General account for use with this application, dont worry about non-secure password as is ultimately
        //a throwaway account
        EmailHandler email = new EmailHandler("proactivese13@gmail.com", "f45d09mFAcHr");

        //Configure system to send emails, need to run this at start of each session
        Properties prop = email.SetUpEmailHandler();

        //Send a basic verification email
        email.sendVerification(prop, "owen.tasker@gmail.com", "483RDc");


    }
}
