package com.mycompany.chatapppart1;

import java.util.Scanner;

/**
 * Main class for the Chat App Part 1.
 *
 * This is a console application.
 */
public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("====================================");
        System.out.println("        CHAT APP - PART 1");
        System.out.println("     REGISTRATION AND LOGIN");
        System.out.println("====================================");

        // Get user's personal details
        System.out.print("Enter your first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine();

        // Get registration details
        System.out.print("Enter a username: ");
        String username = scanner.nextLine();

        System.out.print("Enter a password: ");
        String password = scanner.nextLine();

        System.out.print("Enter your South African cellphone number: ");
        String cellPhone = scanner.nextLine();

        // Create Login object
        Login login = new Login(
                firstName,
                lastName,
                username,
                password,
                cellPhone
        );

        // Register user
        String registrationMessage = login.registerUser();

        System.out.println();
        System.out.println(registrationMessage);

        // Only continue to login if registration was successful
        if (registrationMessage.equals("User registered successfully.")) {

            System.out.println();
            System.out.println("------------ LOGIN ------------");

            System.out.print("Enter your username: ");
            String loginUsername = scanner.nextLine();

            System.out.print("Enter your password: ");
            String loginPassword = scanner.nextLine();

            String loginMessage = login.returnLoginStatus(
                    loginUsername,
                    loginPassword
            );

            System.out.println();
            System.out.println(loginMessage);

        } else {

            System.out.println();
            System.out.println(
                    "Registration was not successful. "
                    + "Please correct your details and try again."
            );
        }

        scanner.close();
    }
}