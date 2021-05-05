package sample;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit testing for TokenHandler.
 *
 * @author Samuel Scarfe
 */

class TokenHandlerTest {

    @Test
    void zeroNum() {
        assertThrows(IllegalArgumentException.class, () -> TokenHandler.createUniqueToken(0));
    }

    @Test
    void negativeNum() {
        assertThrows(IllegalArgumentException.class, () -> TokenHandler.createUniqueToken(-1));
    }

    @Test
    void successfulNum() {
        assertEquals(1, TokenHandler.createUniqueToken(1).length());
    }

}