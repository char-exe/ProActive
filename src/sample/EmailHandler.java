package sample;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

/**
 *  A class to allow for the emailing of codes and other information to users of the ProActive app
 *
 * @author Owen Tasker
 * @author Samuel Scarfe
 * @author Evan Clayton
 *
 * @version 1.3
 *
 * 1.0 - Initial EmailHandler class.
 * 1.1 - Added Object oriented functionality and Javadoc comments.
 * 1.2 - Added additional email methods as well as finished documenting all methods with Javadoc
 * 1.3 - Made email and password static final values, no longer passed as arguments. Removed getters for
 *       email and password.
 * 1.4 - Minor refactor to enforce the Singleton pattern, thereby ensuring that only one instance is created and
 *       that there is only one smtp connection.
 * 1.5 - Added method for sending emails for group goal completion. Changed many email methods to accept a goal object
 *       rather than a goalName as goalName does not exist, the goal.getMessageFragment() method is used instead
 *       of goalName.
 *
 */
public class EmailHandler {

    private static final EmailHandler INSTANCE = new EmailHandler();
    private static final String EMAIL = "proactivese13@gmail.com";
    private static final String PASS = "f45d09mFAcHr";

    /**
     * Private default constructor. Enforces the Singleton pattern.
     */
    private EmailHandler(){
    }

    /**
     * Static method to get the single instance of EmailHandler.
     *
     * @return the EmailHandler instance.
     */
    public static EmailHandler getInstance() {
        return INSTANCE;
    }

    /**
     * Private helper method to set up the properties of the email handler.
     *
     * @return returns a Properties object suitable to begin sending emails
     */
    private static Properties setUpEmailHandler() {

        //Create Properties object, this will store information such as the source smtp server, port and others
        Properties prop = new Properties();

        prop.setProperty("mail.smtp.host", "smtp.gmail.com");
        prop.setProperty("mail.smtp.port", "587");
        prop.setProperty("mail.smtp.auth", "true");
        prop.setProperty("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        return prop;
    }

    /**
     * Method to begin a session, this is required for all mail operations
     *
     * @return returns a Session object, this is a persistent object required in all mail sending methods
     */
    public Session createSession(){

        //Create a session to send this email
        return Session.getInstance(setUpEmailHandler(),
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(EMAIL, PASS);
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
            message.setFrom(new InternetAddress(EMAIL));
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
     * Method to send a Group Invite email to a user
     *
     * @param to               Takes an email to send to
     * @param verificationCode Takes a verification code to be created in a tokenHandler, will be used to confirm
     *                         a user meant to join the app
     */
    public void sendGroupInvite(Session session, String to, String groupName, String verificationCode){

        //Send Verification Email
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL));
            System.out.println(to);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("You Have Been Invited To A Group With Proactive");
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
                            "   <p>You Have been invited to the group <strong> " + groupName + "</strong> please enter " +
                            "   the code below in the 'Groups' panel of the ProActive App</p>" +
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
            message.setFrom(new InternetAddress(EMAIL));
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
     * @param goal        Takes the goal that has been completed
     */
    public void sendGoalCompletion(Session session, String to, Goal goal){
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(to)
            );
            message.setSubject("You Have Completed A Goal!");
            message.setText("You have successfully completed the goal \"" + goal.toString()
                    + "\" Congratulations!");

            Transport.send(message);

            System.out.println("Goal Completion sent");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to send an email to a group member when a different member has completed a group goal.
     *
     * @param session     Takes a session object, this is required to send any emails.
     * @param to          Takes a To Address, this is the address that the email will be sent to.
     * @param goal        Takes the goal that has been completed.
     * @param username        User that completed the goal.
     */
    public void sendGroupGoalCompletion(Session session, String to, Goal goal, String username){
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(to)
            );
            message.setSubject(username + " Has Completed A Group Goal!");
            message.setText(username + " has successfully completed the group goal \""
                    + goal.toString() + "\" Wish Them Congratulations!");

            Transport.send(message);

            System.out.println("Goal Completion sent");

        }
        catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to send an email with CSS formatting to a user that has forgotten their password.
     *
     * @param session Takes a session object, this is required to send any emails.
     * @param to      Takes a To Address, this is the address that the email will be sent to.
     */
    public void sendRecoveryEmailCSS(Session session, String to){
        String token = TokenHandler.createUniqueToken(8);
        String username = DatabaseHandler.getInstance().getUsernameFromEmail(to);
        int userID = DatabaseHandler.getInstance().getUserIDFromUsername(username);
        DatabaseHandler.getInstance().insertRecoveryCode(userID, token);

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL));
            System.out.println(to);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("ProActive Password Recovery");
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
                            "   <p>You have requested a recovery code for resetting your password. " +
                            "Please enter the code below in the ProActive App.</p>" +
                            "   <p><strong> " + token + " </strong></p>" +
                            "</body>",
                    "text/html");

            Transport.send(message);

            System.out.println("Recovery Email Sent");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to send an email with CSS formatting to a user that has forgotten their password.
     *
     * @param session Takes a session object, this is required to send any emails.
     * @param to      Takes a To Address, this is the address that the email will be sent to.
     */
    public void sendUsernameRecoveryEmailCSS(Session session, String to){
        String username = DatabaseHandler.getInstance().getUsernameFromEmail(to);

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL));
            System.out.println(to);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("ProActive Username Recovery");
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
                            "   <p>You have requested the username for your account in the ProActive App.</p>" +
                            "   <p><strong> " + username + " </strong></p>" +
                            "</body>",
                    "text/html");

            Transport.send(message);

            System.out.println("Recovery Email Sent");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
