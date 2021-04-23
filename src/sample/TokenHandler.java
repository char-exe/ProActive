package sample;

import java.util.UUID;

/**
 * Class to hold static methods for creating String based tokens
 *
 * @author Owen Tasker
 *
 * @version 1.1
 *
 * 1.0 - Initial creation of TokenHandler Class
 * 1.1 - Added Static methods and created Javadoc comments
 */
public class TokenHandler {

    /**
     * Method for creating a unique token
     *
     * @return Returns a unique token for confirming a user has intentionally registered for the ProActive app
     */
    public static String createUniqueToken(int num){
        String uniqueRegToken = UUID.randomUUID().toString();

        return uniqueRegToken.substring(0, num);
    }


    public static void main(String[] args) {

        System.out.println(createUniqueToken(6));

    }
}
