package sample;

import java.time.LocalDateTime;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

/**
 *  A class to allow for the emailing of codes and other information to users of the ProActive app
 *
 * @author Owen Tasker
 *
 * @version 1.2
 *
 * 1.0 - Initial EmailHandler class.
 * 1.1 - Added Object oriented functionality and Javadoc comments.
 * 1.2 - Added additional email methods as well as finished documenting all methods with Javadoc
 *
 */
public class EmailHandler {

    private final String email;
    private final String pass;

    /**
     * Create an EmailHandler object, this will store a sending email and the password
     *
     * @param email Email Address that will be used to send from
     * @param pass  Password of the aforementioned email address
     */
    public EmailHandler(String email, String pass){
        this.email = email;
        this.pass = pass;
    }

    /**
     * Email accessor method
     *
     * @return Returns the Users email field
     */
    public String getEmail() {
        return email;
    }

    /**
     * Pass accessor method
     *
     * @return Returns the Users password field
     */
    public String getPass() {
        return pass;
    }

    /**
     * Method to set up the email settings
     *
     * @return returns a Properties object suitable to begin sending emails
     */
    public Properties SetUpEmailHandler() {

        //Create Properties object, this will store information such as the source smtp server, port and others
        Properties prop = new Properties();

        prop.setProperty("mail.smtp.host", "smtp.gmail.com");
        prop.setProperty("mail.smtp.port", "587");
        prop.setProperty("mail.smtp.auth", "true");
        prop.setProperty("mail.smtp.starttls.enable", "true");

        return prop;
    }

    /**
     * Method to begin a session, this is required for all mail operations
     *
     * @param prop Takes in the system properties and properties about the smtp server
     *
     * @return returns a Session object, this is a persistent object required in all mail sending methods
     */
    public Session createSession(Properties prop){
        String email = this.getEmail();
        String pass = this.getPass();

        //Create a session to send this email
        return Session.getInstance(prop,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email, pass);
                    }
                });
    }

    /**
     * Method to send a basic verification email to a user
     *
     * @param to               Takes an email to send to
     * @param verificationCode Takes a verification code to be created in a tokenHandler, will be used to confirm
     *                         a user meant to join the app
     */
    public void sendVerification(Session session, String to, String verificationCode){

        //Send Verification Email
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            System.out.println(to);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("Thank you for creating an account with ProActive");
            message.setContent(
            "<head>" +
            "   <style>" +
            "       h1{" +
            "           background-color:#CCCCCC;" +
            "           border-radius:20px;" +
            "           font-size:40px;" +
            "           text-align:center;" +
            "           padding:25px;" +
            "           color:black;" +
            "       }" +
            "       p{" +
            "           background-color:#CCCCCC;" +
            "           border-radius:20px;" +
            "           font-size:20px;" +
            "           text-align:center;" +
            "           padding:20px;" +
            "           color:black;" +
            "       }" +
            "   </style>" +
            "</head>" +
            "<body>" +
            "   <h1>ProActive</h1>" +
            "   <p>Thanks For Signing Up With ProActive, Please Enter The Code Below To Verify Your Account</p>" +
            "   <p><strong> " + verificationCode + " </strong></p>" +
            "</body>",
            "text/html");

            Transport.send(message);

            System.out.println("verification email sent");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to send a user an invite to a goal in the form of a token in an email
     *
     * @param session     Takes a session object, this is required to send any emails
     * @param to          Takes a To Address, this is the address that the email will be sent to
     * @param inviterName Takes the name of the person/group who invited the user to this goal
     * @param goalToken   Takes a randomly generated token, this will be generated by the tokenGenerator class
     *                    which will be implemented in the future, may need to be changed to a Token object once
     *                    it has been fully implemented
     */
    public void sendGoal(Session session, String to, String goalName, String inviterName, String goalToken){

        //Send an email as an invite to join a goal
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            System.out.println(to);
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(to)
            );
            message.setSubject("You Have Been Invited To Join A Goal!");
            message.setText("You have been invited to join the goal \"" + goalName + "\" by " + inviterName +
                            "\n\n" +
                            "Please copy the following code into the Proactive App to join this goal!  " + goalToken);

            Transport.send(message);

            System.out.println("Goal Invite sent");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to send an email whenever a goal is completed by the user
     *
     * @param session     Takes a session object, this is required to send any emails
     * @param to          Takes a To Address, this is the address that the email will be sent to
     * @param goalName    Takes the goal that has been completed
     */
    public void sendGoalCompletion(Session session, String to, String goalName){
        //Send an email as an invite to join a goal
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(to)
            );
            message.setSubject("You Have Completed A Goal!");
            message.setText("You have successfully completed the goal \"" + goalName + "\" Congratulations!");

            Transport.send(message);

            System.out.println("Goal Completion sent");

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

        //Create email session
        Session session = email.createSession(prop);

        //Send a basic verification email
        email.sendVerification(session, "owen.tasker@gmail.com", TokenHandler.createUniqueToken(5));

    }
}
