package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Entrypoint for the ProActive app.
 *
 * @author ??
 *
 * @version 1.0
 *
 * 1.0 - First working version.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../FXML/SplashPage.fxml"));
        primaryStage.setTitle("ProActive");
        primaryStage.setScene(new Scene(root));

        primaryStage.setMinWidth(350);
        primaryStage.setMinHeight(300);
        primaryStage.setMaxWidth(550);
        primaryStage.setMaxHeight(500);

        primaryStage.show();

        //VBox content = FXMLLoader.load(getClass().getResource("SplashPage.fxml"));
        //root.setCenter(content);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
