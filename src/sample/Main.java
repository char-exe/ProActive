package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.mail.Session;
import java.time.LocalDate;
import java.time.Month;
import java.util.Properties;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {

        DatabaseHandler dh = new DatabaseHandler("jdbc:sqlite:proactive.db");

        User user = new User("Owen","Tasker", User.Sex.MALE, 72.0f, 85.0f,
                LocalDate.of(1998, Month.APRIL, 25), "owen.tasker@gmail.com", "owen2test");

        dh.createUserEntry(user);

        System.out.println("User Inserted into database");

        //General account for use with this application, dont worry about non-secure password as is ultimately
        //a throwaway account
        EmailHandler email = new EmailHandler("proactivese13@gmail.com", "f45d09mFAcHr");

        //Configure system to send emails, need to run this at start of each session
        Properties prop = email.SetUpEmailHandler();

        //Create email session
        Session session = email.createSession(prop);

        //Send a basic verification email
        email.sendVerification(session, user.getEmail(), "483RDc");
        email.sendGoal(session, user.getEmail(), "Set up user table in database", user.getRealName(), "5R3bn2");
        email.sendGoalCompletion(session, user.getEmail(), "Set up user table in database");

    }
}
