package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;

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
        String content = "";
        DataInputStream file = new DataInputStream(new BufferedInputStream(new FileInputStream("src/Resources/TermsAndConditions.txt")));
        String line;
        for (int i= 0; i< 259; i++){
            line = file.readLine() + "\n";
            if (line==null){
                break;
            }
            content += line;
        }
        termsText.setText(content);
    }

}




