package com.mycompany.chatapppart1;

/**
 * Login class for the Chat App Part 1 PoE.
 *
 * This class handles:
 * - Username validation
 * - Password validation
 * - Cell phone validation
 * - User registration
 * - User login
 * - Login status messages
 */
public class Login {

    // User registration details
    private String firstName;
    private String lastName;
    private String registeredUsername;
    private String registeredPassword;
    private String registeredCellPhone;

    /**
     * Constructor.
     */
    public Login() {
    }

    /**
     * Constructor used to store the user's registration details.
     */
    public Login(String firstName, String lastName,
                 String username, String password,
                 String cellPhone) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.registeredUsername = username;
        this.registeredPassword = password;
        this.registeredCellPhone = cellPhone;
    }

    /**
     * Checks whether the username contains an underscore
     * and is no more than five characters long.
     *
     * @param username username to check
     * @return true if valid, otherwise false
     */
    public boolean checkUserName(String username) {

        return username != null
                && username.contains("_")
                && username.length() <= 5;
    }

    /**
     * Checks whether the password:
     * - Has at least eight characters
     * - Contains a capital letter
     * - Contains a number
     * - Contains a special character
     *
     * @param password password to check
     * @return true if valid, otherwise false
     */
    public boolean checkPasswordComplexity(String password) {

        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasCapitalLetter = false;
        boolean hasNumber = false;
        boolean hasSpecialCharacter = false;

        for (int i = 0; i < password.length(); i++) {

            char character = password.charAt(i);

            if (Character.isUpperCase(character)) {
                hasCapitalLetter = true;
            }

            if (Character.isDigit(character)) {
                hasNumber = true;
            }

            if (!Character.isLetterOrDigit(character)) {
                hasSpecialCharacter = true;
            }
        }

        return hasCapitalLetter
                && hasNumber
                && hasSpecialCharacter;
    }

    /**
     * Checks whether the South African cellphone number
     * contains the international country code.
     *
     * Example:
     * +27838968976
     *
     * Regex adapted from the cellphone-number validation
     * approach described in the QuickBlox chat application
     * architecture resource supplied in the PoE.
     *
     * Source:
     * QuickBlox, "Beginner's Guide to Chat App Architecture".
     *
     * @param cellPhone cellphone number to check
     * @return true if valid, otherwise false
     */
    public boolean checkCellPhoneNumber(String cellPhone) {

        if (cellPhone == null) {
            return false;
        }

        String regex = "^\\+27\\d{9}$";

        return cellPhone.matches(regex);
    }

    /**
     * Registers the user and returns the appropriate
     * registration message.
     *
     * @return registration status message
     */
    public String registerUser() {

        if (!checkUserName(registeredUsername)) {

            return "Username is not correctly formatted; "
                    + "please ensure that your username contains "
                    + "an underscore and is no more than five "
                    + "characters in length.";
        }

        if (!checkPasswordComplexity(registeredPassword)) {

            return "Password is not correctly formatted; "
                    + "please ensure that the password contains "
                    + "at least eight characters, a capital letter, "
                    + "a number, and a special character.";
        }

        if (!checkCellPhoneNumber(registeredCellPhone)) {

            return "Cell phone number incorrectly formatted "
                    + "or does not contain international code.";
        }

        return "User registered successfully.";
    }

    /**
     * Checks whether the entered username and password
     * match the registered username and password.
     *
     * @param username entered username
     * @param password entered password
     * @return true if login details are correct
     */
    public boolean loginUser(String username, String password) {

        return registeredUsername != null
                && registeredPassword != null
                && registeredUsername.equals(username)
                && registeredPassword.equals(password);
    }

    /**
     * Returns the appropriate login status message.
     *
     * @param username entered username
     * @param password entered password
     * @return login status message
     */
    public String returnLoginStatus(String username, String password) {

        if (loginUser(username, password)) {

            return "Welcome " + firstName + ", "
                    + lastName
                    + " it is great to see you again.";
        }

        return "Username or password incorrect, please try again.";
    }
}