package com.mycompany.chatapppart1;

import java.util.Scanner;

/**
 * Main class for QuickChat.
 * Part 1 registration and login are retained.
 * Part 2 adds message functionality.
 */
public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("====================================");
        System.out.println("        CHAT APP - PART 1");
        System.out.println("     REGISTRATION AND LOGIN");
        System.out.println("====================================");

        // Registration details
        System.out.print("Enter your first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine();

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

        // Continue only if registration succeeds
        if (!registrationMessage.equals("User registered successfully.")) {
            System.out.println(
                    "Registration was not successful. "
                    + "Please correct your details and try again."
            );
            scanner.close();
            return;
        }

        // Login
        System.out.println("\n------------ LOGIN ------------");

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

        // Only logged-in users can access QuickChat
        if (!login.loginUser(loginUsername, loginPassword)) {
            scanner.close();
            return;
        }

        System.out.println("\nWelcome to QuickChat.");

        boolean running = true;
        int numberOfMessages = -1;
        boolean messageLimitSet = false;

        while (running) {

            System.out.println("\nPlease select an option:");
            System.out.println("1. Send Messages");
            System.out.println("2. Show recently sent messages");
            System.out.println("3. Quit");
            System.out.print("Enter your choice: ");

            String menuChoice = scanner.nextLine();

            switch (menuChoice) {

                case "1":

                    // Ask how many messages the user wants to enter.
                    if (!messageLimitSet) {
                        numberOfMessages = readPositiveInteger(
                                scanner,
                                "How many messages would you like to enter? "
                        );

                        messageLimitSet = true;
                    }

                    int messagesProcessed = 0;

                    while (messagesProcessed < numberOfMessages) {

                        System.out.println(
                                "\nPreparing message "
                                + (messagesProcessed + 1)
                                + " of "
                                + numberOfMessages
                        );

                        System.out.print("Enter recipient cellphone number: ");
                        String recipient = scanner.nextLine();

                        // Validate recipient number
                        Message message = new Message(
                                messagesProcessed + 1,
                                recipient,
                                ""
                        );

                        String recipientStatus = message.checkRecipientCell();
                        System.out.println(recipientStatus);

                        if (!recipientStatus.equals(
                                "Cell phone number successfully captured.")) {
                            System.out.println(
                                    "Please restart this message with a valid number."
                            );
                            continue;
                        }

                        System.out.print("Enter your message: ");
                        String messageText = scanner.nextLine();

                        // Recreate the message with the actual text
                        message = new Message(
                                messagesProcessed + 1,
                                recipient,
                                messageText
                        );

                        String lengthStatus = message.checkMessageLength();
                        System.out.println(lengthStatus);

                        if (!lengthStatus.equals("Message ready to send.")) {
                            continue;
                        }

                        System.out.println("\nMessage ID generated: "
                                + message.getMessageID());

                        System.out.println("Message Hash: "
                                + message.getMessageHash());

                        System.out.println("\nChoose what to do with the message:");
                        System.out.println("1. Send Message");
                        System.out.println("2. Disregard Message");
                        System.out.println("3. Store Message");
                        System.out.print("Enter your choice: ");

                        String action = scanner.nextLine();

                        int actionChoice;

                        switch (action) {
                            case "1":
                                actionChoice = 1;
                                break;
                            case "2":
                                actionChoice = 2;
                                break;
                            case "3":
                                actionChoice = 3;
                                break;
                            default:
                                System.out.println("Invalid message option.");
                                continue;
                        }

                        String actionResult = message.SentMessage(actionChoice);
                        System.out.println(actionResult);

                        if (actionChoice == 1) {
                            System.out.println("\nMessage details:");
                            System.out.println("Message ID: "
                                    + message.getMessageID());
                            System.out.println("Message Hash: "
                                    + message.getMessageHash());
                            System.out.println("Recipient: "
                                    + message.getRecipient());
                            System.out.println("Message: "
                                    + message.getMessageText());
                        }

                        messagesProcessed++;
                    }

                    System.out.println("\nYou have completed your message limit.");
                    System.out.println("Total messages sent: "
                            + new Message(0, "", "").returnTotalMessages());

                    break;

                case "2":
                    System.out.println("Coming Soon.");
                    break;

                case "3":
                    System.out.println("Thank you for using QuickChat. Goodbye!");
                    running = false;
                    break;

                default:
                    System.out.println(
                            "Invalid option. Please select 1, 2 or 3."
                    );
            }
        }

        scanner.close();
    }

    // Reads a positive whole number from the user.
    private static int readPositiveInteger(
            Scanner scanner,
            String prompt) {

        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();

            try {
                int number = Integer.parseInt(input);

                if (number > 0) {
                    return number;
                }

                System.out.println("Please enter a number greater than zero.");

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }
}