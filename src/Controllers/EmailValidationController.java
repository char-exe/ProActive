package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
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
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.ResourceBundle;

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

    public void initData(User user, String initialToken, byte[] hash, byte[] salt){
        this.user = user;
        this.initialToken = initialToken;
        this.hash = hash;
        this.salt = salt;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML protected void pushSubmit() throws SQLException, IOException {
        String tokenFromUser = codeInputBox.getText();
        DatabaseHandler dh = new DatabaseHandler();
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

    @FXML protected void resendVerification(){
        DatabaseHandler dh = new DatabaseHandler();
        long time = System.currentTimeMillis()/1000;
        initialToken = TokenHandler.createUniqueToken(7);


        EmailHandler eh = new EmailHandler();
        Properties prop = eh.SetUpEmailHandler();
        Session session = eh.createSession(prop);

        System.out.println("Email Sending To: " + user.getEmail());
        dh.addTokenEntry(initialToken, time);
        eh.sendVerification(session, user.getEmail(), initialToken);
    }

    public void escapeHome(ActionEvent actionEvent) throws IOException {
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
