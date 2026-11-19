package com.mycompany.chatapppart1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class MessageTest {

    @TempDir
    Path temporaryDirectory;

    @Test
    void testMessageIDIsValid() {
        Message message = new Message(
                1,
                "+27718693002",
                "Hi Mike, can you join us for dinner tonight?"
        );

        assertTrue(message.checkMessageID());
        assertEquals(10, message.getMessageID().length());
        assertTrue(message.getMessageID().startsWith("01"));
    }

    @Test
    void testRecipientNumberIsValid() {
        Message message = new Message(
                1,
                "+27718693002",
                "Hi Mike"
        );

        assertEquals(
                "Cell phone number successfully captured.",
                message.checkRecipientCell()
        );
    }

    @Test
    void testRecipientNumberIsInvalid() {
        Message message = new Message(
                1,
                "08575975889",
                "Hi Keegan"
        );

        assertEquals(
                "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
                message.checkRecipientCell()
        );
    }

    @Test
    void testMessageWithin250Characters() {
        Message message = new Message(
                1,
                "+27718693002",
                "Hi Mike, can you join us for dinner tonight?"
        );

        assertEquals(
                "Message ready to send.",
                message.checkMessageLength()
        );
    }

    @Test
    void testMessageExceeds250Characters() {
        String longText = "A".repeat(260);

        Message message = new Message(
                1,
                "+27718693002",
                longText
        );

        assertEquals(
                "Message exceeds 250 characters by 10; please reduce the size.",
                message.checkMessageLength()
        );
    }

    @Test
    void testMessageHash() {
        Message message = new Message(
                0,
                "+27718693002",
                "Hi Mike, can you join us for dinner tonight?"
        );

        String expected = message.getMessageID().substring(0, 2)
                + ":0:HITONIGHT";

        assertEquals(expected, message.createMessageHash());
    }

    @Test
    void testSendMessage() {
        Message message = new Message(
                1,
                "+27718693002",
                "Hi Mike, can you join us for dinner tonight?"
        );

        assertEquals(
                "Message successfully sent.",
                message.SentMessage(1)
        );
    }

    @Test
    void testDisregardMessage() {
        Message message = new Message(
                2,
                "+27718693002",
                "Hi Keegan, did you receive the payment?"
        );

        assertEquals(
                "Message successfully disregarded.",
                message.SentMessage(2)
        );
    }

    @Test
    void testStoreMessage() throws IOException {
        Path storageFile = temporaryDirectory.resolve("stored_messages.json");
        String previousPath = System.getProperty("quickchat.storage.file");
        System.setProperty("quickchat.storage.file", storageFile.toString());
        Message message = new Message(
                3,
                "+27718693002",
                "This message will be stored."
        );

        try {
            assertEquals("Message successfully stored.", message.SentMessage(3));
            String storedJson = Files.readString(storageFile);
            assertTrue(storedJson.startsWith("["));
            assertTrue(storedJson.trim().endsWith("]"));
            assertTrue(storedJson.contains("\"message\":\"This message will be stored.\""));
        } finally {
            restoreStorageProperty(previousPath);
        }
    }

    @Test
    void testStoreMessageEscapesJsonCharacters() throws IOException {
        Path storageFile = temporaryDirectory.resolve("escaped_messages.json");
        String previousPath = System.getProperty("quickchat.storage.file");
        System.setProperty("quickchat.storage.file", storageFile.toString());

        try {
            Message message = new Message(1, "+27718693002", "Say \"hello\"\nagain");
            assertEquals("Message successfully stored.", message.SentMessage(3));
            String storedJson = Files.readString(storageFile);
            assertTrue(storedJson.contains("Say \\\"hello\\\"\\nagain"));
        } finally {
            restoreStorageProperty(previousPath);
        }
    }

    @Test
    void testStoreMessageMigratesExistingJsonLines() throws IOException {
        Path storageFile = temporaryDirectory.resolve("stored_messages.json");
        Files.writeString(storageFile,
                "{\"messageID\":\"1234567890\",\"message\":\"old\"}\n");
        String previousPath = System.getProperty("quickchat.storage.file");
        System.setProperty("quickchat.storage.file", storageFile.toString());

        try {
            Message message = new Message(2, "+27718693002", "new message");
            assertEquals("Message successfully stored.", message.SentMessage(3));
            String storedJson = Files.readString(storageFile);
            assertTrue(storedJson.startsWith("["));
            assertTrue(storedJson.contains("\"message\":\"old\""));
            assertTrue(storedJson.contains("\"message\":\"new message\""));
        } finally {
            restoreStorageProperty(previousPath);
        }
    }
    
    
@Test
void testDisplayLongestStoredMessage() throws IOException {
    Path storageFile = temporaryDirectory.resolve("longest_test.json");
    String previousPath = System.getProperty("quickchat.storage.file");

    System.setProperty("quickchat.storage.file", storageFile.toString());

    try {
        Message shortMessage = new Message(
                1, "+27718693002", "Hello"
        );

        Message longMessage = new Message(
                2, "+27718693002",
                "This is the longest stored message."
        );

        assertEquals(
                "Message successfully stored.",
                shortMessage.SentMessage(3)
        );

        assertEquals(
                "Message successfully stored.",
                longMessage.SentMessage(3)
        );

        Message.loadStoredMessages();

        assertEquals(
                "Longest stored message: This is the longest stored message.",
                Message.displayLongestMessage()
        );

    } finally {
        restoreStorageProperty(previousPath);
        Message.loadStoredMessages();
    }
}


@Test
void testSearchMessageByID() throws IOException {
    Path storageFile = temporaryDirectory.resolve("search_id_test.json");
    String previousPath = System.getProperty("quickchat.storage.file");

    System.setProperty("quickchat.storage.file", storageFile.toString());

    try {
        Message message = new Message(
                1, "+27718693002", "Hello from QuickChat."
        );

        message.SentMessage(3);
        Message.loadStoredMessages();

        String result = Message.searchMessageByID(message.getMessageID());

        assertTrue(result.contains("Recipient: +27718693002"));
        assertTrue(result.contains("Message: Hello from QuickChat."));

    } finally {
        restoreStorageProperty(previousPath);
        Message.loadStoredMessages();
    }
}


@Test
void testSearchMessagesByRecipient() throws IOException {
    Path storageFile = temporaryDirectory.resolve("search_recipient_test.json");
    String previousPath = System.getProperty("quickchat.storage.file");

    System.setProperty("quickchat.storage.file", storageFile.toString());

    try {
        Message firstMessage = new Message(
                1, "+27838884567", "Where are you?"
        );

        Message secondMessage = new Message(
                2, "+27838884567", "I am waiting for you."
        );

        firstMessage.SentMessage(3);
        secondMessage.SentMessage(3);

        Message.loadStoredMessages();

        String result = Message.searchMessagesByRecipient("+27838884567");

        assertTrue(result.contains("Where are you?"));
        assertTrue(result.contains("I am waiting for you."));

    } finally {
        restoreStorageProperty(previousPath);
        Message.loadStoredMessages();
    }
}



@Test
void testDeleteMessageByHash() throws IOException {
    Path storageFile = temporaryDirectory.resolve("delete_hash_test.json");
    String previousPath = System.getProperty("quickchat.storage.file");

    System.setProperty("quickchat.storage.file", storageFile.toString());

    try {
        Message message = new Message(
                1, "+27718693002", "This message must be deleted."
        );

        message.SentMessage(3);
        Message.loadStoredMessages();

        String result = Message.deleteMessageByHash(
                message.getMessageHash()
        );

        assertTrue(result.contains("successfully deleted."));

        assertTrue(
                Message.displayStoredMessages().isEmpty()
                || !Message.displayStoredMessages().contains(
                        "This message must be deleted."
                )
        );

    } finally {
        restoreStorageProperty(previousPath);
        Message.loadStoredMessages();
    }
}


@Test
void testDisplayMessageReport() throws IOException {
    Path storageFile = temporaryDirectory.resolve("report_test.json");
    String previousPath = System.getProperty("quickchat.storage.file");

    System.setProperty("quickchat.storage.file", storageFile.toString());

    try {
        Message message = new Message(
                1, "+27718693002", "Project meeting tomorrow."
        );

        message.SentMessage(3);
        Message.loadStoredMessages();

        String report = Message.displayReport();

        assertTrue(report.contains("Developer"));
        assertTrue(report.contains("+27718693002"));
        assertTrue(report.contains(message.getMessageID()));
        assertTrue(report.contains(message.getMessageHash()));
        assertTrue(report.contains("Project meeting tomorrow."));
        assertTrue(report.contains("Stored"));

    } finally {
        restoreStorageProperty(previousPath);
        Message.loadStoredMessages();
    }
}


@Test
void testDisplayStoredMessages() throws IOException {
    Path storageFile = temporaryDirectory.resolve("display_test.json");
    String previousPath = System.getProperty("quickchat.storage.file");

    System.setProperty("quickchat.storage.file", storageFile.toString());

    try {
        Message message = new Message(
                1, "+27718693002", "Stored display test."
        );

        message.SentMessage(3);
        Message.loadStoredMessages();

        String result = Message.displayStoredMessages();

        assertTrue(result.contains("Developer"));
        assertTrue(result.contains("+27718693002"));
        assertTrue(result.contains(message.getMessageID()));
        assertTrue(result.contains(message.getMessageHash()));
        assertTrue(result.contains("Stored display test."));

    } finally {
        restoreStorageProperty(previousPath);
        Message.loadStoredMessages();
    }
}


@Test
void testLoadStoredMessagesFromJSON() throws IOException {
    Path storageFile = temporaryDirectory.resolve("load_test.json");
    String previousPath = System.getProperty("quickchat.storage.file");

    System.setProperty("quickchat.storage.file", storageFile.toString());

    try {
        Message message = new Message(
                1, "+27718693002", "JSON loading test."
        );

        message.SentMessage(3);

        Message.loadStoredMessages();

        String result = Message.searchMessageByID(
                message.getMessageID()
        );

        assertTrue(result.contains("Recipient: +27718693002"));
        assertTrue(result.contains("Message: JSON loading test."));

    } finally {
        restoreStorageProperty(previousPath);
        Message.loadStoredMessages();
    }
}


@Test
void testSearchMessagesByRecipientIncludesSentMessages() {

    Message message = new Message(
            1,
            "+27834557896",
            "Did you get the cake?"
    );

    message.SentMessage(1);

    String result = Message.searchMessagesByRecipient(
            "+27834557896"
    );

    assertTrue(result.contains("Recipient: +27834557896"));
    assertTrue(result.contains("Message: Did you get the cake?"));
}



    private void restoreStorageProperty(String previousPath) {
        if (previousPath == null) {
            System.clearProperty("quickchat.storage.file");
        } else {
            System.setProperty("quickchat.storage.file", previousPath);
        }
    }

}
