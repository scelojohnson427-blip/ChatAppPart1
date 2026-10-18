package com.mycompany.chatapppart1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    @Test
    void testMessageIDIsValid() {
        Message message = new Message(
                1,
                "+27718693002",
                "Hi Mike, can you join us for dinner tonight?"
        );

        assertTrue(message.checkMessageID());
        assertEquals(10, message.getMessageID().length());
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
                "Press 0 to delete the message.",
                message.SentMessage(2)
        );
    }

    @Test
    void testStoreMessage() {
        Message message = new Message(
                3,
                "+27718693002",
                "This message will be stored."
        );

        assertEquals(
                "Message successfully stored.",
                message.SentMessage(3)
        );
    }

}