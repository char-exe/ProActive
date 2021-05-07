package sample;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit testing for EmailHandler. Tests only that instantiation methods do not return null. All other functionality
 * is most easily tested by passing real data through the live system.
 *
 * @author Samuel Scarfe
 */
class EmailHandlerTest {

    @Test
    void notNullGetInstance() {
        assertNotNull(EmailHandler.getInstance());
    }

    @Test
    void notNullCreateSession() {
        EmailHandler emailHandler = EmailHandler.getInstance();
        assertNotNull(emailHandler.createSession());
    }
}