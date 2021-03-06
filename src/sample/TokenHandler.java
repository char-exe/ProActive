package sample;

import java.util.Random;
import java.util.UUID;

/**
 * Class to hold static methods for creating String based tokens
 *
 * @author Owen Tasker
 * @author Samuel Scarfe
 * @author Evan Clayton
 *
 * @version 1.3
 *
 * 1.0 - Initial creation of TokenHandler Class
 * 1.1 - Added Static methods and created Javadoc comments
 * 1.2 - Added exception.
 * 1.3 - Added overloaded method that uses a random int rather than an int from an argument.
 */
public class TokenHandler {

    /**
     * Method for creating a unique token
     *
     * @param num An integer number
     *
     * @return Returns a unique token for confirming a user has intentionally registered for the ProActive app
     */
    public static String createUniqueToken(int num){
        if (num <= 0) {
            throw new IllegalArgumentException();
        }

        String uniqueRegToken = UUID.randomUUID().toString();

        return uniqueRegToken.substring(0, num);
    }
}
