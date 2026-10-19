package com.mycompany.chatapppart1;

import java.util.Scanner;

/** Console entry point for QuickChat Part 2. */
public class Main {

    private static final String REGISTRATION_SUCCESS = "User registered successfully.";

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            runQuickChat(scanner);
        }
    }

    private static void runQuickChat(Scanner scanner) {
        System.out.println("====================================");
        System.out.println("          QUICKCHAT - PART 2");
        System.out.println("====================================");

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

        Login login = new Login(firstName, lastName, username, password, cellPhone);
        String registrationMessage = login.registerUser();
        System.out.println("\n" + registrationMessage);

        if (!REGISTRATION_SUCCESS.equals(registrationMessage)) {
            System.out.println("Registration was not successful. Please check your details.");
            return;
        }

        System.out.println("\n------------ LOGIN ------------");
        System.out.print("Enter your username: ");
        String loginUsername = scanner.nextLine();
        System.out.print("Enter your password: ");
        String loginPassword = scanner.nextLine();

        boolean loggedIn = login.loginUser(loginUsername, loginPassword);
        System.out.println(login.returnLoginStatus(loginUsername, loginPassword));
        if (!loggedIn) {
            return;
        }

        System.out.println("\nWelcome to QuickChat.");
        runMenu(scanner);
    }

    static void runMenu(Scanner scanner) {
        int numberOfMessages = 0;
        int messagesProcessed = 0;
        boolean messageLimitSet = false;
        boolean running = true;

        while (running) {
            System.out.println("\nPlease select an option:");
            System.out.println("1. Send Messages");
            System.out.println("2. Show recently sent messages");
            System.out.println("3. Quit");
            System.out.print("Enter your choice: ");

            switch (scanner.nextLine()) {
                case "1":
                    if (!messageLimitSet) {
                        numberOfMessages = readPositiveInteger(
                                scanner,
                                "How many messages would you like to enter? ");
                        messageLimitSet = true;
                    }

                    if (messagesProcessed >= numberOfMessages) {
                        System.out.println("You have completed your message limit.");
                        break;
                    }

                    // The outer for loop advances only after a message is processed.
                    for (int messageNumber = messagesProcessed + 1;
                            messageNumber <= numberOfMessages;
                            messageNumber++) {
                        boolean messageProcessed = false;

                        // Invalid input restarts this message without using a slot.
                        while (!messageProcessed) {
                            messageProcessed = processOneMessage(scanner, messageNumber);
                        }
                        messagesProcessed = messageNumber;
                    }

                    System.out.println("\nYou have completed your message limit.");
                    System.out.println("Total messages sent: "
                            + Message.getTotalMessagesSent());
                    break;

                case "2":
                    String sentMessages = Message.printMessages();
                    if (sentMessages.isEmpty()) {
                        System.out.println("No messages have been sent yet.");
                    } else {
                        System.out.println("\nRecently sent messages:");
                        System.out.print(sentMessages);
                    }
                    break;

                case "3":
                    System.out.println("Thank you for using QuickChat. Goodbye!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option. Please select 1, 2 or 3.");
            }
        }
    }

    private static boolean processOneMessage(Scanner scanner, int messageNumber) {
        System.out.println("\nPreparing message " + messageNumber);
        System.out.print("Enter recipient cellphone number: ");
        String recipient = scanner.nextLine();

        Message message = new Message(messageNumber, recipient, "");
        String recipientStatus = message.checkRecipientCell();
        System.out.println(recipientStatus);
        if (!"Cell phone number successfully captured.".equals(recipientStatus)) {
            System.out.println("Please restart this message with a valid number.");
            return false;
        }

        System.out.print("Enter your message: ");
        String messageText = scanner.nextLine();
        message = new Message(messageNumber, recipient, messageText);

        String lengthStatus = message.checkMessageLength();
        System.out.println(lengthStatus);
        if (!"Message ready to send.".equals(lengthStatus)) {
            return false;
        }

        System.out.println("\nMessage ID generated: " + message.getMessageID());
        if (!message.checkMessageID()) {
            System.out.println("Message ID is invalid. Please try again.");
            return false;
        }
        System.out.println("Message Hash: " + message.getMessageHash());
        System.out.println("\nChoose what to do with the message:");
        System.out.println("1. Send Message");
        System.out.println("2. Disregard Message");
        System.out.println("3. Store Message");
        System.out.print("Enter your choice: ");

        int actionChoice;
        switch (scanner.nextLine()) {
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
                System.out.println("Invalid message option. Please choose 1, 2 or 3.");
                return false;
        }

        System.out.println(message.SentMessage(actionChoice));
        if (actionChoice == 1) {
            System.out.println("\nMessage details:");
            System.out.println("Message ID: " + message.getMessageID());
            System.out.println("Message Hash: " + message.getMessageHash());
            System.out.println("Recipient: " + message.getRecipient());
            System.out.println("Message: " + message.getMessageText());
        }
        return true;
    }

    private static int readPositiveInteger(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                int number = Integer.parseInt(input);
                if (number > 0) {
                    return number;
                }
                System.out.println("Please enter a number greater than zero.");
            } catch (NumberFormatException exception) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }
}
