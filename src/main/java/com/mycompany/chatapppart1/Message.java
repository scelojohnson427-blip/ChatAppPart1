package com.mycompany.chatapppart1;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import java.util.Arrays;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

/** Handles QuickChat message validation, identification, hashing and storage. */
public class Message {

    private final String messageID;
    private final int messageNumber;
    private final String sender;
    private final String recipient;
    @SerializedName("message")
private final String messageText;
    private final String messageHash;
    private String messageStatus;

    private static int totalMessagesSent;
    private static final List<Message> SENT_MESSAGES = new ArrayList<>();
    // Part 3 dynamically populated arrays
private static Message[] sentMessages = new Message[0];
private static Message[] disregardedMessages = new Message[0];
private static Message[] storedMessages = new Message[0];

private static String[] messageHashes = new String[0];
private static String[] messageIDs = new String[0];

    public Message(int messageNumber, String recipient, String messageText) {

    this.messageNumber = messageNumber;
    this.sender = "Developer";
    this.recipient = recipient;
    this.messageText = messageText;
    this.messageID = generateMessageID();
    this.messageHash = createMessageHash();
    this.messageStatus = "Pending";

    registerMessageDetails(this);
}
 
public static void loadStoredMessages() {

    Path storagePath = Path.of(System.getProperty(
        "quickchat.storage.file", "stored_messages.json"));

    try {
        if (!Files.exists(storagePath)) {
            System.out.println("Stored messages file not found.");
            return;
        }

        String json = Files.readString(
                storagePath, StandardCharsets.UTF_8);

        Gson gson = new Gson();

        Type messageListType =
                new TypeToken<List<Message>>() {}.getType();

        List<Message> loadedMessages =
                gson.fromJson(json, messageListType);

        if (loadedMessages != null) {

            storedMessages = loadedMessages.toArray(new Message[0]);

            // Reset the arrays before loading
            messageIDs = new String[0];
            messageHashes = new String[0];

            // Populate message IDs and hashes dynamically
            for (Message message : storedMessages) {
                messageIDs = addString(
                        messageIDs, message.getMessageID());

                messageHashes = addString(
                        messageHashes, message.getMessageHash());
            }

            System.out.println("Number of stored messages: "
                    + storedMessages.length);
        }

        System.out.println("Stored messages loaded successfully.");

    } catch (Exception exception) {
        System.out.println("Error loading stored messages: "
                + exception.getMessage());
    }
}

