package sample;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailHandler {

    //General account for use with this application, dont worry about non-secure password as  ultimately
    //a throwaway account
    private static final String proActiveEmail = "proactivese13@gmail.com";
    private static final String proActivePass = "f45d09mFAcHr";

    private static Properties SetUpEmailHandler() {
        // Get system properties
        Properties prop = new Properties();

        prop.setProperty("mail.smtp.host", "smtp.gmail.com");
        prop.setProperty("mail.smtp.port", "587");
        prop.setProperty("mail.smtp.auth", "true");
        prop.setProperty("mail.smtp.starttls.enable", "true");

        return prop;
    }

    private static void sendVerification(Properties prop, String to, String verificationCode){

        //Create a session to send this email
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(proActiveEmail, proActivePass);
                    }
                });

        //Send Verification Email
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(proActiveEmail));
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

        Properties prop = SetUpEmailHandler();
        sendVerification(prop, "owen.tasker@gmail.com", "483RDc");


    }
}
