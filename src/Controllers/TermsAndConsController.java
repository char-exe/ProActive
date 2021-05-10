package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;
import java.util.Scanner;

/**
 * Class to control the Terms and conditions FXML file
 *
 * @author ??
 *
 * @version 1.0
 */
public class TermsAndConsController {

    @FXML public Label termsText;

    /**
     * Method to be called after all FXML elements have been loaded, will be used to modify contents of FXML elements
     *
     * @throws IOException Throws an IOException whenever a file is potentially not where it is expected to be
     */
    @FXML protected void initialize() throws IOException {
        StringBuilder content = new StringBuilder(10000000);

        File termsAndConds = new File("src/Resources/TermsAndConditions.txt");
        Scanner reader = new Scanner(termsAndConds);
        while (reader.hasNextLine()){
            content.append(reader.nextLine()).append("\n");
        }
        reader.close();
        termsText.setText(content.toString());

    }

}