    // Add a message to a dynamically growing Message array
private static Message[] addMessage(Message[] array, Message message) {
    array = Arrays.copyOf(array, array.length + 1);
    array[array.length - 1] = message;
    return array;
}

// Add a value to a dynamically growing String array
private static String[] addString(String[] array, String value) {
    array = Arrays.copyOf(array, array.length + 1);
    array[array.length - 1] = value;
    return array;
}

// Register every message ID and hash
private static void registerMessageDetails(Message message) {
    messageIDs = addString(messageIDs, message.messageID);
    messageHashes = addString(messageHashes, message.messageHash);
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
            sentMessages = addMessage(sentMessages, this);

            messageStatus = "Sent";
            totalMessagesSent++;

            return "Message successfully sent.";

        case 2:
            messageStatus = "Disregarded";
            disregardedMessages = addMessage(disregardedMessages, this);

            return "Message successfully disregarded.";

        case 3:
            messageStatus = "Stored";

            if (storeMessage()) {
                storedMessages = addMessage(storedMessages, this);

                return "Message successfully stored.";
            }

            messageStatus = "Pending";
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
        + "\",\"sender\":\"" + jsonEscape(sender)
        + "\",\"recipient\":\"" + jsonEscape(recipient)
        + "\",\"message\":\"" + jsonEscape(messageText)
        + "\",\"messageStatus\":\"" + jsonEscape(messageStatus) + "\"}";
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
  
    public String getSender() {
    if (sender == null) {
        return "Developer";
    }
    return sender;
}

    public String getMessageStatus() {
    if (messageStatus == null) {
        return "Stored";
    }

    return messageStatus;
}
   public static String displayStoredMessages() {

    StringBuilder output = new StringBuilder();
    

    if (storedMessages.length == 0) {
        return "No stored messages available.";
    }

    for (Message message : storedMessages) {
        

        output.append("Sender: ")
                .append(message.getSender())
                .append("\n");

        output.append("Recipient: ")
                .append(message.getRecipient())
                .append("\n");

        output.append("Message ID: ")
                .append(message.getMessageID())
                .append("\n");

        output.append("Message Hash: ")
                .append(message.getMessageHash())
                .append("\n");

        output.append("Message: ")
                .append(message.getMessageText())
                .append("\n\n");
    }
    
    return output.toString();
} 
   public static String displayLongestMessage() {

    if (storedMessages.length == 0) {
        return "No stored messages available.";
    }

    Message longest = storedMessages[0];

    for (Message message : storedMessages) {

        if (message.getMessageText().length()
                > longest.getMessageText().length()) {

            longest = message;
        }
    }

    return "Longest stored message: "
            + longest.getMessageText();
}
   public static String searchMessageByID(String searchID) {

    for (int i = 0; i < messageIDs.length; i++) {

        if (messageIDs[i].equals(searchID)) {

            for (Message message : storedMessages) {

                if (message.getMessageID().equals(searchID)) {

                    return "Recipient: " + message.getRecipient()
                            + "\nMessage: " + message.getMessageText();
                }
            }
        }
    }

    return "Message ID not found.";
}
   
public static String searchMessagesByRecipient(String searchRecipient) {

    StringBuilder output = new StringBuilder();

    // Search sent messages
    for (Message message : sentMessages) {

        if (message.getRecipient().equals(searchRecipient)) {

            output.append("Recipient: ")
                    .append(message.getRecipient())
                    .append("\n");

            output.append("Message: ")
                    .append(message.getMessageText())
                    .append("\n\n");
        }
    }

    // Search stored messages
    for (Message message : storedMessages) {

        if (message.getRecipient().equals(searchRecipient)) {

            output.append("Recipient: ")
                    .append(message.getRecipient())
                    .append("\n");

            output.append("Message: ")
                    .append(message.getMessageText())
                    .append("\n\n");
        }
    }

    if (output.length() == 0) {
        return "No messages found for this recipient.";
    }

    return output.toString();
}

 public static String deleteMessageByHash(String searchHash) {

    for (int i = 0; i < storedMessages.length; i++) {

        if (storedMessages[i].getMessageHash().equals(searchHash)) {

            String deletedMessage =
                    storedMessages[i].getMessageText();

            String deletedID =
                    storedMessages[i].getMessageID();

            Message[] oldMessages = storedMessages;

            Message[] updatedMessages =
                    new Message[storedMessages.length - 1];

            System.arraycopy(storedMessages, 0,
                    updatedMessages, 0, i);

            System.arraycopy(storedMessages, i + 1,
                    updatedMessages, i,
                    storedMessages.length - i - 1);

            storedMessages = updatedMessages;

            if (!saveStoredMessages()) {
                storedMessages = oldMessages;
                return "Message could not be deleted.";
            }  

            messageIDs = removeString(messageIDs, deletedID);
            messageHashes = removeString(messageHashes, searchHash);

            return "Message: " + deletedMessage
                    + " successfully deleted.";
        }    
    }
    

    return "Message hash not found.";
}
 public static String displayReport() {
    StringBuilder report = new StringBuilder();

    if (storedMessages.length == 0) {
        return "No stored messages available.";
    }

    report.append("===== QUICKCHAT MESSAGE REPORT =====\n");

    for (Message message : storedMessages) {
        report.append("Sender: ")
                .append(message.getSender()).append("\n");

        report.append("Recipient: ")
                .append(message.getRecipient()).append("\n");

        report.append("Message ID: ")
                .append(message.getMessageID()).append("\n");

        report.append("Message Hash: ")
                .append(message.getMessageHash()).append("\n");

        report.append("Message: ")
                .append(message.getMessageText()).append("\n");

        report.append("Status: ")
                .append(message.getMessageStatus()).append("\n");

        report.append("-----------------------------------\n");
    }

    return report.toString();
}
   private static String[] removeString(String[] array, String value) {

    for (int i = 0; i < array.length; i++) {

        if (array[i].equals(value)) {

            String[] newArray = new String[array.length - 1];

            System.arraycopy(array, 0, newArray, 0, i);

            System.arraycopy(array, i + 1, newArray, i,
                    array.length - i - 1);

            return newArray;
        }
    }

    return array;
}
   public static boolean saveStoredMessages() {

    Path storagePath = Path.of(System.getProperty(
            "quickchat.storage.file", "stored_messages.json"));

    try {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        String json = gson.toJson(storedMessages);

        Files.writeString(
                storagePath,
                json,
                StandardCharsets.UTF_8);

        return true;

    } catch (IOException exception) {
        System.out.println("Error updating stored messages: "
                + exception.getMessage());

        return false;
    }
}
}   
