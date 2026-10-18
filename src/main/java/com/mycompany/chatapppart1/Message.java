package com.mycompany.chatapppart1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Message class for QuickChat Part 2.
 * Handles message validation, hashes, sending and storage.
 */
public class Message {

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;

    private static int totalMessagesSent = 0;

    private static final List<Message> sentMessages = new ArrayList<>();

    private static final List<Message> storedMessages = new ArrayList<>();

    public Message(int messageNumber, String recipient, String messageText) {
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();
    }

    // Generate a random ten-digit message ID.
    private String generateMessageID() {
        Random random = new Random();
        long number = 1000000000L
                + (long) (random.nextDouble() * 9000000000L);

        return String.valueOf(number);
    }

    // Check that the message ID is no longer than ten characters.
    public boolean checkMessageID() {
        return messageID != null
                && messageID.length() <= 10
                && messageID.matches("\\d+");
    }

    // Validate the South African international recipient number.
    public String checkRecipientCell() {
        if (recipient != null && recipient.matches("^\\+27\\d{9}$")) {
            return "Cell phone number successfully captured.";
        }

        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    // Validate the message length.
    public String checkMessageLength() {
        if (messageText == null) {
            return "Please enter a message.";
        }

        if (messageText.length() > 250) {
            int excess = messageText.length() - 250;

            return "Message exceeds 250 characters by "
                    + excess + "; please reduce the size.";
        }

        return "Message ready to send.";
    }

    // Generate the message hash.
    public String createMessageHash() {
        if (messageID == null || messageText == null
                || messageText.trim().isEmpty()) {
            return "";
        }

        String firstTwoDigits = messageID.substring(0, 2);

        String[] words = messageText.trim().split("\\s+");

        String firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "");
        String lastWord = words[words.length - 1]
                .replaceAll("[^a-zA-Z0-9]", "");

        return (firstTwoDigits + ":" + messageNumber + ":"
                + firstWord + lastWord).toUpperCase();
    }

    // Send, disregard or store the message.
    public String SentMessage(int choice) {
        switch (choice) {
            case 1:
                sentMessages.add(this);
                totalMessagesSent++;
                return "Message successfully sent.";

            case 2:
                return "Press 0 to delete the message.";

            case 3:
                storedMessages.add(this);

                if (storeMessage()) {
                    return "Message successfully stored.";
                }

                return "Message could not be stored.";

            default:
                return "Invalid option. Please select 1, 2 or 3.";
        }
    }

    // Return all messages sent during this run.
    public String printMessages() {
        StringBuilder output = new StringBuilder();

        for (Message message : sentMessages) {
            output.append("Message ID: ")
                    .append(message.messageID)
                    .append("\nMessage Hash: ")
                    .append(message.messageHash)
                    .append("\nRecipient: ")
                    .append(message.recipient)
                    .append("\nMessage: ")
                    .append(message.messageText)
                    .append("\n\n");
        }

        return output.toString();
    }

    // Return the total number of messages sent.
    public int returnTotalMessages() {
        return totalMessagesSent;
    }

    // Store messages in a JSON file.
    public boolean storeMessage() {
        try (FileWriter writer = new FileWriter("stored_messages.json", true)) {

            String safeMessage = messageText
                    .replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r");

            writer.write("{");
            writer.write("\"messageID\":\"" + messageID + "\",");
            writer.write("\"messageHash\":\"" + messageHash + "\",");
            writer.write("\"recipient\":\"" + recipient + "\",");
            writer.write("\"message\":\"" + safeMessage + "\"");
            writer.write("}\n");

            return true;

        } catch (IOException e) {
            System.out.println("Error storing message: " + e.getMessage());
            return false;
        }
    }

    // Getters for testing and displaying message details.
    public String getMessageID() {
        return messageID;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageHash() {
        return messageHash;
    }
}