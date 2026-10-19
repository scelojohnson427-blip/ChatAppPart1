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

    private void restoreStorageProperty(String previousPath) {
        if (previousPath == null) {
            System.clearProperty("quickchat.storage.file");
        } else {
            System.setProperty("quickchat.storage.file", previousPath);
        }
    }

}
