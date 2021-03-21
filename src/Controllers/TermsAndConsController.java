package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;


public class TermsAndConsController {

    @FXML public Label termsText;

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




