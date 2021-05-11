package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.DatabaseHandler;
import sample.EmailHandler;
import sample.TokenHandler;
import sample.User;

import javax.mail.Session;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * Class for controlling the email validation FXML document
 *
 * @author Owen Tasker
 *
 * @version 1.1
 *
 * 1.0 - First working version
 * 1.1 - Small refactor to compare times in LocalDateTime rather than System.getCurrentTimeMillis
 */
public class EmailValidationController implements Initializable{
    @FXML public Label header;
    @FXML public Button submitButton;
    @FXML public Label sendStatus;
    @FXML public TextField codeInputBox;
    @FXML public Label codeStatus;
    @FXML public Button resendButton;
    @FXML public Button homeButton;

    private DatabaseHandler dh;
    private User user;
    private String initialToken;
    private byte[] hash;
    private byte[] salt;

    /**
     * Pseudo constructor for FXML, allows for passing information between scenes
     *
     * @param user  Takes in the persistent user object, will be used to create a database object if validation is
     *              successful
     * @param initialToken The first validation email was sent prior to this scene opening, this takes in the token
     *                     that was used for that purpose
     * @param hash The password hash
     * @param salt The password salt
     */
    public void initData(User user, String initialToken, byte[] hash, byte[] salt){
        this.user = user;
        this.initialToken = initialToken;
        this.hash = hash;
        this.salt = salt;
    }

    /**
     * Method for stuff to run after FXML elements are initialized, allows for events to be called as soon as this
     * controller takes over.
     *
     * @param url Called via FXML resource
     * @param resourceBundle Called via FXML resource
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dh = DatabaseHandler.getInstance();
    }

    /**
     * Method to handle the submit button action, after clicking the submit button, if the token matches the
     * expected value, the application will create a user object in the database, close the current window and reopen
     * the splash page.
     *
     * @throws SQLException throws an SQLException, this primarily occurs when a SQL query is incorrectly typed
     * @throws IOException throws an IOException, this primarily occurs when a file is not recognized
     */
    @FXML protected void pushSubmit() throws SQLException, IOException {
        String tokenFromUser = codeInputBox.getText();

        LocalDateTime sentTime = dh.getTokenResult(tokenFromUser);

        if (sentTime == null) {
            codeStatus.setText("Code Invalid, we have sent another to the specified address");
            dh.deleteToken(initialToken);
            resendVerification();
        }
        else if (LocalDateTime.now().isAfter(sentTime.plusMinutes(30))) {
            codeStatus.setText("Code Expired, we have sent another to the specified address");
            dh.deleteToken(initialToken);
            resendVerification();
        }
        else {
            codeStatus.setText("Code Correct, User Account has been created");
            dh.deleteToken(initialToken);
            dh.createUserEntry(user, hash, salt);

            escapeHome();
        }
    }

    /**
     * Method to resend a verification email in the case of request or a failed verification
     */
    @FXML protected void resendVerification(){
        DatabaseHandler dh = DatabaseHandler.getInstance();
        initialToken = TokenHandler.createUniqueToken(7);

        EmailHandler eh = EmailHandler.getInstance();
        Session session = eh.createSession();

        System.out.println("Email Sending To: " + user.getEmail());
        dh.addTokenEntry(initialToken);
        eh.sendVerification(session, user.getEmail(), initialToken);
    }

    /**
     * Method to cancel registration and send user back to the splash page
     *
     * @throws IOException Throws an IOException, this primarily occurs when a file is not recognized
     */
    @FXML protected void escapeHome() throws IOException {
        Stage parentScene = (Stage) homeButton.getScene().getWindow();
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/SplashPage.fxml"));

        Parent splashParent = loader.load();
        Scene sceneParent = new Scene(splashParent);
        stage.setScene(sceneParent);

        parentScene.close();
        stage.show();
    }
}
