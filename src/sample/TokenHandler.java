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
     *
     * @return Returns a 7 digit token for confirming a user has intentionally registered for the ProActive app
     */
    public static String createRegistrationToken(){
        String uniqueRegToken = UUID.randomUUID().toString();

        return uniqueRegToken.substring(0, 7);
    }

    /**
     *
     * @return Returns a 5 digit token for inviting existing users of the app to join a group goal
     */
    public static String createGroupInviteToken(){
        String uniqueGroupInvToken = UUID.randomUUID().toString();

        return uniqueGroupInvToken.substring(0,5);
    }

    public static void main(String[] args) {

        System.out.println(createRegistrationToken());
        System.out.println(createGroupInviteToken());

    }
}
