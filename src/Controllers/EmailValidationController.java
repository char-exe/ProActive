package Controllers;

import javafx.event.ActionEvent;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Class for controlling the email validation FXML document
 *
 * @author Owen Tasker
 *
 * @version 1.0
 */
public class EmailValidationController implements Initializable{
    @FXML public Label header;
    @FXML public Button submitButton;
    @FXML public Label sendStatus;
    @FXML public TextField codeInputBox;
    @FXML public Label codeStatus;
    @FXML public Button resendButton;
    @FXML public Button homeButton;

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
     * controller takes over, empty as required for initializable implementation which allows for the initData method
     * to function correctly
     *
     * @param url Called via FXML resource
     * @param resourceBundle Called via FXML resource
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
        DatabaseHandler dh = new DatabaseHandler("jdbc:sqlite:proactive.db");
        long currentTime = System.currentTimeMillis()/1000;
        ResultSet res = dh.getTokenResult(tokenFromUser);
        if (res == null || currentTime > Long.parseLong(res.getString("timeDelay")) + 1800000){
            codeStatus.setText("Code Not Valid Or Has Expired, We have sent another to the specified address");
            dh.deleteToken(initialToken);
            resendVerification();
        }else{
            codeStatus.setText("Code Correct, User Account has been created");
            dh.createUserEntry(user, hash, salt);

            Stage parentScene = (Stage) submitButton.getScene().getWindow();
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/FXML/SplashPage.fxml"));

            Parent splashParent = loader.load();

            Scene sceneSplash = new Scene(splashParent);

            stage.setScene(sceneSplash);

            SplashPageController controller = loader.getController();
            stage.setScene(sceneSplash);

            parentScene.close();
            stage.show();
        }
    }

    /**
     * Method to resend a verification email in the case of request or a failed verification
     */
    @FXML protected void resendVerification(){
        DatabaseHandler dh = new DatabaseHandler("jdbc:sqlite:proactive.db");
        long time = System.currentTimeMillis()/1000;
        initialToken = TokenHandler.createUniqueToken(7);


        EmailHandler eh = new EmailHandler("proactivese13@gmail.com", "f45d09mFAcHr");
        Properties prop = eh.SetUpEmailHandler();
        Session session = eh.createSession(prop);

        System.out.println("Email Sending To: " + user.getEmail());
        dh.addTokenEntry(initialToken, time);
        eh.sendVerification(session, user.getEmail(), initialToken);
    }

    /**
     * Method to cancel registration and send user back to the splash page
     *
     * @param actionEvent This refers to the button that will cause this method to be called
     *
     * @throws IOException Throws an IOException, this primarily occurs when a file is not recognized
     */
    @FXML protected void escapeHome(ActionEvent actionEvent) throws IOException {
        Stage parentScene = (Stage) homeButton.getScene().getWindow();
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/SplashPage.fxml"));

        Parent splashParent = loader.load();

        Scene sceneParent = new Scene(splashParent);

        stage.setScene(sceneParent);

        SplashPageController controller = loader.getController();
        stage.setScene(sceneParent);

        parentScene.close();
        stage.show();
    }

    public static void main(String[] args) {
    }
}
