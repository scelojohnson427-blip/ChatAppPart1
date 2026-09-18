package com.mycompany.chatapppart1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Login class.
 */
public class LoginTest {

    /**
     * Test username that is correctly formatted.
     *
     * Test data: kyl_1
     * Expected: true
     */
    @Test
    public void testCheckUserNameCorrect() {

        Login login = new Login();

        assertTrue(login.checkUserName("kyl_1"));
    }

    /**
     * Test username that is incorrectly formatted.
     *
     * Test data: kyle!!!!!!!
     * Expected: false
     */
    @Test
    public void testCheckUserNameIncorrect() {

        Login login = new Login();

        assertFalse(login.checkUserName("kyle!!!!!!!"));
    }

    /**
     * Test password that meets complexity requirements.
     *
     * Test data: Ch&&sec@ke99
     * Expected: true
     */
    @Test
    public void testCheckPasswordCorrect() {

        Login login = new Login();

        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99"));
    }

    /**
     * Test password that does not meet complexity requirements.
     *
     * Test data: password
     * Expected: false
     */
    @Test
    public void testCheckPasswordIncorrect() {

        Login login = new Login();

        assertFalse(login.checkPasswordComplexity("password"));
    }

    /**
     * Test correctly formatted cellphone number.
     *
     * Test data: +27838968976
     * Expected: true
     */
    @Test
    public void testCheckCellPhoneCorrect() {

        Login login = new Login();

        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    /**
     * Test incorrectly formatted cellphone number.
     *
     * Test data: 08966553
     * Expected: false
     */
    @Test
    public void testCheckCellPhoneIncorrect() {

        Login login = new Login();

        assertFalse(login.checkCellPhoneNumber("08966553"));
    }

    /**
     * Test successful login.
     */
    @Test
    public void testLoginSuccessful() {

        Login login = new Login(
                "John",
                "Doe",
                "kyl_1",
                "Ch&&sec@ke99",
                "+27838968976"
        );

        assertTrue(
                login.loginUser(
                        "kyl_1",
                        "Ch&&sec@ke99"
                )
        );
    }

    /**
     * Test failed login.
     */
    @Test
    public void testLoginFailed() {

        Login login = new Login(
                "John",
                "Doe",
                "kyl_1",
                "Ch&&sec@ke99",
                "+27838968976"
        );

        assertFalse(
                login.loginUser(
                        "wrong",
                        "wrong"
                )
        );
    }

    /**
     * Test successful login message.
     */
    @Test
    public void testReturnLoginStatusSuccessful() {

        Login login = new Login(
                "John",
                "Doe",
                "kyl_1",
                "Ch&&sec@ke99",
                "+27838968976"
        );

        assertEquals(
                "Welcome John, Doe it is great to see you again.",
                login.returnLoginStatus(
                        "kyl_1",
                        "Ch&&sec@ke99"
                )
        );
    }

    /**
     * Test failed login message.
     */
    @Test
    public void testReturnLoginStatusFailed() {

        Login login = new Login(
                "John",
                "Doe",
                "kyl_1",
                "Ch&&sec@ke99",
                "+27838968976"
        );

        assertEquals(
                "Username or password incorrect, please try again.",
                login.returnLoginStatus(
                        "wrong",
                        "wrong"
                )
        );
    }
}