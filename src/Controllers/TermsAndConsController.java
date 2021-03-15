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
        String content = "*NEED TO CORRECT THE FILE READING BIT*";
        /*DataInputStream file = new DataInputStream(new BufferedInputStream(new FileInputStream("Resources/TermsAndConditions.txt")));
        String line;
        for (int i= 0; i< 500; i++){
            line = file.readLine();
            if (line==null){
                break;
            }
            content += line;
        }*/
        termsText.setText(content);
    }

}




