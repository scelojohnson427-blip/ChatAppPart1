package com.mycompany.chatapppart1;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Handles QuickChat message validation, identification, hashing and storage. */
public class Message {

    private final String messageID;
    private final int messageNumber;
    private final String recipient;
    private final String messageText;
    private final String messageHash;

    private static int totalMessagesSent;
    private static final List<Message> SENT_MESSAGES = new ArrayList<>();

    public Message(int messageNumber, String recipient, String messageText) {
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();
    }

    private String generateMessageID() {
        Random random = new Random();
        long number = 1_000_000_000L
                + (long) (random.nextDouble() * 9_000_000_000L);
        String counterPrefix = String.format("%02d", Math.floorMod(messageNumber, 100));
        String randomSuffix = String.valueOf(number).substring(2);
        return (counterPrefix + randomSuffix).substring(0, 10);
    }

    public boolean checkMessageID() {
        return messageID != null
                && messageID.length() == 10
                && messageID.matches("\\d{10}");
    }

    public String checkRecipientCell() {
        if (recipient != null && recipient.matches("^\\+27\\d{9}$")) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    public String checkMessageLength() {
        if (messageText == null || messageText.isEmpty()) {
            return "Please enter a message.";
        }
        if (messageText.length() > 250) {
            int excess = messageText.length() - 250;
            return "Message exceeds 250 characters by "
                    + excess + "; please reduce the size.";
        }
        return "Message ready to send.";
    }

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

    /** Sends, disregards or stores this message according to the selected option. */
    public String SentMessage(int choice) {
        switch (choice) {
            case 1:
                SENT_MESSAGES.add(this);
                totalMessagesSent++;
                return "Message successfully sent.";
            case 2:
                return "Message successfully disregarded.";
            case 3:
                if (storeMessage()) {
                    return "Message successfully stored.";
                }
                return "Message could not be stored.";
            default:
                return "Invalid option. Please select 1, 2 or 3.";
        }
    }

    public static String printMessages() {
        StringBuilder output = new StringBuilder();
        for (Message message : SENT_MESSAGES) {
            output.append("Message ID: ").append(message.messageID)
                    .append("\nMessage Hash: ").append(message.messageHash)
                    .append("\nRecipient: ").append(message.recipient)
                    .append("\nMessage: ").append(message.messageText)
                    .append("\n\n");
        }
        return output.toString();
    }

    public int returnTotalMessages() {
        return getTotalMessagesSent();
    }

    public static int getTotalMessagesSent() {
        return totalMessagesSent;
    }

    /** Stores records as a JSON array, migrating the previous JSON-lines format. */
    public boolean storeMessage() {
        Path storagePath = Path.of(System.getProperty(
                "quickchat.storage.file", "stored_messages.json"));
        try {
            String existing = Files.exists(storagePath)
                    ? Files.readString(storagePath, StandardCharsets.UTF_8).trim()
                    : "";
            String existingRecords = extractExistingRecords(existing);
            if (existingRecords == null) {
                System.out.println("The existing message file is not valid JSON data.");
                return false;
            }

            String record = "{\"messageID\":\"" + jsonEscape(messageID)
                    + "\",\"messageHash\":\"" + jsonEscape(messageHash)
                    + "\",\"recipient\":\"" + jsonEscape(recipient)
                    + "\",\"message\":\"" + jsonEscape(messageText) + "\"}";
            String separator = existingRecords.isEmpty() ? "" : ",\n";
            String json = "[\n" + existingRecords + separator + record + "\n]\n";
            Files.writeString(storagePath, json, StandardCharsets.UTF_8);
            return true;
        } catch (IOException exception) {
            System.out.println("Error storing message: " + exception.getMessage());
            return false;
        }
    }

    /** Returns array contents, or safely converts the prior one-object-per-line format. */
    private String extractExistingRecords(String existing) {
        if (existing.isEmpty()) {
            return "";
        }
        if (existing.startsWith("[") && existing.endsWith("]")) {
            return existing.substring(1, existing.length() - 1).trim();
        }

        StringBuilder records = new StringBuilder();
        for (String line : existing.split("\\R")) {
            String record = line.trim();
            if (record.isEmpty()) {
                continue;
            }
            if (!record.startsWith("{") || !record.endsWith("}")) {
                return null;
            }
            if (records.length() > 0) {
                records.append(",\n");
            }
            records.append(record);
        }
        return records.toString();
    }

    private String jsonEscape(String value) {
        if (value == null) {
            return "";
        }
        StringBuilder escaped = new StringBuilder();
        for (char character : value.toCharArray()) {
            switch (character) {
                case '"':
                    escaped.append("\\\"");
                    break;
                case '\\':
                    escaped.append("\\\\");
                    break;
                case '\b':
                    escaped.append("\\b");
                    break;
                case '\f':
                    escaped.append("\\f");
                    break;
                case '\n':
                    escaped.append("\\n");
                    break;
                case '\r':
                    escaped.append("\\r");
                    break;
                case '\t':
                    escaped.append("\\t");
                    break;
                default:
                    if (character < 0x20) {
                        escaped.append(String.format("\\u%04x", (int) character));
                    } else {
                        escaped.append(character);
                    }
                    break;
            }
        }
        return escaped.toString();
    }

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
